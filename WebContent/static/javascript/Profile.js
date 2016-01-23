/**
 * 
 */

	//**************************************************************************************************
	//If we standardize properties across classes (i.e id, name),
	//then we could combine these functions.	

	function populateUsers(arr, e){ 		
		//alert("sweet populateUsers");		
		var i;
		e.options.length=0;		
		for(i=0; i< arr.length; i++){
			//alert("here");
			var opt = document.createElement('option');
			opt.value = arr[i].userId;
			opt.innerHTML = arr[i].firstName;
			e.appendChild(opt);					
		}		
	}

	function populateCategories(arr, e){
		//alert("sweet populateCategories");
		e.options.length=0;
		var i;
		for(i = 0; i < arr.length ; i++){
			var opt = document.createElement("option");					
			opt.value = arr[i].id;
			opt.innerHTML = arr[i].name;
			e.appendChild(opt);
		}		
	}
	
	function populateJobs(arr, e, active){
		//active values:
		//pass a negative number to populate all jobs, whether active or complete.
		//pass 0 to populate only completed jobs.
		//pass 1 to populate only active jobs.
		
		//alert("populate jobs length" + arr.length);
			
			e.options.length=0;
			var i;
			for(i = 0; i < arr.length ; i++){
				//alert(arr[i].jobName);		
				if(active < 0){
					var opt = document.createElement("option");					
					opt.value = arr[i].id;
					opt.innerHTML = arr[i].jobName;
					e.appendChild(opt);				
				}else if(arr[i].isActive == active){
					
					var opt = document.createElement("option");					
					opt.value = arr[i].id;
					opt.innerHTML = arr[i].jobName;
					e.appendChild(opt);
				}
			}		
		}	
	
	
	function populateJobs(arr, e, active){
		//active values:
		//pass a negative number to populate all jobs, whether active or complete.
		//pass 0 to populate only completed jobs.
		//pass 1 to populate only active jobs.
		
		//alert("populate jobs length" + arr.length);
			
			e.options.length=0;
			var i;
			for(i = 0; i < arr.length ; i++){
			//	alert(arr[i].jobName);		
				if(active < 0){
					var opt = document.createElement("option");					
					opt.value = arr[i].id;
					opt.innerHTML = arr[i].jobName;
					e.appendChild(opt);				
				}else if(arr[i].isActive == active){
					
					var opt = document.createElement("option");					
					opt.value = arr[i].id;
					opt.innerHTML = arr[i].jobName;
					e.appendChild(opt);
				}
			}		
		}	
	
	function test(){
		alert("test");
	}
	
	
	function getJobs(userId, e, jobStatus){		
	//	function getJobs(e){	
		alert(e);
		$.ajax({
			type: "GET",
			url: 'http://localhost:8080/JobSearch/getJobs?userId=' + userId,
	        dataType: 'json',
			success: _success,
	        error: _error
		    });
			
			function _success(response){	
				populateJobs(response.jobs, e, jobStatus);
			}
			
			function _error(response){
				alert("error");
			}
	}
	//**************************************************************************************************	
	

	function showCategory(){

		//alert('show cat');
		var eFrom = document.getElementById("profileCats");
		var eTo = document.getElementById("catToAdd");
		
		//Store the category's id property in the name attribute
		eTo.value = eFrom.options[eFrom.selectedIndex].text;
		eTo.name = eFrom.options[eFrom.selectedIndex].value;
	}
	
	
	function hireApplicant(){

		var eUser = document.getElementById("applicants");
		var userId = eUser.options[eUser.selectedIndex].value;			
		var jobId = document.getElementById("selectedJob").name;			
		
	//	alert("1");
		$.ajax({
			type: "GET",
			url: 'http://localhost:8080/JobSearch/hireApplicant?userId=' + userId + '&jobId=' + jobId,
	        dataType: 'json',
			success: _success,
	        error: _error
		    });
			
			function _success(response){
							
			populateUsers(response.selectedJob.employees, document.getElementById("employees"));
			//alert("success hire applicant");
			}
			
			function _error(response, errorThrown){
				alert("error");
			}
	}
	
