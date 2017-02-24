<%@ include file="../includes/Header.jsp"%>
<%@ include file="../includes/resources/PageContentManager.jsp" %>

<script src="<c:url value="/static/javascript/Utilities.js" />"></script>

<link rel="stylesheet" type="text/css" href="../static/css/profile.css" />
<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/table.css" />


<div class="container">
	<div class="row">
		<div id="pageContentLinksContainer" class="col-sm-12">
			<span class="page-content-link selected" data-section-id="jobsWaitingToStart">Waiting To Start</span>
			<span>/</span>
			<span class="page-content-link" data-section-id="jobsInProcess">In Process</span>
			<span>/</span>
			<span class="page-content-link" data-section-id="jobsCompleted">Complete</span>
		</div>
	</div>
				
	<div class="row">
		<div class="col-sm-12" id="sectionContainers">	
			<div id="jobsWaitingToStart" class="section-container">
				<%@ include file="./JobsWaitingToStart_Employer.jsp" %>
			</div>
			<div id="jobsInProcess" class="section-container">
				<%@ include file="./JobsInProcess_Employer.jsp" %>
			</div>
			<div id="jobsCompleted" class="section-container">
				<%@ include file="./JobsCompleted_Employer.jsp" %>
			</div>
		</div>
	</div>
</div>

<script>

	
	
	$(document).ready(function(){
	
		$(".mark-complete-ouououoiuiou").click(function(){
		
			var headers = {};
			headers[$("meta[name='_csrf_header']").attr("content")] = $(
					"meta[name='_csrf']").attr("content");
			
			var jobId = $($(this).parents("tr")[0]).attr("id");
			
			$.ajax({
				type : "PUT",
				url : "/JobSearch/job/" + jobId + "/markComplete",
				headers : headers
			}).done(function() {
				$('#home')[0].click();
			}).error(function() {
				$('#home')[0].click();
	
			});
			
		})
	})


</script>

<%@ include file="../includes/Footer.jsp"%>

