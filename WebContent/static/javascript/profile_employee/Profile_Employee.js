

$(document).ready(function(){
	
	$applications_list_view = $("#applications_list_view");
	$applications_calendar_view = $("#applications_calendar_view");
	
	$(".select-page-section-container #show_list_and_calendar").click(function(){
		
		$applications_list_view.show();
		$applications_calendar_view.show();
		
		$applications_list_view.addClass("with-calendar");
		$applications_calendar_view.addClass("with-list");
		
		highlightArrayItem($(this), $(".select-page-section-container .select-page-section"), "selected");
		
	})
	
	$(".select-page-section-container :not(#show_list_and_calendar)").click(function(){


		
		$applications_list_view.removeClass("with-calendar");
		$applications_calendar_view.removeClass("with-list");
		
	})

	
	
})