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
				
				if(workDayDto.hasConflictingEmployment == "1") classNameToAdd += " has-conflicting-employment hide-time hide-select-work-day";
				if(workDayDto.hasConflictingApplications == "1") classNameToAdd += " has-conflicting-applications";
				if(workDayDto.isAccepted == "1") classNameToAdd += " is-accepted";
				
				if(workDayDto.hasOpenPositions == "0") classNameToAdd += " no-available-positions hide-time hide-select-work-day";
				else{
					if(workDayDto.isProposed == "1") classNameToAdd += " is-proposed";
				}
				return [true, classNameToAdd];
			}else return [true, ""];
			
		},
		afterShow: function(){
			
			// If this is a calendar for an applicant counter the proposed work days,
			// get applications that have overlapping work days, if any.
			if($calendar.hasClass("find-conflicting-applications-on-select")){
				var $proposalContainer =  $calendar.closest(".proposal-container");
				var applicationId = $proposalContainer.attr("data-application-id");
				var dateStrings_counterWorkDays = getSelectedDates($calendar, "yy-mm-dd", "is-proposed");
				var $e_renderHtml = $proposalContainer.find(".conflicting-applications-countering").eq(0);
					
				executeAjaxCall_getConflitingApplications($e_renderHtml, applicationId,
						dateStrings_counterWorkDays);
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
				
				var $tr = $(td).closest("tr");
				if($tr.hasClass("show-row") == 0) $tr.addClass("show-row");
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
			
			

			
			
			setValidCss($calendar.find(".invalid"));
			
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
				$calendar.removeClass("show-hover-range");
			}
			else{
				workDayDtos = onSelect_multiDaySelect_withRange_workDayDtos(dateText, workDayDtos);
				
							
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
     		
     		renderWorkDayTimes();
			
			

     		
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
     		
     		if(workDayDto.workDay.startTime != undefined){
         		$startTime.find("option[data-filter-value='" + workDayDto.workDay.startTime.toString() + "']").eq(0).prop("selected", true);
     		}
     		if(workDayDto.workDay.endTime != undefined){
         		$endTime.find("option[data-filter-value='" + workDayDto.workDay.endTime.toString() + "']").eq(0).prop("selected", true);
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




