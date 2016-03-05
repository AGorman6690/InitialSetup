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

	r[++j] = "<table id=activeJobsTable>" +
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

	r[++j] = "<table id='activeJobsTable'>" +
			"<tr>" +
				"<th>Job</th>" +
				"<th>Category</th>" +
				"<th>Total Applicants</th>" +
				"<th>New Applicants</th>" +
				"<th></th>"
			"</tr>";
	
	for (var i in arr){
		
		//Master row
		//List job
		//*********************************
		var job = arr[i];
		r[++j] = '<tr class="job" id="' + job.JobId + '">';
		r[++j] = 	'<td>';
		r[++j] = 		job.jobName;
		r[++j] =	'</td>';
		r[++j] = 	'<td>';
		r[++j] = 		job.category.name;
		r[++j] = 	'</td>';
		r[++j] = 	'<td>';
		r[++j] = 		job.applications.length;	
		r[++j] = 	'</td>';
		r[++j] = 	'<td>';
		r[++j] = 	'</td>';	
		r[++j] = 	'<td><div class="arrow"></div>';
		r[++j] = 	'</td>';	
		r[++j] = '</tr>';
		//*********************************
	
	
		//Detail row
		//List job's applicants
		//*********************************		
		//If the job has at least one application
		if(job.applications.length > 0 ){
			r[++j] = '<tr class="applicants">';
			r[++j] = '<td colspan="5">'	;
			r[++j] = '<table>' +
						"<tr>" +
							"<th>Applicant Name</th>" +
							"<th></th>" +
							"<th></th>" +
							"<th></th>" +
							"<th></th>" +
						"</tr>";
			
			//Iterate over applications.
			//Build a table row for each application.
			for (var k in job.applications){
				var application = job.applications[k];
				r[++j] = '<tr class="applicant" id="' + application.applicant.userId + '">';
				r[++j] = 	'<td>';
				r[++j] = 		application.applicant.firstName;
				r[++j] = 	'</td>';
				r[++j] = '</tr>';
			}
			r[++j] = '</table>'
			r[++j] = '</td>'
			r[++j] = '</tr>'
		}		
	}
	r[++j] = '</table>';
	e.html(r.join(''));
	callback();
	
}

function renderTable3(arr, parentId, callback){
	
	var r = new Array();
	var j = -1;
	
//	r[++j] = "<table id='" + parentId + "'>" +
//			"<tr>" +
//				"<th> </th>" +
//				"<th> </th>" +
//				"<th> </th>" +
//				"<th></th>"
//			"</tr>";
//	
	
	
	//Parse the parent's level from its id
	var parentLevel = getLevel(parentId);
	
	//The new level is 1 level deeper than its parent level
	var newLevel = parseInt(parentLevel) + 1;
	
	for (var i in arr){
		
		//Master row
		//List job
		//*********************************
		var category = arr[i];
//		alert(category.id)
//		alert(category.name)
		r[++j] = '<div id="' + category.id + '-' + newLevel + '--' + '">';
		r[++j] = 	'<tr class="category-1" id="' + category.id + '-' + newLevel + '--' + '">';
		r[++j] = 	'<td>';
		r[++j] = 		category.name;
		r[++j] =	'</td>';
		r[++j] = 	'<td>';
		r[++j] = 		2;
		r[++j] = 	'</td>';
		r[++j] = 	'<td>';
		r[++j] = 		6;	
		r[++j] = 	'</td>';	
		r[++j] = 	'<td><div class="arrow"></div>';
		r[++j] = 	'</td>';	
		r[++j] = '</tr>';
		
		//*********************************
		
		r[++j] = '<div class="subs">';
		r[++j] = '</div>';
		r[++j] = '</div>';
//		//Detail row
//		//List job's applicants
//		//*********************************		
//		//If the job has at least one application
//		alert('length' + category.subCategories.length)
//		if(category.subCategories.length > 0 ){
//			r[++j] = '<tr class="category-">';
//			r[++j] = '<td colspan="4">'	;
//			r[++j] = '<table>' +
//						"<tr>" +
//							"<th>Category</th>" +
//							"<th></th>" +
//							"<th></th>" +
//							"<th></th>" +
//							"<th></th>" +
//						"</tr>";
//			
//			//Iterate over applications.
//			//Build a table row for each application.
//			for (var k in category.subCategories){
//				var subCat = category.subCategories[k];
//				r[++j] = '<tr class="applicant" id="' + subCat.id + '">';
//				r[++j] = 	'<td>';
//				r[++j] = 		subCat.name;
//				r[++j] = 	'</td>';
//				r[++j] = '</tr>';
//			}
			
//			r[++j] = '</td>';
//			r[++j] = '</tr>';
				

//		}		
	}
	

//	r[++j] = '</table>';
	alert(r)
	$('#' + parentId).html(r.join(''));
	callback();
	
}