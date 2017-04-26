<%@ include file="../includes/Header.jsp"%>
<%@ include file="../includes/resources/SelectPageSection.jsp" %>
<%@ include file="../includes/resources/TableFilter.jsp" %>

<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/view_job_employer/calendar_application_summary.css" />
<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/inputValidation.css" />
<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/table.css" />
<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/Templates/popup.css" />
<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/profile_employee/profile_employee_NEW.css" />
		
<script src="<c:url value="/static/javascript/profile_employee/Profile_Employee.js" />"></script>	
	
<div class="container center">
	<div id="applications_list_view" class="">
		<c:choose>
			<c:when test="${applicationDtos.size() > 0 }">	
				<%@ include file="./Applications_Employee.jsp" %>									
			</c:when>
			<c:otherwise>
				<p id="noApplications">You have no open applications at this time.</p>	
				<a id="" href="/JobSearch/jobs/find" class="sqr-btn teal">Find Jobs</a>
			</c:otherwise>		
		</c:choose>
	</div>
</div>

<%@ include file="../includes/Footer.jsp"%>
<%@ include file="../includes/resources/WageProposal.jsp" %>
