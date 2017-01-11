	
$(document).ready(function(){
	$(".nav-items a").click(function(){
		highlightArrayItem(this, $("#navBar").find(".nav-items a"), "selected-green");
		
	  sessionStorage.clickedNavItemId = $(this).attr("id");
	  
	})
	
	$("#nav_logOut").click(function(){
		sessionStorage.clear();
	})

})

function selectNavItem(navItemId){
	highlightArrayItem($("#" + navItemId), $("#navBar").find(".nav-items a"), "selected-green");
}
