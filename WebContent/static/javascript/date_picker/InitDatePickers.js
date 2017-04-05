$(document).ready(function(){
	

})

function initCalendar_showWorkDays($calendar, dates_workDays, dates_unavailable, dates_proposed,
						dates_other_applications, workDayDtos){

	var firstDate = getMinDate($calendar)

	$calendar.datepicker({
//		minDate: firstDate,
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
			$(workDayDtos).each(function(){
				
				var td = getTdByDate($calendar, this.date);
				
				html = "<div class='added-content'>";
				html += "<p>7:30a</p><p>5:30p</p>";
				html += "<div class='select-work-day'>";
				html += "<span class='glyphicon glyphicon-ok'></span>";
				html += "</div>";	
				html += "<div class='other-application'></div>";
				html += "</div>"
				
				$(td).append(html);
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


function initCalendar_showAvailability($calendar, dates_application, dates_job){
	
	$calendar.datepicker({
		minDate: getMinDate($calendar),
		numberOfMonths: parseInt(getNumberOfMonths($calendar)),
		beforeShowDay: function (date) {
			 return beforeShowDay_findEmployees_ifUserHasAvailability(
					 						date, dates_job, dates_application);
		 }
	})
	
}



function initCalendar_showWorkDays_counterProposal($calendar, dates_workDays, dates_unavailable, dates_proposed){
	
	var firstDate = getMinDate($calendar)

	$calendar.datepicker({
//		minDate: firstDate,
		numberOfMonths: getNumberOfMonths($calendar),
		onSelect: function(dateText, inst){
			
			if($(inst.input).closest(".calendar-container").hasClass("read-only") == 0){
				var date = dateify(dateText);
				
				if(doesDateArrayContainDate(date, dates_unavailable)){}
				else if(doesDateArrayContainDate(date, dates_application)){				
					dates_application = removeDateFromArray(date, dates_application);				
				}	
				else if(doesDateArrayContainDate(date, dates_job)){				
					dates_application.push(date);				
				}
			}
			
		},
		beforeShowDay: function(date){
			if(doesDateArrayContainDate(date, dates_proposed)) return [true, "active111 proposed-work-day"];
			else if(doesDateArrayContainDate(date, dates_workDays)) return [true, "active111 job-work-day"];
			else if(doesDateArrayContainDate(date, dates_unavailable)) return [true, "active111 unavailable"];
			else return [true, ""];
		},
		afterShow: function(){
			var html = "";
			$(dates_workDays).each(function(){
				
				var td = getTdByDate($calendar, this);
				
				html = "<div class='start-and-end-times'>";
				html += "<p>7:30a</p><p>5:30p</p>";
				html += "</div>"
				
				$(td).append(html);
			})				
		}
	})
	
	$calendar.datepicker("setDate", firstDate);	
}
function initCalendar_counterApplicationDays($calendar, dates_application, dates_job, dates_unavailable){


	
	$calendar.datepicker({
		minDate: getMinDate($calendar),
		numberOfMonths: parseInt(getNumberOfMonths($calendar)),
		onSelect: function(dateText, inst){
			
			if($(inst.input).closest(".calendar-container").hasClass("read-only") == 0){
				var date = dateify(dateText);
				
				if(doesDateArrayContainDate(date, dates_unavailable)){}
				else if(doesDateArrayContainDate(date, dates_application)){				
					dates_application = removeDateFromArray(date, dates_application);				
				}	
				else if(doesDateArrayContainDate(date, dates_job)){				
					dates_application.push(date);				
				}
			}
			
		},
		beforeShowDay: function (date) {
			 return beforeShowDay_counterApplicationDays(
					 						date, dates_application, dates_job, dates_unavailable);
		 }
	})
	
}