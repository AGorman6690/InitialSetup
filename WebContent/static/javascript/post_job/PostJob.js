var selectedDays = [];

$(document).ready(function(){

	$("#submitPosting_preview").click(function(){
//		 executeAjaxCall_previewJobPosting( getJobDto());
		if(arePostJobInputsValid()){
			executeAjaxCall_previewJobPosting(getJobDto());
			
			$("#submitPosting_final_container").show();
			$("#submitPosting_preview_container").hide();
		}
	})
	
	$("#editPosting").click(function(){
		 
		$("#displayExample_jobInfo").hide();
		$("#postJobInfoContainer").show();
		$("#submitPosting_final_container").hide();
		$("#submitPosting_preview_container").show();
	})
	
	$("#submitPosting_final").click(function(){
		executeAjaxCall_postJob(getJobDto());
//		if(arePostJobInputsValid()) executeAjaxCall_postJob(getJobDto());
	})
	
	$("#noDatesSelected span").click(function(){
		$("#date").click();
	})
	
	$("#datesContainer #clearCalendar").click(function(){
		selectedDays = [];
		$("#workDaysCalendar_postJob").datepicker("refresh");
		$("#timesTable tbody").empty();
		$("#timesTableContainer").hide();
		$("#noDatesSelected").show();
	})
	
	$("#timesTable tbody").on("change", "select.select-all.start-time", function(){
		
		var time = $(this).val();
		$("#timesTable tbody").find("tr.selected select.start-time:not(.select-all)").each(function(){
			$(this).val(time);
		})
	})
	
	$("#timesTable tbody").on("change", "select.select-all.end-time", function(){
		
		var time = $(this).val();
		$("#timesTable tbody").find("tr.selected select.end-time:not(.select-all)").each(function(){
			$(this).val(time);
		})

	})
	
	$("#timesTable tbody").on("change", "input[type=checkbox]", function(){
		
		if($(this).is(":checked")) $(this).closest("tr").addClass("selected");
		else $(this).closest("tr").removeClass("selected");
		
		if($("#timesTable tbody").find("input[type=checkbox][name=time]:checked").length > 1){
			var k = $("#timesTable tbody").find(".start-time.select-all").eq(0);
			$("#timesTable tbody").find(".start-time.select-all").eq(0).prop("disabled", false);
			$("#timesTable tbody").find(".end-time.select-all").eq(0).prop("disabled", false);
		}
		else{
			$("#timesTable tbody").find(".start-time.select-all").eq(0).prop("disabled", true);
			$("#timesTable tbody").find(".end-time.select-all").eq(0).prop("disabled", true);
		}
	})
	
	$("#timesTable tbody").on("change", "input[type=checkbox].select-all", function(){
		
		var doCheck = $(this).is(":checked");

		$("#timesTable tbody").find("input[type=checkbox]:not(.select-all)").each(function(){
			$(this).prop("checked", doCheck).change();			
		})
		
		
	})
	
	$("#postedJobs div[data-posted-job-id]").click(function(){
		importPreviousJobPosting($(this).attr("data-posted-job-id"));
		$("#postedJobsContainer").find("span[data-toggle-id]").eq(0).click();
	})
	
	$("#postedQuestions div[data-question-id]").click(function(){
		importPreviousQuestion($(this).attr("data-question-id"));
		$("#postedQuestionsContainer").find("span[data-toggle-id]").eq(0).click();
	})
	
	$("body").on("click", "#employeeSkillsContainer .add-list-item", function(){
		
		addAnotherSkill($(this).siblings(".list-items-container").eq(0));
		
	})
	
	$("body").on("click", ".skills-container .delete-list-item", function(){
		
		deleteSkill($(this).closest(".list-item"));
		
	})	
	
	setStates();
	setTimeOptions($("#startTime-singleDate"), 30);
	setTimeOptions($("#endTime-singleDate"), 30);
	
	initWorkDaysCalendar();
	
	
})

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
		setControlValues(jobDto);
		broswerIsWaiting(false);	
	}	

	function _error() {
		broswerIsWaiting(false);
		alert('DEBUG: error executeAjaxCall_saveFindJobFilter')		
	}
}

function setControlValues(jobDto){
	
	$("#name").val(jobDto.job.jobName);
	$("#description").val(jobDto.job.description);
	$("#street").val(jobDto.job.streetAddress);
	$("#city").val(jobDto.job.city);
	$("#state").val(jobDto.job.state);
	$("#zipCode").val(jobDto.job.zipCode);
	
	
	resetEntireQuestionSection();
	
	$(jobDto.questions).each(function(){
		showQuestionDto(this);
		addQuestion();
	})
	
}

function initWorkDaysCalendar(){
	$("#workDaysCalendar_postJob").datepicker({
		minDate: new Date(),
		numberOfMonths: 2, 
		onSelect: function(dateText, inst) {	    
			selectedDays = onSelect_multiDaySelect_withRange(dateText, selectedDays);
			
			var tdDate;
			var $clonedRow;
			$("#timesTable tbody").empty();
			$("#timesTable thead .master-row-multi-select").clone().appendTo("#timesTable tbody");
			$(selectedDays).each(function(){		
			
				$clonedRow = $("#timesTable thead .master-row").clone();
				
				tdDate = $clonedRow.find(".date").eq(0);
				$(tdDate).html($.datepicker.formatDate("D M dd", this));
				$(tdDate).attr("data-date", $.datepicker.formatDate("yy-mm-dd", this))
				$("#timesTable tbody").append($clonedRow);
				
				
			})
			
			$("#timesTable tbody tr select.time").each(function(){
				setTimeOptions($(this), 30);
			})
			
			
			if(selectedDays.length == 0){
				$("#timesTableContainer").hide();
				$("#noDatesSelected").show();
			}
			else{
				$("#timesTableContainer").show();
				$("#noDatesSelected").hide();
				
				$("#timesTable tbody input.select-all").prop("checked", true).change();
			}

		},		        
        // This is run for every day visible in the datepicker.
        beforeShowDay: function (date) {        	
        	return beforeShowDay_ifSelected(date, selectedDays);        	
     	}
    });	
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
	jobDto.job.durationTypeId = 2; // Phase this out once we agree that we are only targeting short term labor
	jobDto.job.durationUnitLength = jobDto.workDays.length;
	
	if($("#partialAvailabilityAllowed").is(":checked")) jobDto.job.isPartialAvailabilityAllowed = 1;
	else jobDto.job.isPartialAvailabilityAllowed = 0;

	
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
	var dates = $("#timesTable tbody tr.work-day-row") //getSelectedDates($("#workDaysCalendar_postJob"), "yy-mm-dd");
	var workDays = [];
	
	$(dates).each(function(){
		
		var workDay = {};
		workDay.stringDate = $(this).find("td.date").eq(0).attr("data-date");
		workDay.stringStartTime = $(this).find('select.start-time option:selected').eq(0).attr("data-filter-value");
		workDay.stringEndTime = $(this).find('select.end-time option:selected').eq(0).attr("data-filter-value");
		
		workDays.push(workDay);
	})
	
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
		
		$("#displayExample_jobInfo").show();
		$("#displayExample_jobInfo").html(html_jobInfo);
		$("#postJobInfoContainer").hide();
		
		setWorkDays();
		initCalendar_JobInfo();
		initMap();
//		$.getScript("/JobSearch/static/javascript/JobInfo.js", function(){alert(789)});
		broswerIsWaiting(false);	
	}	

	function _error() {
		broswerIsWaiting(false);
		alert('DEBUG: error executeAjaxCall_saveFindJobFilter')		
	}
}
