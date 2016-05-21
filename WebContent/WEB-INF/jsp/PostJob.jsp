<%@ include file="./includes/Header.jsp"%>

<head>
<script src="<c:url value="/static/javascript/Jobs.js" />"></script>
<script src="<c:url value="/static/javascript/Category.js" />"></script>
<%-- <script src="<c:url value="/static/javascript/AppendHtml.js" />"></script> --%>
<script src="<c:url value="/static/javascript/InputValidation.js" />"></script>
<link rel="stylesheet" type="text/css"
	href="./static/css/categories.css" />

<link rel="stylesheet" type="text/css"
	href="./static/css/inputValidation.css" />
<!-- Time picker -->
<link rel="stylesheet" type="text/css"
	href="./static/External/jquery.timepicker.css" />
<script
	src="<c:url value="/static/External/jquery.timepicker.min.js" />"></script>
<%-- <script src="<c:url value="/static/External/GruntFile.js" />"></script> --%>

<script src="<c:url value="/static/javascript/AppendHtml.js" />"></script>

<!-- 	<link rel="stylesheet" href="http://localhost:8080/JobSearch/static/External/bootstrap-select-1.10.0/dist/css/bootstrap-select.css"> -->
<!-- 	<link rel="stylesheet" href="http://localhost:8080/JobSearch/static/External/bootstrap-select-1.10.0/dist/css/bootstrap-select.css"> -->
<!-- 	<link rel="stylesheet" href="http://localhost:8080/JobSearch/static/External/bootstrap-select-1.10.0/dist/css/bootstrap-select.css"> -->

<!-- 	<script src="http://localhost:8080/JobSearch/static/External/bootstrap-select-1.10.0/dist/js/bootstrap-select.js"></script> -->
<!-- <script src="http://localhost:8080/JobSearch/static/External/bootstrap-select-1.10.0/dist/js/bootstrap-select.js.map"></script> -->
<!-- <script src="http://localhost:8080/JobSearch/static/External/bootstrap-select-1.10.0/dist/js/bootstrap-select.min.js"></script> -->



<!-- Dropdown -->
<!-- <script src="/path/to/bootstrap-hover-dropdown.min.js"></script> -->


	<style>
		.enable-question{
			color: green
		}
		
		.disable-question{
			color: 	#C0C0C0;
			
		}
		

		.post-job-select{
			padding: 5px;
			border: 1px solid #ccc;
			font-size: 16px;
			height: 34px;
			width: 100%;
			-webkit-border-radius: 5px;
			-moz-border-radius: 5px;
			border-radius: 5px;
		   
		/*    width: 268px; */
		}
		
		.post-job-select-container{
			border-radius: 5px
		}
		
		#jobActions button{
/* 			width: 125px; */
		}

	
	</style>

</head>






<body>

	<input name="userId" value="${user.userId}" type="hidden"></input>
	
	<div class="container">
	
		<div class="row">
			<div class="job-cart-container col-sm-12">	
				<div id="jobActions" class="btn-group" role="group" style="margin-bottom: 25px">
				

					<button id="addJob" type="button" class="btn btn-info"
						onclick="addJobToCart()">Add Job</button>
<!-- 					<div style="display: none"> -->
					<button id="deleteJob" type="button" disabled
						class="btn btn-info">Delete Job</button>
					<button id="editJob" type="button" disabled
						class="btn btn-info">Edit Job</button>													
					<button id="saveChanges" type="button" disabled
						class="btn btn-info">Save Changes</button>
					<button id="startNewJob" type="button" class="btn btn-info"
						disabled>Start New Job</button>
					<button id="startSimilarJob" type="button" class="btn btn-info"
						disabled>Start Similar Job</button>						
