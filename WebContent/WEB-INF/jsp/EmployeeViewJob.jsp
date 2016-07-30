<%@ include file="./includes/Header.jsp"%>

<head>
	<script src="<c:url value="/static/javascript/Utilities.js" />"></script>
	<script src="<c:url value="/static/javascript/Category.js" />"></script>
	<script src="<c:url value="/static/javascript/InputValidation.js" />"></script>
	<script src="<c:url value="/static/javascript/PostJob/Jobs.js"/>"></script>
	<script src="<c:url value="/static/javascript/PostJob/Questions.js"/>"></script>
	<script src="<c:url value="/static/javascript/PostJob/ChangeForm.js"/>"></script>

	<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/categories.css" />
	<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/postJob.css" />
	<link rel="stylesheet" type="text/css"	href="/JobSearch/static/css/employeeViewJob.css " />		
	
	<!-- Time picker -->
	<link rel="stylesheet" type="text/css" href="/JobSearch/static/External/jquery.timepicker.css" />
	<script	src="<c:url value="/static/External/jquery.timepicker.min.js" />"></script>
	
	<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
	
</head>


<body>

	<input id="jobId" value="${job.id }" type="hidden"></input>
	
	<div id="viewJobContainer">
		<div class="container" >
			<div class="row row-padding" style="">					
				<div class="col-sm-12" style="">
					
					<div id="jobActionContainer" class="" style="">
						<input id="activeJobId" type="hidden">
					    <div class="btn-group">
							<button onclick="apply()">Apply</button>						
							<button>Add to favorites (not built)</button>																
			
					    </div>			
					</div>							
				</div><!-- end row -->
			</div><!-- end job cart container -->	
	
			<div id="">
	
					<div class="row" >
						<div id="jobGeneralContainer" class="col-sm-6">
							<form>						
									<div class="job-sub-info-container">
										<h3>General</h3>
										<div class="job-sub-info">
											<fieldset class="form-group">
												<label for="jobName" class="form-control-label">Name</label>
												<input disabled name="jobName" type="text"
													class="post-job-input form-control" value="${job.jobName }"></input>
											</fieldset>
												
											<fieldset class="form-group">
												<label for="jobDescription" class="form-control-label">Description</label>
												<textarea disabled name="jobDescription" class="form-control" 
												rows="5" >${job.description }</textarea>
											</fieldset>
										</div>
									</div>
								
									
									<div class="job-sub-info-container">
										<h3>Location</h3>
									
										<div class="job-sub-info">
									
											<fieldset class="form-group">
												<label for="streetAddress" class="form-control-label">Street Address</label>
												<input disabled name="streetAddress" type="text"
													class="post-job-input form-control" value="${job.streetAddress }"></input>
											</fieldset>

											<fieldset class="form-group">
												<label for="city" class="form-control-label">City</label>
												<input disabled name="city" type="text"
													class="post-job-input form-control" value="${job.city }"></input>
											</fieldset>
											
											<fieldset class="form-group">
												<label for="state" class="form-control-label">State</label>
												<input disabled name="state" type="text"
													class="post-job-input form-control" value="${job.state }"></input>
											</fieldset>	
											
											<fieldset class="form-group">
												<label for="zipCode" class="form-control-label">Zip Code</label>
												<input disabled name="zipCode" type="text"
													class="post-job-input form-control" value="${job.zipCode }"></input>
											</fieldset>
										</div>
									</div>
									
									<div class="job-sub-info-container">
										<h3>Date and Time</h3>					
										
										<div class="job-sub-info">
											<fieldset class="form-group">
												<label for="dateRange" class="form-control-label">Start and End Dates</label>
												<input disabled style="width: 250px" class="form-control" type="text"
													value="<fmt:formatDate value="${job.startDate }" pattern="EE, MMM d"/> - <fmt:formatDate value="${job.endDate }" pattern="EE, MMM d"/>"/>
					
											</fieldset>								
											
											<fieldset class="form-group">
												<label for="startTime" class="form-control-label">Start Time</label>
												<input disabled name="startTime" type="text"
													class="post-job-input form-control time ui-timepicker-input"
													value="<fmt:formatDate value="${job.startTime }" pattern="K:mm a"/>"></input>
											</fieldset>
						
											<fieldset class="form-group">
												<label for="endTime" class="form-control-label">End Time</label>
												<input disabled name="endTime" type="text"
													class="post-job-input form-control time ui-timepicker-input"
													value="<fmt:formatDate value="${job.endTime }" pattern="K:mm a"/>"></input>
											</fieldset>
											
										</div>
									</div>
									
									
									
									<div id="categoryContainer" class="job-sub-info-container">
										<h3>Categories</h3>
										<div class="categories job-sub-info">				
											<c:forEach items="${job.categories }" var="category">
												<button class="btn">${category.name }</button>
											</c:forEach>
										</div>									
									</div>				
							
							
							
							</form>
						</div> <!-- end job general container -->
						
						
						
						<div id="questionsColumn" class="col-sm-6">
						
							<div class="questions-container">
								<div class="questions-header">
									<h3>Questions</h3>						
								</div>

								
								<div class="questions">
									<c:forEach items="${job.questions }" var="question">
										<div class="question" data-id="${question.questionId }" data-format-id="${question.formatId }">
											<div class="text">
												${question.text }
											</div>
											<div class="answer">
											<c:choose>
											
												<c:when test="${question.formatId == 0 }">
													<div class="radio">
														<label class="block"><input type="radio" name="yes-no" value="1">Yes</label>
														<label class="block"><input type="radio" name="yes-no" value="0">No</label>
													</div>
												</c:when>
												<c:when test="${question.formatId == 1 }">
													<div class="textarea">
														<textarea class="form-control short-answer" rows="2"></textarea>
													</div>
												</c:when>
												<c:when test="${question.formatId == 2 }">
													<div class="radio">
													<c:forEach items="${question.answerOptions }" var="answerOption">
														<label class="block"><input type="radio" name="single-answer" value="${answerOption.answerOptionId }">${answerOption.answerOption }</label>
													</c:forEach>
													</div>
												</c:when>
												<c:when test="${question.formatId == 3 }">
													<div class="checkbox">
													<c:forEach items="${question.answerOptions }" var="answerOption">
														<label class="block"><input type="checkbox" name="multi-answer" value="${answerOption.answerOptionId }">${answerOption.answerOption }</label>
													</c:forEach>			
													</div>									
												</c:when>
											
											</c:choose>		
											</div>						
										</div>
									</c:forEach>						
								</div>
									
									<br>
									<br>
									----------------- put a map here -----------------				
							</div>
						</div> end job questions container
					</div><!-- end row -->
				</div><!-- end job info container -->
		</div><!-- end container -->
	</div><!-- end post job container -->



