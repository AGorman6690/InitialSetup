//var $calendar_applicationWorkDays;
//var workDayDtos = [];
var workDayDtos = [];
$(document).ready(function() {
	
	$("body").on("click", ".show-job-info-mod", function() {
		showJobInfo($(this));		
	})
	
	$("body").on("click", ".show-ratings-mod", function() {	
		
		// ***********************************************
		// ***********************************************
		// Refactor: use the generic "show ratings mod" in Ratings.js
		// ***********************************************
		// ***********************************************
				
		
		var $e = $(this);
		var $modContainer = $e.closest(".ratings-mod-container");
		if(!$modContainer.find(".mod").eq(0).is(":visible")){			
			var userId_applicant = $e.attr("data-user-id");
			var $e_renderHtml = $modContainer.find(".mod-body").eq(0);
			executeAjaxCall_getRatingsByUser(userId_applicant, $e_renderHtml);	
		}
		
	})
	

	
	$("body").on("click", "#job-address", function(){
// **********************
// 			http://stackoverflow.com/questions/6582834/use-a-url-to-link-to-a-google-map-with-a-marker-on-it
// **********************
		var lat = $("#job-info-map").attr("data-lat");
		var lng = $("#job-info-map").attr("data-lng");
		var win = window.open("https://www.google.com/maps/place/" + lat + "+" + lng + "/@" + lat + "," + lng + ",15z", "_blank");
		win.focus();
	})

	
	workDayDtos = parseWorkDayDtosFromDOM($("#json_work_day_dtos"));
	initCalendar_new($("#work-days-calendar-container .calendar"), workDayDtos);	
}) 
function showJobInfo($e){
	var jobId = $e.attr("data-job-id");
	var p = $e.attr("data-p");
	var context = $e.attr("data-context");
	executeAjaxCall_showJobInfoMod(jobId, context, p);
}
function executeAjaxCall_showJobInfoMod(jobId, c, p, callback) {
	
	var url = "/JobSearch/job/";
		url += jobId + "?c=" + c;
		if (p != undefined) url +=  "&p=" + p;
	
	broswerIsWaiting(true);
	$.ajax({
		type: "POST",
		headers: getAjaxHeaders(),
		url: url,
		dataType: "html"
	}).done(function(html) {
		broswerIsWaiting(false);
		showJobInfoMod(html);		
		executeCallBack(callback);
//		setScrollAction();
	})
	
}
function showJobInfoMod(html){
	
	var $jobInfoMod = $("#job-info-mod");
	$jobInfoMod.find(".mod-body").html(html);
	
	workDayDtos = parseWorkDayDtosFromDOM($("#json_work_day_dtos"));
	initCalendar_new($("#work-days-calendar-container .calendar"), workDayDtos);
	var d = $(".job-info .map").get();

	$jobInfoMod.show();
	setScrollAction();
	// **************************************************
	// Map must be initialized AFTER the modal is shown
	// **************************************************
	initMap_job_info();
	renderStars($jobInfoMod);
}
function setScrollAction() {
	var $e = $(".fix-content-container").eq(0);
	if($e.length){
		var $jobInfo = $(".job-info");		
		var fixAtScrollAmount = $("#job-info-mod .mod-body").position().top + $e.height() - 10;
		var $window = $("#job-info-mod .mod-content");		
		// bind the event
		$window.scroll(function(){
			console.log($e.position().top + " : " + $e.offset().top + " : " + $window.scrollTop());
			if($window.scrollTop() > fixAtScrollAmount){
				$jobInfo.addClass("fixed");
			}
			else $jobInfo.removeClass("fixed");
		})
	}	
}
function renderHtml_jobInfo(html, doRenderStars, doSelectAllWorkDays){
	broswerIsWaiting(false);
	var $jobInfoMod = $("#job-info-mod");
	$jobInfoMod.find(".mod-body").html(html);
	
	workDayDtos = parseWorkDayDtosFromDOM($("#json_work_day_dtos"));
	initCalendar_new($("#work-days-calendar-container .calendar"), workDayDtos);
	var d = $(".job-info .map").get();

	$jobInfoMod.show();
	
	// **************************************************
	// Map must be initialized AFTER the modal is shown
	// **************************************************
	initMap_job_info();
	
	if (doRenderStars) renderStars($jobInfoMod);
	
	
	if (selectAllWorkDays) selectAllWorkDays($(".calendar"), workDayDtos);
	
	
//	setScrollAction();
	
//	executeCallBack(callback);
}

function initMap_job_info() {
		
		var $map = $("#job-info-map");
		var jobLat = parseFloat($map.attr("data-lat"));
		var jobLng = parseFloat($map.attr("data-lng"));

		var center = {
			lat : jobLat,
			lng : jobLng,
		};
		
		var map = new google.maps.Map(document.getElementById('job-info-map'), {
			zoom : 11,
			center : center,
			scrollwheel: false,
			streetViewControl: false,			
//				disableDefaultUI: true,
		    mapTypeControlOptions: {
		      mapTypeIds: [google.maps.MapTypeId.ROADMAP]
		    }

		});
		
		var icon = {
				url: "/JobSearch/static/images/map-marker-black.png",
				scaledSize: new google.maps.Size(30, 30),
			}
		
		var marker = new google.maps.Marker({
			position : center,
			map : map,
			icon: icon,
			clickable: false,
		});
	}