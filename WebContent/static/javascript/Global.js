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
	
	$(".button-group button").click(function(){
	
		selectButton($(this))
		
	})
	
			
})

function selectButton($button){
	
	var $buttonGroup = $button.closest(".button-group");
	var className = $buttonGroup.attr("class-name");
	if(className == undefined) className = "selected";
	
	if($button.hasClass(className)) $button.removeClass(className);
	else highlightArrayItem($button, $buttonGroup.find("button"), className);
}


function redirectToProfile(){
	window.location.replace("/JobSearch/user/profile");
}


function broswerIsWaiting(isWaiting){
	if(isWaiting) $("html").addClass("waiting");
	else $("html").removeClass("waiting");
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
