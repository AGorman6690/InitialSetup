function initMap_find_jobs() {
	//Eventually initialize it to a user defualt
	var myLatLng = {
		lat : 44.954445,
		lng : -93.091301,
	};
	var myStyles =[
	               {
	                   featureType: "poi",
	                   elementType: "labels",
	                   stylers: [
	                         { visibility: "off" }
	                   ]
	               }
	           ];
	
	var map = new google.maps.Map(document.getElementById('find-jobs-map'), {
		zoom : 8,
		center : myLatLng,
//		scrollwheel: true,
		streetViewControl: false,
//			disableDefaultUI: true,
	    mapTypeControlOptions: {
	      mapTypeIds: [google.maps.MapTypeId.ROADMAP]
	    },
    	styler: myStyles,
    	clickableIcons: false,
                

	});	
}

function getZoom(radius){
	
	if (radius < 0)
		//This will occur if no jobs match the user's fiter(s)
		return 11;
	else if (radius < 5)
		return 12
	else if (radius < 25)
		return 11
	else if (radius < 50)
		return 10
	else if (radius < 100)
		return 8
	else if (radius < 500)
		return 6
	else
		return 5;
}


function setMap_renderFindJobsResults(){
	
	var requestedLat = $("#requestOrigin").data("lat");
	var requestedLng = $("#requestOrigin").data("lng");
	var requestedRadius = $("#requestOrigin").data("max-dist");
	
	var requestedLatLng = {
			lat : requestedLat,
			lng : requestedLng,
		};

	var map = new google.maps.Map(document.getElementById('find-jobs-map'), {
		zoom : getZoom(requestedRadius),
		center : requestedLatLng,
//		scrollwheel: true,
		streetViewControl: false,
//			disableDefaultUI: true,
	    mapTypeControlOptions: {
	      mapTypeIds: [google.maps.MapTypeId.ROADMAP]
	    }
	});
	
	//Show job markers
	$("#get-jobs-results").find(".job").each(function(){
		var job = this;
		var jobId = $(this).attr("id");
		var jobName = $(this).find(".job-name").html();
		
		var jobLatLng = {
				lat : $(this).data("lat"),
				lng : $(this).data("lng")
			};
		
		var icon = {
			url: "/JobSearch/static/images/green-square.png",
			scaledSize: new google.maps.Size(10, 10),
		}
		
		var icon_mouseover = {
    			url: "/JobSearch/static/images/map-marker-red.png",
    			scaledSize: new google.maps.Size(30, 30),
    		}
		
		var marker = new google.maps.Marker({
			position : jobLatLng,
			map : map,
			icon: icon,
			jobId: jobId,
		});
		

		var infowindow = new google.maps.InfoWindow({
			content: "<div>" + jobName + "</div>"
		});


		marker.addListener('click', function() {
			scrollToJob(this.jobId);
			addBorderToJob(this.jobId);
		});
      
	 
		marker.addListener('mouseover', function() {
			infowindow.open(map,marker);
		});
	
		
		var centerMapOnJob = $(this).find(".glyphicon-move")[0];
		
	    google.maps.event.addDomListener(job, 'mouseout', function () {    	  
	    	marker.setIcon(icon);
	    });
		
	    google.maps.event.addDomListener(job, 'mouseover', function () {  
	    	marker.setIcon(icon_mouseover);
	
	    });
	    
	    google.maps.event.addDomListener(centerMapOnJob, "click", function () {
	//        infowindow.setContent(this.html);
	//        infowindow.open(map, this);
	        map.setCenter(marker.getPosition()); 
	//        map.setZoom(5);
	    });
	
	    marker.addListener('mouseout', function() {
	    	infowindow.close();
	    });
		
	})	
	
}
