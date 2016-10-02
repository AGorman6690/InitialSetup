$(document).ready(function() {
	

	$("#saveChanges").click(function(){		
		
		var jobId = $("#activeJobId").val();
		var job = getJobById(jobId, jobs);			
	
		//Validate job name
		var requestedName = document.getElementsByName('jobName')[0].value;
		var result = 0;
		
		//If job name changed, verify it is still unique
		if(requestedName != job.jobName){
			result = validateJobName(requestedName, jobs);		
		}
		
		//If the job name is valid
		if(result == 0){
			job = setJobInfo(job);
			$("#addedJobs button[value=" + jobId + "]").html(job.jobName);
			
			$("#startNewJob").attr("disabled", false)
			$("#addJob").attr("disabled", true);
			$("#saveChanges").attr("disabled", true);
			$("#cancelChanges").attr("disabled", true);
			$("#deleteJob").attr("disabled", true);
			$("#editJob").attr("disabled", true);
			$("#copyJob").attr("disabled", true);
			$("#submitJobs").attr("disabled", false);
			
			disableFromControls(true);
			disableJobCart(false);
			deactiveAddedJobButtons();
			
			$("#editJobResponseContainer").hide();
		}
		

		
	})
	
	$("#cancelChanges").click(function(){
		
		//Show the job's original values
		var jobId = $("#activeJobId").val();
		showAddedJob(jobId);
		
		$("#startNewJob").attr("disabled", false)
		$("#addJob").attr("disabled", true);
		$("#saveChanges").attr("disabled", true);
		$("#cancelChanges").attr("disabled", true);
		$("#deleteJob").attr("disabled", true);
		$("#editJob").attr("disabled", true);
		$("#copyJob").attr("disabled", true);
		$("#submitJobs").attr("disabled", false);

		disableFromControls(true);
		disableJobCart(false);
		deactiveAddedJobButtons();
		
		$("#editJobResponseContainer").hide();
		

	})

	$("#editJob").click(function(){
		
		//De-activate certain job actions
		$("#startNewJob").attr("disabled", true);
		$("#addJob").attr("disabled", true);
		$("#saveChanges").attr("disabled", false);
		$("#cancelChanges").attr("disabled", false);
		$("#deleteJob").attr("disabled", true);
		$("#editJob").attr("disabled", true);
		$("#copyJob").attr("disabled", true);
		$("#submitJobs").attr("disabled", true);
		
		$("#editJobResponseContainer").show();		
		disableFromControls(false);
		disableJobCart(true);
//		disableGeneralControls(false);
//		disableNewQuestionControls(true);

	})
	
	$("#startNewJob").click(function(){

		clearPostJobInputs();
		disableFromControls(false);
		deactiveAddedJobButtons();
		removeInvalidFormControlStyles();
				
		//De-activate certain job actions
		$("#startNewJob").attr("disabled", true)
		$("#addJob").attr("disabled", false);
		$("#saveChanges").attr("disabled", true);
		$("#deleteJob").attr("disabled", true);
		$("#editJob").attr("disabled", true);
		$("#copyJob").attr("disabled", true);
		$("#submitJobs").attr("disabled", false);

	})
	
	$("#copyJob").click(function(){

		//Job names must be unique, so make them enter a new one
		document.getElementsByName('jobName')[0].value = "";
		
		disableFromControls(false);
		deactiveAddedJobButtons();
		removeInvalidFormControlStyles();

		//De-activate certain job actions
		$("#startNewJob").attr("disabled", false)
		$("#addJob").attr("disabled", false);
		$("#saveChanges").attr("disabled", true);
		$("#deleteJob").attr("disabled", true);
		$("#editJob").attr("disabled", true);
		$("#copyJob").attr("disabled", true);
		$("#submitJobs").attr("disabled", false);
		
	})
	
//	$("#deleteJob").click(function(){
//	
//		//Check if the alert has been disabled.
//		//If it has been disabled, then the toggle attr will be empty.
//		//Delete the job if alert has been disabled.
//		if($(this).data("toggle") != "modal"){
//			deleteJob(1);
//		}
//	})
	
	$("#disableJobDeleteAlert").click(function(){
		
		if($(this).is(":checked") == 1){
			$("#deleteJob").data("toggle", "");
		}else{
			$("#deleteJob").data("toggle", "modal");
		}
	})
	
	
	
	$("#addedJobsContainer").on('click', ".added-job", function(){
		clearPostJobInputs();
		showAddedJob($(this).val());	
		
		//Set button to active
		deactiveAddedJobButtons();		
		$(this).removeClass("inactive-button");
		$(this).addClass("active-button");
		
		$("#copyJob").removeClass("disabled");
		
		//De-activate certain job actions
		$("#startNewJob").attr("disabled", false);
		$("#addJob").attr("disabled", true);
		$("#saveChanges").attr("disabled", true);
		$("#deleteJob").attr("disabled", false);
		$("#editJob").attr("disabled", false);
		$("#copyJob").attr("disabled", false);
	})

})

	function getJobById(id, jobs){
	
		var i;
		for(i = 0; i < jobs.length; i++){
			if(jobs[i].id == id){
				return jobs[i];			
			}
		}
	}

	function addJobToCart(){
				
		if(is)
// 		if(validatePostJobInputs(jobs) == 0){
		
			jobCount += 1;
		
			var postJobDto = {};
			postJobDto = getPostJobDto();
			postJobDto.id = jobCount;			
			jobs.push(postJobDto);
			
			$("#cartContainer").show(200);

			
			$("#jobCart").append(
					'<button data-job-id=' + jobCount + ' type="button" class="added-job btn inactive-button clickable">'
							+ postJobDto.jobName + '</button>')
			
			clearPostJobInputs();		
			removeInvalidFormControlStyles()
			
			jobCount++			
//		}
	}
	