<!-- 					</div>										 -->
				</div>			
			</div>
		</div>

		<div class="row">
			<div class="col-sm-12">
				<div id="addedJobsContainer" style="display: none; margin-bottom: 20px">
					<span style="font-size: 20px; margin-right: 10px">Job Cart</span>
					<button id="submitJobs" class="btn btn-success">Submit Jobs</button>	
					<div id="addedJobs" class="btn-group" role="group" aria-label="Basic example">						
					</div>				
				</div>
				
				<div id="jobsInProcessContainer" style="display: none">
					<h4>Jobs In Process:</h4>	
					<div id="jobsInProcess" class="btn-group" role="group" aria-label="Basic example">
	
					</div>					
				</div>
			</div>
		</div>
				
		<div class="row">
			<div id="jobInfoContainer" class="col-sm-6">
				<form>
	
	<!-- 				<h2>Job Information</h2> -->
					<div class="" >
					
						<div class="job-sub-info-container">
							<h3>General</h3>
							<div class="job-sub-info">
								<fieldset class="form-group">
									<label for="jobName" class="form-control-label">Name</label>
									<input name="jobName" type="text"
										class="post-job-input form-control" id="jobName"></input>
									<div id="invalidJobName" style="display: none">Job names must be unique</div>
								</fieldset>
									
								<fieldset class="form-group">
									<label for="jobDescription" class="form-control-label">Description</label>
									<textarea name="jobDescription" class="form-control"
										id="jobDescription" rows="5"></textarea>
								</fieldset>
							</div>
						</div>
					
						
						<div class="job-sub-info-container">
							<h3>Location</h3>
							
							<div class="job-sub-info">
								<fieldset class="form-group">
									<label for="streetAddress" class="form-control-label">Street Address</label>
									<input name="streetAddress" type="text"
										class="post-job-input form-control" id="streetAddress"></input>
								</fieldset>
								
								<fieldset class="form-group">
									<label for="city" class="form-control-label">City</label>
									<input name="city" type="text"
										class="post-job-input form-control" id="city"></input>
								</fieldset>
								
								<fieldset class="form-group">
									<label for="state" class="form-control-label">State</label>
										
									<div class="post-job-input post-job-select-container">
										<select style="display: block" id="state" name="state"
										 class="post-job-select post-job-input form-control"></select>	
	<!-- 									<input name="state" type="text" -->
	<!-- 										class="post-job-input form-control" id="state"></input> -->
									</div>
								</fieldset>
								
								<fieldset class="form-group">
									<label for="zipCode" class="form-control-label">Zip Code</label>
									<input name="zipCode" type="text"
										class="post-job-input form-control" id="zipCode"></input>
								</fieldset>
							</div>
						</div>
						
						<div class="job-sub-info-container">
							<h3>Date and Time</h3>					
							
							<div class="job-sub-info">
								<fieldset class="form-group">
									<label for="dateRange" class="form-control-label">Start and End Dates</label>
									<input style="width: 250px" class="form-control" type="text"
										id="dateRange" value=""/>
<!-- 									<script type="text/javascript"> -->
<!-- // 										$(function() { -->
<!-- // 											today = new Date(); -->
<!-- // 											$('#dateRange').daterangepicker({ -->
<!-- // 												minDate: today, -->
<!-- // // 												startDate: "", -->
<!-- // 												endDate: "", -->
<!-- // 												locale : { -->
<!-- // 													format : 'MM/DD/YYYY',		 -->
													
<!-- // 												}											 -->
<!-- // 											}); -->
											
<!-- // // 											$("#dateRange").val(today + " - "); -->
<!-- // 										}); -->
<!-- 									</script> -->
			
								</fieldset>								
								
								<fieldset class="form-group">
									<label for="startTime" class="form-control-label">Start Time</label>
									<input name="startTime" type="text"
										class="post-job-input form-control time ui-timepicker-input"
											autocomplete="off" id="startTime"></input>
								</fieldset>
			
								<fieldset class="form-group">
									<label for="endTime" class="form-control-label">Estimated End Time</label>
									<input name="endTime" type="text"
										class="post-job-input form-control time ui-timepicker-input"
											autocomplete="off" id="endTime"></input>
								</fieldset>
								
							</div>
						</div>
	
						<div class="job-sub-info-container">
							<h3>Categories</h3>					
							
							<div class="job-sub-info">					
								<fieldset class="form-group">
			<!-- 						<label for="categories" class="form-control-label">Categories</label> -->
									<div style="min-height: 50px; display: inline"
										id="categories"></div>
									<div class="category-list-container form-group">
										<div id='0T'></div>
									</div>							
								</fieldset>
							</div>
						</div>				
					</div>
				
				
				
				</form>
			</div>
			
			<div class="col-sm-6">
			
				<div style="margin-left: 50px">
					<div class="job-questions-label"  style="border-top-style: inset"><h3>Questions</h3>						
					</div>
						
					<div id="job-questions-container" class="job-sub-info" style="margin-left: 50px;">
						
						<div id="new-question-container-container">
							<div id="new-question-container">					
								<div class="question-formats-container post-job-select-container dropdown" style="margin-bottom: 10px">									 
		
									<select class="question-formats post-job-select" title="">
									  <option selected value="-1" style="display: none">Select a Question Format</option>	
									  <option class="answer-format-item" value="0">Yes or No</option>
									  <option class="answer-format-item" value="1">Short Answer</option>
									  <option class="answer-format-item" value="2">Single Answer</option>
									  <option class="answer-format-item" value="3">Multiple Answer</option>
									</select>
		
								</div>									
								
								<div class="new-question-text">
									<textarea id="newQuestionText" style="display: inline; resize: none" name="question"
										class="form-control" rows="2"></textarea>
								</div>
								
								
								
							
								<div class="answer-option-list" style="display: none">
									<h4>Answer List</h4>
									<ul class="list-group" style="width: 225px; margin: 10px 0px 5px 0px">
										<li class="list-group-item">
											<span style="font-size: 1.5em" class="delete-answer-item glyphicon glyphicon-remove-circle"></span>
											<input style="margin-left: 10px; display:inline; width: 150px" class="form-control answer-option">
										</li>
										<li class="list-group-item">
											<span style="font-size: 1.5em" class="delete-answer-item glyphicon glyphicon-remove-circle"></span>
											<input style="margin-left: 10px; display:inline; width: 150px" class="form-control answer-option">
										</li>
										<li style="display:table; text-align:center" class="add-new-answer-item list-group-item">
											<span style="display:table-cell; vertical-align:middle; height: 5px
												font-size: 1em" class="glyphicon glyphicon-chevron-right"></span>
										</li>
									</ul>		 				
				 				
								</div>	
							
								<div>
									<span id="addNewQuestion" style="margin:10px 0px 10px 10px; font-size: small" 
									data-content="Add question" class="popover1 glyphicon glyphicon-plus"></span>
								</div>
							</div><!-- end job-questions-new -->
						</div>
					
						<div id="addedQuestions">													
						</div>
					
					</div> <!-- end job-questions-container -->
			
				</div>
			</div>
		</div><!-- end row -->
	</div><!-- end container -->
	
