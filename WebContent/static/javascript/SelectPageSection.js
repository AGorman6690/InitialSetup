


$(document).ready(function(){
	
	$(".select-page-section-container .select-page-section:not(.override-click-event)").click(function(){

		
		
		showPageSection($(this));
		
		// The map doesn't need to be initialized every time.
		// It only needs to be initialized when the map is first **DISPLAYED**
		// ("Displayed" does not mean loaded, but hidden.)
		// Address this later.
//		initMap();
	})
	
	initPage_selectPageSection();

})

function showPageSection($e){
	
	var pageSectionId = $e.attr("data-page-section-id");
	
	$(".page-section").each(function() {
		if($(this).attr("id") == pageSectionId) $(this).show();
		else $(this).hide();
	})
	
	highlightArrayItem($e, $(".select-page-section-container .select-page-section"), "selected");


}

function initPage_selectPageSection(){
	$(".select-page-section-container .select-page-section.selected").click();
	
}