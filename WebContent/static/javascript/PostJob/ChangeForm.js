
function showAddedJob(jobId){
	var i;
	
	removeInvalidFormControlStyles();
	disableFromControls(true);
	
//	var jobId = $(e).val();		
	var job = {};
	job = getJobById(jobId, jobs);

	//Store the selected jobs id in case future action is taken
//	$("#saveChanges").val(jobId);
//	$("#deleteJob").val(jobId);
	$("#activeJobId").val(jobId)

	
	//Update elements' value
	document.getElementsByName('jobName')[0].value = job.jobName;			
	document.getElementsByName('streetAddress')[0].value = job.streetAddress;
	document.getElementsByName('city')[0].value = job.city;
	document.getElementsByName('state')[0].value = job.state ;
	document.getElementsByName('zipCode')[0].value = job.zipCode;
	document.getElementsByName('jobDescription')[0].value = job.description;
	document.getElementsByName('userId')[0].value = job.userId;				
	$("#dateRange").data('daterangepicker').startDate = job.stringStartDate;
	$("#dateRange").data('daterangepicker').endDate = job.stringEndDate;
	$("#dateRange").val(job.stringStartDate.format('MM/DD/YYYY') + ' - ' + job.stringEndDate.format('MM/DD/YYYY'));
	$("#startTime").val(formatTimeTo12Hours(job.stringStartTime));
	$("#endTime").val(formatTimeTo12Hours(job.stringEndTime));
	
	//Show categories
	for(i = 0; i < job.categoryIds.length; i++){
		var id = job.categoryIds[i];
		var name = $($($("#categoryTree").find("[data-cat-id=" + id + "]")[0])
						.find(".category-name")[0]).text();
		showCategory(id, name);
	}
	
	//Reset all question check marks to disabled
	var $check;
	var questionElements = $("#addedQuestions").find(".added-question");
	for(i = 0; i < questionElements.length; i++){
		 $check = $($(questionElements[i]).find(".toggle-question-activeness")[0]);
		if ($check.hasClass('enable-question') == 1){
			$check.removeClass('enable-question');
			$check.addClass('disable-question');
		}
	}

	//Enable the selected questions for the active job
	for(i = 0; i < job.selectedQuestionIds.length; i++){
		
		$check = $($("#" + questionContainerIdPrefix + job.selectedQuestionIds[i])
						.find(".toggle-question-activeness")[0]);
		$check.removeClass("disable-question");
		$check.addClass("enable-question");
	}
}

function deactiveAddedJobButtons(){
	
	$("#addedJobs").find('button').each(function(){
		if($(this).hasClass("active-button") == 1){
			$(this).removeClass("active-button");
			$(this).addClass("inactive-button");
		}
	})	
	
	//Since no jobs will be active, the copy job option must be disabled
	$("#copyJob").addClass("disabled");
}

function disableFromControls(trueFalse){

	//**************************************************************************************
	//Can this go away now that the general and questions have been separated????
	//**************************************************************************************
	
	//Job info
	$("#jobGeneralContainer").find(".form-control").each(function(){
		$(this).attr("disabled", trueFalse);
	})	
	disableCategories(trueFalse);
	
	
	//Questions
	$("#jobQuestionsContainer").find(".form-control").each(function(){
		$(this).attr("disabled", trueFalse);
	})

	if (trueFalse == true){
		$("#jobQuestionsContainer").find(".glyphicon").each(function(){
			$(this).addClass("disabled");
		})
	}else{
		$("#jobQuestionsContainer").find(".glyphicon").each(function(){
			$(this).removeClass("disabled");
		})
	}

}

function disableJobCart(trueFalse){
	
	$("#addedJobs").find("button").each(function(){
		$(this).attr("disabled", trueFalse);
	})
}

function disableCategories(trueFalse){	
	if(trueFalse == true){
		$("#categoryContainer").find(".glyphicon").each(function(){			
			$(this).addClass("disabled-category");
		})	
	}else{
		$("#categoryContainer").find(".glyphicon").each(function(){
			$(this).removeClass("disabled-category");
		})	
	}
}

function disableGeneralControls(trueFalse){
	$("#jobGeneralContainer").find(".form-control").each(function(){
		$(this).attr("disabled", trueFalse);
	})		
}

function disableNewQuestionControls(trueFalse){

	$("#new-question-container-container").find(".form-control").each(function(){
		$(this).attr("disabled", trueFalse);
	})

	if (trueFalse == true){
		$("#new-question-container-container").find(".glyphicon").each(function(){
			$(this).addClass("disabled");
		})
	}else{
		$("#new-question-container-container").find(".glyphicon").each(function(){
			$(this).removeClass("disabled");
		})
	}	
	
}

function clearPostJobInputs(){
	document.getElementsByName('jobName')[0].value = "";		
	document.getElementsByName('streetAddress')[0].value = "";
	document.getElementsByName('city')[0].value = "";
	document.getElementsByName('state')[0].value = "";
	document.getElementsByName('zipCode')[0].value = "";
	document.getElementsByName('jobDescription')[0].value = "";
	document.getElementsByName('userId')[0].value = "";
	$("#dateRange").data('daterangepicker').startDate = "";
	$("#dateRange").data('daterangepicker').endDate = "";
	$("#dateRange").val("");
	$("#startTime").val("");
	$("#endTime").val("");
	
	setDateRange();
	
	$("#selectedCategories").empty();
}

