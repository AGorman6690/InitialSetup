var $calendar_workDays;
var $calendar_times;
//var workDayDtos = [];
var workDayDtos_original = [];
var workDayDtos = [];

$(document).ready(function(){
	
	$("#location-wrapper h3").click(function(){
		// ************************************
		// For debugging.
		$("#street").val("2217 Bonnie Lane");
		$("#city").val("St. Paul");
		$("#state").find("option[value=Minnesota]").prop("selected", true);
	})
	
	
	$calendar_workDays = $("#workDaysCalendar_postJob");
	$calendar_times= $("#select-times-cal");
	
	workDayDtos = [];
	initCalendar_selectWorkDays($calendar_workDays, $calendar_times, 2);

	$("#side-bar [data-scroll-to]").click(function() {
		var $e = $("#" + $(this).attr("data-scroll-to"));
	   $('html, body').animate({
	        scrollTop: $e.offset().top
	    }, 500);
	})
	
	$("#proceed-to-preview-job-posting").click(function(){

		if(1 || validateInputElements($("#post-job-info"), $("#submit-wrapper"))){	
			var addJobRequest = getAddJobRequest();			
			var addressToValidate = "";
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
		}
	})
	
	$("#post-job-info .section").mouseover(function() {
		addHoverClassToSideBarSpan(this.id, true);
	})
	$("#post-job-info .section").mouseout(function() {
		addHoverClassToSideBarSpan(this.id, false);
	})
	$("body").on("click", "#submit-job-post", function(){
		executeAjaxCall_postJob(getAddJobRequest());
	})
	$("#dates-wrapper #clear-calendar").click(function(){		
		resetCalendar();
	})
	$("#previous-job-posts div[data-posted-job-id]").click(function(){
		importPreviousJobPosting($(this).attr("data-posted-job-id"));		
		$(this).closest("#postedJobsContainer").find("[data-toggle-id]").eq(0).click();		
		$("#postSections").find(".post-section").eq(0).click();
	})
	$("#posted-questions [data-question-id]").click(function(){
		importPreviousQuestion($(this).attr("data-question-id"));
		$("#copy-previous-question").click();		
	})
	$("body").on("click", "#skills-wrapper .add-list-item", function(){		
		addAnotherSkill($(this).siblings(".list-items-container").eq(0));		
	})
	$("body").on("click", ".skills-container .delete-list-item", function(){		
		deleteSkill($(this).closest(".list-item"));		
	})	
	$("#supreme-times-wrapper").on("change", "select.start-time", function() {
		var $e = $(this).find("option:selected");
		var date = $e.closest(".time-wrapper").find(".date").attr("data-date");
		var workDayDto = getWorkDayDtoByDate(dateify(date), workDayDtos);
		workDayDto.workDay.stringStartTime = $e.attr("data-filter-value");
	})
	$("#supreme-times-wrapper").on("change", "select.end-time", function() {
		var $e = $(this).find("option:selected");
		var date = $e.closest(".time-wrapper").find(".date").attr("data-date");
		var workDayDto = getWorkDayDtoByDate(dateify(date), workDayDtos);
		workDayDto.workDay.stringEndTime = $e.attr("data-filter-value");
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
			this.workDay.stringStartTime = startTime;
		})
		renderWorkDayTimes();
	})
	$("select#set-all-end-times").change(function() {
		var endTime = $(this).find("option:selected").eq(0).attr("data-filter-value");
		$(workDayDtos).each(function() {
			this.workDay.stringEndTime = endTime;
		})
		renderWorkDayTimes();
	})
	$("body").on("click", "#edit-job-post", function(){
		$("#job-info-mod .mod-header").click();
	})
	
	setStates();
	setTimeOptions($("#set-all-end-times"), 30, "end time");
	setTimeOptions($("#set-all-start-times"), 30, "start time");
	
	$("body").on("mouseover", "#workDaysCalendar_postJob.show-hover-range td", function(){	
		var hoverDate = getDateFromTdElement(this);
		var firstDate = dateify($("#workDaysCalendar_postJob").attr("data-first-date"));
		showHoverDateRange($("#workDaysCalendar_postJob.show-hover-range"), hoverDate, firstDate);		
	})	
})
function addHoverClassToSideBarSpan(id, request){
	var $e = $("[data-scroll-to='" + id + "'] span").eq(0);
	if(request){
		$e.addClass("hover");
	}else{
		$e.removeClass("hover");
	}
}
function resetCalendar(){
	workDayDtos = []
	$calendar_workDays.datepicker("refresh")
	$calendar_workDays.removeClass("show-hover-range")
	$("#dates-wrapper").removeClass("multiple-work-days")
	$("#set-all-times").prop("checked", false).change();
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
function addAnotherSkill($listItemsContainer){
	var $aSkillContainer = $listItemsContainer.find(".list-item").eq(0);			
	var clone = $aSkillContainer.clone(true);			
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
		showQuestionDto(questionDto);		
		showCreateQuestionContainer(true);
	}	

	function _error() {
		broswerIsWaiting(false);
		alert('DEBUG: error executeAjaxCall_saveFindJobFilter')		
	}
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
	addJobRequest.job.zipCode = $("#zip-code").val();
	
	if(addJobRequest.workDays.length == 1){
		addJobRequest.job.isPartialAvailabilityAllowed = 0;
	}else{
		if($("#no-partial").is(":checked")) addJobRequest.job.isPartialAvailabilityAllowed = 0;
		else addJobRequest.job.isPartialAvailabilityAllowed = 1;		
	}

	return addJobRequest;	
}

function getSkills(){	
	var skills = [];
	var skill = {};	
	$("#required-skills-container").find(".list-item input").each(function(){		
		if($(this).val() != ""){
			skill = {};
			skill.text = $(this).val();
			skill.type = 0;			
			skills.push(skill);
		}		 
	})
	$("#desired-skills-container").find(".list-item input").each(function(){		
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
	$(workDayDtos).each(function(){		
		var workDay = {};
		workDay.stringDate = $.datepicker.formatDate("yy-mm-dd", this.date);
		workDay.stringStartTime = this.workDay.stringStartTime;
		workDay.stringEndTime = this.workDay.stringEndTime;		
		workDays.push(workDay);
	})	
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
	})
}
