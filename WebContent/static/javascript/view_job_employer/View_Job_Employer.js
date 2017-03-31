
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
	
	var workDays = getDateFromContainer($("#work-days-calendar-container .work-days"));
	var $calendar = $("#job-calendar-application-summary .calendar");
	var firstDate = getMinDate($calendar)

	$calendar.datepicker({
//		minDate: firstDate,
		numberOfMonths: getNumberOfMonths($calendar),
		beforeShowDay: function(date){
			if(doesDateArrayContainDate(date, workDays)) return [true, "active111"];
			else return [true, ""];
		},
		afterShow: function(){
			var html = "";
			var counts = [3, 8, 10];
			var max = Math.max.apply(null, counts);
			$(workDays).each(function(i, date){
				
				var td = getTdByDate($calendar, date);
				var number = parseInt(Math.random() * 10);
				html = "<div class='employment-fraction'>1 / 4</div>";
				html += "<div class='application-count'>";
				html += "<span>" + number + "</span>";
				html += "</div>"
				
				$(td).append(html);
				var $addedDiv = $(td).find(".application-count");
				
				$addedDiv.css("height", 40 * number / 10 + "%");
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

