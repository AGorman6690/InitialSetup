var $calendar_workDays;
var $calendar_times;
//var workDayDtos = [];
var workDayDtos_original = [];
var workDayDtos = [];

$(document).ready(function(){
	
	$calendar_workDays = $("#workDaysCalendar_postJob");
	$calendar_times= $("#select-times-cal");
	
	workDayDtos = [];
	initCalendar_selectWorkDays($calendar_workDays, $calendar_times, 2);
	
	$("body").on("click", ".calendar", function() {
//		alert(334)
	})
	

	
	
	
	$("#next-section, #previous-section").click(function(){
		
		var $nextSection;
		var $selectedSection = $(".select-page-section-container").find(".select-page-section.selected").eq(0);			

		if($(this).attr("id") == "next-section") $nextSection = $selectedSection.next();
		else  $nextSection = $selectedSection.prev();
		
		if($nextSection.hasClass("select-page-section") == 0){
			$nextSection = $("#select-page-section-container").find(".select-page-section").eq(0);
		}		
		$nextSection.click();				
	})	
	
	$("#select-times.select-page-section").click(function(){
		
//		if($calendar_times.attr("data-required-updating") == "1")
//			initCalendar_setStartAndEndTimes($calendar_times, workDayDtos);
		
	})
	

	$("#proceed-to-preview-job-posting").click(function(){
//		 executeAjaxCall_previewJobPosting( getJobDto());
		var jobDto = getJobDto()
		if(arePostJobInputsValid(jobDto)){
			executeAjaxCall_previewJobPosting(jobDto);
		}
	})
	
	$("#editPosting").click(function(){
		setDisplay_previewJobPost(false);	
	})
	
	$("#submitPosting_final").click(function(){
		executeAjaxCall_postJob(getJobDto());
//		if(arePostJobInputsValid()) executeAjaxCall_postJob(getJobDto());
	})
	
	$("#no-dates-selected").click(function(){
		$("#show-dates-section").click();
	})
	
	$("#datesContainer #clearCalendar").click(function(){
		
		resetCalendar();
		resetTimesSection();

	})
	
	$("#startNewJob").click(function(){
		showPostJobSections();
	})

	
	$("#previous-job-posts div[data-posted-job-id]").click(function(){
		importPreviousJobPosting($(this).attr("data-posted-job-id"));
		
		$(this).closest("#postedJobsContainer").find("[data-toggle-id]").eq(0).click();
		
		$("#postSections").find(".post-section").eq(0).click();
	})
	
	$("#postedQuestions div[data-question-id]").click(function(){
		importPreviousQuestion($(this).attr("data-question-id"));
		$("#copy-previous-question").click();
		
	})
	
	$("body").on("click", "#employeeSkillsContainer .add-list-item", function(){
		
		addAnotherSkill($(this).siblings(".list-items-container").eq(0));
		
	})
	
	$("body").on("click", ".skills-container .delete-list-item", function(){
		
		deleteSkill($(this).closest(".list-item"));
		
	})	
	

	
	setStates();
	setTimeOptions($("#single-start-time"), 30);
	setTimeOptions($("#single-end-time"), 30);
	setTimeOptions($("#multiple-start-times"), 30);
	setTimeOptions($("#multiple-end-times"), 30);
	setTimeOptions($("#startTime-singleDate"), 30);
	setTimeOptions($("#endTime-singleDate"), 30);
	setTimeOptions($("#endTime-singleDate"), 30);
	
//	var workDayDtos = [];

	
	
	$("body").on("mouseover", "#workDaysCalendar_postJob.show-hover-range td", function(){
		
		var hoverDate = getDateFromTdElement(this);
		var firstDate = dateify($("#workDaysCalendar_postJob").attr("data-first-date"));

		showHoverDateRange($("#workDaysCalendar_postJob.show-hover-range"), hoverDate, firstDate);
		
	})
	
})

function resetCalendar(){
	workDayDtos = [];	
	$calendar_workDays.datepicker("refresh");
	$calendar_workDays.removeClass("show-hover-range");
}


function resetTimesSection(){
	$("#times-cont").hide();
	$("#no-dates-selected").show();	
	$("#initial-time-question").hide();
	$("#set-one-start-and-end-time").hide();
	
	var $initalTimeQuestion = $("#initial-time-question");
	$initalTimeQuestion.hide();
	$initalTimeQuestion.find("button").each(function(){ $(this).removeClass("selected"); })
}

function setDisplay_previewJobPost(doShowPreview){

	if(doShowPreview){
		$("#preview-job-posting-container").show();
		$("#post-job-container").hide();
		$("#postSections").hide();		
	}
	else{
		$("#preview-job-posting-container").hide();
		$("#post-job-container").show();
		$("#postSections").show();
	}
	
}

