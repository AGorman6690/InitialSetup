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
	<input id="jobLat" value="${job.lat }" type="hidden"></input>
	<input id="jobLng" value="${job.lng }" type="hidden"></input>
	
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
						<div class="job-info-container col-sm-6">
							<div class="header">
								${job.jobName }
							
								<span class="categories job-sub-info">				
									<c:forEach items="${job.categories }" var="category">
									<button class="btn">${category.name }</button>
									</c:forEach>
								</span>
							</div>
							<div class="body">
								<div class="start">
									<span class="bold">Start: </span><fmt:formatDate value="${job.startDate }" pattern="EE, MMM d"/>, <fmt:formatDate value="${job.startTime }" pattern="K:mm a" />
								</div>
								<div>									
									<span class="bold">End: </span><fmt:formatDate value="${job.endDate }" pattern="EE, MMM d"/>, <fmt:formatDate value="${job.endTime }" pattern="K:mm a" />
								</div>
								<div class="description">
									${job.description }
								</div>
								<div class="location">
									<div>${job.streetAddress }</div>
									<div>${job.city }, ${job.state } ${job.zipCode }</div>
									<div id="map"></div>											
								</div>
							</div>										
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
							</div>
						</div>
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


	function initMap(){
		var lat = $("#jobLat").val();
		var lng = $("#jobLng").val();
		var map =  initializeMap("map", lat, lng);
		showMapMarker(map, lat, lng);

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

<script async defer
	src="https://maps.googleapis.com/maps/api/js?key=AIzaSyAXc_OBQbJCEfhCkBju2_5IfjPqOYRKacI&callback=initMap">
	
</script>


<%@ include file="./includes/Footer.jsp"%>