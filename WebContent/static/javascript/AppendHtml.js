function addCategoryToSelection(event){


	var newButtonId = $(event).parent().attr('id') + "-selected";

	if($('#selectedCategories').find('#' + newButtonId).length == 0){
	
		var html = '<button type="button" class="btn btn-success" id="' + newButtonId + '"';
			html += ' onclick="removeCategoryFromSelection(this)">';
			html += $(event).html();
			html += '<span style="margin: 5px 5px 5px 5px" class="glyphicon glyphicon-remove"></span></button>';	
			
			$("#selectedCategories").append(html);			
	}
}


function removeCategoryFromSelection(event){
	$("#" + event.id).remove();
}

function appendCategories(categoryId, subCategoryDivIdKey, arr, callback) {
	// PARAMETERS:
	// 1) categoryId is the category id. It will be used to identify the div element in
	// which
	// to append the html to. The div element id equals the category id with a
	// 'T' appended to the end.
	// 2) arr is a list of category objects (i.e. sub categories to append)

	// If there are sub categories
	if (arr.length > 0) {

		var j = -1;
		var r = new Array();
		r[++j] = '<ul class="list-group">';

		// For each sub category, create html
		for (var i = 0; i < arr.length; i++) {

			var id = arr[i].id;
			var categoryName = arr[i].name;
			
			r[++j] = '<li id="' + id + '" class="list-group-item">';
			r[++j] = '<a style="cursor: pointer" id="' + id + '-Name" onclick="addCategoryToSelection(this)"'; //\'' + id + '\', \'' + categoryName + '\'"' 
			r[++j] = '>' + categoryName;
			r[++j] = '</a>';

			if (pageContext === "postJob" || pageContext === "profile") {


			} else if (pageContext === "findJob") {
				// Number of active jobs in the category
				r[++j] = '<a class="find-jobs-job-count btn btn-info btn-sm" onclick="appendJobs_Available(\''
						+ id + '\')">';
				r[++j] = arr[i].jobCount + '</a>';
			}
			
			
			// When this hyperlink is clicked, the below div is expanded.
			// The inner html is the job count of ALL its sub categories, not
			// just
			// sub categories 1 level deep.
			// Also on click, the category
			r[++j] = '<a style="margin: 0px 0px 10px 10px; height: 25px" '
						+ 'onclick="appendNextLevelCategories(\'' + id
						+ '\', \'' + subCategoryDivIdKey + '\')" id="#' + id + '-C"';
			r[++j] = 'data-target="#'
					+ id + subCategoryDivIdKey + '"'
					+ ' data-toggle="collapse" class="find-jobs-sub-job-count btn btn-success btn-sm">';
			
			
			r[++j] = '<span style="vertical-align: middle" class="glyphicon glyphicon-menu-down padding-hori">'
			if (pageContext === "findJob"){
				r[++j] = "  " + arr[i].subJobCount;
			}
			r[++j] = '</span></a>';

			// This div will eventually hold the category's sub categories.
			// When this category's sub categories need to be set,
			// this div id will be passed to this function and have this
			// html appended to it (see append method at end of this function).
			r[++j] = '<div id="' + id + subCategoryDivIdKey + '" class="panel-collapse collapse">';
			r[++j] = '</div>';

			r[++j] = '</li>';
		}

		r[++j] = '</ul>'
		// alert('about to append to ' + categoryId)
		// This is the id of div that will hold the sub categories
		$("#" + categoryId + subCategoryDivIdKey).append(r.join(''));

	}
}

function appendNextLevelCategories(elementId, subCategoryDivIdKey) {

	//When the 'second' data attribute is set to true,
	//that signifies the category's second level has been set.
	//So when the category is collapsed and then re-expanded, the
	//below code will not execute again. It only needs to execute once.
	var nextLevelDiv = $('#' + elementId + subCategoryDivIdKey);
	if(nextLevelDiv.data('second') != 'true'){
		nextLevelDiv.data('second', 'true');
		
		var clickedCategory = $('#' + elementId);
		
		// Get the categories 1 level deep.
		// The li element holds the category Id without anything appended to the
		// end.
		getCategoriesBySuperCat(clickedCategory.closest('li').attr('id'), function(response,
				categoryId) {
	
			appendCategories(categoryId, subCategoryDivIdKey, response)
		});
	}
}

