		function isSelectingQuestions(){
	
	
			//If the "Select Question" action's check mark is visible
			if($("#okSelectedQuestions").is(":visible")){
				return true;
			}else{
				return false;
			}
	
		}
			
		function isButtonClickable($e){
			if($e.hasClass("clickable") == 1){
				return true;
			}else{
				return false;
			}
				
		}
		
		function copyJob(){
			disableInputFields(false, "jobInfoBody");
			
			deselectButtons("cartContainer");
			setActionsAsClickable(false, "jobCart");
			setButtonAsClickable(true, $("#addJobToCart"));
		}
		
		function setButtonsAsClickable(request, containerId){
			
			var buttons = $("#" + containerId).find("button");
			$.each(buttons, function(){
				setButtonAsClickable(request, $(this));
			})
			
		}
		
		function setButtonAsClickable(request, $e){
			if(request == true){
				$e.addClass("clickable");
				$e.removeClass("not-clickable");
			}
			else{
				$e.addClass("not-clickable");
				$e.removeClass("clickable");
			}
		}
		
		function selectButton(button){
			$(button).addClass("selected");
		}
		
		function setSelectedQuestionIdsForJob(){
			var selectedJob = getSelectedJob();
			var selectedQuestionIds = getQuestionIdsTiedToJob();
			
			selectedJob.selectedQuestionIds = selectedQuestionIds;
		}
		
		function getQuestionIdsTiedToJob(){
			var selectedQuestionIds = [];
			var selectedButtons = $("#questionCart").find("button.tied-to-job");
			
			$(selectedButtons).each(function(){
				selectedQuestionIds.push($(this).attr("data-question-id"))
			})
			
			return selectedQuestionIds;
		}
		
		function showQuestionForSelectedJob(button){
			$(button).addClass("tied-to-job");
			$(button).removeClass("selected");
		}
		
		function selectQuestion(questionButton){
			
			//Format the cart
			deselectButtons("cartContainer");
			selectButton(questionButton);
			setActionsAsClickable(true, "questionCart");
			
			//Format the question inputs
			showQuestion();
			disableInputFields(true, "questionInfoBody");
			expandInfoBody("questionInfoBody", true);
			setButtonAsClickable(false, $("#addQuestion"));
//			disableInputFields(true, "postingContainer");
			
			//Format the job inputs
			clearPostJobInputs();
			disableInputFields(false, "jobInfoBody");			
			expandInfoBody("jobInfoBody", false);
			setButtonAsClickable(true, $("#addJobToCart"));
			
		}
		
		function doesQuestionHaveAnAnswerList(postQuestionDto){
			if(postQuestionDto.formatId == 2 || postQuestionDto.formatId == 3){
				return true;
			}else{
				return false;
			}
		}
		
		function removeArrayElement(idToRemove, dtos){
			var newArray = [];
			newArray =  $.grep(dtos, function(dto, i){
							return dto.id != idToRemove
						})
									
			return newArray;
		}
		
		function addBorderToQuestionCart(request){
			
			var $e = $("#addedQuestions");
			if(request){
				$e.addClass("green-border");
			}else{
				$e.removeClass("green-border");
			}
		}
		
		function disableInputFields(request, containerId){
			
			var $eContainer = $("#" + containerId);
			var inputs;
			var requestedDisabledValue;
			
			//Per the request, set the value for the "disabled" property
			if(request == true){
				requestedDisabledValue = "disabled";
			}else{
				requestedDisabledValue = "";
			}
				
			//Build an array of all the "input" elements
			inputs = $eContainer.find("input");
			$.merge(inputs, $eContainer.find("textarea"));
			$.merge(inputs, $eContainer.find("select"));
			
			//Set the disabled property for the input elements
			$.each(inputs, function(){
				$(this).prop("disabled", requestedDisabledValue);
			})
			
			
			//Set the date picker's "enabledness"
			if(request == true){
				$("#calendar").datepicker("disable");
			}else{
				$("#calendar").datepicker("enable");
			}
		}
		
		function showQuestion(){
			var selectedQuestion = getSelectedQuestion();
			var i;
			$("#question").val(selectedQuestion.text);
			$("#questionFormat option[value='" + parseInt(selectedQuestion.formatId) + "']").prop("selected", "selected");
			$("#questionFormat").trigger("click");
			
			//If a single or multiple answer
			if(doesQuestionHaveAnAnswerList(selectedQuestion)){		
				
				//Clear all the answer option inputs except the first 2.
				//By definition there must be at least two answer options.
				//Plus, at least 1 .answer-container needs to remain in order to
				//have something to clone in the "#addAnswer" click event.
				i = 0;
				$("#answerList").find(".answer-container").each(function(){
					if(i > 1){
						$(this).remove();
					}					
					i += 1;
				})
				
				//Add and answer option for every answer option other than the first 2 
				for(i=0; i<selectedQuestion.answerOptions.length - 2; i++){
					$("#addAnswer").trigger("click");
				}
				
				//Populate the answer option inputs
				var answerInputs = $("#answerList").find(".answer-container input");
				i = 0;
				$.each(answerInputs, function(){
					$(this).val(selectedQuestion.answerOptions[i]);
					i += 1;
				})
			}
		}
		

		
		function selectJob(jobButton){
			deselectButtons("cartContainer");
			selectButton(jobButton);
			setActionsAsClickable(true, "jobCart");
//			setActionsAsClickable(false, "questionCart");
			
			showAddedJob(jobButton);
			
			//Set the job info inputs			
			disableInputFields(true, "jobInfoBody");
			expandInfoBody("jobInfoBody", true);
			
			//Set the question info input
			//Clear inputs, enable buttons, enable inputs
			clearPostQuestionInputs();	
			setButtonsAsClickable(true, "questionInfo");
			disableInputFields(false, "questionInfoBody");
			
			//Set the add job button as unclickable
			setButtonAsClickable(false, $("#addJobToCart"));
			
			
		}
		
		function expandInfoBody(containerId, request){
			
			var $eToHideOrShow = $("#" + containerId);
			var $eGlyphicon = $($("#postingContainer").find("span[data-toggle='" + containerId + "']")[0]);
			
			//Expand 
			if(request == true){
				$eToHideOrShow.show(200);
				addClassRemoveClass($eGlyphicon, "glyphicon-menu-down", "glyphicon-menu-up");
				
			//Collapse
			}else{
				$eToHideOrShow.hide(200);
				addClassRemoveClass($eGlyphicon, "glyphicon-menu-up", "glyphicon-menu-down");
			}
				
		}
		
		function clearInvalidCss(){
			$("#postingContainer").find(".invalid").each(function(){
				$(this).removeClass("invalid");
			})
			
			$("#postingContainer").find(".invalid-message").each(function(){
				$(this).hide();
			})
		}
		
			
		
		function deselectJob(){
			deselectButtons("cartContainer");
			setActionsAsClickable(false, "jobCart");
			setActionsAsClickable(false, "questionCart");
			setButtonsAsClickable(true, "cartContainer");
			setButtonsAsClickable(true, "jobInfo");
			clearPostJobInputs();
			clearPostQuestionInputs();
			disableInputFields(false, "postingContainer");
			
		}
		
		
		function deselectQuestion(){
			deselectButtons("questionCart");
			setButtonsAsClickable(true, "questionInfo");
			setActionsAsClickable(false, "questionCart");
			clearPostQuestionInputs();
			disableInputFields(false, "postingContainer");
		}
		
		
		function clearPostQuestionInputs(){
			$("#question").val("");
			$("#questionFormat option[value='-1']").prop("selected", "selected");
			
			$("#answerList").find(".answer-container input").each(function(){
				$(this).val("");
			});
			
			clearInvalidCss();
		}
		
		function hideCart(request){
			if(request == true){
				$("#cartContainer").hide(200);	
			}else{
				$("#cartContainer").show(200);
			}
			
		}
		
		function deleteJob(){
			
			//Get and remove the job
			var selectedJob = getSelectedJob();
			jobs = removeArrayElement(selectedJob.id, jobs);
			
			//Remove the job from the cart
			removeElementFromDOM($("#jobCart"), "data-job-id", selectedJob.id);
			
			//Format the inputs
			disableInputFields(false, "postingContainer");
			clearPostJobInputs();
			setActionsAsClickable(false, "jobCart");
			
			//If necessary, hide the cart
			if(jobs.length < 1){
				hideCart(true);
			}
		}
		
		function deleteQuestion(){
			
			//Get and remove the job
			var selectedQuestion = getSelectedQuestion();
			postQuestionDtos = removeArrayElement(selectedQuestion.id, postQuestionDtos);
			
			//Remove the job from the cart
			removeElementFromDOM($("#questionCart"), "data-question-id", selectedQuestion.id);
			
			//Format the inputs
			setActionsAsClickable(false, "questionCart");
			disableInputFields(false, "postingContainer");
			clearPostQuestionInputs();
			
			//If necessary, hide the cart
			if(jobs.length < 1){
				hideCart(true);
			}

		}		
		
