
$(document).ready(function(){
	
	initPage();

	$("#toggle-calendar-numbers").change(function() {
		var doShow = false;
		if($(this).is(":checked")) doShow = true;
		
		if(doShow)
			$("#job-calendar-application-summary td.job-work-day span").show();
		else
			$("#job-calendar-application-summary td.job-work-day span").hide();
	})
	
	$("#questionListContainer input[type='checkbox']").click(function(){
		$("#selectAllQuestions").prop("checked", false);
		$("#selectNoQuestions").prop("checked", false);
	})
	
	$("#showAllApplicants").click(function(){{

		showAllApplications();
		$(this).hide();
	}})
	
	initCalendar_employerViewJob_applicantSummary();
	
	$("body").on("mouseover", "td.job-work-day:not(.all-positions-filled) .col-cont", function() {
		$(this).find(".popuptext").show()
	})
	
	$("body").on("mouseout", "td.job-work-day:not(.all-positions-filled) .col-cont", function() {
		$(this).find(".popuptext").hide()
	})
	
	$("body").on("mouseover", "td.job-work-day.all-positions-filled", function() {
		$(this).find(".col-cont .popuptext").show();
	})
	
	$("body").on("mouseout", "td.job-work-day.all-positions-filled", function() {
		$(this).find(".col-cont .popuptext").hide()
	})
	
	$("body").on("mouseover", "td.job-work-day .application-count", function() {
		$(this).find(".popuptext").show()
	})
	
	$("body").on("mouseout", "td.job-work-day .application-count", function() {
		$(this).find(".popuptext").hide()
	})	
	
	$(".rating-loading").rating({
		min: 0,
		max: 5,
		step: 0.1,
		stars: 5,
		displayOnly: true
	
	});
	
})

function executeAjaxCall_getApplicantsByJobAndDate(jobId, date){
	broswerIsWaiting(true);
	$.ajax({
		type: "GET",
		url: "/JobSearch/job/" + jobId + "/work-day/" + $.datepicker.formatDate("yy-mm-dd", date) + "/applicants",
		headers: getAjaxHeaders(),
		dataType: "html",
		success: function(html) {
			$("#modal_applicants .mod-content").html(html);
			$("#modal_applicants.mod").show();
			broswerIsWaiting(false);
		},
		error: function() {
			broswerIsWaiting(false);
		}
	})	
}

