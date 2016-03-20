$(document).ready(function(){
	
	$("#offeredJobs").change(function(){
		var jobId = $(this).val();
		var userId = $('#userId').val();

		markApplicationViewed(jobId, userId);
	});
	
	$("#acceptJobOffer").click(function(){
		var jobId = $("#offeredJobs").val();
		var userId = $("#userId").val();
		
		markApplicationAccepted(jobId, userId, function(response){});
	})

})

function getApplicationsByEmployer(userId, callback){
	$.ajax({
		type: "GET",
		url: 'http://localhost:8080/JobSearch/getApplicationsByEmployer?userId=' + userId,
        dataType: 'json',
		success: _success,
        error: _error
    });
		
	function _success(response){
	//	alert("success getApplicationsByEmployer");			
		callback(response);
	}
	
	function _error(response){
		alert("error getApplicationsByEmployer");
	}
}


function markApplicationAccepted(jobId, userId){ //, callback){

	$.ajax({
		type: "GET",
		url: 'http://localhost:8080/JobSearch/markApplicationAccepted?jobId=' + jobId + '&userId=' + userId,
        dataType: "json",
		success: _success,
        error: _error
    });

	function _success(response){			
		alert("success markApplicationAccepted");
		callback(response);
	}

	function _error(response){
		alert("error markApplicationAccepted");
	}
}

function markApplicationViewed(jobId, userId){ //, callback){

	$.ajax({
		type: "GET",
		url: 'http://localhost:8080/JobSearch/markApplicationViewed?jobId=' + jobId + '&userId=' + userId,
        dataType: "json",
		success: _success,
        error: _error
    });

	function _success(response){			
		//alert("success markJobUnderConsideration");
		//callback(response);
	}

	function _error(response){
		//alert("error markJobUnderConsideration");
	}
}

function markApplicationViewed(jobId, userId){ //, callback){

	$.ajax({
		type: "GET",
		url: 'http://localhost:8080/JobSearch/markApplicationViewed?jobId=' + jobId + '&userId=' + userId,
        dataType: "json",
		success: _success,
        error: _error
    });

	function _success(response){			
		//alert("success markJobUnderConsideration");
		//callback(response);
	}

	function _error(response){
		//alert("error markJobUnderConsideration");
	}
}



