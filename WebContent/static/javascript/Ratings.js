//*************************************************
//*************************************************
//The rate criterion Id is hard coded right now (first value passed to rateEmployee() method).
//Ideally the rate criteria will be dynamically created.
//Therefore, the criteria name, criteria id, etc., would be held in an element's attributes.
//*************************************************
//*************************************************

$(document).ready(function(){	
	$("#ontime1, #ontime2, #ontime3, #ontime4, #ontime5").click(function(){
		var value = this.value;		
		var jobId = $("#completedJobs").val();
		var employeeId = $("#selectedEmployee").val();
		
		rateEmployee(1, value, jobId, employeeId);
	})


	$("#workEthic1, #workEthic2, #workEthic3, #workEthic4, #workEthic5").click(function(){
		var value = this.value;		
		var jobId = $("#completedJobs").val();
		var employeeId = $("#selectedEmployee").val();
		
		rateEmployee(2, value, jobId, employeeId);
	})


	$("#hireAgain1, #hireAgain2, #hireAgain3").click(function(){
		var value = this.value;		
		var jobId = $("#completedJobs").val();
		var employeeId = $("#selectedEmployee").val();
		
		rateEmployee(3, value, jobId, employeeId);
	})
	
		

	
	$("#completedJobs").change(function(){		
		//Get job id
		var jobId = $("#completedJobs").val();
	
		//Get the employees for the selected job
		getEmployeesByJob(jobId, function(response){
		//	populateUsers(response, document.getElementById("employeesCompletedJob"));
			//popUl_user(response, $("#employeesToRate"));
			populateUsers(response, document.getElementById("employeesToRate"));
		});
	})	
	
	$("#employeesToRate").change(function(){
		//Get the name and id of the empoyee to rate
		var employeeName = this.options[this.selectedIndex].innerHTML;
		var employeeId = $(this).val();
		
		//Show the selection
		selectEmployee(employeeId, employeeName);
	})	
	
	$("#nextEmployee").click(function(){
		
		//Get the next index of the employeeToRate select box
		var e = document.getElementById("employeesToRate");
		var nextIndex = getNextIndex(e);
		
		//Update the selected index
		e.selectedIndex = nextIndex;
		
		//Trigger the change event to show the next employee
		$("#employeesToRate").trigger('change');
	})
	
})

function selectEmployee(id, name){	
	//Store the employee's id in the input element's value attribute
	$('#selectedEmployee').val(id);
	$('#selectedEmployee').html("How did " + name + " perform?");
}

function getNextIndex(e){
	//PURPOSE: This takes in a select element and returns the next options

	if(e.selectedIndex == e.length - 1){
		return 0;
	} else{
		return e.selectedIndex + 1;
	}
}

function rateEmployee(rateCriterionId, value, jobId, employeeId){
	//alert("rateEmployee");
	$.ajax({
		type: "GET",
		url: 'http://localhost:8080/JobSearch/rateEmployee?rateCriterionId=' + rateCriterionId + '&value=' + value +
				'&jobId=' + jobId + '&employeeId=' + employeeId,
			dataType: "json",
	        success: _success,
	        error: _error
	    });

		function _success(response){
			//alert("success rateEmpoyee");
			//callback(response);
		}

		function _error(response, errorThrown){
			alert("error rateEmpoyee");
		} 
}