$(document).ready(function(){
	$("[data-toggle-id]").click(function(){
		var toggleId = $(this).attr("data-toggle-id");
		//For whatever reason, using .toggle() function makes the container of the element-to-toggle
		//move to the right and back again ever so slightly.
		//Using .hide
		$("#" + toggleId).slideToggle(300);
//		var $eToToggle = $("#" + toggleId);
//        if($eToToggle.is(":visible")){
//        	$eToToggle.hide(200);
//    	} 
//    	else {
//    		$eToToggle.show(200);
//		}
		  
		
		toggleClasses($(this), "glyphicon-menu-up", "glyphicon-menu-down");
	})
	
	
})

