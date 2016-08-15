<%@ include file="./includes/Header.jsp"%>
<head>
<%-- <script src="<c:url value="/static/javascript/Jobs.js" />"></script> --%>
<%-- <script src="<c:url value="/static/javascript/Category.js" />"></script> --%>
<%-- <script src="<c:url value="/static/javascript/User.js" />"></script> --%>
<%-- <script src="<c:url value="/static/javascript/AppendHtml.js" />"></script> --%>
<link rel="stylesheet" type="text/css" href="../static/css/employerViewJob.css" />

</head>




	<span>(note: job info will be shown here. Allow user to hide/show the job info. hide on load)</span>
	<br>
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
								<th id="questions"><span class="toggle-select-options"> Questions <span class="glyphicon glyphicon-menu-down"></span></span>
									<div class="select-options-container">
										<div class="select-options full-border-thick">
											<span id="selectQuestionsOK" class="select-OK glyphicon glyphicon-ok"></span>			
											<div class="radio">
												<label class="block select-option"><input id="showAll" class="show-all" type="radio" name="mass-select" value="all">All</label>
												<label class="block select-option"><input id="showNone" class="show-none" type="radio" name="mass-select" value="none">None</label>
											</div>
											<div class="checkbox">
											<c:forEach items="${job.questions }" var="question">
												<label class="block select-option"><input type="checkbox" name="individual-select" value="${question.questionId }">${question.text }</label>
											</c:forEach>
												
											</div>
											
										</div>		
																	
									</div>							
								</th>
<!-- 								<th id="answers">Answers</th> -->
								<th id="status"><span class="toggle-select-options"> Status <span class="glyphicon glyphicon-menu-down"></span></span>
									<div class="select-options-container">
										<div class="select-options full-border-thick">
											<span id="selectStatusesOK" class="select-OK glyphicon glyphicon-ok"></span>			
											<div class="radio">
												<label class="block select-option"><input class="show-all" id="showAllStatus" type="radio" name="mass-select" value="all">All</label>
						
											</div>
											<div class="checkbox">
											<label class="block select-option"><input type="checkbox" name="individual-select-status" value="0">Submitted</label>
												<label class="block select-option"><input type="checkbox" name="individual-select-status" value="1">Decline</label>
												<label class="block select-option"><input type="checkbox" name="individual-select-status" value="2">Considering</label>
												<label class="block select-option"><input type="checkbox" name="individual-select-status" value="3">Hire</label>
											</div>
											
										</div>	
								
									</div>	
								</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${job.getApplications() }" var="application">
								<tr id="${application.applicationId }" class="applicant bottom-border-thin" data-select-option-value="${application.status }"
									data-application-id="${application.applicationId }">
									<td><a href="/JobSearch/user/${application.applicant.userId}/jobs/completed"> ${application.applicant.firstName }</a></td>
									<td> ${application.applicant.rating}</td>
<!-- 								Set endorsements -->
									<td>										
										<c:forEach items="${application.applicant.endorsements }" var="endorsement">
										
											<div class="endorsement">													
												${endorsement.categoryName } <span class="badge count">  ${endorsement.count }</span>
											</div>
										</c:forEach>

									</td>	
<!-- 								Questions and answers -->
									<td>
									<c:forEach items="${application.questions }" var="question">
										<div data-select-option-value="${question.questionId }" class="question bottom-border-thin">										
											${question.text }
										
											<div class="answer">
												<c:forEach items="${question.answers }" var="answer">
													${answer.text }
												</c:forEach>
												
											</div>
										</div>
									</c:forEach>
									</td>