function setEmployeeId(e) {
	$("#employeeId").val(e.target.id);
}


function selectCategory(categoryId) {

	$("#selectedCategory").val(categoryId);

}

function uncheckCheckboxes(parentId, excludeId) {

	var arr = $("#" + parentId).find("input[type=checkbox]");
	for (var i = 0; i < arr.length; i++) {

		if (arr[i].id != excludeId) {
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


function appendFilteredJobsTable(jobs, userId){
	
	$("#filteredJobs").empty();

	// If there are sub categories
	if (jobs.length > 0) {
		
		var j = -1;
		var r = new Array();
		r[++j] = '<table id="filterJobTable" class="table table-hover table-striped table-bordered" cellspacing="0" width="100%">';
		r[++j] = '<thead>';
		r[++j] = '<tr>';
		r[++j] = '<th>Job Name</th>';
		r[++j] = '<th>City</th>';
		r[++j] = '<th>State</th>';
		r[++j] = '<th>Distance</th>';
		r[++j] = '<th>Categories</th>';
		r[++j] = '<th>Start Date</th>';
		r[++j] = '<th>End Date</th>';		
		r[++j] = '<th>Start Time</th>';
		r[++j] = '<th>End Time</th>';
		r[++j] = '</tr>';
		r[++j] = '</thead>';		
		r[++j] = '<tbody>';

		for (var i = 0; i < jobs.length; i++) {
			
			var job = jobs[i];
			var id = job.id;
//			alert(JSON.stringify(job))

			r[++j] = '<tr id="' + id + '">';
			r[++j] = '<td>' + job.jobName + '</td>';
			r[++j] = '<td>' + job.city + '</td>';
			r[++j] = '<td>' + job.state + '</td>';
			r[++j] = '<td>' + Math.round(job.distanceFromFilterLocation * 10) / 10 + '</td>';

			var categoryNames = "";
			for (var k = 0; k < job.categories.length; k++){
				categoryNames += job.categories[k].name;
				if ( k < job.categories.length - 1){
					categoryNames +=  ", ";
				}
			}

			r[++j] = '<td>' + categoryNames + '</td>';
			r[++j] = '<td>' + moment(job.startDate).format("MM/DD/YYYY") + '</td>';
			r[++j] = '<td>' + moment(job.endDate).format("MM/DD/YYYY") + '</td>';
			r[++j] = '<td>' + job.startTime + '</td>';
			r[++j] = '<td>' + job.endTime + '</td>';
			r[++j] = '</tr>';
		}

		r[++j] = '</tbody>';
		r[++j] = '</table>';
		$("#filteredJobs").append(r.join(''));
		
		$("#filterJobTable tr td").click(function(){
			window.location = '../job/' + $(this).parent().attr('id');
		})
		
		$('#filterJobTable').DataTable();
		
	}
	
}

$(document).ready(
		function() {

//			$("#addJob").click(function() {
//
//				var jobName = $("#jobToAdd").val();
//				var categoryId = $('#selectedCategory').val();
//				var userId = $('#userId').val()
//				addJob(jobName, userId, categoryId, function(response) {
//				});
//			});

			$("#saveEditProfileCats").click(function() {

				// Add categories
				var addCats = new Array();
				addCats = $("#addCategories").find('div');

				var ids = new Array();
				;
				for (var i = 0; i < addCats.length; i++) {
					ids[i] = getCategoryId(addCats[i].id)
				}

				addCategoriesToUser(ids, $('#userId').val())

				// Remove categories
				var removeCats = new Array();
				removeCats = $("#removeCategories").find('div');

				var ids = new Array();
				;
				for (var i = 0; i < removeCats.length; i++) {
					ids[i] = getCategoryId(removeCats[i].id)
				}

				removeCategoriesFromUser(ids, $('#userId').val())
			})


		});
