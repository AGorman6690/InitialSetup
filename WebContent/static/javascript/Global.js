function redirectToProfile(){
	window.location.replace("/JobSearch/user/profile");
}


function broswerIsWaiting(isWaiting){
	if(isWaiting) $("html").addClass("waiting");
	else $("html").removeClass("waiting");
}