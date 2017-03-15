
$(document).ready(function(){
	
	$(".select-page-section-container .select-page-section").click(function(){
		
		var pageSectionId = $(this).attr("data-page-section-id");
		
		$(".page-section").each(function() {
			if($(this).attr("id") == pageSectionId) $(this).show();
			else $(this).hide();
		})
		
		highlightArrayItem($(this), $(".select-page-section-container .select-page-section"), "selected");
		
	})
	
	initPage_selectPageSection();

})

function initPage_selectPageSection(){
	$(".select-page-section-container .select-page-section.selected").click();
}