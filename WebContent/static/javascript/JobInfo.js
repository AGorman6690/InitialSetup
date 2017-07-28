//var $calendar_applicationWorkDays;
//var workDayDtos = [];
var workDayDtos = [];
$(document).ready(function() {
	
	$("body").on("click", ".show-job-info-mod", function() {
		var $e = $(this);
		var jobId = $e.attr("data-job-id");
		var p = $e.attr("data-p");
		var context = $e.attr("data-context");
		
		executeAjaxCall_showJobInfoMod(jobId, context, p);
	})
	
	$("body").on("click", ".show-ratings-mod", function() {	
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
		var lat = $("#map").attr("data-lat");
		var lng = $("#map").attr("data-lng");
		var win = window.open("https://www.google.com/maps/place/" + lat + "+" + lng + "/@" + lat + "," + lng + ",15z", "_blank");
		win.focus();
	})

	workDayDtos = parseWorkDayDtosFromDOM($("#json_work_day_dtos"));
	initCalendar_new($("#work-days-calendar-container .calendar"), workDayDtos);	
}) 
function executeAjaxCall_showJobInfoMod(jobId, c, p, callback) {
	broswerIsWaiting(true);
	var url = "/JobSearch/preview/job-info/" + jobId + "?c=" + c;
	if (p != undefined) url +=  "&p=" + p;
	$.ajax({
		type: "POST",
		headers: getAjaxHeaders(),
		url: url,
//		url: "/JobSearch/job/" + jobId + "?c=" + c + "&p=" + p,
		dataType: "html"
	}).done(function(html) {
		
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
		renderStars($jobInfoMod);
		
		executeCallBack(callback);
	})
	
}
function setScrollAction() {
	var $e = $(".to-be-fixed").eq(0);
	if($e.length){
		var $applyCont = $(".to-be-fixed-cont").eq(0);
		var $jobInfo = $(".job-info");
		
		var fixAtScrollAmount = $e.offset().top - 10;
//		$("body").on("scroll", "#job-info-mod .mod-content", function(){
		var $window = $("#job-info-mod .mod-content"); 
		$window.scroll(function(){
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