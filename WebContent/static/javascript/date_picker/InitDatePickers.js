$(document).ready(function(){
	

})
function initCalendar_selectSingleDate($calendar){
	var selectedDate;
	$calendar.datepicker({
		minDate: new Date(),
		numberOfMonths: 1,
		onSelect: function(dateText, inst){			
			var date = dateify(dateText);
			if(selectedDate != undefined){
				// if user is deselecting the date
				if(date.getTime() == selectedDate.getTime()){
					selectedDate = undefined;
				}else selectedDate = date;
			}else selectedDate = date;
			

				
			
		},
		beforeShowDay: function(date){			
			if(selectedDate != undefined){
				if(date.getTime() == selectedDate.getTime()) return [true, "selected"];	
				else return [true, ""];				
			}else return [true, ""];						
		},
		afterShow: function(){
			// find jobs calendar
			if($calendar.hasClass("find-jobs-filter-calendar")){				
				if(selectedDate != undefined){
					var value = selectedDate
					setAppliedFilterValue($calendar, value)
					getJobsAfterApplyingFilter($calendar, value)
				}else{
					clearFilterValue($calendar)
				}
			}			
		}
	})	
}

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
				
				if (workDayDto.isComplete){
					classNameToAdd = " is-complete";
				}else{
					if (workDayDto.hasConflictingEmployment == "1") classNameToAdd += " has-conflicting-employment hide-time hide-select-work-day";
					if (workDayDto.hasConflictingApplications == "1") classNameToAdd += " has-conflicting-applications";
					if (workDayDto.isAccepted == "1") classNameToAdd += " is-accepted";
					
					if (workDayDto.hasOpenPositions == "0") classNameToAdd += " no-available-positions hide-time hide-select-work-day";
					else{
						if (workDayDtos.length == 1) classNameToAdd += " is-proposed";
						else if (workDayDto.isProposed == "1") classNameToAdd += " is-proposed";
					}						
				}
			
				return [true, classNameToAdd];
			}else return [true, ""];
			
		},
		afterShow: function(){
//			$calendar.datepicker("setDate", firstDate);	
			
			if($calendar.hasClass("invalid")){
				validateCalendar($calendar);	
			}
			
			
			// if proposal calendar, update whether work days are being accepted/proposed
			if($calendar.closest(".calendar-container").hasClass("negotiating-context")){
				setWorkDayAcceptanceContext();
			}
			
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
					html_message += "<p>Other Emp.</p>";
				}else if(workDayDto.hasOpenPositions == "0"){
					html_message += "<p>All Pos. Filled</p>";
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
			
			// NOTE: The proposed dates must be obtained AFTER the above loop.
			// If all the job work days are in the next month, all the rows will be hidden 
			// because the calendar is first initialized to the current month. 
			if($calendar.hasClass("find-conflicting-applications-on-select")){
				var $proposalContainer =  $calendar.closest(".proposal-container");
				var applicationId = $proposalContainer.attr("data-application-id");
				var dateStrings_counterWorkDays = getSelectedDates($calendar, "yy-mm-dd", "is-proposed");
				var $e_renderHtml = 
				
//				if(dateStrings_counterWorkDays.length > 0){
					executeAjaxCall_getConflitingApplications($e_renderHtml, applicationId,
							dateStrings_counterWorkDays);
//				}
			}
		}
	})
	
	$calendar.datepicker("setDate", firstDate);	
}
function getConflictingApplicationsContainer($e){
	return $e.find(".conflicting-applications-countering").eq(0);
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
				
				var $tr = $(td).closest("tr");
				if($tr.hasClass("show-row") == 0) $tr.addClass("show-row");
			})
		}
	})
	
	$cal.datepicker("setDate", new Date(Math.min.apply(null, dates)));

}

