
$(document).ready(function(){
	
	initPage();

	
	$("#questionListContainer input[type='checkbox']").click(function(){
		$("#selectAllQuestions").prop("checked", false);
		$("#selectNoQuestions").prop("checked", false);
	})
	
	$("#showAllApplicants").click(function(){{

		showAllApplications();
		$(this).hide();
	}})
	
	initCalendar_employerViewJob_applicantSummary()
	
})

function initCalendar_employerViewJob_applicantSummary() {
	
	var workDayDtos = getWorkDayDtosFromContainer($("#work-day-dtos"));
	var $calendar = $("#job-calendar-application-summary .calendar");
	var firstDate = getMinDate($calendar)

	$calendar.datepicker({
//		minDate: firstDate,
		numberOfMonths: getNumberOfMonths($calendar),
		onSelect: function(dateString, inst){
			var jobId = $("#jobId").val();
			var date = new Date(dateString);
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
		},
		beforeShowDay: function(date){
			if(doesWorkDayDtoArrayContainDate(date, workDayDtos)) return [true, "job-work-day"];
			else return [true, ""];
		},
		afterShow: function(){
			var html = "";
			var counts = [3, 8, 10];
			var max = Math.max.apply(null, $.map(workDayDtos, function(workDayDto){ return workDayDto.count_applicants }));
			$(workDayDtos).each(function(i, workDayDto){
				
				var td = getTdByDate($calendar, workDayDto.date);
//				var number = parseInt(Math.random() * 10);
//				html = "<div class='employment-fraction'>1 / 4</div>";
//				html += "<div class='application-count'>";
//				html += "<span>" + number + "</span>";
//				html += "</div>"
				
				html = "<div class='employment-fraction'>" + workDayDto.count_positionsFilled + " / " + workDayDto.count_totalPositions + "</div>";
				html += "<div class='application-count'>";
				html += "<span>" + workDayDto.count_applicants + "</span>";
				html += "</div>"					
					
					
				$(td).append(html);
				var $addedDiv = $(td).find(".application-count");
				
				var maxHeightPercentage = 32;
//				$addedDiv.css("height", 32 * number / 10 + "%");
				$addedDiv.css("height", maxHeightPercentage * ( workDayDto.count_applicants / max ) + "%");
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

	if(data_initPage != "all-apps"){
	
		var filters = [];
		var filter = {};
		filter.values = [];
	
		switch(data_initPage){
		case "new-apps":
			filter.attr = "data-is-old";
			filter.values.push("0");
			break;
		
	// 	case "all-apps":
	// 		showApplications(true, true, false, false );
	// 		break;
		
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

