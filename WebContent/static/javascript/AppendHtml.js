

function appendFirstLevelCategories_FindJobs(eId, arr, callback){
	//PARAMETERS:
	//1) eId is the category id. It will be used to identify the div element in which
	//	to append the html to. The div element id equals the category id with a 'T' appended to the end.
	//2) arr is a list of category objects (i.e. sub categories to append)

	
	//If there are sub categories
	if (arr.length > 0){
		var j = -1;
		var r = new Array();
		r[++j] = '<ul class="list-group">';
		
		//For each sub category, create html
		for(var i=0; i<arr.length; i++){
			
			var id =  arr[i].id;
			
			//Li element. Its id is equal to the category id
			r[++j] = '<li id="' + id + '" class="list-group-item">';
			
			//Category name
			r[++j] = 	'<a href="#" id="' + id + 'Name" >';
			r[++j] = 	arr[i].name + "  ";
			r[++j] = 	'</a>';
			
			//Number of active jobs in the category
			r[++j] = 	'<a class="find-jobs-job-count btn btn-info btn-sm" onclick="appendJobs_Available(\'' + id + '\')">';
			r[++j] =	arr[i].jobCount + '</a>';
			
//			alert(arr[i].jobCount)
			//When this hyperlink is clicked, the below div is expanded.
			//The inner html is the job count of ALL its sub categories, not just
			//sub categories 1 level deep.
			//Also on click, the category
			r[++j] = 	'<a  onclick="appendSecondLevelCategories_FindJobs(\'' + id + '\')" id="#' + id + 'C"';
			r[++j] =  	'data-target="#' + id + 'T" data-toggle="collapse" class="find-jobs-sub-job-count btn btn-success btn-sm">';
			r[++j] = 		'<span class="glyphicon glyphicon-menu-down padding-hori">' + "  " + arr[i].subJobCount + '</span>';
			r[++j] =	'</a>';
			
			//This div will eventually hold the category's sub categories.
			//When this category's sub categories need to be set,
			//this div id will be passed to this function and have this
			//html appended to it (see append method at end of this function).
			r[++j] = 	'<div id="' + id + 'T" class="panel-collapse collapse">';
			r[++j] = 	'</div>';
			
			r[++j] = '</li>';	
		}

		r[++j] = '</ul>'
		//alert('about to append to ' + eId)
		//This is the id of div that will hold the sub categories 
		$("#" + eId + "T").append(r.join(''));
				
		callback();		
		
	}
}

function appendFirstLevelCategories_FilterCategories(eId, arr, callback){
	//PARAMETERS:
	//1) eId is the category id. It will be used to identify the div element in which
	//	to append the html to. The div element id equals the category id with a 'T' appended to the end.
	//2) arr is a list of category objects (i.e. sub categories to append)

	
	//If there are sub categories
	if (arr.length > 0){
		var j = -1;
		var r = new Array();
		r[++j] = '<ul class="list-group">';
		
		//For each sub category, create html
		for(var i=0; i<arr.length; i++){
			
			var id =  arr[i].id;
			
			//Li element. Its id is equal to the category id
			r[++j] = '<li id="' + id + '" class="list-group-item">';
			
			//Category name
			r[++j] = 	'<a href="#" id="' + id + 'Name" >';
			r[++j] = 	arr[i].name + "  ";
			r[++j] = 	'</a>';
			
			//Checkbox to select the category
			r[++j] = '<input id="' + id + '-Click" type="checkbox" class="margin-hori"';
			r[++j] = 'onClick="selectCategory2(\'' + id + '\')">';
			
//			alert(arr[i].jobCount)
			//When this hyperlink is clicked, the below div is expanded.
			//The inner html is the job count of ALL its sub categories, not just
			//sub categories 1 level deep.
			//Also on click, the category
			r[++j] = 	'<a  onclick="appendSecondLevelCategories_FindJobs(\'' + id + '\')" id="#' + id + 'C"';
			r[++j] =  	'data-target="#' + id + 'F" data-toggle="collapse" class="find-jobs-sub-job-count btn btn-success btn-sm">';
			r[++j] = 		'<span class="glyphicon glyphicon-menu-down padding-hori"></span>';
			r[++j] =	'</a>';
			
			//This div will eventually hold the category's sub categories.
			//When this category's sub categories need to be set,
			//this div id will be passed to this function and have this
			//html appended to it (see append method at end of this function).
			r[++j] = 	'<div id="' + id + 'F" class="panel-collapse collapse">';
			r[++j] = 	'</div>';
			
			r[++j] = '</li>';	
		}

		r[++j] = '</ul>'
		//alert('about to append to ' + eId)
		//This is the id of div that will hold the sub categories 
		$("#" + eId + "F").append(r.join(''));
				
		callback();		
		
	}
}


