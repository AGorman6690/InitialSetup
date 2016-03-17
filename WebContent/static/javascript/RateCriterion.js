/**
 * 
 */

function populateRateCriterion(arr, e){
	//alert("sweet populateCategories");
	e.options.length=0;
	var i;
	for(i = 0; i < arr.length ; i++){
		var opt = document.createElement("option");					
		opt.value = arr[i].rateCriterionId;
		opt.innerHTML = arr[i].name;
		e.appendChild(opt);
	}		
}


function getAppRateCriteria(callback){
	//alert("get getAppRateCriteria");
	$.ajax({
		type: "GET",
		url: 'http://ec2-54-84-39-231.compute-1.amazonaws.com:8080/JobSearch/getAppRateCriteria',
			dataType: "json",
	        success: _success,
	        error: _error
	    });

		function _success(response){					
			//alert("success getAppRateCriteria");
			callback(response);
		}
		
		function _error(){
			alert("error getAppRateCriteria");
		}			
}