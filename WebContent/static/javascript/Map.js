function initMap() {
		
		var jobLat = parseFloat($("#map").attr("data-lat"));
		var jobLng = parseFloat($("#map").attr("data-lng"));

		var center = {
			lat : jobLat,
			lng : jobLng,
		};
		var map = new google.maps.Map(document.getElementById('map'), {
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