<!-- 			This is the template html for added questions -->
<!-- ********************************************************************************************* -->
<div style="display: none" id="added-question-template-container">
	<div class="added-question-template" style="margin: 10px 10px 10px 10px">	
		<div style="margin-top: 10px">
			
		
<!-- 				<span style="width: 100%; font-size: 20px; color: black; vertical-align: top; margin: 0px 0px 10px 10px" data-content="Hide question" -->
<!-- 				 class="border-blue hide-question popover1 glyphicon glyphicon-resize-small" aria-hidden="true"></span> -->
								
						
						
	
			<span style="font-size: 25px; vertical-align: top; margin: 10px 10px 10px 10px"
			data-content="Disable question for current job posting" 
			class="popover1 toggle-question-activeness enable-question glyphicon glyphicon-ok" aria-hidden="true"></span>
	
		
			<textarea style="display: inline; width: 75%; resize: none" name="question"
				class="form-control question-text" rows="3"></textarea>
				
			<span style="font-size: 20px; color: black; vertical-align: top; margin: 10px 0px 10px 10px" data-content="Delete question"
			 class="delete-question popover1 glyphicon glyphicon-remove" aria-hidden="true"></span>

		</div>
		
		<div>
			<span style="margin: 10px 0px 10px 10px" class="toggle-question-info-container glyphicon glyphicon-menu-right" aria-hidden="true"></span>
			<div class="question-info-container" style="margin-left: 50px; display:none">

			</div>
		</div>						
	</div>
</div>	


<!-- ********************************************************************************************* -->


