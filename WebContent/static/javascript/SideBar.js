	function hideSectionContainers(sectionContainerId){
		$.each($("body").find(".section-container"), function(){
			if($(this).attr("id") != sectionContainerId){
				//slideUp($(this), 500);
				$(this).hide();
			}
			
		})
	}
	
	function showSectionContainer(sectionContainerId){
		var sectionContainer = $("body").find("#" + sectionContainerId)[0];
		
		$(sectionContainer).show();
		
	}
	
	function hideContentContainers(sectionContainerId){
		$.each($("body").find(".content-container"), function(){
			if($(this).attr("id") != sectionContainerId){
				//slideUp($(this), 500);
				$(this).hide();
			}
			
		})
	}
	
	function showContentContainer(sectionContainerId){
		var sectionContainer = $("#sectionContainers").find("#" + sectionContainerId)[0];
		
		$(sectionContainer).show();
		
	}
	
	function selectSideBar(sectionContainerId){
		
		hideSectionContainers(sectionContainerId);
		showSectionContainer(sectionContainerId);
		
		
		highlightArrayItemByAttributeValue("data-section-id", sectionContainerId, $("body").find(".side-bar"), "selected-blue");
	}
	
	function selectContentBar(sectionContainerId){
		
		hideSectionContainers(sectionContainerId);
		showSectionContainer(sectionContainerId);

		highlightArrayItemByAttributeValue("data-section-id", sectionContainerId, $("body").find(".content-bar"), "selected-lines");
	}
	

	$(document).ready(function() {
		
		$("#sideBarContainer").find(".selected-blue").eq(0).click();
		
		//$("#contentBarContainer").find(".selected-lines").eq(0).click();
		
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
		
		
		$(".content-bar").click(function(){
			
		
			var sectionContainerId = $(this).attr("data-section-id");
			selectContentBar(sectionContainerId);
			
//			var urlPath = window.location.pathname;
//			var obj = {};
//			obj.urlPath = urlPath;
//			obj.sectionContainerId = sectionContainerId;
			
			
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
