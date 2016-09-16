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
	<link rel="stylesheet" type="text/css"	href="/JobSearch/static/css/inputValidation.css " />	
	
	<!-- Time picker -->
	<link rel="stylesheet" type="text/css" href="/JobSearch/static/External/jquery.timepicker.css" />
	<script	src="<c:url value="/static/External/jquery.timepicker.min.js" />"></script>
	
	<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
	
</head>


<body>

	<div class="container" >
		<div>${vtJobInformation }</div>
	</div><!-- end container -->




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
	
	function showInvalidAmountMessage(message){
		var $e = $("#invalidAmount");
		$e.html(message);
		$e.show();
		return 0;
	}
	
	function isInputValid(){
		
		//*******************************************************		
		//*******************************************************
		//Still need to verify aswers
		//*******************************************************		
		//*******************************************************
		
		var isValid = 1;
		var amount = $("#amount").val();
		
		//Verify pay proposal		
		if(amount == ""){
			isValid = showInvalidAmountMessage("Desired pay is required.");
		}else if($.isNumeric(amount) == 0){
			isValid = showInvalidAmountMessage("Desired pay must be numeric.");
		}else if(amount <= 0){
			isValid = showInvalidAmountMessage("Desired pay must be greater than 0.");
		}
		
		//Hide error message if input is valid
		if(isValid == 1){
			$("#invalidAmount").hide();		}
		
		return isValid;
	}
	
	
	function getApplicationRequestDTO(){
		
		var jobId = $("#jobId").val();
		var dto = {};
		
		dto.jobId = jobId;
		
		//Set wage proposal
		dto.wageProposal = {}
		dto.wageProposal.amount = $("#amount").val();
// 		dto.wageProposal.jobId = jobId;
		dto.wageProposal.status = -1;
		
		return dto;
		

	};
	
	function getAnswers(){
		
		var answerText;
		var answerTexts = [];
		var questionId;
		var j;
		var answer = {};
		var answers = [];
		var questions = $(".questions-container").find(".question");
		var invalidAnswer = 0;

		//Loop through each question
		for(var i = 0; i < questions.length; i++){
			
			
			var questionElement = questions[i];
			var questionFormatId = $(questionElement).data("formatId"); //$(questionElement).find(".question-format-id").val();
			var questionId = $(questionElement).data('id');
			

			answerTexts = [];
			
			//Get answer value and validate answer value.
			
			//Yes/no or single answer
			if(questionFormatId == 0 || questionFormatId == 2){
				answerTexts[0] = $(questionElement).find('input[type=radio]:checked').parents('label').text();
				
			//Short answer
			}else if(questionFormatId == 1){
				
				answerTexts[0] = $(questionElement).find('textarea').val()
			
			//Multi answer
			}else if(questionFormatId == 3){
				
				//Get the checked checkbox's parent's text
				answerTexts = $(questionElement).find('input[type=checkbox]:checked').map(function(){ return $(this).parents('label').text() }).get();

			}
			
			//Validate answer
			if(answerTexts.length == 0 || answerTexts[0] == ""){
				invalidAnswer = 1;
			}else{
				
				//Loop through all answers.
				//Only multi answer will have more than 1.
				for(j = 0; j < answerTexts.length; j++){
					
					//Initialize answer object
					answer = {};
					answer.text = answerTexts[j];
					answer.questionId = questionId;
					answers.push(answer);
				}

			}
		}
		
		return answers;
	}
	
	
	function apply(){
		
		var applicationRequestDTO = {};

		//Verify input
		if(isInputValid()){
			
			//Initialize application DTO
			applicationRequestDTO = getApplicationRequestDTO();
			
			//Get the applicant's answers
			applicationRequestDTO.answers = [];
			applicationRequestDTO.answers = getAnswers();

	
			//Submit the apppliation
			var headers = {};
			headers[$("meta[name='_csrf_header']").attr("content")] = $(
					"meta[name='_csrf']").attr("content");
			$.ajax({
				type : "POST",
				url : environmentVariables.LaborVaultHost + '/JobSearch/job/apply',
				headers : headers,
				contentType : "application/json",
				data : JSON.stringify(applicationRequestDTO),
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