<script>

	var pageContext = "postJob";
	var jobs = [];
	var jobCount = 0;
	var questions = [];
	var questionCount = 0;

	getCategoriesBySuperCat('0', function(response, categoryId) {
		appendCategories(categoryId, "T", response, function() {
		});
	})

	$(document).ready(function() {
		
		var $clonedNewQuestionContainer = $($("#new-question-container").clone(true, true));


		$('#startTime').timepicker({
			'scrollDefault' : '7:00am'
		});
		$('#endTime').timepicker({
			'scrollDefault' : '5:00pm'
		});
		
		$("#saveChanges").click(function(){			
			
			var jobId = $(this).val();
			var job = getJobById(jobId, jobs);			
		
			//Validate job name
			var requestedName = document.getElementsByName('jobName')[0].value;
			var result = 0;
			
			//If job name changed, verify it is still unique
			if(requestedName != job.jobName){
				result = validateJobName(requestedName, jobs);
			}
				
			if(result == 0){
				job = setJobInfo(job);
				$("#addedJobs button[value=" + jobId + "]").html(job.jobName);
				
				$("#startNewJob").attr("disabled", false)
				$("#startSimilarJob").attr("disabled", false)
				$("#addJob").attr("disabled", true);
				$("#saveChanges").attr("disabled", true);
				$("#deleteJob").attr("disabled", false);
				$("#editJob").attr("disabled", false);
				disableFromControls(true);
			}
			
			

		})
		
		$("#editJob").click(function(){
			//De-activate certain job actions
			$("#startNewJob").attr("disabled", false)
			$("#startSimilarJob").attr("disabled", false)
			$("#addJob").attr("disabled", true);
			$("#saveChanges").attr("disabled", false);
			$("#deleteJob").attr("disabled", false);
			$("#editJob").attr("disabled", true);
			
			disableFromControls(false);
// 			$("#jobInfoContainer").find(".form-control").each(function(){
// 				$(this).attr("disabled", false);
// 			})
		})
		
		$("#deleteJob").click(function(){
			
			
			//Get the selected job
			var jobId = $(this).val();
			var job = getJobById(jobId, jobs);

			//Display the job's info
			job = setJobInfo(job);

			//Remove the job
			$("#addedJobs button[value=" + jobId + "]").remove();			
			var i;
			for(i = 0; i < jobs.length; i++){
				if(jobs[i].id == jobId){
					jobs.splice(i, 1);
				}
			}
			
			if(jobs.length == 0){
				$("#addedJobsContainer").hide();	
				disableFromControls(false);
			}
								
			//De-activate certain job actions
			$("#startNewJob").attr("disabled", false)
			$("#startSimilarJob").attr("disabled", true)
			$("#addJob").attr("disabled", false);
			$("#saveChanges").attr("disabled", true);
			$("#deleteJob").attr("disabled", true);
			$("#editJob").attr("disabled", true);
			
			clearPostJobInputs();
			
		})
		
		$("#startNewJob, #startSimilarJob").click(function(){
			
			if($(this).attr('id') == "startNewJob"){
				clearPostJobInputs();	
			}
			
			//De-activate certain job actions
			$("#startNewJob").attr("disabled", false)
			$("#startSimilarJob").attr("disabled", true)
			$("#addJob").attr("disabled", false);
			$("#saveChanges").attr("disabled", true);
			$("#deleteJob").attr("disabled", true);
			$("#editJob").attr("disabled", true);
			
			disableFromControls(false);
			deactiveAddedJobButtons();
			removeInvalidFormControlSyles();
		})

		$("#addedJobsContainer").on('click', ".added-job", function(){
			
			removeInvalidFormControlSyles();
			disableFromControls(true);
			
				var jobId = $(this).val();		
				var job = {};
				job = getJobById(jobId, jobs);
				
				//Set button to active
				deactiveAddedJobButtons();		
				$(this).addClass("active");

				//Store the selected jobs id in case future action is taken
				$("#saveChanges").val(jobId);
				$("#deleteJob").val(jobId);
				
				//De-activate certain job actions
				$("#startNewJob").attr("disabled", false);
				$("#startSimilarJob").attr("disabled", false);
				$("#addJob").attr("disabled", true);
				$("#saveChanges").attr("disabled", true);
				$("#deleteJob").attr("disabled", false);
				$("#editJob").attr("disabled", false);
				
				//Update elements' value
				document.getElementsByName('jobName')[0].value = job.jobName;			
				document.getElementsByName('streetAddress')[0].value = job.streetAddress;
				document.getElementsByName('city')[0].value = job.city;
				document.getElementsByName('state')[0].value = job.state ;
				document.getElementsByName('zipCode')[0].value = job.zipCode;
				document.getElementsByName('jobDescription')[0].value = job.description;
				document.getElementsByName('userId')[0].value = job.userId;				
				$("#dateRange").data('daterangepicker').startDate = job.stringStartDate;
				$("#dateRange").data('daterangepicker').endDate = job.stringEndDate;
				$("#dateRange").val(job.stringStartDate.format('MM/DD/YYYY') + ' - ' + job.stringEndDate.format('MM/DD/YYYY'));
				$("#startTime").val(job.stringStartTime);
				$("#endTime").val(job.stringEndTime);
				
				//Reset all question check marks to disabled
				var i;
				var $check;
				var questionElements = $("#addedQuestions").find(".added-question");
				for(i = 0; i < questionElements.length; i++){
					 $check = $($(questionElements[i]).find(".toggle-question-activeness")[0]);
					if ($check.hasClass('enable-question') == 1){
						$check.removeClass('enable-question');
						$check.addClass('disable-question');
					}
				}
				
				//Enable the selected questions for the active job
				for(i = 0; i < job.selectedQuestionIds.length; i++){
					$check = $($("#" + job.selectedQuestionIds[i]).find(".toggle-question-activeness")[0]);
					$check.removeClass("disable-question");
					$check.addClass("enable-question");
				}
			
		
			
			
		})
		
		$(".job-questions-added-apply").click(function(){
			if($(this).hasClass("btn-default")){				
				$(this).addClass("btn-primary")
				$(this).removeClass("btn-default")
			}else{
				$(this).removeClass("btn-primary")
				$(this).addClass("btn-default")
			}
		})
		
		$("#new-question-container-container").on("click", "#addNewQuestion", function(e){
			
			
			if(validateAddQuestionInputs() == 1){

				var selectedAnswerFormatValue = $($("#new-question-container").find("select")[0]).find(":selected").val();
				var questionText = $("#newQuestionText").val();
				
	
				
				if (selectedAnswerFormatValue > -1 && questionText != "" ){
				
		// 			event.stopPropagation();
					e.preventDefault();
					
					var newId = 'question-' + questionCount;
					
					$("#added-question-template-container .added-question-template").clone().appendTo($("#addedQuestions"));			
					var $addedQuestion = $($("#addedQuestions").find(".added-question-template")[0]);
					$addedQuestion.attr('id', newId);
						
					$addedQuestion.removeClass("added-question-template");
					$addedQuestion.addClass("added-question");
					$addedQuestion.find('.question-text').val(questionText);
					
					var $addedQuestionInfoContainer = $($addedQuestion.find(".question-info-container")[0]);
					$("#new-question-container .question-formats-container").clone().appendTo($addedQuestionInfoContainer);
					$("#new-question-container .answer-option-list").clone(true, true).appendTo($addedQuestionInfoContainer);
					
					$($addedQuestion.find("select")[0]).val(selectedAnswerFormatValue).change();
																		 
					$("#new-question-container").remove();
		
					$clonedNewQuestionContainer.clone(true, true).appendTo($("#new-question-container-container"));
				
					questionCount += 1;
		
					setPopovers();

				}
			
			}

		})
		
		//This must come AFTER the "$("#addNewQuestion").on(...)" event has been bound to the
		//add new question element.
// 		var $cloned = $("#questionFormats").clone(true, true);

		$("body").on("click", ".add-new-answer-item", function(e){
// 			e.stopPropagation();
			var $clonedClone = $clonedAnswerListItem.clone(true, true);
			$clonedClone.insertBefore($(this));
		})		
		var $clonedAnswerListItem = $($($("#new-question-container").find(".list-group-item")[0]).clone(true, true));
	
		$("body").on("click", ".delete-question", function(){
			$(this).closest(".added-question").remove();	
		})
		
		$("body").on("click", ".delete-answer-item", function(){
			$(this).parent().remove();
		})
		
		$("#addedQuestions").on("click", ".toggle-question-info-container", function(){
		
			var infoContainer = $(this).siblings(".question-info-container")[0];
		
			if( $(infoContainer).is(":visible") == true){
				$(this).removeClass("glyphicon-menu-down");
				$(this).addClass("glyphicon-menu-right");
			}else{			
				$(this).removeClass("glyphicon-menu-right");
				$(this).addClass("glyphicon-menu-down");
			}
				
			$(infoContainer).toggle();

		})
	
// 		setPopovers();
		
// 		$('.popover1').popover({
// 			trigger: "hover",
// // 			selector: '.popover1',
// 			delay: {
// 				show: "500",
				
// 			}
// 		});
		
		$('#addedQuestions').on('click', '.toggle-question-activeness', function(){
			if($(this).hasClass("enable-question") == 1){
				$(this).removeClass("enable-question");
				$(this).addClass("disable-question");
				this.setAttribute("data-content", "Enable question for current job posting");
				
			}else{
				$(this).removeClass("disable-question");
				$(this).addClass("enable-question");
				this.setAttribute("data-content", "Disable question for current job posting");
			}
			
			$(".popover1").popover('hide');
		})
		
		$("#job-questions-container").on("change", "select", function(event) {
			
			event.stopPropagation();
			
			var selectedAnswerFormatValue = $($(this).find(":selected")).val();
			var $answerList = $($(this).parent().siblings(".answer-option-list")[0]);
 			
			if (selectedAnswerFormatValue == 2
 					|| selectedAnswerFormatValue == 3) {
				
				$answerList.show();
 			} else {
 				$answerList.hide();
 			}

			//If necessary, remove "invalid" css
			validateSelectInput($(this).parent(), $(this).find(":selected").val());

 		})
 		
 		$("body").on("change", ".post-job-select-container.invalid-select-input", function(){
 			validateSelectInput($(this), $(this).find('.post-job-select').val());
 		}) 	
 		
 		$("body").on("change", ".invalid-input-existence", function(){
 			validateInputExistence($(this), $(this).val());
 		}) 	 		
 		
 		
 		$("#submitJobs").click(function(){
 			var headers = {};
 			headers[$("meta[name='_csrf_header']").attr("content")] = $(
 					"meta[name='_csrf']").attr("content");

 			$.ajax({
 				type : "POST",
 				url : "http://localhost:8080/JobSearch/jobs/post",
 				headers : headers,
 				contentType : "application/json",
// 				dataType : "application/json", // Response
 				data : JSON.stringify(jobs)
 			}).done(function() {
 				
 				$('#home')[0].click();
 			}).error(function() {
 				alert("error submit jobs")
 				$('#home')[0].click();
 			});
 		})


 		
 		setDateRange();
		setStates();
	})
	
	function setDateRange(){
		
		today = new Date();
		var $e = $('#dateRange'); 
		$e.daterangepicker({
			minDate: today,
			autoUpdateInput: false,
			endDate: "",
			locale : {
				format : 'MM/DD/YYYY',		
				
			}											
		});		
		
		  $('#dateRange').on('apply.daterangepicker', function(ev, picker) {
		      $(this).val(picker.startDate.format('MM/DD/YYYY') + ' - ' + picker.endDate.format('MM/DD/YYYY'));
		      
		      //This event handles all changes for the date range picker.
		      //Therefore, the validation is also here.
		      validateInputExistence($(this), $(this).val());
		  });

// 		  $('#dateRange').on('cancel.daterangepicker', function(ev, picker) {
// 		      $(this).val('');
// 		  });		
// 		alert($e.data('daterangepicker').startdate)

	}
	
	function disableFromControls(trueFalse){
		
		$("#jobInfoContainer").find(".form-control").each(function(){
			$(this).attr("disabled", trueFalse);
		})		
	}
	
	function removeInvalidFormControlSyles(){
		
		$("#jobInfoContainer").find(".invalid-input-existence").each(function(){		
			$(this).removeClass("invalid-input-existence");
		})
		
		$("#jobInfoContainer").find(".invalid-select-input").each(function(){		
			$(this).removeClass("invalid-select-input");
		})
	}
	
	function setPopovers(){

		$('.popover1').popover({
			trigger: "hover",
			delay: {
				show: "500",		
			}
		});
		
		
	}
	
	function deactiveAddedJobButtons(){
		$("#addedJobs").find('button').each(function(){
			if($(this).hasClass("active") == 1){
				$(this).removeClass("active");
			}
		})	
	}

	function addJobToCart(){
		
		//Validate job name
// 		var $e = $(document.getElementsByName('jobName')[0]);
// 		var result = 0;
// 		result = validateInputExistence($e, $e.val());
		//If job name changed, verify it is still unique
// 		if(result == 0){
			
		
			
// 		if(validatePostJobInputs(jobs) == 1){

			var job = {};
			job = setJobInfo(job);
			job.id = jobCount;
			
			jobs.push(job);
			$("#addedJobsContainer").show();
			$("#addedJobs").append(
					'<button value=' + jobCount + ' type="button" class="added-job btn btn-info">' + job.jobName + '</button>')
			
			clearPostJobInputs();		
			jobCount++
			
			setDateRange();
// 		}
	}
	
	function setStates(){
		var $e = $("#state"); 
		$e.append('<option value="" selected style="display: none"></option>');
		$e.append('<option value="AL">AL</option>');
		$e.append('<option value="AK">AK</option>');
		$e.append('<option value="AZ">AZ</option>');
		$e.append('<option value="AR">AR</option>');
		$e.append('<option value="CA">CA</option>');
		$e.append('<option value="CO">CO</option>');
		$e.append('<option value="CT">CT</option>');
		$e.append('<option value="DE">DE</option>');
		$e.append('<option value="DC">DC</option>');
		$e.append('<option value="FL">FL</option>');
		$e.append('<option value="GA">GA</option>');
		$e.append('<option value="HI">HI</option>');
		$e.append('<option value="ID">ID</option>');
		$e.append('<option value="IL">IL</option>');
		$e.append('<option value="IN">IN</option>');
		$e.append('<option value="IA">IA</option>');
		$e.append('<option value="KS">KS</option>');
		$e.append('<option value="KY">KY</option>');
		$e.append('<option value="LA">LA</option>');
		$e.append('<option value="ME">ME</option>');
		$e.append('<option value="MD">MD</option>');
		$e.append('<option value="MA">MA</option>');
		$e.append('<option value="MI">MI</option>');
		$e.append('<option value="MN">MN</option>');
		$e.append('<option value="MS">MS</option>');
		$e.append('<option value="MO">MO</option>');
		$e.append('<option value="MT">MT</option>');
		$e.append('<option value="NE">NE</option>');
		$e.append('<option value="NV">NV</option>');
		$e.append('<option value="NH">NH</option>');
		$e.append('<option value="NJ">NJ</option>');
		$e.append('<option value="NM">NM</option>');
		$e.append('<option value="NY">NY</option>');
		$e.append('<option value="NC">NC</option>');
		$e.append('<option value="ND">ND</option>');
		$e.append('<option value="OH">OH</option>');
		$e.append('<option value="OK">OK</option>');
		$e.append('<option value="OR">OR</option>');
		$e.append('<option value="PA">PA</option>');
		$e.append('<option value="RI">RI</option>');
		$e.append('<option value="SC">SC</option>');
		$e.append('<option value="SD">SD</option>');
		$e.append('<option value="TN">TN</option>');
		$e.append('<option value="TX">TX</option>');
		$e.append('<option value="UT">UT</option>');
		$e.append('<option value="VT">VT</option>');
		$e.append('<option value="VA">VA</option>');
		$e.append('<option value="WA">WA</option>');
		$e.append('<option value="WV">WV</option>');
		$e.append('<option value="WI">WI</option>');
		$e.append('<option value="WY">WY</option>');
		
		
	}

	
	