//		function clearInputs(containerId){
//			
//			if(containerId == "jobInfoBody"){
//				document.getElementsByName('name')[0].value = "";		
//				document.getElementsByName('streetAddress')[0].value = "";
//				document.getElementsByName('city')[0].value = "";
//				document.getElementsByName('state')[0].value = "";
//				document.getElementsByName('zipCode')[0].value = "";
//				document.getElementsByName('description')[0].value = "";
//				
//				
//				$("#clearCalendar").trigger("click");
//				
//				
//				$("#selectedCategories").empty();
//			}
//			else if (containderId == "questionInfoBody"){
//				$("#question").val("");
//				$("#questionFormat option[value='-1']").prop("selected", "selected");
//				
//				$("#answerList").find(".answer-container input").each(function(){
//					$(this).val("");
//				});				
//			}
//		}
	
		function clearPostJobInputs(){
			document.getElementsByName('name')[0].value = "";		
			document.getElementsByName('streetAddress')[0].value = "";
			document.getElementsByName('city')[0].value = "";
			document.getElementsByName('state')[0].value = "";
			document.getElementsByName('zipCode')[0].value = "";
			document.getElementsByName('description')[0].value = "";
			
			
			$("#clearCalendar").trigger("click");
			
			
			$("#selectedCategories").empty();
			
			clearInvalidCss();
		}	
		
		function getSelectedQuestion(){
			var selectedQuestionId = $($("#questionCart").find("button.selected")[0]).attr("data-question-id");
			selectedQuestionId = parseInt(selectedQuestionId);
			
			var selectedQuestion = {};
			$.each(postQuestionDtos, function(){
				if(this.id == selectedQuestionId){
					selectedQuestion = this;		
				}
			})
			
			return selectedQuestion;
			
		}
		
		function getSelectedJob(){
			var selectedJobId = $($("#jobCart").find("button.selected")[0]).attr("data-job-id");
			selectedJobId = parseInt(selectedJobId);
			var selectedJob = {};
			$.each(jobs, function(){
				if(this.id == selectedJobId){
					selectedJob = this;		
				}
			})
			
			return selectedJob;
			
		}
		
