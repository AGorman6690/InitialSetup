$(document).ready(function(){
	$("#personal-info .value").click(function() {
		$(this).closest(".info-item").find(".edit-container").eq(0).toggle();
	})
	
	$("#save-home-location").click(function(){		
		updateHomeLocation();
	})	
	$("#save-max-distance").click(function(){		
		updateMaxWorkRadius();
	})	
	$("#save-about").click(function(){		
		updateAbout();
	})		
})


function updateHomeLocation(){	
	var city = $("#city").val();;
	var state = $("#state option:selected").val();
	var zip = $("#zipCode").val();
	var url = "/JobSearch/user/update-home-location?city=" + city + "&state=" + state + "&zip=" + zip;
	executeAjaxCall_updateUserSettings(url);
}
function updateMaxWorkRadius(){	
	maxWorkRadius = $("#miles").val();
	var url = "/JobSearch/user/update-max-work-radius?maxWorkRadius=" + maxWorkRadius;
	executeAjaxCall_updateUserSettings(url);
}
function updateAbout(){	
	about = $("#about").val();
	var url = "/JobSearch/user/update-about?about=" + about;
	executeAjaxCall_updateUserSettings(url);
}
function executeAjaxCall_updateUserSettings(url){	

	broswerIsWaiting(true);
	$.ajax({
		type : "POST",
		url: url,
		headers : getAjaxHeaders(),
		dataType : "text",	
	}).done(function _success(response) {
		broswerIsWaiting(false);	
		location.reload();
	})
}