function setDisplay_createQuestionContainer(doShow){
	if(doShow) $("#create-question-container").show();
	else $("#create-question-container").hide();
}

function addAnotherSkill($listItemsContainer){
	var $aSkillContainer = $listItemsContainer.find(".list-item").eq(0);			
	var clone = $aSkillContainer.clone(true);			
	
	//Clear the input
	$(clone).find("input").val("");
	
	$listItemsContainer.append(clone);
}

function deleteSkill($clickedListItem){
	
	var $listItemsContainer = $clickedListItem.closest(".list-items-container");
	
	// There must remain at least 1 skill
	if($listItemsContainer.find(".list-item").length > 1){
		$clickedListItem.remove();
	}
	else{
		$clickedListItem.find("input").eq(0).val("");
	}
}

function importPreviousQuestion(questionId){
	
	broswerIsWaiting(true);
	$.ajax({
		type : "GET",
		url: '/JobSearch/post-job/previous-question/load?questionId=' + questionId,
		headers : getAjaxHeaders(),
//		contentType : "application/json",
//		data : JSON.stringify(postJobDto),
		dataType : "json",
		success : _success,
		error : _error,
		cache: true
	});

	function _success(questionDto) {			
		
		broswerIsWaiting(false);
		
		// Reset the question section
		clearAllInputs($("#questionsContainer"));
		enableAllInputFields($("#questionsContainer"));
		$("#answerListContainer").hide();
		
		setClickableness_ForQuestionActions(true, true, false, false);
		
		showQuestionDto(questionDto);
		
		setDisplay_createQuestionContainer(true);
	}	

	function _error() {
		broswerIsWaiting(false);
		alert('DEBUG: error executeAjaxCall_saveFindJobFilter')		
	}
}

function importPreviousJobPosting(jobId){
	
	broswerIsWaiting(true);
	$.ajax({
		type : "GET",
		url: '/JobSearch/post-job/previous-post/load?jobId=' + jobId,
		headers : getAjaxHeaders(),
//		contentType : "application/json",
//		data : JSON.stringify(postJobDto),
		dataType : "json",		
		success : _success,
		error : _error,
		cache: true
	});

	function _success(jobDto) {		
		broswerIsWaiting(false);	
		jobDto.workDayDtos = dateifyWorkDayDtos(jobDto.workDayDtos);
		setControlValues(jobDto);
		showPostJobSections();

	}	

	function _error() {
		broswerIsWaiting(false);
		alert('DEBUG: error executeAjaxCall_saveFindJobFilter')		
	}
}

function showPostJobSections(){
	$(".hide-on-load").each(function(){ $(this).removeClass("hide-on-load") });
	$("#copy-or-new-container").hide();
}

function setControlValues(jobDto){
	
	$("#name").val(jobDto.job.jobName);
	$("#description").val(jobDto.job.description);
	$("#street").val(jobDto.job.streetAddress);
	$("#city").val(jobDto.job.city);
	$("#state").val(jobDto.job.state);
	$("#zipCode").val(jobDto.job.zipCode);
	
	// Dates
//	if(jobDto.job.isPartialAvailabilityAllowed) $("#yes-partial").click();
//	else $("#no-partial").click();	
//	workDayDtos = jobDto.workDayDtos;
//	$calendar_workDays.datepicker("destroy");
	initCalendar_selectWorkDays($calendar_workDays, $calendar_times, 2);

	// Times
//	if(jobDto.areAllTimesTheSame){
//		$("#times-are-the-same").click();
//		$("#single-start-time option[data-filter-value='" +
//				jobDto.workDayDtos[0].workDay.stringStartTime + "']").prop("selected", true);
//		$("#single-end-time option[data-filter-value='" +
//				jobDto.workDayDtos[0].workDay.stringEndTime + "']").prop("selected", true);
//	}else{
//		$("#times-are-not-the-same").click();
//		initCalendar_setStartAndEndTimes($calendar_times);
//	}

	
	// Questions
	resetEntireQuestionSection();	
	$(jobDto.questions).each(function(){
		showQuestionDto(this);
		addQuestion();
	})
	
	// Skills
	var len = jobDto.skillsRequired.length;
	var $addListItem = $("#requiredSkillsContainer").siblings(".add-list-item").eq(0);
	$(jobDto.skillsRequired).each(function(i, e){
		var $e = $("#requiredSkillsContainer").find(".list-item input").last();
		$e.val(this.text);
		
		if(i < len - 1) $addListItem.click();
	})
	
	var len = jobDto.skillsDesired.length;
	var $addListItem = $("#desiredSkillsContainer").siblings(".add-list-item").eq(0);
	$(jobDto.skillsDesired).each(function(i, e){
		var $e = $("#desiredSkillsContainer").find(".list-item input").last();
		$e.val(this.text);
		
		if(i < len - 1) $addListItem.click();
	})
	
}

