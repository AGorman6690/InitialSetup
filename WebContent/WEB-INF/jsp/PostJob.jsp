<%@ include file="./includes/Header.jsp"%>

<head>
	<script src="<c:url value="/static/javascript/Utilities.js" />"></script>
	<script src="<c:url value="/static/javascript/Category.js" />"></script>
	<script src="<c:url value="/static/javascript/InputValidation.js" />"></script>
<%-- 	<script src="<c:url value="/static/javascript/PostJob/Jobs.js"/>"></script> --%>
<%-- 	<script src="<c:url value="/static/javascript/PostJob/Questions.js"/>"></script> --%>
<%-- 	<script src="<c:url value="/static/javascript/PostJob/ChangeForm.js"/>"></script> --%>

<!-- 	<link rel="stylesheet" type="text/css" href="./static/css/categories.css" /> -->
<!-- 	<link rel="stylesheet" type="text/css" href="./static/css/postJob.css" /> -->
	<link rel="stylesheet" type="text/css"	href="/JobSearch/static/css/inputValidation.css" />		
	
	<script src="<c:url value="/static/javascript/TimePickerUtilities.js"/>"></script>	
	<link rel="stylesheet" type="text/css"	href="/JobSearch/static/css/calendar.css" />		
	<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/postJob_new.css" />
	
	<!-- Time picker -->
	<link rel="stylesheet" type="text/css" href="/JobSearch/static/External/jquery.timepicker.css" />
	<script	src="<c:url value="/static/External/jquery.timepicker.min.js" />"></script>
	
	<script	src="<c:url value="/static/javascript/DatePickerUtilities.js" />"></script>
	<script	src="<c:url value="/static/javascript/PostJob.js" />"></script>
	
	
	<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js" integrity="sha256-T0Vest3yCU7pafRw9r+settMBX6JkKN06dqBnpQ8d30="   crossorigin="anonymous"></script>
		
</head>

<body>

	<div class="container">
		<div id="cartContainer" class="section actions-not-clickable">
			<div class="header">
				<span class="header-text">Cart</span>
				<span id="" class="button-container">
				<button id="submitJobs" data-confirmed="0" type="button" class="clickable square-button"
					 data-toggle="modal" data-target="#confirmJobSubmit">Submit Jobs</button>
				</span>				
			</div>		
			<div id="jobCart" class="sub-cart section-body">
				<div class="relative">
					<div class="header-container">
						<h4 data-show="jobInfoBody" class="show-section">Jobs</h4>
						<div class="action-container">
							<span id="deleteJob" class="delete action" data-toggle="modal" data-target="">Delete</span>
