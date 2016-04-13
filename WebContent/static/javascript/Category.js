


function getCategoryIds(containerId){

	var elements = $("#" + containerId).children()
	var ids = new Array();
	for(var i=0; i<elements.length; i++){
		ids[i] = getCategoryId(elements[i].id);
	}
	
	return ids;
}
		
//function addCategoryToUser(categoryId, userId, callback) {
//	// alert("add category");
//
//	$.ajax({
//		type : "GET",
//		url : 'http://localhost:8080/JobSearch/addCategoryToUser?categoryId='
//				+ categoryId + '&userId=' + userId,
//		contentType : "application/json", // Request
//		dataType : "json", // Response
//		success : _success,
//		error : _error,
//	});
//
//	function _success(response) {
//
//		callback(response);
//		// populateCategories(response.categories,
//		// document.getElementById("selectedCats"));
//	}
//
//	function _error() {
//		alert("error addCategoryToUser");
//	}
//}

//function addCategoriesToUser(categoryIds, userId, callback) {
//
//	if (categoryIds.length > 0) {
//
//		// Build a parameter string for the category ids
//		var catString;
//		catString = 'category=' + categoryIds[0];
//		for (var i = 1; i < categoryIds.length; i++) {
//			catString += '&category=' + categoryIds[i];
//		}
//
//		// alert(catString)
//		$.ajax({
//			type : "GET",
//			url : 'http://localhost:8080/JobSearch/addCategoriesToUser?'
//					+ catString + '&userId=' + userId,
//			contentType : "application/json", // Request
//			dataType : "json", // Response
//			success : _success,
//			error : _error,
//		});
//
//		function _success(response) {
//			// alert("success addCategoriesToUser");
//			callback(response);
//			// populateCategories(response.categories,
//			// document.getElementById("selectedCats"));
//		}
//
//		function _error() {
//			alert("error addCategoriesToUser");
//		}
//
//	}
//}

//function removeCategoriesFromUser(categoryIds, userId, callback) {
//	var headers = {};
//	headers[$("meta[name='_csrf_header']").attr("content")] = $(
//			"meta[name='_csrf']").attr("content");
//	if (categoryIds.length > 0) {
//
//		// Build a parameter string for the category ids
//		var catString;
//		catString = 'category=' + categoryIds[0];
//		for (var i = 1; i < categoryIds.length; i++) {
//			catString += '&category=' + categoryIds[i];
//		}
//
//		$.ajax({
//			type : "PUT",
//			url : 'http://localhost:8080/JobSearch/user/' + userId
//					+ '/removeCategories?' + catString,
//			headers : headers,
//			contentType : "application/json", // Request
//			dataType : "json", // Response
//			success : _success,
//			error : _error
//		});
//
//		function _success(response) {
//			// alert("success removeCategoriesFromUser");
//			callback(response);
//			// populateCategories(response.categories,
//			// document.getElementById("selectedCats"));
//		}
//
//		function _error() {
//			alert("error removeCategoriesFromUser");
//		}
//
//	}
//}

//function addCategoryToJob(categoryId, jobId, callback) {
//
//	// alert("addCategoryToJob");
//	$.ajax({
//		type : "GET",
//		url : 'http://localhost:8080/JobSearch/addCategoryToJob?jobId=' + jobId
//				+ '&categoryId=' + categoryId,
//		dataType : "json", // Response
//		success : _success,
//		error : _error,
//	});
//
//	function _success(response) {
//		// alert("success add cat to job");
//		callback(response);
//	}
//
//	function _error(response) {
//		alert("error addCatToJob");
//	}
//}

function getCategoriesByJob(jobId, callback) {
	// alert(jobId);
	$
			.ajax({
				type : "GET",
				url : 'http://localhost:8080/JobSearch/getCategoryByJob?jobId='
						+ jobId,
				dataType : "json",
				success : _success,
				error : _error
			});

	function _success(response) {
		// alert("success getCategoriesByJob");
		callback(response);
	}

	function _error() {
		alert("error getCategoriesByJob");
	}
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

function getCategoriesByUser(userId, callback) {

	$.ajax({
		type : "GET",
		url : 'http://localhost:8080/JobSearch/user/' + userId + '/categories',
		dataType : "json",
		success : _success,
		error : _error
	});

	function _success(response) {
		// alert("success getCategoriesByUser");
		callback(response);
	}

	function _error() {
		alert("error getCategoriesByUser");
	}
}
