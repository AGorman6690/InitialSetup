<%@ include file="./includes/Header.jsp"%>

<head>
<script src="<c:url value="/static/javascript/Utilities.js" />"></script>
<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/table.css" />
<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/employerViewEmployee.css" />
</head>

<body>

	<div class="container">
	
<%-- 		<c:if test="${applicants.size() > 1 }"> --%>
			<div class="row">
				<div id="sideBarContainer" class="col-sm-2">
					<c:forEach items="${applicants }" var="applicant">
<%-- 						<c:set var="selectedClassName" value="" /> --%>
						<c:if test="${applicant.userId == clickedUserId }">
<%-- 							<c:set var="selectedClassName" value="selected-blue" /> --%>
	
							<div data-user-id="${applicant.userId }"
								 class="applicant side-bar ${applicant.userId == clickedUserId ? 'selected-blue' : '' }">
								 ${applicant.firstName }</div>						 
 						</c:if>
					</c:forEach>
										
				</div>
				
			
				<div class="col-sm-10">
					<div id="workHistoryContainer" class="section-container">
						<div class="section-body">
							<h4>Work History</h4>
								<div id="workHistory">
									<%@include file="./templates/WorkHistory.jsp"%>
								</div>					
						</div>
					</div>
				</div>
				
			</div>	
<%-- 		</c:if> --%>
</div>
	
	
</body>


<script>
$(document).ready(function(){


	$(".applicant").click(function(){
		
		var applicantId = $(this).attr("data-user-id");
		var clickedElement = this;
		
		$.ajax({
			type : "GET",
			url : '/JobSearch/user/' + applicantId + '/jobs/completed',
			headers : getAjaxHeaders(),
			contentType : "application/json",
			success: _success,
			error: _error
		})
		
		function _success(response){		
			
			$("#workHistory").html(response);
			highlightArrayItem(clickedElement, $("#sideBarContainer").find(".applicant"), "selected-blue");

		}
		
		function _error(){
//	 		alert("status error")
		}		
	})

	
})



</script>



<%@ include file="./includes/Footer.jsp"%>