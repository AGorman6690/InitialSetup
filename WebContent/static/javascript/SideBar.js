	function hideSectionContainers(sectionContainerId){
		$.each($("#sectionContainers").find(".section-container"), function(){
			if($(this).attr("id") != sectionContainerId){
				//slideUp($(this), 500);
				$(this).hide();
			}
			
		})
	}
	
	function showSectionContainer(sectionContainerId){
		var sectionContainer = $("#sectionContainers").find("#" + sectionContainerId)[0];
		
		//slideDown($(sectionContainer), 500);
		$(sectionContainer).show();
		
	}
	
	function selectSideBar(sectionContainerId){
		hideSectionContainers(sectionContainerId);
		showSectionContainer(sectionContainerId);
		
		highlightArrayItemByAttributeValue("data-section-id", sectionContainerId, $("#sideBarContainer").find(".side-bar"), "selected-blue");
//		highlightArrayItem($("#" + sectionContainerId)
	}
	

	$(document).ready(function() {
		
		
		$(".side-bar").click(function(){
			
			var sectionContainerId = $(this).attr("data-section-id");
			selectSideBar(sectionContainerId);
			
			var urlPath = window.location.pathname;
			var obj = {};
			obj.urlPath = urlPath;
			obj.sectionContainerId = sectionContainerId;
			
			
			var newStringArray = addElementToStringArray(obj, sessionStorage.sectionContainerIds);
			sessionStorage.sectionContainerIds = newStringArray;

		})
		
		$("#next").click(function(){
			var selectedSection = $("#sideBarContainer").find(".side-bar.selected-blue")[0];			
			var nextSection = $(selectedSection).next();
			
			if($(nextSection).hasClass("side-bar") == 0){
				nextSection = ($("#sideBarContainer").find(".first"));
			}
			
			selectSideBar($(nextSection).attr("data-section-id"));
			
			
		})
		
	})