function initCalendar_selectWorkDays($calendar, $calendar_startAndEndTimes
										, numberOfMonths){
	
	
	// ****************************************************************************
	// ****************************************************************************
	// TODO: Refactor.
	// This has become ridiculous.
	// ****************************************************************************
	// ****************************************************************************
	
	
	$calendar.datepicker({
		minDate: new Date(),
		numberOfMonths: numberOfMonths, 
		onSelect: function(dateText, inst) {	   
				
			// This is for post job re-validation
			if($("body #postSections").length > 0 )
				validateSection($calendar);	
			
			$("#select-times-cal").attr("data-required-updating", "1");
			
			var isThisTheFirstDateSelected = false;
			var isThisTheSecondDateSelected = false;
			
			if(workDayDtos.length == 0) isThisTheFirstDateSelected = true;
			if(workDayDtos.length == 1) isThisTheSecondDateSelected = true;
			
			// If the user's second click is the same as the first,
			// then remove hover mode.
			if(workDayDtos.length == 1 &&
					(dateify(dateText).getTime() == workDayDtos[0].date.getTime())){
				
				// if hovering, stop hovering
				if($calendar.hasClass("show-hover-range") == 1){
					$calendar.removeClass("show-hover-range");
				}
				//else remove the user is attempting to remove the 1 day they clicked
				else{
					workDayDtos = [];
				}
				
			}
			else{
				var inHoverMode = false;
				if($calendar.hasClass("show-hover-range") == 1){
					inHoverMode = true;
				}
				workDayDtos = onSelect_multiDaySelect_withRange_workDayDtos(dateText, workDayDtos, inHoverMode);
							
				if(isThisTheFirstDateSelected){
					$calendar.addClass("show-hover-range");
					$calendar.attr("data-first-date", dateText);
				}
				else $calendar.removeClass("show-hover-range");
				
				
				// This is only for job posts
				if($calendar.attr("id") == "workDaysCalendar_postJob"){
					
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
					
					
					
					if(workDayDtos.length > 1){
	//					$("#availabilityContainer").show();
						$("#dates-wrapper").addClass("multiple-work-days");
					}else{
	//					$("#availabilityContainer").hide();
						$("#dates-wrapper").removeClass("multiple-work-days");
					}
					
								
					if($calendar_startAndEndTimes != undefined)
						initCalendar_setStartAndEndTimes($calendar_startAndEndTimes);
					
					if(workDayDtos_original.length > 0){
						
					}				
				}
			
			}
		},		        
        beforeShowDay: function (date) {       
        	var className = "";
        	// This is for job posts
        	if(typeof workDayDtos_original !== "undefined"){
        		if(doesWorkDayDtoArrayContainDate(date, workDayDtos_original)) className += "original";	
        	}        	
        	
        	if(doesWorkDayDtoArrayContainDate(date, workDayDtos)) className += " active111 selected";
        	else className += " not-selected";
        	
        	return [true, className];
        
     	},
     	afterShow: function(){
     		// For some reason the afterShow method is not firing on the initial caledar load.
     		// Maybe because this calendar is not initially visible on page load???
//     		changePrevNextText($calendar, "<<", ">>");
     		
     		if($calendar.attr("id") == "workDaysCalendar_postJob"){
     			renderWorkDayTimes();
     		}
     			
			if($calendar.hasClass("invalid")){
				validateCalendar($calendar);
			}
     	}
    });	
}

function renderWorkDayTimes(){
		$append = $("#appendges");
 		$append.empty();
 		$(workDayDtos).each(function(i, workDayDto) {

 			
 			// create start and end time select boxes for each date
 			var $clone = $("#clone-start-and-end-times .time-wrapper").clone();
 			$append.append($clone);
 			var $date = $($clone.find(".date").eq(0));
 			var $startTime = $($clone.find(".start-time").eq(0));
 			var $endTime = $($clone.find(".end-time").eq(0));
 			
 			var dateText = $.datepicker.formatDate("D M d", workDayDto.date);
 			$date.html(dateText);
 			$date.attr("data-date", $.datepicker.formatDate("yy-mm-dd", workDayDto.date));
			setTimeOptions($startTime, 30, "start time");
     		setTimeOptions($endTime, 30, "end time");
     		
     		if(workDayDto.workDay.stringStartTime != undefined){
         		$startTime.find("option[data-filter-value='" + workDayDto.workDay.stringStartTime.toString() + "']").eq(0).prop("selected", true);
     		}
     		if(workDayDto.workDay.stringEndTime != undefined){
         		$endTime.find("option[data-filter-value='" + workDayDto.workDay.stringEndTime.toString() + "']").eq(0).prop("selected", true);
     		}
		})
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




