$(document).ready(function(){
//	
	$("html").click(function(e){
		
		// Close all dropdowns if user clicked outside of a dropdown		
		if($(e.target).closest(".dropdown-container").length == 0 &&
				$(e.target).hasClass("dropdown-container") == 0){

				$("html").find(".dropdown-style:visible").each(function(){
					$(this).closest(".dropdown-container").eq(0).find("[data-toggle-id]").eq(0).click();
				})
//					}
			
		}
	})
			
})


function redirectToProfile(){
	window.location.replace("/JobSearch/user/profile");
}


function broswerIsWaiting(isWaiting){
	if(isWaiting) $("html").addClass("waiting");
	else $("html").removeClass("waiting");
}

