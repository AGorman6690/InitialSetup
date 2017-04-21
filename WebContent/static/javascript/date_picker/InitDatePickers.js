$(document).ready(function(){
	

})

function initCalendar_new($calendar, workDayDtos){

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
				
				if(workDayDto.hasConflictingEmployment == "1") classNameToAdd += " has-conflicting-employment";
				if(workDayDto.hasConflictingApplications == "1") classNameToAdd += " has-conflicting-applications";
				if(workDayDto.isAccepted == "1") classNameToAdd += " is-accepted";
				
				if(workDayDto.hasOpenPositions == "0") classNameToAdd += " no-available-positions";
				else{
					if(workDayDto.isProposed == "1") classNameToAdd += " is-proposed";
				}
				return [true, classNameToAdd];
			}else return [true, ""];
			
		},
		afterShow: function(){
			
			$(workDayDtos).each(function(i, workDayDto){				
				var td = getTdByDate($calendar, dateify(workDayDto.workDay.stringDate));
				
				var html_select_work_day = "";
				var html_time_display = "";
				var html_message = "";
				var html = "<div class='added-content'>";

				html_time_display += "<div class='time-display'>"			
						 + "<p class='start-time'>" + 
								formatTimeTo12Hours(workDayDto.workDay.stringStartTime) + "</p>"
						 + "<p class='end-time'>" + 
								formatTimeTo12Hours(workDayDto.workDay.stringEndTime) + "</p>"
						 + "</div>";			

				html_select_work_day += "<div class='select-work-day'>"
							 + "<span class='glyphicon glyphicon-ok'></span>"
							 + "</div>";					
				
				html_message = "<div class='message'>";
				if(workDayDto.hasConflictingEmployment == "1"){
					html_message += "<p>Busy</p>";
				}
				html_message += "</div>";
				
				
				html += html_time_display;
				html += html_select_work_day;
				html += html_message;
				
				html += "</div>";
				
				$(td).append(html);
				
				var $tr = $(td).closest("tr");
				if($tr.hasClass("show-row") == 0) $tr.addClass("show-row"); 
			})				
		}
	})
	
	$calendar.datepicker("setDate", firstDate);	
}

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
				
				if(workDayDto.hasConflictingEmployment == "1") classNameToAdd += " has-conflicting-employment";
				if(workDayDto.hasConflictingApplications == "1") classNameToAdd += " has-conflicting-applications";
				
				if(workDayDto.hasOpenPositions == "0") classNameToAdd += " no-available-positions";
				else{
					if(workDayDto.isProposed == "1") classNameToAdd += " proposed-work-day";
				}
			}
			return [true, classNameToAdd];
		},
		afterShow: function(){
			
			var html = "";
			$(workDayDtos).each(function(i, workDayDto){				
				var td = getTdByDate($calendar, dateify(workDayDto.workDay.stringDate));
				
				html = "<div class='added-content'>";
				html += "<p class='full'>Full</p>";
				html += "<p class='time'>7:30a</p><p class='time'>5:30p</p>";
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


function initCalendar_setStartAndEndTimes($cal){
		
	var dates = [];
	dates = getDatesFromWorkDayDtos(workDayDtos, dates)
	
	$cal.datepicker("destroy");
	$cal.datepicker({
		numberOfMonths: getMonthsSpan(dates),
		onSelect: function(dateText) {
			
			var date = new Date(dateText);
			var workDayDto = getWorkDayDtoByDate_new(date, workDayDtos);
			
			if(workDayDto.isSelected) workDayDto.isSelected = false;
			else workDayDto.isSelected = true;
			
			$("#select-all-dates").prop("checked", false);
			$("#deselect-all-dates").prop("checked", false);
			
		},
		afterShow: function(date, inst) {
			$(workDayDtos).each(function(i, workDayDto){			
				
				var html = "";
				html += "<div class='time-display'>";
//				if(workDayDto.workDay.startTime != undefined &&
//						workDayDto.workDay.endTime != undefined){					
					html += "<p class='start-time'>" + 
							formatTimeTo12Hours(workDayDto.workDay.stringStartTime) + "</p>";
					html += "<p class='end-time'>" + 
							formatTimeTo12Hours(workDayDto.workDay.stringEndTime) + "</p>";
//				}else{
//					html += "<p class='start-time'></p>";
//					html += "<p class='end-time'></p>";
//
//				}
				html += "</div>"			

				html += "<div class='select-date-box'><span class='glyphicon glyphicon-ok'></span></div>";	

				var td = getTdByDate($cal, workDayDto.date);
				$(td).append(html);
				
				$(td).addClass("a-date-to-set");
				if(workDayDto.isSelected) $(td).addClass("selected-date");
			})
		}
	})
	
	$cal.datepicker("setDate", new Date(Math.min.apply(null, dates)));

}

function initCalendar_selectWorkDays($calendar, $calendar_startAndEndTimes
										, numberOfMonths){
	
	$calendar.datepicker({
		minDate: new Date(),
		numberOfMonths: numberOfMonths, 
		onSelect: function(dateText, inst) {	   
			
			$("#select-times-cal").attr("data-required-updating", "1");
			
			var isThisTheFirstDateSelected = false;
			var isThisTheSecondDateSelected = false;
			
			if(workDayDtos.length == 0) isThisTheFirstDateSelected = true;
			if(workDayDtos.length == 1) isThisTheSecondDateSelected = true;
			
			workDayDtos = onSelect_multiDaySelect_withRange_workDayDtos(dateText, workDayDtos);
			
						
			if(isThisTheFirstDateSelected){
				$calendar.addClass("show-hover-range");
				$calendar.attr("data-first-date", dateText);
			}
			else $calendar.removeClass("show-hover-range");
			
			if(workDayDtos.length == 0){
				resetTimesSection();
				resetCalendar();
			}
			
			else if(isThisTheSecondDateSelected){
				$("#no-dates-selected").hide();
				$("#initial-time-question").show();
				$("#set-one-start-and-end-time").hide();
				$("#times-cont").hide();
			}
			else if(workDayDtos.length == 1){
				$("#times-cont").hide();
				$("#no-dates-selected").hide();	
				$("#initial-time-question").hide();
				$("#set-one-start-and-end-time").show();
			}
						
			if($calendar_startAndEndTimes != undefined)
				initCalendar_setStartAndEndTimes($calendar_startAndEndTimes);
			
			if(workDayDtos_original.length > 0){
				
			}

		},		        
        // This is run for every day visible in the datepicker.
        beforeShowDay: function (date) {       
        	var className = "";
        	
        	if(doesWorkDayDtoArrayContainDate(date, workDayDtos_original)) className += "original";
        	
        	if(doesWorkDayDtoArrayContainDate(date, workDayDtos)) className += " active111";
        	else className += " not-selected";
        	
        	 return [true, className];
        
     	}
    });	
	

}

function initCalendar_jobInfo_workDays($calendar, workDayDtos){

//	var workDays = getDateFromContainer($("#work-days-calendar-container .work-days"));
	var firstDate = getMinDate($calendar)

	$calendar.datepicker({
//		minDate: firstDate,
		numberOfMonths: getNumberOfMonths($calendar),
		beforeShowDay: function(date){
			if(doesWorkDayDtoArrayContainDate(date, workDayDtos)) return [true, "job-work-day job-post-style"];
			else return [true, ""];
		},
		afterShow: function(){
			var html = "";
			$(workDayDtos).each(function(){
				
				var td = getTdByDate($calendar, dateify(this.workDay.stringDate));
				
				html = "<div class='added-content'";
				html += "<div class='time-display'>";
				html += "<p>" + formatTimeTo12Hours(this.workDay.stringStartTime) + "</p>";
				html += "<p>" + formatTimeTo12Hours(this.workDay.stringEndTime) + "</p>";
				html += "</div>";
				html += "</div>";
				
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




