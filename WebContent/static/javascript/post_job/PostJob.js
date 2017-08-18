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
	
	$("#show-dates-section").click(function() {
		// ***********************************
		// see note in 	initCalendar_selectWorkDays::afterShow();
		// ***********************************
		changePrevNextText($calendar_workDays, "<<", ">>");
	})
	
	$("#next-section").click(function(){
		
		var $selectedSection = $(".select-page-section-container").find(".select-page-section.selected").eq(0);			

		if($selectedSection.attr("id") == "show-skills"){
			$("#proceed-to-preview-job-posting").click();
		}
		else{
			$selectedSection.nextAll(".select-page-section:visible").first().click();
		}
				
	})	
	
	$("#previous-section").click(function(){		
		var $selectedSection = $(".select-page-section-container").find(".select-page-section.selected").eq(0);			
		$selectedSection.prevAll(".select-page-section:visible").first().click();				
	})	

	$("#proceed-to-preview-job-posting").click(function(){

		var addJobRequest = getAddJobRequest()
//		if(arePostJobInputsValid(jobDto)){
		
			var addressToValidate = ""
			addressToValidate += addJobRequest.job.streetAddress;
			addressToValidate += " " + addJobRequest.job.city;
			addressToValidate += " " + addJobRequest.job.state;
			addressToValidate += " " + addJobRequest.job.zipCode;
			
			executeAjaxCall_isAddressValid(addressToValidate, function(response){
				response = JSON.parse(response);
				if(response.isValid == true){			
					$("#invalid-address-error-message").hide();
					executeAjaxCall_previewJobPosting(addJobRequest);
				}else{
					$("#show-location").click();
					$("#invalid-address-error-message").show();
				}				
			})
//		}
	})
	
	$("#editPosting").click(function(){
		setDisplay_previewJobPost(false);	
	})
	
	$("body").on("click", "#submit-job-post", function(){
		executeAjaxCall_postJob(getAddJobRequest());
	})
	
	$("#no-dates-selected").click(function(){
		$("#show-dates-section").click();
	})
	
	$("#dates-wrapper #clear-calendar").click(function(){		
		resetCalendar();
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
	
	$("#supreme-times-wrapper").on("change", "select.start-time", function() {
		var $e = $(this).find("option:selected");
		var date = $e.closest(".time-wrapper").find(".date").attr("data-date");
		var workDayDto = getWorkDayDtoByDate(dateify(date), workDayDtos);
		workDayDto.workDay.startTime = $e.attr("data-filter-value");
	})
	$("#supreme-times-wrapper").on("change", "select.end-time", function() {
		var $e = $(this).find("option:selected");
		var date = $e.closest(".time-wrapper").find(".date").attr("data-date");
		var workDayDto = getWorkDayDtoByDate(dateify(date), workDayDtos);
		workDayDto.workDay.endTime = $e.attr("data-filter-value");
	})
		
	$("#set-all-times").change(function() {
		if($(this).is(":checked")){
			$("#set-all-times-wrapper").find("select").show();
			$("#supreme-times-wrapper").addClass("setting-all-times");
		}else{
			$("#set-all-times-wrapper").find("select").hide();
			$("#supreme-times-wrapper").removeClass("setting-all-times");
		}
	})
	
	$("select#set-all-start-times").change(function() {
		var startTime = $(this).find("option:selected").eq(0).attr("data-filter-value");
		$(workDayDtos).each(function() {
			this.workDay.startTime = startTime;
		})
		renderWorkDayTimes();
	})
	$("select#set-all-end-times").change(function() {
		var endTime = $(this).find("option:selected").eq(0).attr("data-filter-value");
		$(workDayDtos).each(function() {
			this.workDay.endTime = endTime;
		})
		renderWorkDayTimes();
	})
	
	setStates();
	setTimeOptions($("#set-all-end-times"), 30, "start time");
	setTimeOptions($("#set-all-start-times"), 30, "end time");
	
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
	$("#dates-wrapper").removeClass("multiple-work-days");
}


function resetTimesSection(){
	$("#times-cont").hide();
	$("#no-dates-selected").show();	
	$("#initial-time-question").hide();
	$("#set-one-start-and-end-time").hide();
	$("#timesContainer input[type=radio]").prop("checked", false);
	
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



function getAddJobRequest(){
	
	// *********************************************************
	// *********************************************************
	// Need to validate skills' character length
	// *********************************************************
	// *********************************************************
	
	
	var addJobRequest = {};
	
	addJobRequest.questions = [];
	addJobRequest.workDays = [];
	addJobRequest.skills = [];
	addJobRequest.job = {};
	
	addJobRequest.questions = questions;
	addJobRequest.workDays = getWorkDays();
	addJobRequest.skills = getSkills();
	
	addJobRequest.job.jobName = $("#name").val();
	addJobRequest.job.description = $("#description").val();
	addJobRequest.job.streetAddress = $("#street").val();
	addJobRequest.job.city = $("#city").val();
	addJobRequest.job.state =$("#state option:selected").html();
	addJobRequest.job.zipCode = $("#zipCode").val();
	addJobRequest.job.positionsPerDay = $("#positionsContainer input").val();
	
	if($("#no-partial").is(":checked")) addJobRequest.job.isPartialAvailabilityAllowed = 0;
	else addJobRequest.job.isPartialAvailabilityAllowed = 1;
	
	return addJobRequest;	
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
		url: '/JobSearch/job',
		headers : getAjaxHeaders(),
		contentType : "application/json",
		data : JSON.stringify(jobDto),
		dataType: "text"
	}).done(function(text) {
		broswerIsWaiting(false);
		redirectToProfile();
	});
}

function executeAjaxCall_isAddressValid(address, callback) {
	$.ajax({
		type: "GET",
		headers: getAjaxHeaders(),
		url: "/JobSearch/job/validate-address?address=" + address,
		dataType: "text"
	}).done(function(response) {
		callback(response);
	})
}

function executeAjaxCall_previewJobPosting(addJobRequest){
	
	broswerIsWaiting(true);
	$.ajax({
		type : "POST",
		url: '/JobSearch/job/preview/',
		headers : getAjaxHeaders(),
		contentType : "application/json",
		data : JSON.stringify(addJobRequest),
		dataType : "html",	
		async: false,
		cache: true
	}).done( function (html) {
		broswerIsWaiting(false);
		showJobInfoMod(html)
//		renderHtml_jobInfo(html_jobInfo, false, true);

	})


}