</script>



<!-- <!doctype html> -->
<!-- <html lang="en" class="no-js"> -->
<!-- <head> -->
<!-- 	<meta charset="UTF-8"> -->
<!-- 	<meta name="viewport" content="width=device-width, initial-scale=1"> -->

<!-- 	<link href='http://fonts.googleapis.com/css?family=Open+Sans:400,300,600,700' rel='stylesheet' type='text/css'> -->

<!-- 	<link rel="stylesheet" href="http://localhost:8080/JobSearch/static/External/Codyhouse/content-filter/css/reset.css"> CSS reset -->
<!-- 	<link rel="stylesheet" href="http://localhost:8080/JobSearch/static/External/Codyhouse/content-filter/css/style.css"> Resource style -->
<!-- 	<script src="http://localhost:8080/JobSearch/static/External/Codyhouse/content-filter/js/modernizr.js"></script> Modernizr -->
  	
<!-- 	<title>Content Filters | CodyHouse</title> -->
<!-- </head> -->
<!-- <body> -->
<!-- 	<header class="cd-header"> -->
<!-- 		<h1>Content Filters</h1> -->
<!-- 	</header> -->

<!-- 	<main class="cd-main-content"> -->
<!-- 		<div class="cd-tab-filter-wrapper"> -->
<!-- 			<div class="cd-tab-filter"> -->
<!-- 				<ul class="cd-filters"> -->
<!-- 					<li class="placeholder">  -->
<!-- 						<a data-type="all" href="#0">All</a> selected option on mobile -->
<!-- 					</li>  -->
<!-- 					<li class="filter"><a class="selected" href="#0" data-type="all">All</a></li> -->
<!-- 					<li class="filter" data-filter=".color-1"><a href="#0" data-type="color-1">Color 1</a></li> -->
<!-- 					<li class="filter" data-filter=".color-2"><a href="#0" data-type="color-2">Color 2</a></li> -->
<!-- 				</ul> cd-filters -->
<!-- 			</div> cd-tab-filter -->
<!-- 		</div> cd-tab-filter-wrapper -->