function appendSecondLevelCategories_FindJobs(elementId){
	
	var e = $('#' + elementId);
	
	//When the 'second' data attribute is set to true,
	//that signifies the category's second level has been set.
	//So when the category is collapsed and then re-expanded, the
	//below code will not execute again. It only needs to execute once.
	if(e.data('second') != 'true'){
		e.data('second', 'true');	
		
		//Get the categories 1 level deep.
		//The li element holds the category Id without anything appended to the end.
		getCategoriesBySuperCat(e.closest('li').attr('id'), function(response, elementId){		
			
			//For each sub category
			for(var i=0; i < response.length; i++){
				
				//Get the categories 1 level deep (i.e. 2 levels deep relative 
				//to the original category. 
				getCategoriesBySuperCat(response[i].id, function(response, elementId){
					
					appendFirstLevelCategories_FindJobs(elementId, response, function(){						
					
					})						
				})			
			}	
		})
	}
}




function appendFirstLevelCategories_PostJob(eId, arr, callback){
	//PARAMETERS:
	//1) eId is the category id. It will be used to identify the div element in which
	//	to append the html to. The div element id equals the category id with a 'T' appended to the end.
	//2) arr is a list of category objects (i.e. sub categories to append)
	
	
	//If there are sub categories
	if (arr.length > 0){
		var j = -1;
		var r = new Array();
		r[++j] = '<ul class="list-group">';
		
		//For each sub category, create html
		for(var i=0; i<arr.length; i++){
			
			var id =  arr[i].id;
			
			//Li element. Its id is equal to the category id
			r[++j] = '<li id="' + id + '" class="category-list-item list-group-item">';
			
			//Category name
			r[++j] = 	'<a href="#" id="' + id + 'Name" >';
			r[++j] = 	arr[i].name + "  ";
			r[++j] = 	'</a>';
			
			//Checkbox to select the category
			r[++j] = '<input id="' + id + 'Click" type="checkbox" class="margin-hori"';
			r[++j] = 'onClick="selectCategory(\'' + id + '\')">';
			
			//When this hyperlink is clicked, the below div is expanded.
			//The inner html is the job count of ALL its sub categories, not just
			//sub categories 1 level deep.
			//Also on click, the categor
			r[++j] = 	'<a onclick="appendSecondLevelCategories_PostJob(\'' + id + '\')" id="#' + id + 'C"';
			r[++j] =    'data-target="#' + id + 'T" data-toggle="collapse" class="category-list-down-arrow btn btn-success btn-sm">';
			r[++j] = 		'<span class="glyphicon glyphicon-menu-down"></span>';
			r[++j] =	'</a>';
			
			//This div will eventually hold the category's sub categories.
			//When this category's sub categories need to be set,
			//this div id will be passed to this function and have this
			//html appended to it (see append method at end of this function).
			r[++j] = 	'<div id="' + id + 'T" class="panel-collapse collapse">';
			r[++j] = 	'</div>';
			
			r[++j] = '</li>';	
		}

		r[++j] = '</ul>'
		//alert('about to append to ' + eId)
		//This is the id of div that will hold the sub categories 
		$("#" + eId + "T").append(r.join(''));
				
		callback();		
		
	}
}


