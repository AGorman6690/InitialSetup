
$(document).ready(function(){

	
	$(".page-content-link").click(function(){

		var sectionContainerId = $(this).attr("data-section-id");
		selectSectionContainer(sectionContainerId);
		
		highlightArrayItem(this, $("#pageContentLinksContainer").find(".page-content-link"), "selected");
	
	})
	
	$("#pageContentLinksContainer").find(".page-content-link.selected").eq(0).trigger("click");

	
})
	

function selectSectionContainer(sectionContainerId){
	
	$.each($("body").find(".section-container"), function(){
		
		if($(this).attr("id") == sectionContainerId) $(this).show();
		else $(this).hide();
				
	})
}

