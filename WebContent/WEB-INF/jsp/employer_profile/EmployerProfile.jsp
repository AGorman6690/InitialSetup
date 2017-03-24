<%@ include file="../includes/Header.jsp"%>
<script src="<c:url value="/static/javascript/profile_employer/Profile_Employer.js" />"></script>
<%@ include file="../includes/resources/SelectPageSection.jsp"%>


<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/table.css" />
<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/profile_employer/profile_employer.css" />

<script src="<c:url value="/static/javascript/Utilities.js" />"></script>


<!-- <div class="select-page-section-container employer-profile"> -->
<!-- 	<span class="select-page-section selected" data-perspective="wage-proposal">Wage Proposals</span> -->
<!-- 	<span class="select-page-section" data-perspective="application">Applications</span> -->
<!-- 	<span class="select-page-section" data-perspective="employee">Employees</span> -->
<!-- </div> -->


<div class="container">

	<%@ include file="./Jobs_Current_Employer.jsp" %>

</div>


<%@ include file="../includes/Footer.jsp"%>