<!-- Modals -->
<!-- ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ -->
<!-- ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ -->
	<div id="confirmJobDelete" class="modal fade" role="dialog">
	  <div class="modal-dialog modal-sm">
	
	    <!-- Modal content-->
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal">&times;</button>
	        <h4 class="modal-title">Confirmation</h4>
	      </div>
	      <div class="modal-body">
	        <p>Continue to delete job?</p>
	        
	        <div class="checkbox" style="margin: 10px 0px 0px 15px">
	        	<label>
	        		<input id="disableJobDeleteAlert" data-confirmed="0" type="checkbox"> Disable alert
	        	</label>
	        </div>	        
	      </div>
	      <div class="modal-footer">
	        <button id="confirmJobDelete" type="button" class="btn btn-default" 
	        	data-dismiss="modal" onclick="deleteJob(1)">Yes</button>
	        <button id="cancelJobDelete" type="button" class="btn btn-default"
	        	data-dismiss="modal" onclick="deleteJob(0)">No</button>
	      </div>
	    </div>
	
	  </div>
	</div>	

	<div id="confirmJobSubmit" class="modal fade" role="dialog">
	  <div class="modal-dialog modal-sm">
	
	    <!-- Modal content-->
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal">&times;</button>
	        <h4 class="modal-title">Confirmation</h4>
	      </div>
	      <div class="modal-body">
	        <p>Complete job posting and submit jobs?</p>
	      </div>
	      <div class="modal-footer">
	        <button id="confirmJobSubmit" type="button" class="btn btn-default" 
	        	data-dismiss="modal" onclick="submitJobs(1)">Yes</button>
	        <button id="cancelJobSubmit" type="button" class="btn btn-default"
	        	data-dismiss="modal" onclick="submitJobs(0)">No</button>
	      </div>
	    </div>
	
	  </div>
	</div>	
	
