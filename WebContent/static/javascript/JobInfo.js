//var $calendar_applicationWorkDays;
//var workDayDtos = [];
var workDayDtos = [];
$(document).ready(function() {
	
	$("body").on("click", ".show-ratings-mod", function() {	
		var $e = $(this);
		var $modContainer = $e.closest(".ratings-mod-container");
		if(!$modContainer.find(".mod").eq(0).is(":visible")){			
			var userId_applicant = $e.attr("data-user-id");
			var $e_renderHtml = $modContainer.find(".mod-body").eq(0);
			executeAjaxCall_getRatingsByUser(userId_applicant, $e_renderHtml);	
		}
		
	})
	
	var $e = $("#apply-for-job");
	if($e.length){
		var $applyCont = $("#apply-for-job-cont");
		var $jobInfo = $(".job-info");
		
		var fixAtScrollAmount = $e.offset().top - 10;
		$(window).scroll(function(){
			if($(window).scrollTop() > fixAtScrollAmount) $applyCont.addClass("fixed");
			else $applyCont.removeClass("fixed");
		})
	}	
	
	$("body").on("click", "#job-address", function(){
// **********************
// 			http://stackoverflow.com/questions/6582834/use-a-url-to-link-to-a-google-map-with-a-marker-on-it
// **********************
		var lat = $("#map").attr("data-lat");
		var lng = $("#map").attr("data-lng");
		var win = window.open("https://www.google.com/maps/place/" + lat + "+" + lng + "/@" + lat + "," + lng + ",15z", "_blank");
		win.focus();
	})

	workDayDtos = parseWorkDayDtosFromDOM($("#json_work_day_dtos"));
	initCalendar_new($("#work-days-calendar-container .calendar"), workDayDtos);	
}) 

