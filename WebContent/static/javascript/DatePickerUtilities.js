
var firstworkDay = {};
var secondworkDay = {};
var isSecondDaySelected = 0;
var isMoreThanTwoDaysSelected = 0;
var rangeIsSet = 0;
var rangeIsBeingSet = 0;




$(document).ready(function(){
	
	$("#clearCalendar").click(function(e){
		e.preventDefault();
		clearCalendar();
		
		
	})
	
	$("#calendar").datepicker({
		minDate: new Date(),
		numberOfMonths: 1,
		 onSelect: function(dateText, inst) {
			 
			 	//**********************************************
			 	//**********************************************
			 	//Clean this up and make comments
			 	//**********************************************
			 	//**********************************************
		            
				 	//Set the selected day
				 	var workDay = {}
				 	workDay.date = new Date(dateText);
		            workDay.date = workDay.date.getTime();
		            
		            if(workDays.length == 3){
		            	rangeIsSet = 1;
		            }
		            
		            //If the day was already selected, remove the date
// 		            if($.inArray(workDay.date, workDays) > -1){
					if(isWorkDayAlreadyAdded(workDay.date, workDays)){
		            	
		            	workDays = removeWorkDay(workDay.date, workDays);
		            	
// 		            	workDays = $.grep(workDays, function(value){
// 		            		return value != workDay;
// 		            	})
		            }
		            else{
// 		            	workDays.push(workDay);
						addWorkDay(workDay, workDays);
		            }
		            
		            if(workDays.length == 0){
		            	resetVars();
		            }
	            	else if(workDays.length == 1){
	            		firstworkDay = workDays[0];
	            		rangeIsSet = 0
	            		isSecondDaySelected = 0;
	            		
	            	}
	            	else if(workDays.length == 2){

	            		secondworkDay = workDays[1];
	            		
	            		isSecondDaySelected = 1;
	            		rangeIsBeingSet = 1;
	            		
	            		if(secondworkDay.date < firstworkDay.date){
	            			clearCalendar(this);
	            		}
	            		
	            	}
		            else{
		            	rangeIsSet = 1;
		            }
		            
				 	if($("#calendar").hasClass("invalid") == 1){
				 		validateDates();	
				 	}
		            
			 	
	        },
	        
	        // This is run for every day visible in the datepicker.
	        beforeShowDay: function (date) {

	        	
	        	if($("#calendar").attr("data-is-showing-job") == 1){
//			        	if($.inArray(date.getTime(), workDays) > -1){
					if(isWorkDayAlreadyAdded(date.getTime(), workDays)){
		        		return [true, "active111"]; 
		        	}
		        	else{
		        		return [true, ""];
		        	}
	        	}
        		else if(rangeIsSet == 1){
	        		
//			        	if($.inArray(date.getTime(), workDays) > -1){
					if(isWorkDayAlreadyAdded(date.getTime(), workDays)){
		        		return [true, "active111"]; 
		        	}
		        	else{
		        		return [true, ""];
		        	}

	        	}
	        	else if(isSecondDaySelected && !rangeIsSet){
	        		
	        		
	        		if(date.getTime() >= firstworkDay.date && date.getTime() <= secondworkDay.date){
	        			
//		        			if($.inArray(date.getTime(), workDays) == -1){
						if(!isWorkDayAlreadyAdded(date.getTime(), workDays)){
	        				var workDay = {};
	        				workDay.date = date.getTime()
//								workDays.push(workDay);
	        				addWorkDay(workDay, workDays);
	        			}
	        			
	        			if(workDays.length == 2){
//		        				rangeIsSet = 1;
	        			}
	        			return [true, "active111"];
	        		}
	        		else{
	        			return [true, ""];
	        		}	  
	        		
	        		
	        		
	        	}else{
//			        	if($.inArray(date.getTime(), workDays) > -1){
					if(isWorkDayAlreadyAdded(date.getTime(), workDays)){
		        		return [true, "active111"]; 
		        	}
		        	else{
		        		return [true, ""];
		        	}
		        		
	        	}

	        }
	    });
	
})

	function clearCalendar(){
		
		
		resetVars();
		
		removeActiveDaysFormatting();
		$("#times").empty();
		
		$("#timesContainer").hide(200);
	}
	
	

	
	function addWorkDay(workDay, workDays){
		
		var newTimeDiv = null;
		var $insertBeforeE;
		var formattedDate = new Date();
		
		//Show the times container
// 		$("#selectAllContainer").show();
		$("#timesContainer").show(200);
		
		//Add the work day JSON object to the array
		workDays.push(workDay);
		
		//Format the date
		
		formattedDate.setTime(workDay.date);
		formattedDate = $.datepicker.formatDate("D M d", formattedDate);
		
		//Build the html
		var html = "<div class='time' data-date='" + workDay.date + "'>"
					+ "<span>" + formattedDate + "</span>"
					+ "<div class='time-container'><input class='form-control start-time' type='text'></div>"
					+ "<div class='time-container'><input class='form-control end-time' type='text'></div>"
		     		+ "</div>";
		
		//By the logic established for calendar date selection,
		//the second date must be later than the first date.
		//Therefore, the correct placement does not need to be determined for the first two dates.
		if(workDays.length < 2){
			$("#times").append(html);	
		}
		else{
			
			//Determine which time div element this new date will be place before
			$insertBeforeE = getInsertBeforeElement(workDay.date);
			
			//If the new date is in the middle of the already-added days
			if($insertBeforeE != null){
				$(html).insertBefore($insertBeforeE);
			}
			//Else the new day is the latest thus far
			else{
				$("#times").append(html);	
			}
			
		}
				
		//Set the time picker
		newTimeDiv = $("#times").find("div[data-date='" + workDay.date + "']");
		$($(newTimeDiv).find("input.start-time")[0]).timepicker({
			'scrollDefault' : '7:00am'
		});
		$($(newTimeDiv).find("input.end-time")[0]).timepicker({
			'scrollDefault' : '5:00pm'
		});
		
		
	}
	
	function getInsertBeforeElement(newDate){
		
		var $e = null;
		
		//loop through all days added
		$("#times").find(".time").each(function(){
			
			var thisDate = parseInt($(this).attr("data-date"));
			
			//If the date is larger
			if(thisDate > newDate){
				
				//Insert before this time
				$e = $(this);
				
				//break from .each loop
				return false;
			}
		})
	
		if($e != null){
			return $e;
		}else{
			//The new date is greater than all dates already added
			return null;	
		}
		
		
	
	}
	
	function resetVars(){
		workDays = [];
		firstworkDay = {};
		secondworkDay = {};
		isSecondDaySelected = 0;
		isMoreThanTwoDaysSelected = 0;	
		rangeIsSet = 0;
		rangeIsBeingSet = 0;
	}
	
	
	function isWorkDayAlreadyAdded(date, workDays){
		
		var arr = [];
		
		arr = $.grep(workDays, function(workDay){
			return workDay.date == date;
		})
		
		if(arr.length > 0){
			return true;
		}
		else{
			return false;
		}
		
		
	}
	
	function removeWorkDay(dateToRemove, workDays){
		
    	
    	//Delete the html for the date's set-time-functionality
    	var e = $("#times").find("div[data-date='" + dateToRemove + "']")[0];
    	$(e).remove();
				
		//Remove JSON object form array
		return workDays = $.grep(workDays, function(workDay){
			
			//Return the work day if its date does NOT equal the date to remove
    		return workDay.date != dateToRemove;
    	})		

	}


	function select(date, obj){
		
		var days = $("#" + obj.id ).find("td[data-month=" + obj.currentMonth + "] a");
		
		days.each(function(){
			if($(this).text() == obj.currentDay){
				var par = $(this).parents()[0];
				$(par).addClass("active111");
			}
		})
		
	}
	
	
	
	
	function removeDate(date, days){
		return $.grep(days, function(day){
			return day != date;
		})
	}
	
	function isDayAlreadyAdded(date, days){
		
		var arr = [];
		
		arr = $.grep(days, function(day){
			return day == date;
		})
		
		if(arr.length > 0){
			return true;
		}
		else{
			return false;
		}
		
		
	}
