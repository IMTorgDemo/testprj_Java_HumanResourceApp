package com.hrsystem.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.hrsystem.dao.AssignmentDAO;
import com.hrsystem.dao.AssignmentDAOImpl;
import com.hrsystem.dao.EmployeeDAO;
import com.hrsystem.dao.EmployeeDAOImpl;
import com.hrsystem.dao.JobDAO;
import com.hrsystem.dao.JobDAOImpl;
import com.hrsystem.model.Assignment;
import com.hrsystem.model.Employee;
import com.hrsystem.model.Job;
import com.hrsystem.util.HRUtil;

public class AssignmentController extends HttpServlet {

	private static final long	serialVersionUID	= 1L;
	private static final String	ADD_PAGE			= "/assignment/add.html";
	private static final String	LIST_PAGE			= "/assignment/list.html";
	private static final String	VIEW_PAGE			= "/assignment/view.html";
	private static final String	EDIT_PAGE			= "/assignment/edit.html";
	private static final String	DELETE_PAGE			= "/assignment/delete.html";

	private AssignmentDAO		assignmentDAO;
	private EmployeeDAO			employeeDAO;
	private JobDAO				jobDAO;

	public AssignmentController() {
		super();
		this.assignmentDAO = new AssignmentDAOImpl();
		this.employeeDAO = new EmployeeDAOImpl();
		this.jobDAO = new JobDAOImpl();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String pageContext = request.getContextPath();
		String page = "";
		String action = request.getParameter("action");
		System.out.println("Operation @ GET : " + action);
		HttpSession session = request.getSession();

		if (action.equals(HRUtil.Action.ADD)) {
			if (session.getAttribute("employees") == null) {
				session.setAttribute("employees", employeeDAO.getAllEmployees());
			}
			page = pageContext + ADD_PAGE;

		} else if (action.equals(HRUtil.Ajax.LOAD_EMP_INFO)) {
			int employeeId = Integer.parseInt(request.getParameter("id"));
			String ssn = request.getParameter("ssn");

			Gson gson = new Gson();
			JsonObject data = new JsonObject();

			Employee employee = assignmentDAO.loadEmployeeInfo(employeeId);
			JsonElement employeeObj = gson.toJsonTree(employee);

			if (ssn.equals(employee.getSsn())) {
				data.addProperty("success", true);

				List<Job> jobs = assignmentDAO.loadMapJob(employeeId);
				JsonElement jsonJobList = gson.toJsonTree(jobs);
				data.add("availableJobs", jsonJobList);

			} else {
				data.addProperty("success", false);
			}

			data.add("EmployeeInfo", employeeObj);

			// Sent Ajax data ...
			response.setContentType("text/html");
			response.setHeader("Cache-control", "no-cache, no-store");
			response.setHeader("Pragma", "no-cache");
			response.setHeader("Expires", "-1");

			response.getWriter().write(data.toString());
		} else if (action.equals(HRUtil.Ajax.LOAD_JOB_INFO)) {
			int jobId = Integer.parseInt(request.getParameter("id"));
			String jobnumber = request.getParameter("jobnumber");

			System.out.println("Id : " + jobId + "\t\t jobnumber:" + jobnumber);

			Gson gson = new Gson();
			JsonObject data = new JsonObject();

			Job job = assignmentDAO.loadJobInfo(jobId);
			JsonElement jobObj = gson.toJsonTree(job);

			if (jobnumber.equals(job.getJobNumber())) {
				data.addProperty("success", true);
			} else {
				data.addProperty("success", false);
			}

			data.add("JobInfo", jobObj);

			// Sent Ajax data ...
			response.setContentType("text/html");
			response.setHeader("Cache-control", "no-cache, no-store");
			response.setHeader("Pragma", "no-cache");
			response.setHeader("Expires", "-1");

			response.getWriter().write(data.toString());

		} else if (action.equals(HRUtil.Action.LIST)) {
			request.setAttribute("assignments",
					assignmentDAO.getAllJobAssignment(HRUtil.Status.Active));
			page = LIST_PAGE;

		} else if (action.equals(HRUtil.Action.VIEW)) {
			int assignmentId = Integer.parseInt(request.getParameter("id"));
			request.setAttribute("assignment", assignmentDAO.getJobAssignment(assignmentId));
			page = VIEW_PAGE;
		} else if (action.equals(HRUtil.Action.EDIT)) {
			int assignmentId = Integer.parseInt(request.getParameter("id"));

			Assignment assignment = assignmentDAO.getJobAssignment(assignmentId);
			request.setAttribute("assignment", assignment);

			List<Job> jobs = assignmentDAO.loadMapJob(assignment.getEmployeeId());
			request.setAttribute("jobs", jobs);

			page = EDIT_PAGE;
		} else if (action.equals(HRUtil.Action.DELETE)) {
			int assignmentId = Integer.parseInt(request.getParameter("id"));
			request.setAttribute("assignment", assignmentDAO.getJobAssignment(assignmentId));
			page = DELETE_PAGE;
		} else if (action.equals(HRUtil.Action.SEARCH)) {
			String searchKey = request.getParameter("searchassignment");
			List<Assignment> assignments = new ArrayList<Assignment>();
			if (searchKey.trim().length() > 0) {
				List<Employee> employees = employeeDAO.searchEmployees(searchKey);
				// System.out.println("employees.size() " + employees.size());
				if (employees.size() > 0) {
					for (Employee employee : employees) {
						List<Assignment> jobAssignments = assignmentDAO
								.getAssignmentByEmpId(employee.getEmployeeId());
						// System.out.println("assignment.getAssignmentId() " +
						// assignments);
						for (Assignment assignment : jobAssignments) {
							assignments.add(assignment);
						}
					}
				}

				List<Job> jobs = jobDAO.searchJobs(searchKey);
				// System.out.println("jobs.size() " + jobs.size());
				if (jobs.size() > 0) {
					for (Job job : jobs) {
						List<Assignment> jobAssignments = assignmentDAO
								.getAssignmentByJobId(job.getJobId());
						// System.out.println("assignment.getAssignmentId() " +
						// assignment);
						for (Assignment assignment : jobAssignments) {
							assignments.add(assignment);
						}
					}
				}
			}
			request.setAttribute("assignments", assignments);
			page = "search.html";
		}

		if (action.equals(HRUtil.Action.ADD))

		{
			response.sendRedirect(page);
		} else if (action.equals(HRUtil.Action.LIST) || action.equals(HRUtil.Action.VIEW)
				|| action.equals(HRUtil.Action.EDIT) || action.equals(HRUtil.Action.DELETE)
				|| action.equals(HRUtil.Action.SEARCH)) {
			RequestDispatcher view = request.getRequestDispatcher(page);
			view.forward(request, response);
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String page = request.getContextPath();
		String action = request.getParameter("action");
		System.out.println("Operation @ POST : " + action);

		if (action.equals(HRUtil.Action.ADD)) {

			int employeeId = Integer.parseInt(request.getParameter("emp_ssn"));
			int jobId = Integer.parseInt(request.getParameter("sel_jobnumber"));

			System.out.println(employeeId + "\t" + jobId);

			Assignment assignment = new Assignment();
			assignment.setEmployeeId(employeeId);
			assignment.setJobId(jobId);
			assignment.setStatus(HRUtil.Status.Active);
			assignment.setCreatedDate(new Date());

			assignmentDAO.addAssignment(assignment);

			page += ADD_PAGE;

		} else if (action.equals(HRUtil.Action.UPDATE)) {

			int assignmentId = Integer.parseInt(request.getParameter("id"));
			int employeeId = Integer.parseInt(request.getParameter("emp_ssn"));
			int jobId = Integer.parseInt(request.getParameter("sel_jobnumber"));

			// System.out.println(employeeId + "\t" + jobId);
			Assignment assignment = new Assignment();
			assignment.setAssignmentId(assignmentId);
			assignment.setEmployeeId(employeeId);
			assignment.setJobId(jobId);
			assignment.setStatus(HRUtil.Status.Active);
			assignment.setUpdatedDate(new Date());

			assignmentDAO.updateAssignment(assignment);

			page = "Controller?action=list";
		} else if (action.equals(HRUtil.Action.DELETE)) {
			int assignmentId = Integer.parseInt(request.getParameter("id"));
			assignmentDAO.deleteAssignment(assignmentId);

			page = "Controller?action=list";
		}
		response.sendRedirect(page);
	}

}
