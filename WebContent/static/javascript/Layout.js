$(document).ready(function(){
	$("[data-toggle-id]").click(function(){
		var toggleId = $(this).attr("data-toggle-id");
		var toggleSpeed = $(this).attr("data-toggle-speed");
		var $e = $("#" + toggleId);
		//For whatever reason, using .toggle() function makes the container of the element-to-toggle
		//move to the right and back again ever so slightly.
		//Using .hide
		
		if(toggleSpeed == 0){
			$e.slideToggle(700);	
		}
		else if(toggleSpeed == 1){
			$e.slideToggle(500);	
		}
		else if(toggleSpeed == 2){
			$e.slideToggle(300);	
		}else{
			$e.slideToggle(500);	
		}
		
//		var $eToToggle = $("#" + toggleId);
//        if($eToToggle.is(":visible")){
//        	$eToToggle.hide(200);
//    	} 
//    	else {
//    		$eToToggle.show(200);
//		}
		  
		
		//For this toggle feature, sometimes this span is a wrapper around the header text.
		//If it's not a wrapper.
		if($(this).hasClass("glyphicon-menu-up") == 1 || $(this).hasClass("glyphicon-menu-down") == 1){
			toggleClasses($(this), "glyphicon-menu-up", "glyphicon-menu-down");
		}
		//If it's a wrapper.
		else{
			var glyphicon = $(this).find(".glyphicon-menu-up, .glyphicon-menu-down")[0];
			toggleClasses($(glyphicon), "glyphicon-menu-up", "glyphicon-menu-down");
		}
		
	})
	
	
})

function triggerToggle(dataToggleId){
	$("span[data-toggle-id='" + dataToggleId + "']").click();
}