<!-- 							<span class="glyphicon glyphicon-ok"></span> -->
						</div>
						<div class="action-container">
							<span id="editJob" class="action requires-acknowledgement">Edit</span>
							<span id="okEditJob" class="glyphicon glyphicon-ok"></span>							
						</div>
						<div class="action-container">
							<span id="copyJob" class="action">Copy</span>
							<span class="glyphicon glyphicon-ok"></span>							
						</div>
						<div class="action-container">
							<span id="selectQuestions" class="action requires-acknowledgement">Select Questions</span>
							<span id="okSelectedQuestions" class="glyphicon glyphicon-ok"></span>
						</div>															
					</div>
				</div>
			</div>
			<div id="questionCart" class="sub-cart section-body" data-edit="0">
				
				<div class="relative">
					<div class="header-container">
						<h4 data-show="questionInfoBody" class="show-section">Questions</h4>
						<div class="action-container">
							<span id="deleteQuestion" class="delete action">Delete</span>
						</div>
						<div class="action-container">
							<span id="editQuestion" class="action requires-acknowledgement">Edit</span>
							<span id="okEditQuestion" class="glyphicon glyphicon-ok"></span>
						</div>
					</div>

				</div>
				<div id="addedQuestions">
				</div>
			</div>
		</div>
		<div id="postingContainer">
			<div id="jobInfo" class="section info-container">
				<div class="header">
					<span data-toggle="jobInfoBody" class="toggle-section glyphicon glyphicon-menu-down"></span>
					<span class="header-text">Job Info</span>
					<span id="jobInfoButtons" class="button-container">
						<button id="newJob" class="clickable new square-button">New</button>
						<button id="addJobToCart" class="clickable square-button">Add</button>
						<span id="invalidAddJob" class="invalid-message">Please fill in all required fields</span>
					</span>
				</div>	
				<div id="jobInfoBody">
					<div class="section-body">
						<h4>General</h4>
						<div class="body-element-container form-group bottom-border-thinner">
							<div class="input-container">
								<div id="invalidJobName" class="invalid-message">Job names must be unique</div>
								<label for="name"
									class="form-control-label">Name</label>
								<input name="name" type="text" class="form-control"
									id="name" value=""></input>
							</div>
							<div class="input-container">
								<label for="description"
									class="form-control-label">Description</label>
								<textarea name="description" class="form-control"
									id="description" rows="3"></textarea>
							</div>
						</div>					
					</div>				
					<div class="section-body">
						<h4>Location</h4>
						<div class="body-element-container form-group bottom-border-thinner">
							<div class="input-container">
								<label for="streetAddress"
									class="form-control-label">Street Address</label>
								<input name="streetAddress" type="text" class="form-control"
									id="streetAddress" value=""></input>
							</div>
							<div class="input-container">
								<label for="city"
									class="form-control-label">City</label>
								<input name="city" type="text" class="form-control"
									id="city" value=""></input>
							</div>
							<div class="input-container">
								<label for="streetAddress"
									class="form-control-label">State</label>
								<select id="state" name="state" class="form-control"></select>	
							</div>
							<div class="input-container">
								<label for="zipCode"
									class="form-control-label">Zip Code</label>
								<input name="zipCode" type="text" class="form-control"
									id="zipCode" value=""></input>
							</div>
						</div>					
					</div>
					<div class="section-body">
						<h4>Dates and Times</h4>
							<div class="body-element-container form-group bottom-border-thinner">
								<div class="input-container">
									<label class="form-control-label">Dates</label>
									<div id="calendarContainer">
										<div id="calendar" data-is-showing-job="0">
										</div>
										<button class="btn" id="clearCalendar">Clear</button>
									</div>
								</div>							

							
								<div id="timesContainer" class="input-container">
									<label class="form-control-label">Times</label>
									<div id="timeInputsContainer">
										<div id="selectAllContainer" class="">
											<span id="expandSelectedDates" class="glyphicon glyphicon-menu-up"></span>
											<span id="setAllLabel">Set All Dates</span>
											<div class="form-group time-container">
											  <label for="allStartTimes">Start Time</label>
											  <input type="text" class="form-control" id="allStartTimes">
											</div>
											<div class="form-group time-container">
											  <label for="allEndTimes">End Time</label>
											  <input type="text" class="form-control" id="allEndTimes">
											</div>		
											<span id="applyTimesToAllDates" class="glyphicon glyphicon-ok"></span>								
		   								</div>							
										
			
										<div id="times">												
										</div>
									</div>
								</div>
							</div>					
					</div>
				
					<div class="section-body">
						<h4>Categories</h4>
				
										<div id="invalidCategoryInput-None" class="invalid-message">At least one category must be selected</div>
										<div id="invalidCategoryInput-TooMany" class="invalid-message">A maximum of five categories can be selected</div>										
	<!-- 									Eventaully render this with jstl -->
	<!-- 									//********************************************************************************* -->
	<!-- 									//********************************************************************************* -->
										<div class="job-sub-info">
											<div id="selectedCategories">
											</div>
										
										
											<ul id="categoryTree" class="list-group ">
												<li class="category-list-item list-group-item"
													data-cat-id="1" data-super-cat-id="0" data-level="0"
													data-sub-categories-set="0">
													<span style="float:left" class="category-name level-zero">Concrete</span>	
													<span style="font-size: 1em; float:left"
														class="add-category glyphicon glyphicon-plus"></span>																					 
													<span style="font-size: 1em; display: inline-block" 
														class="show-sub-categories glyphicon glyphicon-menu-down"></span>
												
												</li>
											
												<li class="category-list-item list-group-item"
												data-cat-id="3" data-super-cat-id="0" data-level="0"
												data-sub-categories-set="0">
													<span class="category-name level-zero">Construction</span>	
													<span style="font-size: 1em" 
														class="add-category glyphicon glyphicon-plus"></span>												 
													<span style="font-size: 1em" 
														class="show-sub-categories glyphicon glyphicon-menu-down"></span>
												
												</li>
												<li class="category-list-item list-group-item"
												data-cat-id="2" data-super-cat-id="0" data-level="0"
												data-sub-categories-set="0">
													<span class="category-name level-zero">Landscape</span>	
													<span style="font-size: 1em" 
														class="add-category glyphicon glyphicon-plus"></span>												 
													<span style="font-size: 1em" 
														class="show-sub-categories glyphicon glyphicon-menu-down"></span>
												
												</li>
												
												<li class="category-list-item list-group-item"
												data-cat-id="9" data-super-cat-id="0" data-level="0"
												data-sub-categories-set="0">											 
													<span class="category-name level-zero">Snow Removal</span>
													<span style="font-size: 1em" 
														class="add-category glyphicon glyphicon-plus"></span>													
													<span style="font-size: 1em" 
														class="show-sub-categories glyphicon glyphicon-menu-down"></span>
												
												</li>										
											</ul>
										</div>
	<!-- 									Eventaully render this with jstl -->
	<!-- 									//********************************************************************************* -->
	<!-- 									//********************************************************************************* -->
	
									</div>						
									
				</div>
			</div>
			<div id="questionInfo" class="section info-container">
				<div class="header">
					<span data-toggle="questionInfoBody" class="toggle-section glyphicon glyphicon-menu-down"></span>
					<span class="header-text">Question Info</span>
					<span class="button-container">
						<button id="newQuestion" class="clickable new square-button">New</button>
						<button id="addQuestion" class="clickable square-button">Add</button>
						<span id="invalidAddQuestion" class="invalid-message">Please fill in all required fields</span>
					</span>					
				</div>	
				<div id="questionInfoBody">
					<div class="section-body">
						<h4>Question Format</h4>
						<div class="body-element-container form-group bottom-border-thinner">
							<select id="questionFormat" class="question-formats form-control" title="">
							  <option selected value="-1" style="display: none"></option>	
							  <option class="answer-format-item" value="0">Yes or No</option>
							  <option class="answer-format-item" value="1">Short Answer</option>
							  <option class="answer-format-item" value="2">Single Answer</option>
							  <option class="answer-format-item" value="3">Multiple Answer</option>
							</select>
						</div>					
					</div>	
					<div class="section-body">
						<h4>Question</h4>
						<div class="body-element-container form-group bottom-border-thinner">
							<textarea id="question" class="form-control" rows="2"></textarea>
						</div>					
					</div>	
					<div id="answerListContainer" class="section-body">
						<h4>Answers</h4>
						<div class="body-element-container form-group bottom-border-thinner">
							<div id="answerList">
								<div class="answer-container">
									<span class="delete-answer glyphicon glyphicon-remove"></span>
									<input class="form-control answer-option">
								</div>
								<div class="answer-container">
									<span class="delete-answer glyphicon glyphicon-remove"></span>
									<input class="form-control answer-option">
								</div>
							</div>
							<span id="addAnswer" class="glyphicon glyphicon-plus"></span>
						</div>					
					</div>	
				</div>												
			</div>			
		</div>
	</div>
	
	
	
	
	
	
	
	
	
