<%@ include file="./includes/Header.jsp"%>
<head>
<%-- <script src="<c:url value="/static/javascript/Jobs.js" />"></script> --%>
<%-- <script src="<c:url value="/static/javascript/Category.js" />"></script> --%>
<%-- <script src="<c:url value="/static/javascript/User.js" />"></script> --%>
<%-- <script src="<c:url value="/static/javascript/AppendHtml.js" />"></script> --%>
<link rel="stylesheet" type="text/css" href="../static/css/employerViewJob.css" />

</head>




	<span>toggle job info. hide on load</span>
	<br>


	<div class="container">
		<div class="table-container">
			<c:choose>
				<c:when test="${empty job.applications}">
					<div>There are currently no applicants for this job</div>
				</c:when>
				
				<c:otherwise>
					<table>
						<thead>
							<tr>
								<th id="applicantName">Applicant Name</th>
								<th id="rating">Rating</th>
								<th id="endorsements">Endorsements</th>
								<th id="questions">Questions</th>
<!-- 								<th id="answers">Answers</th> -->
								<th id="status">Status</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${job.getApplications() }" var="application">
								<tr>
									<td> ${application.applicant.firstName }</td>
									<td> ${application.applicant.rating}</td>
									<td>
<!-- 									Set endorsements -->
										<c:forEach items="${application.applicant.endorsements }" var="endorsement">
										
											<div class="endorsement">													
												${endorsement.getCategoryName() } <span class="badge count">  ${endorsement.getCount() }</span>
											</div>
										</c:forEach>

									</td>	
									<td>
<!-- 								Questions and answers -->
									<c:forEach items="${application.questions }" var="question">
										<div>										
											${question.question }
										</div>
										<div>
											<c:choose>
												<c:when test="${question.formatId == 0 }">
													<c:choose>
														<c:when test="${question.answer.answerBoolean == 1}">
														 Yes
														</c:when>
														<c:otherwise>
														No
														</c:otherwise>	
													</c:choose>
													
												</c:when>
												<c:when test="${question.formatId == 1 }">
													${question.answer.answerText }
												</c:when>
												<c:when test="${question.formatId == 2 }">
													
												</c:when>
												<c:when test="${question.formatId == 3 }">
												
												</c:when>
											</c:choose>
											
										</div>
									</c:forEach>
									</td>						
								</tr>
							</c:forEach>
							
						</tbody>
					
					</table>				
				</c:otherwise>
			</c:choose>
		</div>
	</div>








	
			