function appendSecondLevelCategories_PostJob(elementId){
	
	var e = $('#' + elementId);
	
	//When the 'second' data attribute is set to true,
	//that signifies the category's second level has been set.
	//So when the category is collapsed and then re-expanded, the
	//below code will not execute again. It only needs to execute once.
	if(e.data('second') != 'true'){
		e.data('second', 'true');	
		
		//Get the categories 1 level deep.
		//The li element holds the category Id without anything appended to the end.
		getCategoriesBySuperCat(e.closest('li').attr('id'), function(response, elementId){		
			
			//For each sub category
			for(var i=0; i < response.length; i++){
				
				//Get the categories 1 level deep (i.e. 2 levels deep relative 
				//to the original category. 
				getCategoriesBySuperCat(response[i].id, function(response, elementId){
					
					appendFirstLevelCategories_PostJob(elementId, response, function(){						
					
					})						
				})			
			}	
		})
	}
}



function appendFirstLevelCategories_ProfileCats(eId, arr, callback){
	//PARAMETERS:
	//1) eId is the category id. It will be used to identify the div element in which
	//	to append the html to. The div element id equals the category id with a 'T' appended to the end.
	//2) arr is a list of category objects (i.e. sub categories to append)
	
	
	//If there are sub categories
	if (arr.length > 0){
		var j = -1;
		var r = new Array();
		r[++j] = '<ul class="list-group">';
		
		//For each sub category, create html
		for(var i=0; i<arr.length; i++){
			
			var id =  arr[i].id;
			
			//Li element. Its id is equal to the category id
			r[++j] = '<li id="' + id + '" class="list-group-item margin-hori">';
			
			//Category name
			r[++j] = 	'<a href="#" id="' + id + 'Name" >';
			r[++j] = 	arr[i].name + "  ";
			r[++j] = 	'</a>';
			
			//Checkbox to select the category
			r[++j] = '<input id="' + id + 'Click" type="checkbox" class="margin-hori"';
			r[++j] = 'onClick="selectCategory2(\'' + id + '\')">';
			
			//When this hyperlink is clicked, the below div is expanded.
			//The inner html is the job count of ALL its sub categories, not just
			//sub categories 1 level deep.
			//Also on click, the categor
			r[++j] = 	'<a onclick="appendSecondLevelCategories_ProfileCats(\'' + id + '\')" id="#' + id + 'C"';
			r[++j] =    'data-target="#' + id + 'T" data-toggle="collapse" class="btn margin-hori btn-success btn-sm">';
			r[++j] = 		'<span class="glyphicon glyphicon-menu-down"></span>';
			r[++j] =	'</a>';
			
			//This div will eventually hold the category's sub categories.
			//When this category's sub categories need to be set,
			//this div id will be passed to this function and have this
			//html appended to it (see append method at end of this function).
			r[++j] = 	'<div id="' + id + 'T" class="panel-collapse collapse">';
			r[++j] = 	'</div>';
			
			r[++j] = '</li>';	
		}

		r[++j] = '</ul>'
		//alert('about to append to ' + eId)
		//This is the id of div that will hold the sub categories 
		$("#" + eId + "T").append(r.join(''));
				
		callback(eId, arr);		
		
	}
}

function setCategoriesCheckbox(usersCats){
	
	for(var i=0; i<usersCats.length; i++){
		//alert("#" + usersCats[i].id + 'Check')
		$("#" + usersCats[i].id + 'Click').attr('checked', true);
	}
	
	
}