<!-- 		<section class="cd-gallery"> -->
<!-- 			<ul> -->
<!-- 				<li class="mix color-1 check1 radio2 option3"><img src="img/img-1.jpg" alt="Image 1"></li> -->
<!-- 				<li class="mix color-2 check2 radio2 option2"><img src="img/img-2.jpg" alt="Image 2"></li> -->
<!-- 				<li class="mix color-1 check3 radio3 option1"><img src="img/img-3.jpg" alt="Image 3"></li> -->
<!-- 				<li class="mix color-1 check3 radio2 option4"><img src="img/img-4.jpg" alt="Image 4"></li> -->
<!-- 				<li class="mix color-1 check1 radio3 option2"><img src="img/img-5.jpg" alt="Image 5"></li> -->
<!-- 				<li class="mix color-2 check2 radio3 option3"><img src="img/img-6.jpg" alt="Image 6"></li> -->
<!-- 				<li class="mix color-2 check2 radio2 option1"><img src="img/img-7.jpg" alt="Image 7"></li> -->
<!-- 				<li class="mix color-1 check1 radio3 option4"><img src="img/img-8.jpg" alt="Image 8"></li> -->
<!-- 				<li class="mix color-2 check1 radio2 option3"><img src="img/img-9.jpg" alt="Image 9"></li> -->
<!-- 				<li class="mix color-1 check3 radio2 option4"><img src="img/img-10.jpg" alt="Image 10"></li> -->
<!-- 				<li class="mix color-1 check3 radio3 option2"><img src="img/img-11.jpg" alt="Image 11"></li> -->
<!-- 				<li class="mix color-2 check1 radio3 option1"><img src="img/img-12.jpg" alt="Image 12"></li> -->
<!-- 				<li class="gap"></li> -->
<!-- 				<li class="gap"></li> -->
<!-- 				<li class="gap"></li> -->
<!-- 			</ul> -->
<!-- 			<div class="cd-fail-message">No results found</div> -->
<!-- 		</section> cd-gallery -->

