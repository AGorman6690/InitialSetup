$(document).ready(function(){
	

})



function initCalendar_showWorkDays($calendar, workDayDtos){

	var firstDate = getMinDate($calendar)

	$calendar.datepicker({
		numberOfMonths: getNumberOfMonths($calendar),
		onSelect: function(dateText, inst){			
			if($(inst.input).closest(".calendar-container").hasClass("read-only") == 0){

				var workDayDto = getWorkDayDtoByDate(dateify(dateText), workDayDtos);
				
				if(workDayDto != undefined){					
					if(workDayDto.hasConflictingEmployment != "1"){
						if(workDayDto.isProposed == "1") workDayDto.isProposed = "0";
						else workDayDto.isProposed = "1";
					}					
				}
			}			
		},
		beforeShowDay: function(date){			
			var classNameToAdd = "";
			var workDayDto = getWorkDayDtoByDate(date, workDayDtos);
			
			if(workDayDto != undefined){				
				classNameToAdd += "job-work-day";
				if(workDayDto.isProposed == "1") classNameToAdd += " proposed-work-day";
				if(workDayDto.hasConflictingEmployment == "1") classNameToAdd += " has-conflicting-employment";
				if(workDayDto.hasConflictingApplications == "1") classNameToAdd += " has-conflicting-applications";
			}
			return [true, classNameToAdd];
		},
		afterShow: function(){
			var html = "";
			$(workDayDtos).each(function(i, workDayDto){				
				var td = getTdByDate($calendar, dateify(workDayDto.workDay.stringDate));
				
				html = "<div class='added-content'>";
				html += "<p>7:30a</p><p>5:30p</p>";
				html += "<div class='select-work-day'>";
				html += "<span class='glyphicon glyphicon-ok'></span>";
				html += "</div>";	
				html += "<div class='other-application'></div>";
				html += "</div>"
				
				$(td).append(html);
				
				var $tr = $(td).closest("tr");
				if($tr.hasClass("show-row") == 0) $tr.addClass("show-row"); 
			})				
		}
	})
	
	$calendar.datepicker("setDate", firstDate);
}

function getDateFromContainer($container){
	
	var dates = [];
	var date;
	$container.find("[data-date]").each(function(){
		
		date = dateify($(this).attr("data-date"));		
		if(date != undefined) dates.push(date);		
	})
	
	return dates;
	
}

function getDateFromContainer($container){
	
	var dates = [];
	var date;
	$container.find("[data-date]").each(function(){
		
		date = dateify($(this).attr("data-date"));		
		if(date != undefined) dates.push(date);		
	})
	
	return dates;
	
}

function getWorkDayDtosFromContainer($container){
	
	var workDayDtos = [];
	
	$container.find(".work-day-dto").each(function(){
		var workDayDto = {};
		workDayDto.date = dateify($(this).attr("data-date"));
		workDayDto.count_applicants = $(this).attr("data-count-applicants");
		workDayDto.count_positionsFilled = $(this).attr("data-count-positions-filled");
		workDayDto.count_totalPositions = $(this).attr("data-count-total-positions");
		workDayDto.isProposed = $(this).attr("data-is-proposed");
		workDayDto.hasConflictingEmployment = $(this).attr("data-has-conflicting-employment");
		workDayDto.hasConflictingApplications = $(this).attr("data-has-conflicting-applications");
		workDayDtos.push(workDayDto);
	})
	
	return workDayDtos;
	
}


function initCalendar_selectAvailability($calendar, currentAvailability, workDays){
		
	
	
	var numberOfMonths = getNumberOfMonths($calendar);	
	var minDate = getMinDate($calendar);
	
	$calendar.datepicker({
		minDate: minDate,
		numberOfMonths: numberOfMonths,
		onSelect: function(dateText, inst){
			
			// The user can only select a valid work day
			if(doesDateArrayContainDate(new Date(dateText), workDays)){
				currentAvailability = onSelect_multiDaySelect_withRange(
						dateText, currentAvailability);	
			}
			
		},
        beforeShowDay: function (date) {

			if(isDateAlreadySelected(date, currentAvailability)) return [true, "active111 apply-selected-work-day"];
			else if(isDateAlreadySelected(date, workDays)) return [true, "active111"];
			else return [true, ""];
        	
        }
    });		

}




