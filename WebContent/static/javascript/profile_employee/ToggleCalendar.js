$(document).ready(function(){
	$("#toggleCalendar").click(function(){
	
		var $toggleCalendar = $("#toggleCalendar");
		var $calendarColumn = $("#calendarColumn");
		var $tableColumn = $("#tableColumn");
		
		
		toggleClasses($tableColumn, "col-sm-6", "col-sm-12");
		toggleClasses($tableColumn, "with-calendar", "no-calendar");
		$calendarColumn.toggle();
		toggleClasses($toggleCalendar, "do-hide", "do-show");
		
		if($calendarColumn.is(":visible")){
						
			
		}
		
	})
	
})
