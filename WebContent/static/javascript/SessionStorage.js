$(document).ready(function(){
	
	var sectionContainerId;
	var urlPath = window.location.pathname;

	//Select the nav item
	if(sessionStorage.clickedNavItemId != undefined){
		selectNavItem(sessionStorage.clickedNavItemId);	
	}

	// Initialize section container ids
	if(sessionStorage.sectionContainerIds == undefined){
		sessionStorage.sectionContainerIds = JSON.stringify([]);
		
		
	}
	
	// Set the section container
	sectionContainerId = getSectionContainerIdByUrlPath(urlPath)
	if(sectionContainerId != undefined){		
		selectSideBar(sectionContainerId);
	}
	
	
	
})

function getSectionContainerIdByUrlPath(urlPath){
	
	var sectionContainerId = undefined;
	var arr = JSON.parse(sessionStorage.sectionContainerIds);
	
	
	$.each(arr, function(){
		if(this.urlPath == urlPath){
			sectionContainerId = this.sectionContainerId;
		}
	})
	
	return sectionContainerId;
	
}



function addElementToStringArray(element, stringArray){
	
	var realArray = JSON.parse(stringArray);
	realArray.push(element);
	
	return JSON.stringify(realArray);
	
	
}