<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	String contextPath = request.getContextPath();
%>

<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>Human Resource System</title>
</head>
<body>

	<div id="wrapper" class="container">
		<div id="top_header">
			<h2 id="Project_header" class="title_color">Human Resource System</h2>
		</div>
		<div id="menu">
			<jsp:include page="../common/menu.jsp"></jsp:include>
		</div>
		<div id="container">
			<div id="container_header">
				<h4>Edit Job</h4>
			</div>
			<div id="container_body">

				<form method="POST" action="Controller" class="form-horizontal" role="form">
					<input type="hidden" name="action" value="update" /> <input type="hidden" name="id" value="${job.jobId}" />
					<div class="form-group">
						<label class="control-label col-sm-2" for="jobnumber">Job Number:</label>
						<div class="col-sm-6">
							<input id="jobnumber" name="jobnumber" type="text" class="form-control" value="${job.jobNumber}" placeholder="Enter Job Number" maxlength="25" disabled>
						</div>
					</div>
					<div class="form-group">
						<label class="control-label col-sm-2" for="jobname">Job Name:</label>
						<div class="col-sm-6">
							<input id="jobname" name="jobname" type="text" class="form-control" value="${job.jobName}" placeholder="Enter Job Name" maxlength="50">
						</div>
					</div>
					<div class="form-group">
						<label class="control-label col-sm-2" for="description">Description:</label>
						<div class="col-sm-6">
							<textarea id="description" name="description" class="form-control" rows="5" maxlength="200" placeholder="Enter Description">${job.description}</textarea>
						</div>
					</div>
					<div class="form-group">
						<div class="col-sm-offset-2 col-sm-6">
							<input type="button" name="updatejobbtn" class="col-sm-offset-4 btn btn-default" value="Update" />
							<input type="button" class="col-sm-offset-4 btn btn-default" value="Back" onclick="showJobList()" />
						</div>
					</div>
				</form>


			</div>
		</div>
		<div id="bfooter">
			<p>Copyright &copy; 2016 &middot; All Rights Reserved.</p>
		</div>
	</div>

</body>
<link rel="stylesheet" type="text/css" href="<%=contextPath%>/css/bMin.css">
<link rel="stylesheet" type="text/css" href="<%=contextPath%>/css/_main.css">
<script type="text/javascript" language="javascript" src="<%=contextPath%>/js/jMin.js"></script>
<script type="text/javascript" language="javascript" src="<%=contextPath%>/js/bMin.js"></script>
<script type="text/javascript" language="javascript" src="<%=contextPath%>/js/hr.main.js"></script>
<script type="text/javascript" language="javascript" src="<%=contextPath%>/js/hr.job.js"></script>
</html>