function initCalendar_employerViewJob_applicantSummary() {
	
    var workDayDtos = parseWorkDayDtosFromDOM($("#json_work_day_dtos"));
	var $calendar = $("#job-calendar-application-summary .calendar");
	var firstDate = getMinDate($calendar)

	$calendar.datepicker({
//		minDate: firstDate,
		numberOfMonths: getNumberOfMonths($calendar),
		onSelect: function(dateString, inst){
			var jobId = $("#jobId").val();
			var date = new Date(dateString);
			executeAjaxCall_getApplicantsByJobAndDate(jobId, date);
		},
		beforeShowDay: function(date){
			if(doesWorkDayDtoArrayContainDate(date, workDayDtos)) return [true, "job-work-day"];
			else return [true, ""];
		},
		afterShow: function(){
			
			var counts = [3, 8, 10];
			var max = Math.max.apply(null, $.map(workDayDtos, function(workDayDto){ return workDayDto.count_applicants }));
			$(workDayDtos).each(function(i, workDayDto){
				
				var td = getTdByDate($calendar, workDayDto.date);
				var html = "";
//				var number = parseInt(Math.random() * 10);
//				html = "<div class='employment-fraction'>1 / 4</div>";
//				html += "<div class='application-count'>";
//				html += "<span>" + number + "</span>";
//				html += "</div>"
				
//				html = "<div class='employment-fraction'>" + workDayDto.count_positionsFilled + " / " + workDayDto.count_totalPositions + "</div>";
				html += "<div class='col-cont'>";
				html += "<div class='popup'>"
						+ "<div class='popuptext'>"
						+ "<h4 class=''>Positions Filled</h4>" +
						+ workDayDto.count_positionsFilled +
									" of " + workDayDto.count_totalPositions
						+ "</div></div>";
				html += "<div class='employment-col'></div>";
				html += "<span>" + workDayDto.count_positionsFilled +
									" of " + workDayDto.count_totalPositions + "</span>";
				html += "</div>";
				html += "<div class='spacer'></div>";
				if(workDayDto.count_applicants == 0) html += "<div class='application-count zero-applications'>";
				else html += "<div class='application-count'>";
				html += "<div class='popup'>"
					+ "<div class='popuptext'>"
					+ "<h4 class=''>Applications</h4>"
					+ workDayDto.count_applicants
					+ "</div></div>";				
				html += "<span>";
				html += workDayDto.count_applicants;
				html += "</span>";
				html += "</div>";		
					
				
					
					
				$(td).append(html);
				var $applicationDiv = $(td).find(".application-count");
				var $employmentDiv_container = $(td).find(".col-cont");
				var $employmentDiv = $(td).find(".employment-col");
				
				var maxHeightPercentage = 37;
				var minHeight_compensation = 0;
				var minHeight = 19;
				var centerLine_topPosition_percent = 60;
				var centerLine_bottomPosition_percent = 100 - centerLine_topPosition_percent;
				
//				if(workDayDto.count_applicants > 1) minHeight_compensation = 19;
//				else minHeight_compensation = 0;
				 
//				var oneApplicantHeight = minHeight_compensation + maxHeightPercentage * ( 1 / max );
//				if(oneApplicantHeight < minHeight) minHeight_compensation = minHeight;
//				else minHeight_compensation = 0;
				
				$applicationDiv.css("height", minHeight_compensation + maxHeightPercentage 
													* ( workDayDto.count_applicants / max ) + "%");
				
				if(workDayDto.count_positionsFilled == workDayDto.count_totalPositions )
					$(td).addClass("all-positions-filled");
				else{
					$employmentDiv.css("height", minHeight_compensation +
							 100 * ( workDayDto.count_positionsFilled / workDayDto.count_totalPositions ) + "%");	
				}
					
				
				
				$applicationDiv.css("top", centerLine_topPosition_percent + "%");
				$employmentDiv_container.css("bottom", centerLine_bottomPosition_percent + "%");
				
				var $tr = $(td).closest("tr");
				if($tr.hasClass("show-row") == 0) $tr.addClass("show-row");
			})				
		}
	})
	
	$calendar.datepicker("setDate", firstDate);
}

function showAllApplications(){
	
	var filters = [];
	var filter = {};
	
	filter.attr = "data-is-sent-proposal";
	filter.values = [];
	filter.values.push("0");
	filter.values.push("1");
	
	filters.push(filter);
	
	filterTableRows(filters, $("#applicantsTable"));
}

function initPage(){
	
	var data_initPage = $("#data_pageInit").val();

	if(data_initPage == "hired") {
		$(".select-page-section[data-page-section-id='employeesContainer']").eq(0).click();
		$("#showAllApplicants").hide();
	}
	else if(data_initPage != "all-apps"){
	
		var filters = [];
		var filter = {};
		filter.values = [];
	
		switch(data_initPage){
		case "applicants-new":
			filter.attr = "data-is-new";
			filter.values.push("1");
			break;
			
		case "sent-proposals":
			filter.attr = "data-is-sent-proposal";
			filter.values.push("1");
			break;
		
		case "received-proposals":
			filter.attr = "data-is-sent-proposal";
			filter.values.push("0");
			break;
			
		case "received-proposals-new":
			filter.attr = "data-wage-proposal-status";
			filter.values.push("-2");
			break;		

		}
		
		
		filters.push(filter);
		filterTableRows(filters, $("#applicantsTable"));
		
		var hiddenRows = $("#applicantsTable > tbody").find("> tr:hidden").length;
		var allRows = $("#applicantsTable > tbody").find("> tr").length;
		if(hiddenRows == 0) $("#showAllApplicants").hide();
		
	}	
}




function isAddingApplicationStatus(clickedStatusButton){
	
	var currentApplicationStatus = $($(clickedStatusButton).closest("[data-application-status]")[0]).attr("data-application-status");
	var clickedApplicationStatus = $(clickedStatusButton).val();
	
	// If the row's application status is the same as the clicked button's status,
	// then the user is toggling the status to off (i.e. they are removing the application status) 
	if (currentApplicationStatus == clickedApplicationStatus){
		return false;
	}
	else{
		return true;
	}
}