<script type="text/javascript">

	$(document).ready(function(){
		$('#applicantsTable').DataTable();
		$('#employeesTable').DataTable();

		$(".clickToWorkHistory").click(function(){
			elementId = $(this).parent().attr('id');
			var idBegin = elementId.indexOf("_") + 1;
			var userId =  elementId.substring(idBegin);
			window.location = "../jobs/completed/employee/?userId=" + userId +
								'&jobId=' + $("#jobId").val() + '&c=0';
		})

		$("#applicationStatus").change(function(){

			var i = 0;
			var j = 0;
			var statusesToShow = $(this).find(":selected")
									.map(function(){ return $(this).val() }).get();


			var statuses = $("#applicantsTable").find("tbody input.applicant-status")

			if(statusesToShow.length == 0){
				for(i = 0; i < statuses.length; i++){
					var status = statuses[i];
					var rows = $(status).parents('tr');
					var row = rows[0];
					$(row).hide();
				}
			}else if(statusesToShow.length == 2){
				for(i = 0; i < statuses.length; i++){
					var status = statuses[i];
					var rows = $(status).parents('tr');
					var row = rows[0];
					$(row).show();
				}
			}else{
				for(i = 0; i < statuses.length; i++){
					var status = statuses[i];
					var rows = $(status).parents('tr');
					var row = rows[0];

					for(j = 0; j < statusesToShow.length; j++){

						if($(status).val() == statusesToShow[j]){
							$(row).show();
							j = statusesToShow.length;
						}else if(j == statusesToShow.length - 1){
							$(row).hide();
						}
					}
				}
			}
		})

		$(".hide-show-columns").change(function(){
			filterApplicants();
		})


		$(".update-application-status").click(function(){

			//Store the value of the application status
			$(this).parent().siblings('input.applicant-status').val($(this).val());

			//Show all default buttons
			var buttons = $(this).parents('div.applicant-status').find('button');
			for(var i = 0; i < buttons.length; i++){
				var button = buttons[i];

				if($(button).hasClass("btn-info")){
					$(button).hide();
				}else if($(button).hasClass("btn-default")){
					$(button).show();
				}
			}

			//Toggle the clicked button
			$(this).hide();
			var otherButton = $(this).siblings('button')[0];
			$(otherButton).show();

		})

	});

	function filterApplicants(){

		var headers = $("#applicantsTable").find("thead tr th");
		var headerNames = [];
		var i = 0;
		var j = 0;
		var headersToShow = $(".hide-show-columns").find(":selected")
								.map(function(){ return $(this).html() }).get();

// 		alert(endorsementsToShow)

		for(i = 0; i < headers.length; i++){
			var header = headers[i];
			var headerName = $(header).text();

			if(headerName != "Applicant Name" && headerName != "Rating"
					&& headerName != "Application Status"){

				var showColumn = 0;
				for(j = 0; j < headersToShow.length; j++){
					if(headerName == headersToShow[j]){
						showColumn = 1;
					}
				}

				if(showColumn == 1){
					$("#applicantsTable td:nth-child(" + parseInt(i + 1) + ")").show();
					$("#applicantsTable th:nth-child(" + parseInt(i + 1) + ")").show();
				}else{
					$("#applicantsTable td:nth-child(" + parseInt(i + 1) + ")").hide();
					$("#applicantsTable th:nth-child(" + parseInt(i + 1) + ")").hide();
				}
			}
		}
	}

	function apply(){

		var userId = $("#userId").val();
		var applicationDTO = {};

		applicationDTO.jobId = $("#jobId").val();
		applicationDTO.userId = userId;
		applicationDTO.answers = [];

		var questions = $("#questions").find(".question");
		var invalidAnswer = 0;

		for(var i = 0; i < questions.length; i++){

			var questionElement = questions[i];
			var questionFormatId = $(questionElement).find(".question-format-id").val();

			var answer = {};
			answer.answerText = "";
			answer.answerBoolean = -1;
			answer.answerOptionId = -1;
			answer.answerOptionIds = [];

			if(questionFormatId == 0){
				answer.answerBoolean = $(questionElement).find('select').find(":selected").val();
				if(answer.answerBoolean == "") invalidAnswer = 1;
			}else if(questionFormatId == 1){
				answer.answerText = $(questionElement).find('.answer-text').val()
				if(answer.answerText == "") invalidAnswer = 1;
			}else if(questionFormatId == 2){
				answer.answerOptionId = $(questionElement).find('select').find(":selected").val();
				if(answer.answerOptionId == "") invalidAnswer = 1;
			}else if(questionFormatId == 3){
				answer.answerOptionIds = $(questionElement).find('select').find(":selected").map(function(){ return this.value }).get();
				if(answer.answerOptionIds == "") invalidAnswer = 1;
			}

			var questionElementId = $(questionElement).attr('id');
			answer.questionId = questionElementId.substring(questionElementId.indexOf("-") + 1);
			answer.userId = userId;

			applicationDTO.answers.push(answer);
		}

// 		alert(JSON.stringify(applicationDTO))

		if(invalidAnswer == 0){
			var headers = {};
			headers[$("meta[name='_csrf_header']").attr("content")] = $(
					"meta[name='_csrf']").attr("content");
			$.ajax({
				type : "POST",
				url : environmentVariables.LaborVaultHost + '/JobSearch/job/apply',
				headers : headers,
// 				dataType : "application/json",
				contentType : "application/json",
				data : JSON.stringify(applicationDTO),
			}).done(function() {
				$('#home')[0].click();
			}).error(function() {
				$('#home')[0].click();
			});
		}
	}


	function markJobComplete(jobId) {
		var headers = {};
		headers[$("meta[name='_csrf_header']").attr("content")] = $(
				"meta[name='_csrf']").attr("content");
		$.ajax({
			type : "PUT",
			url : environmentVariables.LaborVaultHost + '/JobSearch/job/' + jobId + '/markComplete',
			headers : headers
		}).done(function() {
			$('#home')[0].click();
		}).error(function() {
			$('#home')[0].click();

		});
	}


</script>

<%@ include file="./includes/Footer.jsp"%>