//		function deselectButton(containerId){
//			$($("#" + containerId).find("button.selected")[0]).removeClass("selected");
//		}
//		
		function deselectButtons(containerId){
			
			//Build and array of the selected buttons
			var selectedButtons = $("#" + containerId).find("button.selected");
			$.merge(selectedButtons, $("#" + containerId).find("button.tied-to-job"));
			
			$.each(selectedButtons, function(){
				$(this).removeClass("selected");
				$(this).removeClass("tied-to-job");
			})
			
		}
		
		function jobIsClicked(button){
			var subCart = $(button).parents(".sub-cart")[0];
			
			if($(subCart).attr("id") == "jobCart"){
				return true;
			}
			else{
				return false;
			}
		}
		
		function buttonIsCurrentlySelected(button){
			if($(button).hasClass("selected") == 1){
				return true;
			}
			else{
				return false;
			}
		}
		
		
		function setActionsAsClickable(request, cartId){
			//The cart id is passed because if the user clicks a job, then only the job
			//actions should be enabled, and vice versa
			
			var $e;
			
			
//			toggleClasses($("#" + cartId), "actions-clickable", "actions-not-clickable")
			$e = $("#" + cartId);
			if(request == true){
				$e.removeClass("actions-not-clickable");
				$e.addClass("actions-clickable");
			}else{
				$e.removeClass("actions-clickable");
				$e.addClass("actions-not-clickable");
			}
			
			//Depending if questions have been added, set the "select quesions" action.
			//By removing the these classes, it will not appear as clickable.
			$e = $("#selectQuestions");
			if(postQuestionDtos.length == 0){
				$e.removeClass("action");
				$e.removeClass("requires-acknowledgement");
			}else{
				$e.addClass("action");
				$e.addClass("requires-acknowledgement");
			}

			//Review this logic. Is it still applicable???????
			//*************************************************************************
			//*************************************************************************			
			//Only one cart's actions can be clickable.
			//Always remove the "clickable" feature from the other cart.
			if(cartId == "jobCart"){
				$("#questionCart").removeClass("actions-clickable");
			}else{
				$("#jobCart").removeClass("actions-clickable");	
			}
			//*************************************************************************
			//*************************************************************************			
			
			//This ensures the confirmation modal to delete the job does not show when the
			//user clicks "Delete" when the actions are not clickable
			$e =$("#deleteJob");
			if(cartId == "jobCart" && request == false){
				
				//Remove the modal target
				$e.attr("data-target", "");
			}else{
			
				//If the alert/modal has not be disabled
				if($e.attr("data-disable-alert") != 1){
					//Apply the modal target					
					$e.attr("data-target", "#confirmJobDeleteModal");	
				}
				
			}
		}
		
		function toggleActionAppearances(cartId){
			//The cart id is passed because if the user clicks a job, then only the job
			//actions should be enabled, and vice versa
			
			toggleClasses($("#" + cartId), "actions-clickable", "actions-not-clickable")
			
			//Only one cart's actions can be clickable.
			//Always remove the "clickable" feature from the other cart.
			if(cartId == "jobCart"){
				$("#questionCart").removeClass("actions-clickable");
			}else{
				$("#jobCart").removeClass("actions-clickable");	
			}
			
			//This ensures the confirmation modal to delete the job does not show when the
			//user clicks "Delete" when the actions are disabled 
			if($("#jobCart").hasClass("actions-not-clickable") == 1){
				
				//Remove the modal target
				$("#deleteJob").attr("data-target", "");
			}else{
			
				//Apply the modal target
				$("#deleteJob").attr("data-target", "#confirmJobDelete");
			}
			
// 			//By "Action" I mean the various actions the user can perform on an
// 			//already-added job or question		
// 			var actions = $("#cartContainer").find(".action");
// 			$(actions).each(function(){
// 				toggleClasses($(this), "mock-anchor", "not-clickable");
// 			})
			
// 			var buttons = $("#cartContainer").find("button");
// 			$(buttons).each(function(){
// 				toggleClasses($(this), "clickable", "not-clickable");
// 			})
		
		}		
		
		
		function addJobToCart(){
			
//	 		if(validatePostJobInputs(jobs) == 0){
				if(isButtonClickable($("#addJobToCart"))){
					jobCount += 1;
				
					var postJobDto = {};
					postJobDto = getPostJobDto();
					postJobDto.id = jobCount;			
					jobs.push(postJobDto);
					
					$("#cartContainer").show(200);
	
					
					$("#jobCart").append(
							'<button data-job-id=' + jobCount + ' type="button" class="added-job btn inactive-button clickable">'
									+ postJobDto.jobName + '</button>')
					
					clearPostJobInputs();		
//					removeInvalidFormControlStyles()
					
					$("#cartContainer").show(200);
					
					jobCount++			
				}
//			}
		}
		
		
		
	function submitJobs(confirmation){
		
		if(confirmation == 1){
				
			var headers = getAjaxHeaders();
			
			var submitJobPostingDto ={};
			submitJobPostingDto.postJobDtos = jobs;
			submitJobPostingDto.postQuestionDtos = postQuestionDtos;
//			salert(posting)

			$.ajax({
				type : "POST",
				url : environmentVariables.LaborVaultHost + "/JobSearch/jobs/post",
				headers : headers,
				contentType : "application/json",
				data : JSON.stringify(submitJobPostingDto)
			}).done(function() { 				
//				window.location = "/JobSearch/user/profile";
			}).error(function() {
//				alert("error submit jobs")
//				$('#home')[0].click();
			});
		
		}

	}
	
	
	function getWorkDays(){
		
		var days = $("#times").find(".time");
		var date;
		var eStartTime;
		var eEndTime;	
		var workDays = [];
		var workDay;
		
		$(days).each(function(){
			
			//Read the DOM
			date = new Date;
			date.setTime($(this).attr("data-date"));		
			date = $.datepicker.formatDate("yy-mm-dd", date);
			
//			date = date.toString();
			
			
			
			eStartTime = $(this).find(".start-time")[0];
			eEndTime = $(this).find(".end-time")[0];
			
			//Set the JSON
			workDay = {};
			workDay.stringDate = date;
			workDay.stringStartTime = formatTime($(eStartTime).val());
			workDay.stringEndTime = formatTime($(eEndTime).val());
			workDay.millisecondsDate = $(this).attr("data-date");
			
			//Add to array
			workDays.push(workDay);
		})
		
		return workDays;
	}

	function getPostJobDto() {
		var i;
		var postJobDto = {};

		postJobDto.jobName = document.getElementsByName('name')[0].value;
		postJobDto.streetAddress = document.getElementsByName('streetAddress')[0].value;
		postJobDto.city = document.getElementsByName('city')[0].value;
		postJobDto.state = document.getElementsByName('state')[0].value;
		postJobDto.zipCode = document.getElementsByName('zipCode')[0].value;
		postJobDto.description = document.getElementsByName('description')[0].value;
//		job.stringStartDate = $("#dateRange").data('daterangepicker').startDate;
//		job.stringEndDate =  $("#dateRange").data('daterangepicker').endDate;
//		job.stringStartTime = formatTime($("#startTime").val());
//		job.stringEndTime = formatTime($("#endTime").val());
		
		
		postJobDto.workDays = getWorkDays();
		
		//set categories
		postJobDto.categoryIds = [];
		var selectedCats = $("#selectedCategories").find("button");	
		for(i = 0; i < selectedCats.length; i++){
			postJobDto.categoryIds.push($(selectedCats[i]).attr("data-cat-id"));
		}

		//Set questions
		postJobDto.selectedQuestionIds = [];
		var questions = $("#addedQuestions").find(".added-question");
		for(i = 0; i < questions.length; i++){	
			var question = questions[i];	
			
			//Check if question is enabled
			var $enableQuestion = $($(question).find(".toggle-question-activeness")[0]);
			if($enableQuestion.hasClass("enable-question")){
				postJobDto.selectedQuestionIds.push($(question).data("questionId"));
			}
		}
		
		return postJobDto;

	}