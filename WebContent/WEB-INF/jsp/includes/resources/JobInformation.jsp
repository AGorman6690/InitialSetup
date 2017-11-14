<%@ include file="../../includes/TagLibs.jsp"%>



<!-- **************************************************************** -->
<!-- **************************************************************** -->
<!-- Review how the styling for the job info calendar should be packaged. -->
<!-- Sould it be in its own css file and used across the site??? -->
<!-- Almost all calendars are placing the start and end times in the calendar. -->
<!-- Review this -->
<!-- **************************************************************** -->
<!-- **************************************************************** -->

<script src="/JobSearch/static/javascript/JobInfo.js" type="text/javascript"></script>
<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/job_info/job_info_NEW.css" />

<c:if test="${empty doSkip_loadGoogleMapsApiForJobInfo || doSkip_loadGoogleMapsApiForJobInfo == 0 }">
<!-- 	<script -->
<!-- 		src="https://maps.googleapis.com/maps/api/ -->
<!--  			js?key=AIzaSyAXc_OBQbJCEfhCkBju2_5IfjPqOYRKacI&callback=initMap_job_info"></script> -->
<script
	src="https://maps.googleapis.com/maps/api/js?key=AIzaSyAXc_OBQbJCEfhCkBju2_5IfjPqOYRKacI">
</script>		
</c:if>

<div id="job-info-mod" class="mod simple-header">
	<div class="mod-content">
		<div class="mod-header"></div>
		<div class="mod-body"></div>
	</div>
</div>