<!-- 								Application Status						 -->
									<td>
										<div class="application-status-container">
											<c:choose>
												<c:when test="${application.status == 1 }">
												<button id="declineApplicant" value="1" class="active">Decline</button>
												</c:when>
												<c:otherwise>
												<button id="declineApplicant" value="1" class="">Decline</button>
												</c:otherwise>
											</c:choose>
											<c:choose>
												<c:when test="${application.status == 2 }">
												<button id="declineApplicant" value="2" class="active">Consider</button>
												</c:when>
												<c:otherwise>
												<button id="declineApplicant" value="2" class="">Consider</button>
												</c:otherwise>
											</c:choose>
											<c:choose>
												<c:when test="${application.status == 3 }">
												<button id="declineApplicant" value="3" class="active">Hire</button>
												</c:when>
												<c:otherwise>
												<button id="declineApplicant" value="3" class="">Hire</button>
												</c:otherwise>
											</c:choose>																						
											
										</div>
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
		
		$(".toggle-select-options").click(function(){
		
			$($(this).parent().find(".select-options-container")[0]).toggle();
		
		})
		
		$(".application-status-container button").click(function(){
			var applicationId;
			var statusValue;
			var clickedButton = $(this);
			var clickedRow = $(this).parents(".applicant");
			//Get applicant's id
			applicationId = clickedRow.data("application-id");

			//Get the status value
			statusValue = $(this).val();
			
			var headers = {};
			headers[$("meta[name='_csrf_header']").attr("content")] = $(
					"meta[name='_csrf']").attr("content");
			
			$.ajax({
				type : "POST",
				url : '/JobSearch/application/status/update?applicationId=' + applicationId + "&status=" + statusValue,
				headers : headers,
				contentType : "application/json",
				success: _success,
				error: _error
			})
			
			function _success(){
				$(".application-status-container button").each(function(){
					$(this).removeClass("active");
				})
				
				$(clickedRow).attr("data-select-option-value", statusValue);
				$(clickedButton).addClass("active");
			}
			
			function _error(){
				alert("status error")
			}

			
		})
		
		
		$(".select-OK").click(function(){
			
			var clickedId = $(this).attr("id");
			var elementsToToggle = [];
			var userSelectedOptionsValues = [];
			var elementData;
			var selectOptionsContainer = $(this).parents(".select-options-container")[0];
			
			//Determine which header is being filtered.
			//Set the elements to toggle accordingly.
			if(clickedId == "selectQuestionsOK"){
				elementsToToggle = $("tbody").find(".question");
			}else if(clickedId == "selectStatusesOK"){
				elementsToToggle = $("tbody").find(".applicant");
			}			
			
			//Hide the dropdown
			$(selectOptionsContainer).hide();
					
			//If show all
			if($(selectOptionsContainer).find(".show-all").is(":checked")){
				elementsToToggle.each(function(){
					$(this).show();
				})
				
			//If show none
			}else if($(selectOptionsContainer).find(".show-none").is(":checked")){
				elementsToToggle.each(function(){
					$(this).hide();
				})
				
			//Else selectively show
			}else{		
				
				//Get the values selected by the user
				userSelectedOptionsValues = $(selectOptionsContainer).find(".select-options .checkbox input:checked")
															.map(function(){return $(this).val()}).get();	
				
				//Hide and show the applicants accordingly
				elementsToToggle.each(function(){
					
					elementData = $(this).attr("data-select-option-value"); 
					if(jQuery.inArray(elementData.toString(), userSelectedOptionsValues) !== -1){
						$(this).show();
					}else{
						$(this).hide();
					}
				})
			}
		})

		
		$(".select-option input[type=checkbox]").click(function(){
			$(".select-option input[type=radio]").attr("checked", false);
		})
		
		$(".select-option input[type=radio]").click(function(){

			var doCheck;
			
			//If "All" was clicked
			if($(this).val() == "all"){
				doCheck = true;
			}else{
				doCheck = false;
			}
			
			$(".select-option input[type=checkbox]").prop("checked", doCheck);
		})
		
		//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
		//Old stuff
		
		
		
		
		
		
		
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





</script>

<%@ include file="./includes/Footer.jsp"%>