//	function getEmployees(){
//		//alert("get employees");
//		var e = document.getElementById("selectedJob");
//		
//		//The job id is stored in the input's name attribute (see displayCat())
//		$.ajax({
//			type: "GET",
//			url: 'http://localhost:8080/JobSearch/getEmployees?jobId=' + e.name,
//			dataType: "json",
//		        success: _success,
//		        error: _error
//		    });
//		
//			//Executes if the ajax call is successful
//			function _success(response){		
//		//	alert("pop employees");
//			populateUsers(response.selectedJob.employees, document.getElementById("employees"));
//			
//			}
//			
//			//Executs if the ajax call errors out
//			function _error(response, errorThrown){
//				alert("error");
//			}
//	}
	
	function markJobComplete(){
		
		var e = document.getElementById("activeJobs");
		var jobId = document.getElementById("selectedJob").name;
		
		var item = {};
		item.jobId = jobId;
		//alert("mark");
		$.ajax({
			type: "GET",
			url: 'http://localhost:8080/JobSearch/markJobComplete?jobId=' + jobId,
	        dataType: "json",
			success: _success,
	        error: _error
		    });
		
		//Executes if the ajax call is successful
			function _success(response){
			//alert("success");
			populateJobs(response.activeJobs, document.getElementById("activeJobs"));
			
			$("#rateEmployee").html(response);
			}
			
			//Executs if the ajax call errors out
			function _error(response, errorThrown){
				alert("error");
				alert(errorThrown);
			}
	}
	
	
	function getSelectedJob(){
		//alert("displat job");
		var e = document.getElementById("activeJobs");
		var jobId = e.options[e.selectedIndex].value;;
		
		$.ajax({
			type: "GET",
			url: 'http://localhost:8080/JobSearch/getSelectedJob?jobId=' + jobId,
				dataType: "json",
		        success: _success,
		        error: _error
		    });
	
			function _success(response){	
				
				//Set the input's name attribute equal to the selected job's id.
				//This name attribute will be used when adding categories to the selected job.
				$("#selectedJob").val(response.selectedJob.jobName);
				$("#selectedJob").attr('name', response.selectedJob.id);
				
				populateUsers(response.selectedJob.applicants, document.getElementById("applicants"));
				populateUsers(response.selectedJob.employees, document.getElementById("employees"));				populateCategories(response.selectedJob.categories, document.getElementById("selectedJobCats"));
				//getEmployees()
			}

			function _error(response, errorThrown){
				alert("error");
			}	
	}
	
	
	
	function addCatToJob(){
		
		var jobId = document.getElementById("selectedJob").name;
		var catId = document.getElementById("catToAdd").name;

		$.ajax({	 	
			type: "GET",
	        url: 'http://localhost:8080/JobSearch/addCategoryToJob?jobId=' + jobId + '&categoryId=' + catId,
	        dataType: "json", // Response
	        success: _success,
	        error: _error,
	    });	
		
		function _success(response){
			//alert("success add cat to job");
			populateCategories(response.selectedJob.categories, document.getElementById("selectedJobCats"));
		}
		
		function _error(response){
			alert("error");
		}
	}
	
	function addJob(event){
		alert("sdfs");
		var jobName = event.data.element.value;
		//var jobName = document.getElementById("jobToAdd").value;
		//Add the usercategory item to the db
		$.ajax({	 	
			type: "GET",
	        url: "http://localhost:8080/JobSearch/addJob?jobName=" + jobName + "&userId=" + event.data.userId,
	        dataType: "json", // Response
	        success: _success,
	        error: _error
	    });
		
		//Executes if the ajax call is successful
		function _success(response){
		//	alert("success add job");
			populateJobs(response.jobs, document.getElementById("activeJobs"), 1);
			$("#jobToAdd").val("");
		}
		
		//Executs if the ajax call errors out
		function _error(response, errorThrown){
			alert("error");
		}

	}
	
	function deleteCategory(){

		var catId = document.getElementById("profileCats").value;
			
		$.ajax({	 	
			type: "GET",
	        url: 'http://localhost:8080/JobSearch/deleteCategory?categoryId=' + catId,
	        contentType: "application/json", // Request
	        dataType: "json", // Response
	        success: _success,
	        error: _error
	    });
			
		function _success(response){
			//alert("success");
			populateCategories(response.categories , document.getElementById("profileCats"));			
		}

		function _error(response, errorThrown){
			alert("error");
		}	
	}

	function addCategory(){
		var e = document.getElementById("allCats");
		var catId = e.options[e.selectedIndex].value;
		
		//Add the usercategory item to the db
		$.ajax({	 	
			type: "GET",
	        url: 'http://localhost:8080/JobSearch/addCategory?categoryId=' + catId,
	        contentType: "application/json", // Request
	        dataType: "json", // Response
	        success: _success,
	        error: _error
	    });
	
		function _success(response){
			//alert("success");
			//showCategories(response);
			populateCategories(response.categories , document.getElementById("profileCats"));
		}
		
		function _error(response, errorThrown){
			alert("error");
		}	

	}
