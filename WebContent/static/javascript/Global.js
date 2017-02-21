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
			
})


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
	$e.append('<option value="AL">AL</option>');
	$e.append('<option value="AK">AK</option>');
	$e.append('<option value="AZ">AZ</option>');
	$e.append('<option value="AR">AR</option>');
	$e.append('<option value="CA">CA</option>');
	$e.append('<option value="CO">CO</option>');
	$e.append('<option value="CT">CT</option>');
	$e.append('<option value="DE">DE</option>');
	$e.append('<option value="DC">DC</option>');
	$e.append('<option value="FL">FL</option>');
	$e.append('<option value="GA">GA</option>');
	$e.append('<option value="HI">HI</option>');
	$e.append('<option value="ID">ID</option>');
	$e.append('<option value="IL">IL</option>');
	$e.append('<option value="IN">IN</option>');
	$e.append('<option value="IA">IA</option>');
	$e.append('<option value="KS">KS</option>');
	$e.append('<option value="KY">KY</option>');
	$e.append('<option value="LA">LA</option>');
	$e.append('<option value="ME">ME</option>');
	$e.append('<option value="MD">MD</option>');
	$e.append('<option value="MA">MA</option>');
	$e.append('<option value="MI">MI</option>');
	$e.append('<option value="MN">MN</option>');
	$e.append('<option value="MS">MS</option>');
	$e.append('<option value="MO">MO</option>');
	$e.append('<option value="MT">MT</option>');
	$e.append('<option value="NE">NE</option>');
	$e.append('<option value="NV">NV</option>');
	$e.append('<option value="NH">NH</option>');
	$e.append('<option value="NJ">NJ</option>');
	$e.append('<option value="NM">NM</option>');
	$e.append('<option value="NY">NY</option>');
	$e.append('<option value="NC">NC</option>');
	$e.append('<option value="ND">ND</option>');
	$e.append('<option value="OH">OH</option>');
	$e.append('<option value="OK">OK</option>');
	$e.append('<option value="OR">OR</option>');
	$e.append('<option value="PA">PA</option>');
	$e.append('<option value="RI">RI</option>');
	$e.append('<option value="SC">SC</option>');
	$e.append('<option value="SD">SD</option>');
	$e.append('<option value="TN">TN</option>');
	$e.append('<option value="TX">TX</option>');
	$e.append('<option value="UT">UT</option>');
	$e.append('<option value="VT">VT</option>');
	$e.append('<option value="VA">VA</option>');
	$e.append('<option value="WA">WA</option>');
	$e.append('<option value="WV">WV</option>');
	$e.append('<option value="WI">WI</option>');
	$e.append('<option value="WY">WY</option>');
	
	
}
