<%@ include file="./includes/Header.jsp"%>
<link href="/JobSearch/static/css/replace_an_employee.css" rel="stylesheet" />	
<script src="<c:url value="/static/javascript/ReplaceAnEmployee.js" />"></script>		

<div class="page-container">

	<div id="select-an-employee-cont">
		<button class="sqr-btn teal" data-toggle-id="employees">Selected An Employee To Replace</button>
		<div id="employees" class="dropdown-style">
			<c:forEach items="${users_employees }" var="user">
				<p data-user-id="${user.userId }">${user.firstName } ${user.lastName }</p>
			</c:forEach>
		</div>
	</div>
	<div id="post-select" class="pad-top">
		<div id="replace-cont">
			<p>Replace:</p>
			<p id="employee-to-replace-name"></p>
		</div>
		<div id="questions-cont">
			<div class="question">
				<p>Was this individual fired?</p>
				<div class="button-group">
					<button class="sqr-btn gray-2">No, this individual unfortunately had to leave the job</button>
					<button class="sqr-btn gray-2">Yes, I asked this individual to leave the job</button>
				</div>
			</div>
		</div>
		<div id="send-request" class="pad-top-2">
			<button class="sqr-btn teal">Send Request</button>
		</div>
	</div>
</div>
<input id="jobId" type="hidden" value="${jobId }">