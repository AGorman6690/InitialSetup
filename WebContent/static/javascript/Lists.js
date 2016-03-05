

function appendFirstLevelCategories(eId, arr, callback){
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
			r[++j] = 	'<a class="btn btn-info btn-sm margin-both" onclick="showJobs(\'' + id + '\')">';
			r[++j] =	arr[i].jobCount + '</a>';
			
//			alert(arr[i].jobCount)
			//When this hyperlink is clicked, the below div is expanded.
			//The inner html is the job count of ALL its sub categories, not just
			//sub categories 1 level deep.
			//Also on click, the categor
			r[++j] = 	'<a onclick="appendSecondLevelCategories(\'' + id + '\')" id="#' + id + 'C"';
			r[++j] =  	'data-target="#' + id + 'T" data-toggle="collapse" class="btn margin-both btn-success btn-sm">';
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


function appendSecondLevelCategories(elementId){
	
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
					
					appendFirstLevelCategories(elementId, response, function(){						
					
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
			r[++j] = '<li id="' + id + '" class="list-group-item margin-hori">';
			
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

function showJobs(categoryId){
	
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

function applyToJob(jobId){

	applyForJob(jobId, $("#userId").val(), function(response){
		alert("Application Received!")
	})
}

function selectCategory(categoryId){
//	alert(categoryId)
//	selectedId = categoryId;
//	alert(selectedId)
	$("#selectedCategory").val(categoryId);
	
	//alert($("#selectedCategory").val())
	
	uncheckCheckboxes("0T", categoryId + "Click");
	
//	//If checked, then
//	if($('#' + categoryId + "Click").is(':checked')){
//		
//		//append div element 
//		var e = $("<div id='" + categoryId + 'Add' + "'></div>");
//		$("#selectedCategories").append(e);
//
//	}else{		
//		$('#' + categoryId + "Add").remove();
//		//alert('false2')
//	}
	
}

function selectCategory2(categoryId){
//	alert(categoryId)
//	selectedId = categoryId;
//	alert(selectedId)

	
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

function getCategoryId(id){
//PURPOSE: This will take an Id an parse it to find the id.
//The div tag's id syntax is {categoryId}-{level}--.	
//For example, if id = "12345-56--", this will return 12345. 

	var idEnd = id.indexOf("-");
	return id.substring(0, idEnd);
}


function appendSubCategories(categoryId){
	
	//For each seed (the "T" is simply the div element in which the sub categories
	//will be appended to),
	var arr = $('#' + categoryId + 'T').find('li');
	for(var i = 0; i < arr.length; i++){
		
		//get sub categories.
		getCategoriesBySuperCat(arr[i].id, function(response, elementId){

			//Append sub categories
			appendFirstLevelCategories_ProfileCats(elementId, response, function(){		
				alert("firsst")
			})
		})
	}
}


$(document).ready(function(){

	
	$("#addJob").click(function(){
		
		alert($("#userId").val())
		
 		var jobName = $("#jobToAdd").val();
 		var categoryId = $('#selectedCategory').val();
 		var userId = $('#userId').val()
 		alert(categoryId);
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
		
});