function appendSecondLevelCategories_ProfileCats(elementId){
	
	var e = $('#' + elementId);
	
	//When the 'second' data attribute is set to true,
	//that signifies the category's second level has been set.
	//So when the category is collapsed and then re-expanded, the
	//below code will not execute again. It only needs to execute once.
	if(e.data('second') != 'true'){
		e.data('second', 'true');	
		
		//Get the categories 1 level deep.
		//The li element holds the category Id without anything appended to the end.
		getCategoriesBySuperCat(e.closest('li').attr('id'), function(response, elementId){		
			
			//For each sub category
			for(var i=0; i < response.length; i++){
				
				//Get the categories 1 level deep (i.e. 2 levels deep relative 
				//to the original category. 
				getCategoriesBySuperCat(response[i].id, function(response, elementId){
					
					appendFirstLevelCategories_ProfileCats(elementId, response, function(){						
					
					})						
				})			
			}	
		})
	}
}



function setEmployeeId(e){
	$("#employeeId").val(e.target.id);
}

function applyToJob(jobId){

	applyForJob(jobId, $("#userId").val(), function(response){
		alert("Application Received!")
	})
}

function selectCategory(categoryId){

	$("#selectedCategory").val(categoryId);	
	uncheckCheckboxes("0T", categoryId + "Click");
	
}

function selectCategory2(categoryId){

	
//	//If checked, then
	if($('#' + categoryId + "Click").is(':checked')){
		
		//append div element to ADD
		var e = $("<div id='" + categoryId + '-Add' + "'></div>");
		$("#addCategories").append(e);
		
		//remove div element from REMOVE
		$('#' + categoryId + "-Remove").remove();

	//Else uncheck,
	}else{		
		
		//remove div element from ADD
		$('#' + categoryId + "-Add").remove();
		
		//append div element to REMOVE
		var e = $("<div id='" + categoryId + '-Add' + "'></div>");
		$("#removeCategories").append(e);

	}	
}

function uncheckCheckboxes(parentId, excludeId){

	var arr = $("#" + parentId).find("input[type=checkbox]");	
	for(var i = 0 ; i < arr.length; i++){
		
		if(arr[i].id != excludeId ){
			$("#" + arr[i].id).attr('checked', false);
		}
	}
	
}

function getCheckedCheckboxesId(parentId){

	var checkedIds = new Array();
	var arr = $("#" + parentId).find("input[type=checkbox]");	
	
	var j = -1;
	
	for(var i = 0 ; i < arr.length; i++){
		
		if($(arr[i]).is(':checked')){
			checkedIds[++j] = getCategoryId(arr[i].id)
		}
	}
	
	return checkedIds;
}

function getCategoryId(id){
//PURPOSE: This will take an Id an parse it to find the id.
//The div tag's id syntax is {categoryId}-{level}--.	
//For example, if id = "12345-56--", this will return 12345. 

	var idEnd = id.indexOf("-");
	return id.substring(0, idEnd);
}


//function appendSubCategories(categoryId){
//	
//	//For each seed (the "T" is simply the div element in which the sub categories
//	//will be appended to),
//	var arr = $('#' + categoryId + 'T').find('li');
//	for(var i = 0; i < arr.length; i++){
//		
//		//get sub categories.
//		getCategoriesBySuperCat(arr[i].id, function(response, elementId){
//
//			//Append sub categories
//			appendFirstLevelCategories_ProfileCats(elementId, response, function(){		
//				alert("firsst")
//			})
//		})
//	}
//}

function appendJobs(eId, jobs, callback){

	//If there are sub categories
	if (jobs.length > 0){
		var j = -1;
		var r = new Array();
		r[++j] = '<ul class="list-group">';
		
		//For each sub category, create html
		for(var i=0; i<jobs.length; i++){
			
			var id =  jobs[i].id;
			
			//Li element. Its id is equal to the category id
			r[++j] = '<li id="' + id + '" class="list-group-item margin-hori">';
			
			//Job name
			r[++j] = 	'<a href="#" id="' + id + '-Name" >';
			r[++j] = 	jobs[i].jobName + "  ";
			r[++j] = 	'</a>';
			
			r[++j] = '</li>';	
		}
		
		r[++j] = '</ul>'	
		$("#" + eId).append(r.join(''));
				
		callback(eId, jobs);		
		
	}
	
}

