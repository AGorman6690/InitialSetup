function clearAllInputs($container){
	
	$container.find("input[type=text], textarea, select").each(function(){		
		$(this).val("");
	})
	
	$container.find("input[type=radio]").each(function(){		
		$(this).prop("checked", false);
	})	
	
	$container.find(".calendar .active111").each(function(){		
		$(this).removeClass("active111");
	})	
}

function removeAllInvalidStyles($container){
	$container.find(".invalid").each(function(){
		$(this).removeClass("invalid");
	})
}

function disableAllInputFields($container){
	
	
	$container.find("input, textarea, select").each(function(){		
		$(this).prop("disabled", true);
	})

	//Set the date picker's "enabledness"
	$container.find("#calendar").each(function(){
		$(this).datepicker("disable");
	})


}

function enableAllInputFields($container){
	
	
	$container.find("input, textarea, select").each(function(){		
		$(this).prop("disabled", false);
	})

	//Set the date picker's "enabledness"
	$container.find("#calendar").each(function(){
		$(this).datepicker("enable");
	})


}

function isRadioContainerSelected($container){
	if($container.find("input[type=radio]:checked").length == 0) return false;
	else return true;
}

function areInputsValid_Container($container){
	
	var option;
	var isInvalid = 0;
	var name = "";
	
	// A container might have multiple radio groups.
	// Check all of them.
	$container.find("input[type=radio]").each(function(){
		name = $(this).attr("name");		
		if($container.find("input[type=radio][name=" + name + "]:checked").length == 0){
			isInvalid += 1;
		}
			
	})
	
	// Text inputs
	$container.find("input[type=text]").each(function(){
		if($(this).val() == "") isInvalid += 1;
		
	})
	
	// Selects
	$container.find("select").each(function(){		
		
		if($(this).val() == "") isInvalid += 1;
		
		if($(this).find("option:selected").length == 0) isInvalid += 1;
	})
	
	// Calendar
	$container.find(".calendar").each(function(){	
		if($(this).find(".active111").length == 0) isInvalid += 1;		
	})	
	
	
	if(isInvalid > 0 ) return false;
	else return true;
}

function removeInvalidCss($container){
	$container.find(".invalid").each(function(){
		$(this).removeClass("invalid");
	})
}

function setTimeOptions($eSelect, increment, placeholder){
	
	
	//***********************************************************
	//***********************************************************
	// Obsolete this.
	// This thing is ridiculous.
	//***********************************************************
	//***********************************************************
	
	
	
	var firstOption = "<option selected disabled style='color: #888'>";
	if(placeholder != undefined){
		firstOption += placeholder;
	}
	firstOption += "</option>";
	
	
	
	
	
	// For whatever reason the Local Time object will not apped
	// the seconds if the seconds are zero...
//	var initTime = $eSelect.attr("data-init-time") + ":00";
	var selected = "";
	var formattedTime = "";
	
	if(increment > 0){
			
		$eSelect.empty();
//		$eSelect.append('<option value="" selected style="display: none"></option>');
		$eSelect.append(firstOption);
		
		var hourCount;
		var hour;
		var minute;
		var modifiedMinute;
		var amPm;
		var time;
		//Hour
		hour = 12;
		for(hourCount = 0; hourCount < 24; hourCount++){

			//Am or pm
			if(hourCount < 12){
				amPm = " am";
			}else{
				amPm = " pm";
			}
			
			if(hourCount < 10) hourCount = "0" + hourCount;
			
			
			//Minute
			for(minute = 0; minute < 60; minute += increment){
				if(minute < 10){
					modifiedMinute = "0" + minute;	
				}else{
					modifiedMinute = minute;
				}	
				
				
				
				time = hour + ":" + modifiedMinute + amPm;
				
				formattedTime = hourCount + ":" + modifiedMinute + ":00";
//				formattedTime = formatTime(time);
				
//				if(formattedTime == initTime) selected = "selected";
//				else selected = "";
				
				var className = "";
				if(modifiedMinute == "00") className = "bold";
				
				
				$eSelect.append("<option class='" + className + "' data-filter-value='" + formattedTime + "'>"
									+ time + "</option>");
			}
			
			//Incerment the hour
			if(hour == 12){
				hour = 1;
			}else{
				hour ++;
			}				 
		}
	}
}