<div style="display: none">
	<ul>
		<li id="categoryListItemTemplate" style="display: none" 
			class="category-list-item list-group-item" 
			data-cat-id="1" data-super-cat-id="0" data-level="-1"
			data-sub-categories-set="0">
			<span class="category-name indent">Category Name</span>	
			<span style="font-size: 1em" 
				class="add-category glyphicon glyphicon-plus"></span>												 
			<span style="font-size: 1em" 
				class="show-sub-categories glyphicon glyphicon-menu-down"></span>
		
		</li>	
	</ul>
</div>

<!-- Modals -->
<!-- ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ -->
<!-- ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ -->
	<div id="confirmJobDeleteModal" class="modal fade" role="dialog">
	  <div class="modal-dialog modal-sm">
	
	    <!-- Modal content-->
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal">&times;</button>
	        <h4 class="modal-title">Confirmation</h4>
	      </div>
	      <div class="modal-body">
	        <p>Continue to delete job?</p>
	        
	        <div class="checkbox" style="margin: 10px 0px 0px 15px">
	        	<label>
	        		<input id="disableJobDeleteAlert" data-confirmed="0" type="checkbox"> Disable alert
	        	</label>
	        </div>	        
	      </div>
	      <div class="modal-footer">
	        <button id="confirmJobDelete" type="button" class="btn btn-default" 
	        	data-dismiss="modal">Yes</button>
	        <button id="cancelJobDelete" type="button" class="btn btn-default"
	        	data-dismiss="modal">No</button>
	      </div>
	    </div>
	
	  </div>
	</div>	

	<div id="confirmJobSubmit" class="modal fade" role="dialog">
	  <div class="modal-dialog modal-sm">
	
	    <!-- Modal content-->
	    <div class="modal-content">
	      <div class="modal-header">
	        <button type="button" class="close" data-dismiss="modal">&times;</button>
	        <h4 class="modal-title">Confirmation</h4>
	      </div>
	      <div class="modal-body">
	        <p>Complete job posting and submit jobs?</p>
	      </div>
	      <div class="modal-footer">
	        <button id="confirmJobSubmit" type="button" class="btn btn-default" 
	        	data-dismiss="modal" onclick="submitJobs(1)">Yes</button>
	        <button id="cancelJobSubmit" type="button" class="btn btn-default"
	        	data-dismiss="modal" onclick="submitJobs(0)">No</button>
	      </div>
	    </div>
	
	  </div>
	</div>	