//function appendUsers(eId, users, callback){
//		//If there are sub categories
//	if (users.length > 0){
//		var j = -1;
//		var r = new Array();
//		r[++j] = '<ul class="list-group">';
//
//		//For each sub category, create html
//		for(var i=0; i<users.length; i++){
//			
//			var id =  users[i].userId;			
//
//			//Li element. Its id is equal to the category id
//			r[++j] = '<li id="' + id + '" class="list-group-item margin-hori">';
//			
//			//Job name
//			r[++j] = 	'<a href="#" id="' + id + '-Name" >';
//			r[++j] = 	users[i].firstName + "  " + users[i].lastName;
//			r[++j] = 	'</a>';
//
//			
//			r[++j] = '</li>';	
//		}
//
//
//		r[++j] = '</ul>'	
//		$("#" + eId).append(r.join(''));				
//		callback(eId, users);		
//		
//	}
//	
//}

function appendUsers_RateEmployees(eId, users, callback){

	if (users.length > 0){
		var j = -1;
		var r = new Array();
		
		var containerId = 'rateEmployees';
		r[++j] = '<div class="list-group" id="' + containerId + '">';
	
		for(var i=0; i<users.length; i++){
			
			var id =  users[i].userId;			
	
			r[++j] = '<a href="#" id="' + id + '" class="list-group-item margin-hori"';
			r[++j] = ' onclick="activateItem(\'' + id + '\',\'' + containerId + '\',\'active\')">';
			r[++j] = 	users[i].firstName;
			r[++j] = '</a>';	
		}
		
		r[++j] = '</div>'	
		$("#" + eId).append(r.join(''));				
		
		$("#" + containerId + " a").click(setEmployeeId);		
		callback();		
		
	}
}



function activateItem(itemId, containerId, className){
	var items = $("#" + containerId).find("a")

	for(var i = 0; i<items.length; i++){				
		if($('#' + items[i].id).hasClass(className) == 1){
			$('#' + items[i].id).removeClass(className);
		}			
	}
	
	$('#' + itemId).addClass(className);
}

function activateButton(buttonId, containerId, className){
	
	var items = $("#" + containerId).find("button")

	for(var i = 0; i<items.length; i++){	
		if($('#' + items[i].id).hasClass(className) == 1){
			$('#' + items[i].id).removeClass(className);
			$('#' + items[i].id).addClass('btn-default');
		}			
	}
	
	$('#' + buttonId).removeClass('btn-default');
	$('#' + buttonId).addClass(className);
}


function hire(userId, jobId){
	
	hireApplicant(userId, jobId, function(activeJob){
		appendUsers_ApplicantsAndEmployeesForActiveJob(activeJob, "applicants", "employees")
	})
		
}

function appendUsers_ApplicantsAndEmployeesForActiveJob(activeJob, applicantsDivId, employeesDivId){
	
	if(unacceptedApplicantsExist(activeJob.applicants)){
		appendApplicants("applicants", activeJob.applicants, jobId, function(){});
	}else{
		$("#" + applicantsDivId).empty();
		$("#" + applicantsDivId).append("<div>No applicants</div>");
	}

	if(activeJob.employees.length > 0){
		appendEmployees("employees", activeJob.employees, function(){});
	}else{
		$("#" + employeesDivId).empty();
		$("#" + employeesDivId).append("<div>No employees</div>");
	}
}