<!-- 		<div class="cd-filter"> -->
<!-- 			<form> -->
<!-- 				<div class="cd-filter-block"> -->
<!-- 					<h4>Search</h4> -->
					
<!-- 					<div class="cd-filter-content"> -->
<!-- 						<input type="search" placeholder="Try color-1..."> -->
<!-- 					</div> cd-filter-content -->
<!-- 				</div> cd-filter-block -->

<!-- 				<div class="cd-filter-block"> -->
<!-- 					<h4>Check boxes</h4> -->

<!-- 					<ul class="cd-filter-content cd-filters list"> -->
<!-- 						<li> -->
<!-- 							<input class="filter" data-filter=".check1" type="checkbox" id="checkbox1"> -->
<!-- 			    			<label class="checkbox-label" for="checkbox1">Option 1</label> -->
<!-- 						</li> -->

<!-- 						<li> -->
<!-- 							<input class="filter" data-filter=".check2" type="checkbox" id="checkbox2"> -->
<!-- 							<label class="checkbox-label" for="checkbox2">Option 2</label> -->
<!-- 						</li> -->

<!-- 						<li> -->
<!-- 							<input class="filter" data-filter=".check3" type="checkbox" id="checkbox3"> -->
<!-- 							<label class="checkbox-label" for="checkbox3">Option 3</label> -->
<!-- 						</li> -->
<!-- 					</ul> cd-filter-content -->
<!-- 				</div> cd-filter-block -->