function submitJobs(confirmation){
	
	if(confirmation == 1){
			
		var headers = {};
		headers[$("meta[name='_csrf_header']").attr("content")] = $(
				"meta[name='_csrf']").attr("content");
		
		var posting ={};
		posting.jobs = jobs;
		posting.questions = questions;
//		salert(posting)

		$.ajax({
			type : "POST",
			url : environmentVariables.LaborVaultHost + "/JobSearch/jobs/post",
			headers : headers,
			contentType : "application/json",
//	 				dataType : "application/json", // Response
			data : JSON.stringify(posting)
		}).done(function() { 				
			window.location = "/JobSearch/user/profile";
		}).error(function() {
			alert("error submit jobs")
			$('#home')[0].click();
		});
	
	}

}

//function deleteJob(confirmation){
//	
//	if(confirmation == 1){
//		//Get the selected job
////		var jobId = $(this).val();
//		var jobId = $("#activeJobId").val();
//		var job = getJobById(jobId, jobs);
//
//		//Display the job's info
////		job = setJobInfo(job);
//
//		//Remove the job
//		$("#addedJobs button[value=" + jobId + "]").remove();			
//		var i;
//		for(i = 0; i < jobs.length; i++){
//			if(jobs[i].id == jobId){
//				jobs.splice(i, 1);
//			}
//		}
//		
//		if(jobs.length == 0){
//			$("#addedJobsContainer").hide();
//			$("#submitJobsContainer").hide();
//			disableFromControls(true);
//		}
//							
//		//De-activate certain job actions
//		$("#startNewJob").attr("disabled", false)
//		$("#addJob").attr("disabled", true);
//		$("#saveChanges").attr("disabled", true);
//		$("#deleteJob").attr("disabled", true);
//		$("#editJob").attr("disabled", true);
//		$("#copyJob").attr("disabled", true);
//		
//		clearPostJobInputs();
//		deactiveAddedJobButtons();			
//		//For a better ux, it seems appropriate for the controls to be enabled
//		//if the current selection(s) are being unselected.
//		//It feels awkward to have the controls disabled.
//		disableInputFields(false, containerId);
//	}
//}

function getWorkDays(){
	
	var days = $("#times").find(".time");
	var date;
	var eStartTime;
	var eEndTime;	
	var workDays = [];
	var workDay;
	
	$(days).each(function(){
		
		//Read the DOM
		date = new Date;
		date.setTime($(this).attr("data-date"));		
		date = $.datepicker.formatDate("yy-mm-dd", date);
		
//		date = date.toString();
		
		
		
		eStartTime = $(this).find(".start-time")[0];
		eEndTime = $(this).find(".end-time")[0];
		
		//Set the JSON
		workDay = {};
		workDay.stringDate = date;
		workDay.stringStartTime = formatTime($(eStartTime).val());
		workDay.stringEndTime = formatTime($(eEndTime).val());
		workDay.millisecondsDate = $(this).attr("data-date");
		
		//Add to array
		workDays.push(workDay);
	})
	
	return workDays;
}

function getPostJobDto() {
	var i;
	var postJobDto = {};

	postJobDto.jobName = document.getElementsByName('name')[0].value;
	postJobDto.streetAddress = document.getElementsByName('streetAddress')[0].value;
	postJobDto.city = document.getElementsByName('city')[0].value;
	postJobDto.state = document.getElementsByName('state')[0].value;
	postJobDto.zipCode = document.getElementsByName('zipCode')[0].value;
	postJobDto.description = document.getElementsByName('description')[0].value;
//	job.stringStartDate = $("#dateRange").data('daterangepicker').startDate;
//	job.stringEndDate =  $("#dateRange").data('daterangepicker').endDate;
//	job.stringStartTime = formatTime($("#startTime").val());
//	job.stringEndTime = formatTime($("#endTime").val());
	
	
	postJobDto.workDays = getWorkDays();
	
	//set categories
	postJobDto.categoryIds = [];
	var selectedCats = $("#selectedCategories").find("button");	
	for(i = 0; i < selectedCats.length; i++){
		postJobDto.categoryIds.push($(selectedCats[i]).attr("data-cat-id"));
	}

	//Set questions
	postJobDto.selectedQuestionIds = [];
	var questions = $("#addedQuestions").find(".added-question");
	for(i = 0; i < questions.length; i++){	
		var question = questions[i];	
		
		//Check if question is enabled
		var $enableQuestion = $($(question).find(".toggle-question-activeness")[0]);
		if($enableQuestion.hasClass("enable-question")){
			postJobDto.selectedQuestionIds.push($(question).data("questionId"));
		}
	}
	
	return postJobDto;

}