function appendJobs_EmployerActive(eId, jobs, callback){

	//If there are sub categories
	if (jobs.length > 0){
		
		var j = -1;
		var r = new Array();
		r[++j] = '<table class="table table-hover">';
		r[++j] = '<thead>';
		r[++j] =   '<tr>';
		r[++j] =     '<th>Job Name</th>';
		r[++j] =     '<th>New Applicants</th>';
		r[++j] =     '<th>Total Applicants</th>';
		r[++j] =     '<th>Employees</th>';
		r[++j] =   '</tr>';
		r[++j] = '</thead>';
		r[++j] = '<tbody>';
		
		//For each sub category, create html
		for(var i=0; i<jobs.length; i++){
			
			var id =  jobs[i].id;

			r[++j] = '<tr id="' + id + '">';
			r[++j] = 	'<td><a href="./viewActiveJob_Employer?jobId=' + id + '">';
			r[++j] =     jobs[i].jobName + '</a></td>'
			r[++j] = 	'<td>(not built)</td>';
			r[++j] = 	'<td>' + jobs[i].applications.length + '</td>';
			r[++j] = 	'<td>' + jobs[i].employees.length + '</td>';			
			r[++j] = '</tr>';	
		}

		r[++j] = '</tbody>';
		r[++j] = '</table>';
		$("#" + eId).append(r.join(''));
				
		callback(eId, jobs);		
		
	}
	
}

function appendJobs_EmployeeAppliedTo(eId, jobs, callback){

	//If there are sub categories
	if (jobs.length > 0){
		
		var j = -1;
		var r = new Array();
		r[++j] = '<table class="table table-hover">';
		r[++j] = '<thead>';
		r[++j] =   '<tr>';
		r[++j] =     '<th>Job Name</th>';
		r[++j] =   '</tr>';
		r[++j] = '</thead>';
		r[++j] = '<tbody>';
		
		//For each sub category, create html
		for(var i=0; i<jobs.length; i++){			
			var id =  jobs[i].id;
			r[++j] = '<tr id="' + id + '">';
			r[++j] = 	'<td>';
			r[++j] =     jobs[i].jobName + '</td>';		
			r[++j] = '</tr>';	
		}

		r[++j] = '</tbody>';
		r[++j] = '</table>';
		$("#" + eId).append(r.join(''));
				
		callback(eId, jobs);		
		
	}	
}

function appendJobs_Available_OLD(categoryId){
	
	var jobs = getJobsByCategory(categoryId, function(response){

		$('#jobList').empty();
		var j = -1;
		var r = new Array();
		var id;
		for(var i=0; i<response.length; i++){
			id = response[i].id;
			r[++j] = '<li class="list-group-item margin-hori">';
			r[++j] = response[i].jobName;
			r[++j] = 	'<a class="btn btn-info btn-sm margin-hori" onclick="applyToJob(\'' + id + '\')">';
			r[++j] =	'Apply</a>';
			r[++j] = '</li>';
		}
	
		$("#jobList").append(r.join(''));
		
	});
	
}

function appendJobs_Available(categoryId){
	
	var jobs = getJobsByCategory(categoryId, function(jobs){

		$('#jobList').empty();
		//If there are sub categories
		if (jobs.length > 0){
			
			var j = -1;
			var r = new Array();
			r[++j] = '<table class="table table-hover">';
			r[++j] = 	'<thead>';
			r[++j] =   		'<tr>';
			r[++j] =     	'<th>Job Name</th>';
			r[++j] =     	'<th>  </th>';
			r[++j] =   	'</tr>';
			r[++j] = 	'</thead>';
			r[++j] = 	'<tbody>';
			
			for(var i=0; i<jobs.length; i++){
				
				if (jobs[i].isActive == 1){				
					var id =  jobs[i].id;			
					r[++j] = '<tr id="' + id + '">';
					r[++j] = 	'<td width="120px"><a>';
					r[++j] =    	jobs[i].jobName;
					r[++j] =	'</a></td>';
					r[++j] = 	'<td><a onclick="applyToJob(\'' + id + '\')" class="btn btn-info btn-sm margin-hori">';
					r[++j] =	'Apply</a></td>';
					r[++j] = '</tr>';	
				}
			}

			r[++j] = 	'</tbody>';
			r[++j] = '</table>';			
			$("#jobList").append(r.join(''));					
			callback();					
		}
	});	
}



