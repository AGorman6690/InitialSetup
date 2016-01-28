$(document).ready(function(){
	
	$("#addCatToJob").click(function(){
	//	alert("jquery addCatToJob111");
//		var jobId = document.getElementById("selectedJob").name;
//		var catId = document.getElementById("catToAdd").name;
		var jobId = $("#selectedJob").attr("name");
		var catId = $("#profileCats").val();
		
		//alert(jobId);
		//alert(catId);

		addCategoryToJob(catId, jobId, function(response){
			populateCategories(response, document.getElementById("selectedJobCats"));
		});
	});
	
	$("profileCats").click(function(){
		
	})

})


function populateCategories(arr, e){
//	alert("sweet populateCategories");
	e.options.length=0;
	var i;
	for(i = 0; i < arr.length ; i++){
		var opt = document.createElement("option");					
		opt.value = arr[i].id;
		opt.innerHTML = arr[i].name;
		e.appendChild(opt);
	}		
}

function addCategoryToUser(categoryId, userId, callback) {
	// alert("add category");


	$.ajax({
		type : "GET",
		url : 'http://localhost:8080/JobSearch/addCategoryToUser?categoryId=' + categoryId + '&userId=' + userId,
		contentType : "application/json", // Request
		dataType : "json", // Response
		success : _success,
		error : _error,
	});

	function _success(response) {
		
		callback(response);
		//populateCategories(response.categories, document.getElementById("selectedCats"));
	}

	function _error() {
		alert("error addCategoryToUser");
	}
}

function addCategoryToJob(categoryId, jobId, callback){
	
	alert("addCategoryToJob");
	$.ajax({	 	
		type: "GET",                         
        url: 'http://localhost:8080/JobSearch/addCategoryToJob?jobId=' + jobId + '&categoryId=' + categoryId,
        dataType: "json", // Response
        success: _success,
        error: _error,
    });	
	
	function _success(response){
		alert("success add cat to job");
		callback(response);
	}
	
	function _error(response){
		alert("error addCatToJob");
	}
}

function deleteCategoryFromUser(categoryId, userId, callback) {

	$.ajax({
		type : "GET",
		url : 'http://localhost:8080/JobSearch/deleteCategoryFromUser?categoryId=' + categoryId + '&userId=' + userId,
		contentType : "application/json", // Request
		dataType : "json", // Response
		success : _success,
		error : _error
	});

	function _success(response) {
		//alert("success deleteCategoryFromUser");
		callback(response);

	}

	function _error(response, errorThrown) {
		alert("error deleteCategoryFromUser");
	}
}

function getAppCategories(callback){
	//alert("getAppCategories");
	$.ajax({
		type: "GET",
		url: 'http://localhost:8080/JobSearch/getAppCategories',
			dataType: "json",
	        success: _success,
	        error: _error
	    });

		function _success(response){					
			//alert("success get categories");

			//populateCategories(response.categories, document.getElementById(elementId))
			callback(response);	
		}
		
		function _error(){
			alert("error getAppCategories");
		}		
	
}

function getCategoriesByJob(jobId, callback){
	//alert(jobId);
	$.ajax({
		type: "GET",
		url: 'http://localhost:8080/JobSearch/getCategoryByJob?jobId=' + jobId,
			dataType: "json",
	        success: _success,
	        error: _error
	    });

		function _success(response){					
		//	alert("success getCategoriesByJob");
			callback(response);
		}
		
		function _error(){
			alert("error getCategoriesByJob");
		}
}

function getCategoriesByUser(userId, callback){
	//alert(elementId);
	$.ajax({
		type: "GET",
		url: 'http://localhost:8080/JobSearch/getCategoriesByUser?userId=' + userId,
		dataType: "json",
        success: _success,
        error: _error
	    });

		function _success(response){					
		//	alert("success getCategoriesByUser");
			callback(response);
		}
		
		function _error(){
			alert("error");
		}
}