<!-- 				<div class="cd-filter-block"> -->
<!-- 					<h4>Select</h4> -->
					
<!-- 					<div class="cd-filter-content"> -->
<!-- 						<div class="cd-select cd-filters"> -->
<!-- 							<select class="filter" name="selectThis" id="selectThis"> -->
<!-- 								<option value="">Choose an option</option> -->
<!-- 								<option value=".option1">Option 1</option> -->
<!-- 								<option value=".option2">Option 2</option> -->
<!-- 								<option value=".option3">Option 3</option> -->
<!-- 								<option value=".option4">Option 4</option> -->
<!-- 							</select> -->
<!-- 						</div> cd-select -->
<!-- 					</div> cd-filter-content -->
<!-- 				</div> cd-filter-block -->

<!-- 				<div class="cd-filter-block"> -->
<!-- 					<h4>Radio buttons</h4> -->

<!-- 					<ul class="cd-filter-content cd-filters list"> -->
<!-- 						<li> -->
<!-- 							<input class="filter" data-filter="" type="radio" name="radioButton" id="radio1" checked> -->
<!-- 							<label class="radio-label" for="radio1">All</label> -->
<!-- 						</li> -->

<!-- 						<li> -->
<!-- 							<input class="filter" data-filter=".radio2" type="radio" name="radioButton" id="radio2"> -->
<!-- 							<label class="radio-label" for="radio2">Choice 2</label> -->
<!-- 						</li> -->

<!-- 						<li> -->
<!-- 							<input class="filter" data-filter=".radio3" type="radio" name="radioButton" id="radio3"> -->
<!-- 							<label class="radio-label" for="radio3">Choice 3</label> -->
<!-- 						</li> -->
<!-- 					</ul> cd-filter-content -->
<!-- 				</div> cd-filter-block -->
<!-- 			</form> -->

<!-- 			<a href="#0" class="cd-close">Close</a> -->
<!-- 		</div> cd-filter -->

<!-- 		<a href="#0" class="cd-filter-trigger">Filters</a> -->
<!-- 	</main> cd-main-content -->
<!-- <script src="http://localhost:8080/JobSearch/static/External/Codyhouse/content-filter/js/jquery-2.1.1.js"></script> -->
<!-- <script src="http://localhost:8080/JobSearch/static/External/Codyhouse/content-filter/js/jquery.mixitup.min.js"></script> -->
<!-- <script src="http://localhost:8080/JobSearch/static/External/Codyhouse/content-filter/js/main.js"></script> Resource jQuery -->
<!-- </body> -->
<!-- </html> -->

<%@ include file="./includes/Footer.jsp"%>