function appendJobs_EmployeeHiredFor(eId, jobs, callback){

	//If there are sub categories
	if (jobs.length > 0){
		
		var j = -1;
		var r = new Array();
		r[++j] = '<table class="table table-hover">';
		r[++j] = '<thead>';
		r[++j] =   '<tr>';
		r[++j] =     '<th>Job Name</th>';
		r[++j] =   '</tr>';
		r[++j] = '</thead>';
		r[++j] = '<tbody>';
		
		//For each sub category, create html
		for(var i=0; i<jobs.length; i++){
			
			var id =  jobs[i].id;

			r[++j] = '<tr id="' + id + '">';
			r[++j] = 	'<td>';
			r[++j] =     jobs[i].jobName + '</td>';		
			r[++j] = '</tr>';	
		}

		r[++j] = '</tbody>';
		r[++j] = '</table>';
		$("#" + eId).append(r.join(''));
				
		callback(eId, jobs);		
		
	}	
}

function appendApplicants(eId, applicants, jobId, callback){
	$("#" + eId).empty();
	//If there are sub categories
	if (applicants.length > 0){
		
		var j = -1;
		var r = new Array();
		r[++j] = '<table class="table table-condensed">';
		r[++j] = '<thead>';
		r[++j] =   '<tr>';

		r[++j] =     '<th>Applicant Name</th>';
//		alert(JSON.stringify(applicants[0].ratings[0]))
		for(var k = 0; k < applicants[0].ratings.length; k++){
			r[++j] =     '<th>' + applicants[0].ratings[k].name + '</th>';
		}

		r[++j] =     '<th> </th>';
		r[++j] =   '</tr>';
		r[++j] = '</thead>';
		r[++j] = '<tbody>';
		
		
		//For each sub category, create html
		for(var i=0; i<applicants.length; i++){
	
			var id =  applicants[i].userId;

			if(applicants[i].application.isAccepted == 0){

				r[++j] = '<tr id="' + id + '">';
				r[++j] = 	'<td><a href="#">';
				r[++j] =     applicants[i].firstName + " " + applicants[i].lastName + '</a></td>';			
				for(k = 0; k < applicants[0].ratings.length; k++){
					r[++j] = 	'<td>' + applicants[i].ratings[k].value + '</td>';	
				}
				r[++j] = 	'<td><a class="hire btn btn-info btn-sm margin-hori"';
				r[++j] =	'onclick="hire(\'' + id + '\',' + '\'' + jobId + '\')">';
//				r[++j] = 	' href="./user/hire?userId=' + id + '&jobId=' + jobId + '">'
				r[++j] =	'Hire</a></td>';			
				r[++j] = '</tr>';	
				

			
			}
		}

		r[++j] = '</tbody>';
		r[++j] = '</table>';
		$("#" + eId).append(r.join(''));
				
		callback();		

		
	}
	
}

function appendEmployees(eId, applicants, callback){
	$("#" + eId).empty();
	//If there are sub categories
	if (applicants.length > 0){
		
		var j = -1;
		var r = new Array();
		r[++j] = '<table class="table table-condensed">';
		r[++j] = '<thead>';
		r[++j] =   '<tr>';

		r[++j] =     '<th>Employee Name</th>';
//		alert(JSON.stringify(applicants[0].ratings[0]))
		for(var k = 0; k < applicants[0].ratings.length; k++){
			r[++j] =     '<th>' + applicants[0].ratings[k].name + '</th>';
		}

		r[++j] =     '<th> </th>';
		r[++j] =   '</tr>';
		r[++j] = '</thead>';
		r[++j] = '<tbody>';
		
		
		//For each sub category, create html
		for(var i=0; i<applicants.length; i++){
	
			var id =  applicants[i].userId;

			r[++j] = '<tr id="' + id + '">';
			r[++j] = 	'<td><a href="#">';
			r[++j] =     applicants[i].firstName + " " + applicants[i].lastName + '</a></td>';			
			for(k = 0; k < applicants[0].ratings.length; k++){
				r[++j] = 	'<td>' + applicants[i].ratings[k].value + '</td>';	
			}
			r[++j] = 	'<td></td>';	
			r[++j] = '</tr>';	
		}

		r[++j] = '</tbody>';
		r[++j] = '</table>';
		$("#" + eId).append(r.join(''));
				
		callback();		
		
	}
	
}

