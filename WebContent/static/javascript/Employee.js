function getEmployees() {

	var e = document.getElementById("categories");
	var categoryId = e.options[e.selectedIndex].value;

	$.ajax({
		type : "GET",
		url : 'http://localhost:8080/JobSearch/getSelectedCategory?categoryId='
				+ categoryId,
		contentType : "application/json", // Request
		dataType : "json", // Response
		success : _success,
		error : _error
	});

	function _success(response) {
		// alert("success");
		populateUsers(response.selectedCategory.users, document
				.getElementById("employees"));
	}

	function _error(response) {
		alert("error getEmployees");
	}
}