function complete() {
	// ****************************************************
	// This is not loading the profile page as desired
	// ****************************************************
	$.ajax({
		type : "GET",
		url : 'http://localhost:8080/JobSearch/getProfile',
	// contentType: "application/json", // Request
	// dataType: "json", // Response
	});
}

function addCategory() {
	// alert("add category");
	var e = document.getElementById("allCategories");
	var id = e.value;
	// alert(id);

	$.ajax({
		type : "GET",
		url : 'http://localhost:8080/JobSearch/addCategory?categoryId=' + id,
		contentType : "application/json", // Request
		dataType : "json", // Response
		success : _success,
		error : _error,
	});

	function _success(response) {
		populateCategories(response.categories, document
				.getElementById("selectedCats"));
	}

	function _error() {
		alert("error");
	}
}

function deleteCategory() {
	var e = document.getElementById("selectedCats");
	var catId = e.value;

	$.ajax({
		type : "GET",
		url : 'http://localhost:8080/JobSearch/deleteCategory?categoryId='
				+ catId,
		contentType : "application/json", // Request
		dataType : "json", // Response
		success : _success,
		error : _error
	});

	function _success(response) {
		// alert("success");
		populateCategories(response.categories, document
				.getElementById("selectedCats"));
	}

	function _error(response, errorThrown) {
		alert("error");
	}
}