<!-- ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ -->
<!-- ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^ -->



</body>


<script>

	var pageContext = "postJob";
	var jobs = [];	
	var jobCount = 0;
	var postQuestionDtos = [];
	var questionCount = 0;
// 	var questionContainerIdPrefix = 'question-';
	var workDays = [];
		

	$(document).ready(function() {
		
		
		$("#confirmJobDelete").click(function(){
			var d = this;
			deleteJob();
			
			//If necessary, disable the "delete job" alert
			if($("#disableJobDeleteAlert").is(":checked")){
				
				//Set the element's attributes such that the alert/modal will not display
				var $e = $("#deleteJob");
				$e.attr("data-disable-alert", 1);
				$e.attr("data-target", "");
				
				//Attach the delete job function directly to the "delete" action
				$e.click(deleteJob);
			}
			
			
		})
		
		$("#deleteQuestion").click(function(){
			deleteQuestion();
		})
		
		$("#copyJob").click(function(){
			copyJob();
		})
		
		$("#submitJobs").click(function(){
			submitJobs(1);
		})
		
		
		
		$(".button-container .new").click(function(){
			
			if(isButtonClickable($(this))){
				//Get the info container's id
				var infoContainerId = $($(this).parents(".info-container")[0]).attr("id");
		
				//Format input elements.
				//Expand the info body. 
				disableInputFields(false, infoContainerId);			
				if(infoContainerId == "jobInfo"){
					clearPostJobInputs();	
					expandInfoBody("jobInfoBody", true);
					setButtonAsClickable(true, $("#addJobToCart"));
				}
				else if(infoContainerId == "questionInfo"){
					clearPostQuestionInputs();
					expandInfoBody("questionInfoBody", true);
					setButtonAsClickable(true, $("#addQuestion"));
				}
				
				//Deselect the cart container's buttons
				deselectButtons("cartContainer");
			
			}

		})
		
		
		//Click event for an action that requires acknowledgement
		$("body").on("click", ".actions-clickable .action.requires-acknowledgement", function(){
			
			var clickedId = $(this).attr("id");
			
			//Show check mark
			$($(this).siblings(".glyphicon-ok")).show();
			
			//Display the actions as "un-clickable".
			//This forces the user to click the checkmark, and not another action, to signify
			//they are finished with the action they clicked.
			//Determine whether the button is a job or question
			var subCartId = getSubCartId(this)			
			toggleActionAppearances(subCartId);
			
			//Set all job and question buttons as un-clickable
			setButtonsAsClickable(false, "jobCart");
			setButtonsAsClickable(false, "postingContainer");
			
// 			disableInputFields(false, "postingContainer");
			
			//If editing a quesiton, then collapse the job info body.
			//This saves the user from having to scroll to the bottom of the page.
			if(clickedId == "editQuestion"){
				expandInfoBody("jobInfoBody", false);
				expandInfoBody("questionInfoBody", true);
				disableInputFields(false, "questionInfoBody");
				disableInputFields(true, "jobInfoBody");
			}
			else if(clickedId == "editJob"){
				expandInfoBody("jobInfoBody", true);
				expandInfoBody("questionInfoBody", false);
				disableInputFields(true, "questionInfoBody");
				disableInputFields(false, "jobInfoBody");
			}
			else if(clickedId == "selectQuestions"){
				addBorderToQuestionCart(true);
			}
			
			
		})

		
		$(".action-container .glyphicon-ok").click(function(){
			
			var clickedId = $(this).attr("id");
			var editedQuestion = {};
			var selectedQuestion = {};
			var selectedJob = {};
			var editedJob = {};
			
			//Hide check mark
			$(this).hide();
			
			//Display the action "mock anchors" as "clickable"
// 			toggleActionAppearances();
			
			
			
			if(clickedId == "okSelectedQuestions"){
				setSelectedQuestionIdsForJob();

// 				setActionsAsClickable(false, "questionCart");
// 				deselectButtons("cartContainer");
				setActionsAsClickable(true, "jobCart");
				
				addBorderToQuestionCart(false);
				
				//All the user to start a new job, but not add
				setButtonAsClickable(true, $("#newJob"));
				
				//All the user to start a new qeustion and add
				setButtonsAsClickable(true, "questionInfo");
			}
			else if(clickedId == "okEditQuestion"){
				

				
				if(validateAddQuestionInputs()){
					
					//Format elements
					setActionsAsClickable(true, "questionCart");
					disableInputFields(true, "questionInfoBody");
					
					selectedQuestion = getSelectedQuestion();
					editedQuestion = getPostQuestionDto();
					
					//When editing a question, the id must remain the same
					editedQuestion.id = selectedQuestion.id;
					
					//Remove the selected question
					postQuestionDtos = removeArrayElement(selectedQuestion.id, postQuestionDtos);
					
					//Add the edited question
					postQuestionDtos.push(editedQuestion);
					

					setActionsAsClickable(true, "questionCart");
					setButtonAsClickable(true, $("#newQuestion"));
				}
				
			}
			else if(clickedId == "okEditJob"){
				
// 				if(validatePostJobInputs(jobs)){
					
					//Format elements
					setActionsAsClickable(true, "jobCart");
					disableInputFields(true, "jobInfoBody");
					
					selectedJob = getSelectedJob();
					editedJob = getPostJobDto();
					
					//When editing a job, the id must remain the same
					editedJob.id = selectedJob.id;					
					
					//Remove the selected job
					jobs = removeArrayElement(selectedJob.id, jobs);
					
					//Add the edited job
					jobs.push(editedJob);
					
					setActionsAsClickable(true, "jobCart");
					setButtonAsClickable(true, $("#newJob"));
					
// 				}
			}
				

			setButtonsAsClickable(true, "jobCart");
			setButtonsAsClickable(true, "questionCart");


			
			//Clear selected button.
			//By design, there can only be one selected button at any one time
// 			deselectButton();
			
			
		})
		

		
		$("#cartContainer").on("click", "button", function(){
			var buttons;
			
			//If the clicked button's current state is clickable
			if(isButtonClickable($(this))){
				//Determine whether the clicked button is a job or question
// 				var subCartId = getSubCartId(this)
				
// 				toggleActionAppearances(subCartId);
				
				
				//If the user is selecting questions for a job
				if(isSelectingQuestions()){
					
					//If a question button was clicked
					if(jobIsClicked(this) == false){
						toggleClass($(this), "tied-to-job");
					}
				}
				//If a job was clicked
				else if(jobIsClicked(this)) {
					
					if(buttonIsCurrentlySelected(this)){
						deselectJob();	
					}else{
						selectJob(this);
						
					}
					
				}
				//Else a question was clicked
				else{
					
					
					if(buttonIsCurrentlySelected(this)){
						deselectQuestion();	
					}else{
						selectQuestion(this);
					}	
				}
				
// 				//Else If the button is selected, un-select it
// 				else if($(this).hasClass("selected") == 1){
// 					$(this).removeClass("selected");
				
// 				//Else hightlight the clicked button
// 				}else{
					
// 					//Get all job and question buttons in cart
// 					buttons = $("#cartContainer").find("button");		
// 					highlightArrayItemByAttribute(this, buttons, "selected");
// 				}
				
// 				//If a job button was clicked, then show the job
// 				if(subCartId == "jobCart"){
// 					showAddedJob(this); 
// 				}
			
			}
		})
		
// 		$("#questionCart").on("click", "button", function(){
			
// 			//If the button is clickable
// 			if($(this).hasClass("not-clickable") == 0){
// // 			if($("#questionCart").attr("data-edit") == 1){
// 				var buttons = $("#questionCart").find("button");			
// 				highlightGroupItemById(this, buttons, "question-selected", "data-question-id");	
// 			}
			
			
// 		})	

		function getSubCartId(childElement){
			var subCart = $(childElement).parents(".sub-cart")[0];
			return $(subCart).attr("id");
	
		}
	
		
		$("#addQuestion").click(function(){
			
			//Validate inputs
			if(isButtonClickable($(this))){
				if(validateAddQuestionInputs()){
					
					//Get the question dto
					var postQuestionDto = getPostQuestionDto();
					
					//Set its id
					questionCount += 1;				
					postQuestionDto.id = questionCount;
					
					//Add question to the array
					postQuestionDtos.push(postQuestionDto);
					
					//Add question to the DOM
					addQuestionToDOM(postQuestionDto);
					
					$("#cartContainer").show(200);
				}
			}
		})
		
		function addQuestionToDOM(postQuestionDto){
			var html = "<button data-question-id='" + postQuestionDto.id + "' class='btn clickable'>";
			
			//If the qustion is longer than 20 characters, then only show the first 20.
			if(postQuestionDto.text.length > 20){
				html += postQuestionDto.text.substring(0, 19) + "..."
			}else{
				html += postQuestionDto.text;
			}
			
			html += "</button>";
						
			$("#addedQuestions").append(html);
			
			clearPostQuestionInputs();
				
		}
		
		
		$(".toggle-section").click(function(){
			var idToToggle = $(this).attr("data-toggle");
			
			//If the secion is currenlyt visible
			if($("#" + idToToggle).is(":visible")){
				//Collapse
				expandInfoBody(idToToggle, false);
			}
			else{
				//Expand
				expandInfoBody(idToToggle, true);
			}

		})
		
		$(".show-section").click(function(){
			var idToToggle = $(this).attr("data-show");
			
			if(idToToggle == "questionInfoBody"){				
				expandInfoBody("jobInfoBody", false);
				expandInfoBody("questionInfoBody", true);
			}
			else if(idToToggle == "jobInfoBody"){
				expandInfoBody("jobInfoBody", true);
			}

		})		
		
		
		$("#questionFormat").click(function(){
		
			var selectedOption = $(this).find("option:selected")[0];
			var value = $(selectedOption).val(); 
			if(value == 2 || value == 3){
				$("#answerListContainer").show(200);				
			}else{
				$("#answerListContainer").hide(200);
			}
			
		})
		
		$("#addAnswer").click(function(){ 
			
			
			var answerContainer = $("#answerList").find(".answer-container")[0];			
			var clone = $(answerContainer).clone(true);			
			
			//Clear the input
			$(clone).find("input").val("");
			
			$("#answerList").append(clone);
		
		})
		
		$(".delete-answer").click(function(){
	
			//There must be at least 2 answers.
			//If there are not at least 2 answers, then the user should not be using this question format.
			var answers = $("#answerList").find(".answer-container");
			if(answers.length > 2){
				$(this).parent().remove();
			}
		})
			
		$("#addJobToCart").click(function(){
			addJobToCart();
			
		
		})

				
		
		$("#expandSelectedDates").click(function(){
			$("#times").toggle(200);
			toggleClasses($(this), "glyphicon-menu-up", "glyphicon-menu-down");
		})
		
		$("#applyTimesToAllDates").click(function(){
			setEndTimes($("#allEndTimes").val(), workDays);
			setStartTimes($("#allStartTimes").val(), workDays);
			showTimes();
		})	
		
		function setEndTimes(endTime, workDays){
			
			$(workDays).each(function(){				
				this.endTime = endTime;
			})
			
		}
		
		function setStartTimes(startTime, workDays){
			
			$(workDays).each(function(){				
				this.startTime = startTime;
			})
			
		}
		
		function showTimes(){
			
			var containerDiv;
			var startTimeInput;
			var endTimeInput;
			
			$(workDays).each(function(){				
				
				containerDiv = $("#times").find("div[data-date='" + this.date + "']")[0];
				startTimeInput = $(containerDiv).find("input.start-time")[0];
				endTimeInput = $(containerDiv).find("input.end-time")[0];
				
				$(startTimeInput).val(this.startTime);
				$(endTimeInput).val(this.endTime);
			})			
			
		}
		

		$('#allStartTimes').timepicker({
			'scrollDefault' : '7:00am'
		});
		$('#allEndTimes').timepicker({
			'scrollDefault' : '5:00pm'
		});
		
 		setPopovers();
 		
		setStates();
		
		//Load the seed category's sub categories
		var i;
		var seedCategoryIds = [];
		$("#categoryTree li").each(function(){
			seedCategoryIds.push($(this).attr("data-cat-id"));
		})		
		getSubCategories(seedCategoryIds);
		
		
		
		
		
	})
	
	
	
		
	function setPopovers(){

		$('.popover1').popover({
			trigger: "hover",
			delay: {
				show: "250",		
			}
		});
		
		$('.popoverInstant').popover({
			trigger: "hover",
			delay: {
				show: "0",		
			}
		});	
		
	}
	

	
	function showAddedJob(buttonElement){
		
		var questionButton;
		var i;
		var jobId = $(buttonElement).attr("data-job-id");
// 		removeInvalidFormControlStyles();
// 		disableFromControls(true);

		var job = getSelectedJob();
		
		//Show the job details
		document.getElementsByName('name')[0].value = job.jobName;			
		document.getElementsByName('streetAddress')[0].value = job.streetAddress;
		document.getElementsByName('city')[0].value = job.city;
		document.getElementsByName('state')[0].value = job.state ;
		document.getElementsByName('zipCode')[0].value = job.zipCode;
		document.getElementsByName('description')[0].value = job.description;
		

			
// 		//Show work days
		workDays = [];
		var workDay = {};
		$("#calendar").attr("data-is-showing-job", "1");
		for(i = 0; i< job.workDays.length; i++){
			
			workDay.date = job.workDays[i].millisecondsDate;
			workDays.push(workDay);
			$('#calendar').datepicker("setDate", new Date(job.workDays[i].stringDate) );
	    	$('.ui-datepicker-current-day').click(); // rapresent the current selected day

		}
		$("#calendar").attr("data-is-showing-job", "0");

		//Show categories
		for(i = 0; i < job.categoryIds.length; i++){
			var id = job.categoryIds[i];
			var name = $($($("#categoryTree").find("[data-cat-id=" + id + "]")[0])
							.find(".category-name")[0]).text();
			showCategory(id, name);
		}

		//Hightlight the job's selected questions
		deselectButtons("questionContainer");
		$.each(job.selectedQuestionIds, function(){
			questionButton = $("#questionCart").find("button[data-question-id='" + this + "']")[0];
			showQuestionForSelectedJob(questionButton);
		})
		
		
		
		
	}	
	function getPostQuestionDto(){
		
		var postQuestionDto = {};
		var answerOptionsInputs = []
		var answerOptions = [];
		
		
		postQuestionDto.text = $("#question").val();
		postQuestionDto.formatId = $("#questionFormat").find("option:selected").val();
	
		//If necessary, set the answer options
		if(doesQuestionHaveAnAnswerList(postQuestionDto)){
			answerInputs = $("#answerList").find(".answer-container input");
			$.each(answerInputs, function(){
				answerOptions.push($(this).val());
			})
			
			postQuestionDto.answerOptions = answerOptions;
		
		}else{
			postQuestionDto.answerOptions = [];
		}
		
		return postQuestionDto;
	}
	

	

	function setStates(){
		var $e = $("#state"); 
		$e.append('<option value="" selected style="display: none"></option>');
		$e.append('<option value="AL">AL</option>');
		$e.append('<option value="AK">AK</option>');
		$e.append('<option value="AZ">AZ</option>');
		$e.append('<option value="AR">AR</option>');
		$e.append('<option value="CA">CA</option>');
		$e.append('<option value="CO">CO</option>');
		$e.append('<option value="CT">CT</option>');
		$e.append('<option value="DE">DE</option>');
		$e.append('<option value="DC">DC</option>');
		$e.append('<option value="FL">FL</option>');
		$e.append('<option value="GA">GA</option>');
		$e.append('<option value="HI">HI</option>');
		$e.append('<option value="ID">ID</option>');
		$e.append('<option value="IL">IL</option>');
		$e.append('<option value="IN">IN</option>');
		$e.append('<option value="IA">IA</option>');
		$e.append('<option value="KS">KS</option>');
		$e.append('<option value="KY">KY</option>');
		$e.append('<option value="LA">LA</option>');
		$e.append('<option value="ME">ME</option>');
		$e.append('<option value="MD">MD</option>');
		$e.append('<option value="MA">MA</option>');
		$e.append('<option value="MI">MI</option>');
		$e.append('<option value="MN">MN</option>');
		$e.append('<option value="MS">MS</option>');
		$e.append('<option value="MO">MO</option>');
		$e.append('<option value="MT">MT</option>');
		$e.append('<option value="NE">NE</option>');
		$e.append('<option value="NV">NV</option>');
		$e.append('<option value="NH">NH</option>');
		$e.append('<option value="NJ">NJ</option>');
		$e.append('<option value="NM">NM</option>');
		$e.append('<option value="NY">NY</option>');
		$e.append('<option value="NC">NC</option>');
		$e.append('<option value="ND">ND</option>');
		$e.append('<option value="OH">OH</option>');
		$e.append('<option value="OK">OK</option>');
		$e.append('<option value="OR">OR</option>');
		$e.append('<option value="PA">PA</option>');
		$e.append('<option value="RI">RI</option>');
		$e.append('<option value="SC">SC</option>');
		$e.append('<option value="SD">SD</option>');
		$e.append('<option value="TN">TN</option>');
		$e.append('<option value="TX">TX</option>');
		$e.append('<option value="UT">UT</option>');
		$e.append('<option value="VT">VT</option>');
		$e.append('<option value="VA">VA</option>');
		$e.append('<option value="WA">WA</option>');
		$e.append('<option value="WV">WV</option>');
		$e.append('<option value="WI">WI</option>');
		$e.append('<option value="WY">WY</option>');
		
		
	}
</script>


<%@ include file="./includes/Footer.jsp"%>