function resetSkills(){

	$("#employeeSkillsContainer").find(".list-item .delete-list-item").each(function(){
		$(this).click();
	})
}

function resetDatesAndTimes(){
	workDayDtos = [];
	$("#workDaysCalendar_postJob").datepicker("refresh");
}



function getJobDto(){
	
	// *********************************************************
	// *********************************************************
	// Need to validate skills' character length
	// *********************************************************
	// *********************************************************
	
	
	var jobDto = {};
	
	jobDto.questions = [];
	jobDto.categoryIds = [];
	jobDto.workDays = [];
	jobDto.skills = [];
	jobDto.job = {};
	
	jobDto.questions = questions;
	jobDto.workDays = getWorkDays();
	jobDto.categoryIds.push(1);
	jobDto.skills = getSkills();
	
	jobDto.job.jobName = $("#name").val();
	jobDto.job.description = $("#description").val();
	jobDto.job.streetAddress = $("#street").val();
	jobDto.job.city = $("#city").val();
	jobDto.job.state = $("#state").val();
	jobDto.job.zipCode = $("#zipCode").val();
	jobDto.job.positionsPerDay = $("#positionsContainer input").val();
	
	if($("#no-partial").is(":checked")) jobDto.job.isPartialAvailabilityAllowed = 0;
	else jobDto.job.isPartialAvailabilityAllowed = 1;

	
	return jobDto;
	
}

function getSkills(){
	
	var skills = [];
	var skill = {};
	
	$("#requiredSkillsContainer").find(".list-item input").each(function(){
		
		if($(this).val() != ""){
			skill = {};
			skill.text = $(this).val();
			skill.type = 0;
			
			skills.push(skill);
		}
		 
	})
	
	$("#desiredSkillsContainer").find(".list-item input").each(function(){
		
		if($(this).val() != ""){
			skill = {};
			skill.text = $(this).val();
			skill.type = 1;
			
			skills.push(skill);
		}
		 
	})
	
	return skills;
	
}

function getWorkDays(){

	var workDays = [];
	if($("#same-times").is(":checked")){
		var singleStartTime = $("#single-start-time").find("option:selected").attr("data-filter-value");
		var singleEndTime = $("#single-end-time").find("option:selected").attr("data-filter-value");
	
		$(workDayDtos).each(function(){			
			var workDay = {};
			workDay.stringDate = $.datepicker.formatDate("yy-mm-dd", this.date);
			workDay.stringStartTime = singleStartTime;
			workDay.stringEndTime = singleEndTime;			
			workDays.push(workDay);
		})
	}else{
	
		$(workDayDtos).each(function(){
			
			var workDay = {};
			workDay.stringDate = $.datepicker.formatDate("yy-mm-dd", this.date);
			workDay.stringStartTime = this.workDay.stringStartTime;
			workDay.stringEndTime = this.workDay.stringEndTime;
			
			workDays.push(workDay);
		})	
	}

	
	return workDays;
}

function executeAjaxCall_postJob(jobDto){
	broswerIsWaiting(true);
	$.ajax({
		type : "POST",
		url: '/JobSearch/job/post',
		headers : getAjaxHeaders(),
		contentType : "application/json",
		data : JSON.stringify(jobDto),
//		dataType : "json",		
		success : _success,
		error : _error,
		cache: true
	});

	function _success() {
				
		window.location.replace("/JobSearch/user/profile");
		broswerIsWaiting(false);	
	}	

	function _error() {
		broswerIsWaiting(false);
		alert('DEBUG: error executeAjaxCall_saveFindJobFilter')		
	}
}

function executeAjaxCall_previewJobPosting(jobDto){
	
	broswerIsWaiting(true);
	$.ajax({
		type : "POST",
		url: '/JobSearch/preview/job-info',
		headers : getAjaxHeaders(),
		contentType : "application/json",
		data : JSON.stringify(jobDto),
		dataType : "html",	
		async: false,
		success : _success,
		error : _error,
		cache: true
	});

	function _success(html_jobInfo) {
		broswerIsWaiting(false);	
	
		setDisplay_previewJobPost(true);
	
		$("#displayExample_jobInfo").html(html_jobInfo);
		
		var $e = $("#json_work_day_dtos");
		workDayDtos_preview = JSON.parse($e.html());
		$e.empty(); 
//		setWorkDays();
		initCalendar_jobInfo_workDays($("#work-days-calendar-container .calendar"), workDayDtos_preview);
		initMap();
		$("[data-toggle-id]").click();
//		$.getScript("/JobSearch/static/javascript/JobInfo.js", function(){alert(789)});
	}		

	function _error() {
		broswerIsWaiting(false);
		alert('DEBUG: error executeAjaxCall_saveFindJobFilter')		
	}
}
