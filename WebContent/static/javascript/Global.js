var global_workDayDtos = [];

$(document).ready(function(){
//	
	$("html").click(function(e){

		// Close all dropdowns if user clicked outside of a dropdown		
		if($(e.target).closest(".dropdown-container").length == 0 &&
				$(e.target).hasClass("dropdown-container") == 0 && 
				$(e.target).closest(".mod").length == 0){

				$("html").find(".dropdown-style:visible").each(function(){
					$(this).closest(".dropdown-container").eq(0).find("[data-toggle-id]").eq(0).click();
				})
//					}
			
		}
	})
	
	$("body").on("click", ".button-group button", function(){
	
		selectButton($(this))
		
	})
	
	
	$(document).on("click", "[data-toggle-id]", function(){
		var toggleId = $(this).attr("data-toggle-id");
		var toggleSpeed = $(this).attr("data-toggle-speed");
		var $e = $("#" + toggleId);
		//For whatever reason, using .toggle() function makes the container of the element-to-toggle
		//move to the right and back again ever so slightly.
		//Using .hide
		
		if(toggleSpeed == -2){
			if($e.is(":visible")) $e.hide();
			else $e.show();
		}
		else if(toggleSpeed == -1){
			$e.slideToggle(1000);	
		}
		else if(toggleSpeed == 0){
			$e.slideToggle(700);	
		}
		else if(toggleSpeed == 1){
			$e.slideToggle(500);	
		}
		else if(toggleSpeed == 2){
			$e.slideToggle(300);	
		}else{
//			$e.slideToggle(500);	
			if($e.is(":visible")){
				$e.hide();
				$e.removeClass("always-show");
			}
			else{
				$e.show();
				$e.addClass("always-show");
			}
			// No idea why this show() was here in tandem with the slideToggle()....
//			$e.show();
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


function parseWorkDayDtosFromDOM($e) {
	
	// ****************************************************
	// ****************************************************
	// I don't think this is the best way to get work day objects when a page loads.
	// Is it a security risk to show the user how our data is structured?
	// This div containing json is not deleted after the work day objects are initialized
	// because some pages load work day objects more than once.
	// i.e. The ViewJob_Employer page needs work day dtos for the job posting calendar
	// and the summary calendars.
	// After the code is "finalized", this structer can be revisited and perhaps 
	// a global variable can be used for ALL calendars on a page. 
	//______________________________________________________________________
	// An alternative is to make an ajax call after the page loads and retrieve the work day dtos
	// Revisit this.
	// ****************************************************
	// ****************************************************
	
	var workDayDtos = [];
	if($e.length > 0){
		workDayDtos = JSON.parse($e.html());
//		$e.empty();	
	}
	
	$(workDayDtos).each(function() {
		this.date = dateify(this.workDay.stringDate);
	})
	
	return workDayDtos;
	
}


function selectButton($button){
	
	
	var $buttonGroup = $button.closest(".button-group");
	var className = $buttonGroup.attr("data-class-name");
	if(className == undefined) className = "selected";
	
	var doNotToggle = false;
	if($buttonGroup.hasClass("no-toggle")) doNotToggle = true;
	
	if(doNotToggle){
		highlightArrayItem($button, $buttonGroup.find("button"), className);
	}else{
		if($button.hasClass(className)) $button.removeClass(className);
		else highlightArrayItem($button, $buttonGroup.find("button"), className);	
	}
	
}


function redirectToProfile(){
	window.location.replace("/JobSearch/user/profile");
}


function broswerIsWaiting(isWaiting){
	if(isWaiting) $("body").addClass("waiting");
	else $("body").removeClass("waiting");
}



function setStates(){
	var $e = $("#state"); 
	$e.append('<option value="" selected disabled>State</option>');
	$e.append('<option value="Alabama">AL</option>');
	$e.append('<option value="Alaska">AK</option>');
	$e.append('<option value="Arizona">AZ</option>');
	$e.append('<option value="Arkansas">AR</option>');
	$e.append('<option value="California">CA</option>');
	$e.append('<option value="Colorado">CO</option>');
	$e.append('<option value="Connecticut">CT</option>');
	$e.append('<option value="Delaware">DE</option>');
	$e.append('<option value="Florida">FL</option>');
	$e.append('<option value="Georgia">GA</option>');
	$e.append('<option value="Hawaii">HI</option>');
	$e.append('<option value="Idaho">ID</option>');
	$e.append('<option value="Illinois">IL</option>');
	$e.append('<option value="Indiana">IN</option>');
	$e.append('<option value="Iowa">IA</option>');
	$e.append('<option value="Kansas">KS</option>');
	$e.append('<option value="Kentucky">KY</option>');
	$e.append('<option value="Louisiana">LA</option>');
	$e.append('<option value="Maine">ME</option>');
	$e.append('<option value="Maryland">MD</option>');
	$e.append('<option value="Massachusetts">MA</option>');
	$e.append('<option value="Michigan">MI</option>');
	$e.append('<option value="Minnesota">MN</option>');
	$e.append('<option value="Mississippi">MS</option>');
	$e.append('<option value="Missouri">MO</option>');
	$e.append('<option value="Montana">MT</option>');
	$e.append('<option value="Nebraska">NE</option>');
	$e.append('<option value="Nevada">NV</option>');
	$e.append('<option value="New Hampshire">NH</option>');
	$e.append('<option value="New Jersey">NJ</option>');
	$e.append('<option value="New Mexico">NM</option>');
	$e.append('<option value="New York">NY</option>');
	$e.append('<option value="North Carolina">NC</option>');
	$e.append('<option value="North Dakota">ND</option>');
	$e.append('<option value="Ohio">OH</option>');
	$e.append('<option value="Oklahoma">OK</option>');
	$e.append('<option value="Oregon">OR</option>');
	$e.append('<option value="Pennsylvania">PA</option>');
	$e.append('<option value="Rhode Island">RI</option>');
	$e.append('<option value="South Carolina">SC</option>');
	$e.append('<option value="South Dakota">SD</option>');
	$e.append('<option value="Tennessee">TN</option>');
	$e.append('<option value="Texas">TX</option>');
	$e.append('<option value="Utah">UT</option>');
	$e.append('<option value="Vermont">VT</option>');
	$e.append('<option value="Virginia">VA</option>');
	$e.append('<option value="Washington">WA</option>');
	$e.append('<option value="West Virginia">WV</option>');
	$e.append('<option value="Wisconsin">WI</option>');
	$e.append('<option value="Wyoming">WY</option>');	
	
	
	var initValue = $e.attr("data-init-value");
	$e.find("option[value='" + initValue + "']").prop("selected", true);
	
	
	
}