function appendJobs_EmployerComplete(eId, jobs, callback){

	//If there are sub categories
	if (jobs.length > 0){
		
		var j = -1;
		var r = new Array();
		r[++j] = '<table class="table table-hover">';
		r[++j] = '<thead>';
		r[++j] =   '<tr>';
		r[++j] =     '<th>Job Name</th>';
		r[++j] =     '<th>  </th>';
		r[++j] =   '</tr>';
		r[++j] = '</thead>';
		r[++j] = '<tbody>';
		
		//For each sub category, create html
		for(var i=0; i<jobs.length; i++){
			
			var id =  jobs[i].id;			
			r[++j] = '<tr id="' + id + '">';

			//Job name
			//alert(JSON.stringify(jobs[i]))
//			var str = JSON.stringify(jobs[i]);
//			alert(str)
			r[++j] = 	'<td><a href="./viewActiveJob_Employer?jobId=' + id + '">';
			r[++j] =     jobs[i].jobName + '</a></td>'	
			r[++j] = 	'<td><a href="./rateEmployees?jobId=' + id + '" class="btn btn-info btn-sm margin-hori">';
			r[++j] =	'Rate Employees</a></td>';
			r[++j] = '</tr>';	
		}

		r[++j] = '</tbody>';
		r[++j] = '</table>';
		$("#" + eId).append(r.join(''));
				
		callback();		
		
	}
	
}


$(document).ready(function(){

	
	$("#addJob").click(function(){

 		var jobName = $("#jobToAdd").val();
 		var categoryId = $('#selectedCategory').val();
 		var userId = $('#userId').val()
 		addJob(jobName,  userId, categoryId, function(response){
 		});
	});
	
	$("#saveEditProfileCats").click(function(){
		
		//Add categories
		var addCats = new Array();
		addCats = $("#addCategories").find('div');
		
		var ids = new Array();;
		for(var i=0; i<addCats.length; i++){
			ids[i] = getCategoryId(addCats[i].id)
		}

		addCategoriesToUser(ids, $('#userId').val())
		
		//Remove categories
		var removeCats = new Array();
		removeCats = $("#removeCategories").find('div');
		
		var ids = new Array();;
		for(var i=0; i<removeCats.length; i++){
			ids[i] = getCategoryId(removeCats[i].id)
		}

		removeCategoriesFromUser(ids, $('#userId').val())
	})
	
	$(".rate-values").click(function(){	
		activateButton($(this).attr('id'), $(this).parent().attr('id'), 'button-active')
	})
	
	$("#workEthic button").click(function(){
		$("#workEthicRating").val($(this).val())
	})
	
	$("#onTime button").click(function(){
		$("#onTimeRating").val($(this).val())
	})
	
	$("#hireAgain button").click(function(){
		$("#hireAgainRating").val($(this).val())
	})
	

	$("#submitRating").click(function(){
		
//		alert($("#onTimeRating").val())
//		alert($("#workEthicRating").val())
//		alert($("#hireAgainRating").val())
//		alert($("#employeeId").val())
//		
		var jobId = $("#jobId").val();
		
		
		var employeeId = $("#employeeId").val();
		var rating1 = $("#workEthicRating").val();
		var rating2 =  $("#onTimeRating").val();
		var rating3 =  $("#hireAgainRating").val();
		
		if(employeeId != "" && rating1 != "" && rating2 != "" && rating3 != ""){			
			rateEmployee(1, rating1, jobId, employeeId);
			rateEmployee(2, rating2, jobId, employeeId);
			rateEmployee(3, rating3, jobId, employeeId);
		}
	})
		
});


