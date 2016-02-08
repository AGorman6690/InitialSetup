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

function renderTable(arr, e, callback){
	
	var r = new Array();
	var j = -1;

	r[++j] = "<table id='report'>" +
			"<tr>" +
			"<th>Job</th>" +
			"<th>Applicant Name</th>" +
			"<th>Status</th>" +
			"</tr>";
	
	for (var i in arr){
		
		//Master row
		//*********************************
		var d = arr[i];
		r[++j] = '<tr><td>'
		r[++j] = d.jobName;
		r[++j] = '</td>';
		r[++j] = '<td>'
		r[++j] = d.firstName;
		r[++j] = '</td>';
		
		r[++j] = '<td>'
		if(d.isAccepted == 1){
			r[++j] = 'Accepted';
		}else if(d.beenViewed == 1){
			r[++j] = 'Viewed';
		}else {
			r[++j] = 'Offered';
		}		
		r[++j] = '</td></tr>';
		//*********************************
		
		
		//Detail row
		//*********************************
		r[++j] = '<tr>';
		r[++j] = '<td colspan="3">';
		r[++j] = '<ul>';
		r[++j] = '<li>1</li>';
		r[++j] = '<li>2</li>';
		r[++j] = '</ul>';
		r[++j] = '</td>';
		r[++j] = '</tr>';		
	}
	r[++j] = '</table>';
	e.html(r.join(''));
	callback();	
}

function renderTable2(arr, e, callback){
	
	var r = new Array();
	var j = -1;

	r[++j] = "<table id='report'>" +
			"<tr>" +
			"<th>Job</th>" +
			"<th>Category</th>" +
			"<th>Total Applicants</th>" +
			"<th>New Applicants</th>" +
			"</tr>";
	
	for (var i in arr){
		
		//Master row
		//*********************************
		var d = arr[i];
		r[++j] = '<tr id=' + d.JobId + '>'
		r[++j] = '<td>'
		r[++j] = d.jobName;
		r[++j] = '</td>';
		r[++j] = '<td>'
		r[++j] = d.category.name;
		r[++j] = '</td>';
		r[++j] = '<td>'
		r[++j] = d.applications.length;	
		r[++j] = '</td>'	
		r[++j] = '</tr>';
		//*********************************
		
		
//		//Detail row
//		//*********************************
//		r[++j] = '<tr>';
//		r[++j] = '<td colspan="3">';
//		r[++j] = '<ul>';
//		r[++j] = '<li>1</li>';
//		r[++j] = '<li>2</li>';
//		r[++j] = '</ul>';
//		r[++j] = '</td>';
//		r[++j] = '</tr>';
		
		
		
		
		
		
	}
	r[++j] = '</table>';
	e.html(r.join(''));
	callback();
	
}
