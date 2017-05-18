function saveAvailability(){
	var availabilityDto = getAvailabilityDto();
//	if(availabilityDto.stringDays.length > 0){
		executeAjaxCall_updateAvailability(availabilityDto);	
//	}

}

function getAvailabilityDto(){
	
	var availabilityDto = {};
	availabilityDto.stringDays = [];
	
	var temp = [];
	$("#calendarContainers").find(".calendar").each(function(){
		
		temp  = getSelectedDates($(this), "yy-mm-dd");		
		if(temp.length > 0) $.merge(availabilityDto.stringDays, temp);
	})
	
	return availabilityDto;
}


function executeAjaxCall_updateAvailability(availabilityDto){	
	
	broswerIsWaiting(true);
	
	$.ajax({
		type : "POST",
		url: '/JobSearch/availability/update',
		headers : getAjaxHeaders(),
		contentType : "application/json",
		data : JSON.stringify(availabilityDto),
		dataType : "html",		
		success : _success,
		error : _error,
		cache: true
	});

	function _success(response) {
		broswerIsWaiting(false);
		$("#availableDays").html(response);
		setState_AfterAvailabilityAlterations();
	}	

	function _error(response) {
		broswerIsWaiting(false);
		alert('DEBUG: error executeAjaxCall_saveFindJobFilter')		
	}
}