<!-- ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ -->
<!-- ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ -->




<script>

	var pageContext = "postJob";
	var jobs = [];	
	var jobCount = 0;
	var questions = [];
	var questionCount = 0;
	var questionContainerIdPrefix = 'question-';
	
		

	$(document).ready(function() {
	

		
//  		setPopovers();

				
	})
	
	
	function apply(){
		
		//*******************************************************		
		//*******************************************************
		//Still need to include verification css 
		//*******************************************************		
		//*******************************************************
		
		var answerText;
		var answerTexts = [];
		var questionId;
		var j;
		var answer = {};

		//Initialize application DTO
		var applicationDTO = {};
		applicationDTO.jobId = $("#jobId").val();
		applicationDTO.answers = [];

		var questions = $(".questions-container").find(".question");
		var invalidAnswer = 0;

		//Loop through each question
		for(var i = 0; i < questions.length; i++){
			
			//Initialize answer object
// 			var answer = {};
// // 			answer.answerText = "";
// // 			answer.answerBoolean = -1;
// // 			answer.answerOptionId = -1;
// // 			answer.answerOptionIds = [];
// 			answer.text = "";
// 			answer.texts = [];
			
			var questionElement = questions[i];
			var questionFormatId = $(questionElement).data("formatId"); //$(questionElement).find(".question-format-id").val();
			var questionId = $(questionElement).data('id');
			//Get answer value and validate answer value.
			//questionFormatIds:
			//0: Yes/No
			//1: Short answer
			//2: Single answer
			//3: Multi answer
			answerTexts = [];
			if(questionFormatId == 0 || questionFormatId == 2){
				answerTexts[0] = $(questionElement).find('input[type=radio]:checked').parents('label').text();;
// 				if(value == 1){
// 					answer.text = "Yes";
// 				}else if(value == 0){
// 					answer.text = "No";
// 				}else{
// 					invalidAnswer = 1;
// 				}
				
			}else if(questionFormatId == 1){
				
				answerTexts[0] = $(questionElement).find('textarea').val()
// 				if(value == ""){
// 					invalidAnswer = 1;
// 				}else{
// 					answer.text = value;
// 				}
// 			}else if(questionFormatId == 2){
				
// 				answerTexts = $(questionElement).find('input[type=radio]:checked').val();
// 				if(value == ""){
// 					invalidAnswer = 1;
// 				}else{
// 					answer.text = answerText;
// 				}
			}else if(questionFormatId == 3){
				
				//Get the checked checkbox's parent's text
				answerTexts = $(questionElement).find('input[type=checkbox]:checked').map(function(){ return $(this).parents('label').text() }).get();
// 				if(answerTexts.length == 0){
// 					invalidAnswer = 1;
// 				}else{
// 					answer.texts = answerTexts;
// 				}
			}
			
			if(answerTexts.length == 0 || answerTexts[0] == ""){
				invalidAnswer = 1;
			}else{
				for(j = 0; j < answerTexts.length; j++){
					
					//Initialize answer object
					answer = {};
					answer.text = answerTexts[j];
					answer.questionId = questionId;
// 					answer.texts = [];
					applicationDTO.answers.push(answer);
				}

				
				
				
			}

// 			answer.questionId = $(questionElement).data('id');
// 			applicationDTO.answers.push(answer);
		}


		//If all answers are valid, then submit the application
		if(invalidAnswer == 0){
			var headers = {};
			headers[$("meta[name='_csrf_header']").attr("content")] = $(
					"meta[name='_csrf']").attr("content");
			$.ajax({
				type : "POST",
				url : environmentVariables.LaborVaultHost + '/JobSearch/job/apply',
				headers : headers,
				contentType : "application/json",
				data : JSON.stringify(applicationDTO),
			}).done(function() {
				$('#home')[0].click();
			}).error(function() {
				$('#home')[0].click();
			});
		}
	}
	

		
// 	function setPopovers(){

// 		$('.popover1').popover({
// 			trigger: "hover",
// 			delay: {
// 				show: "250",		
// 			}
// 		});
		
// 		$('.popoverInstant').popover({
// 			trigger: "hover",
// 			delay: {
// 				show: "0",		
// 			}
// 		});	
		
// 	}
		

</script>


<%@ include file="./includes/Footer.jsp"%>