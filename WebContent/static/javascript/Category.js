


function getCategoryIds(containerId){

	var elements = $("#" + containerId).children()
	var ids = new Array();
	for(var i=0; i<elements.length; i++){
		ids[i] = getCategoryId(elements[i].id);
	}
	
	return ids;
}
		
function getCategoriesBySuperCat(categoryId, callback) {

	$.ajax({
		type : "GET",
		url : 'http://localhost:8080/JobSearch/category/' + categoryId
				+ '/subCategories',
		dataType : "json",
		success : _success,
		error : _error
	});

	function _success(response) {
		// alert("success getCategoriesBySuperCat for " + elementId );
		// alert(JSON.stringify(response))
		callback(response, categoryId);
	}

	function _error() {
		alert("error getCategoriesBySuperCat " + categoryId + " 999");
	}
}

