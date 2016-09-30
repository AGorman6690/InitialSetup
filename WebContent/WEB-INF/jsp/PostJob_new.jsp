<%@ include file="./includes/Header.jsp"%>

<head>
	<script src="<c:url value="/static/javascript/Utilities.js" />"></script>
	<script src="<c:url value="/static/javascript/Category.js" />"></script>
	<script src="<c:url value="/static/javascript/InputValidation.js" />"></script>
	<script src="<c:url value="/static/javascript/PostJob/Jobs.js"/>"></script>
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
	
	<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js" integrity="sha256-T0Vest3yCU7pafRw9r+settMBX6JkKN06dqBnpQ8d30="   crossorigin="anonymous"></script>
		
</head>

<body>

	<div class="container">
		<div id="cartContainer" class="section actions-not-clickable">
			<div class="header">
				<span class="header-text">Cart</span>
				<span id="" class="button-container">
				<button id="submitJobs" data-confirmed="0" type="button" class="square-button"
					 data-toggle="modal" data-target="#confirmJobSubmit">Submit Jobs</button>
				</span>				
			</div>		
			<div id="jobCart" class="sub-cart section-body">
				<div class="relative">
					<div class="header-container">
						<h4>Jobs</h4>
						<div class="action-container">
							<span id="deleteJob" class="action" data-toggle="modal" data-target="">Delete</span>
<!-- 							<span class="glyphicon glyphicon-ok"></span> -->
						</div>
						<div class="action-container">
							<span class="action requires-acknowledgement">Edit</span>
							<span class="glyphicon glyphicon-ok"></span>
						</div>
<!-- 						<div class="action-container"> -->
<!-- 							<span class="action">Copy</span> -->
<!-- <!-- 							<span class="glyphicon glyphicon-ok"></span> --> 
<!-- 						</div> -->
						<div class="action-container">
							<span class="action requires-acknowledgement">Select Questions</span>
							<span class="glyphicon glyphicon-ok"></span>
						</div>															
					</div>
				</div>
			</div>
			<div id="questionCart" class="sub-cart section-body" data-edit="0">
				<div class="relative">
					<div class="header-container">
						<h4>Questions</h4>
						<div class="action-container">
							<span id="editQuestion" class="action requires-acknowledgement">Edit</span>
							<span id="okEditQuestion" class="glyphicon glyphicon-ok"></span>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div id="postingContainer">
			<div id="jobInfo" class="section">
				<div class="header">
					<span data-toggle="jobInfoBody" class="toggle-section glyphicon glyphicon-menu-down"></span>
					<span class="header-text">Job Info</span>
					<span id="jobInfoButtons" class="button-container">
<!-- 						<button class="square-button">New</button> -->
						<button id="addJobToCart" class="square-button">Add</button>
					</span>
				</div>	
				<div id="jobInfoBody">
					<div class="section-body">
						<h4>General</h4>
						<div class="body-element-container form-group bottom-border-thinner">
							<div class="input-container">
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
							<div class="body-element-container bottom-border-thinner">
								<div id="calendar" data-is-showing-job="0">
								</div>
								<button class="btn" id="clearCalendar">Clear</button>
								
								<div id="timesContainer">
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
			<div id="questionInfo" class="section">
				<div class="header">
					<span data-toggle="questionInfoBody" class="toggle-section glyphicon glyphicon-menu-down"></span>
					<span class="header-text">Question Info</span>
					<span class="button-container">
						<button class="square-button">New</button>
						<button id="addQuestion" class="square-button">Add</button>
					</span>					
				</div>	
<!-- 				<div class="section-body"> -->
<!-- 					<div class="input-container"> -->
<!-- 						<label for="streetAddress" -->
<!-- 							class="form-control-label">Question Format</label> -->
<!-- 						<select class="question-formats form-control" title=""> -->
<!-- 						  <option selected value="-1" style="display: none"></option>	 -->
<!-- 						  <option class="answer-format-item" value="0">Yes or No</option> -->
<!-- 						  <option class="answer-format-item" value="1">Short Answer</option> -->
<!-- 						  <option class="answer-format-item" value="2">Single Answer</option> -->
<!-- 						  <option class="answer-format-item" value="3">Multiple Answer</option> -->
<!-- 						</select> -->
<!-- 					</div>				 -->
<!-- 				</div> -->
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
	<div id="confirmJobDelete" class="modal fade" role="dialog">
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
	        	data-dismiss="modal" onclick="deleteJob(1)">Yes</button>
	        <button id="cancelJobDelete" type="button" class="btn btn-default"
	        	data-dismiss="modal" onclick="deleteJob(0)">No</button>
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
		
		
		//Click event for an action that requires acknowledgement
		$("body").on("click", ".actions-clickable .action.requires-acknowledgement", function(){
			
			//Show check mark
			$($(this).siblings(".glyphicon-ok")).show();
			
			//Display the actions as "un-clickable".
			//This forces the user to click the checkmark, and not another action, to signify
			//they are finished with the action they clicked.
			//Determine whether the button is a job or question
			var subCartId = getSubCartId(this)			
			toggleActionAppearances(subCartId);
			
			
			
			//If editing a quesiton, then collapse the job info body.
			//This saves the user from having to scroll to the bottom of the page.
			if($(this).attr("id") == "editQuestion"){
				$("#jobInfoBody").hide(200);	
			}
			
			
		})
		
		$(".action-container .glyphicon-ok").click(function(){
			
			//Hide check mark
			$(this).hide();
			
			//Display the action "mock anchors" as "clickable"
// 			toggleActionAppearances();
			
			//Clear selected button.
			//By design, there can only be one selected button at any one time
			deselectButton();
			
			
		})
		
		function deselectButton(){
			$($("#cartContainer").find("button.selected")[0]).removeClass("selected");
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
		
		
		
		
// 		$("#editQuestion").click(function(){
// 			$("#questionCart").attr("data-edit", 1);
// 			$("#okEditQuestion").show();
			
// 			var buttons = $("#questionCart").find("button");
// 			addClassToArrayItems(buttons, "question-edit");
			
// 		})
		
		
		$("#cartContainer").on("click", "button", function(){
			var buttons;
			
			//Determine whether the button is a job or question
			var subCartId = getSubCartId(this)
			
			toggleActionAppearances(subCartId);
			
			if(subCartId == "jobCart"){
				showAddedJob(this); 
			}
			

			
			//If the button is selected, un-select it
			if($(this).hasClass("selected") == 1){
				$(this).removeClass("selected");
			
			//Else hightlight the clicked button
			}else{
				//Get all job and question buttons in cart
				buttons = $("#cartContainer").find("button");		
				highlightArrayItemByAttribute(this, buttons, "selected");
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
			if(validateAddQuestionInputs()){
				
				//Get the question dto
				var postQuestionDto = getPostQuestionDto();
				
				//Add question to the array
				postQuestionDtos.push(postQuestionDto);
				
				//Add question to the DOM
				addQuestionToDOM(postQuestionDto);
				
				$("#cartContainer").show(200);
			}
		
		})
		
		function addQuestionToDOM(postQuestionDto){
			var html = "<button data-question-id='" + postQuestionDto.id + "' class='btn'>";
			
			//If the qustion is longer than 20 characters, then only show the first 20.
			if(postQuestionDto.text.length > 20){
				html += postQuestionDto.text.substring(0, 19) + "..."
			}else{
				html += postQuestionDto.text;
			}
			
			html += "</button>";
						
			$("#questionCart").append(html);
				
		}
		
		function getPostQuestionDto(){
			
			questionCount += 1;
			
			var postQuestionDto = {};
			postQuestionDto.id = questionCount;
			postQuestionDto.text = $("#question").val();
			postQuestionDto.formatId = $("#questionFormat").find("option:selected").val();
		
			return postQuestionDto;
		}
		
		
		$(".toggle-section").click(function(){
			var idToToggle = $(this).attr("data-toggle");
			$("#" + idToToggle).toggle(200);
			toggleClasses($(this), "glyphicon-menu-up", "glyphicon-menu-down");
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
		
		$("#submitJobs").click(function(){
			submitJobs(confirmation)
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
		
		function resetVars(){
			workDays = [];
			firstworkDay = {};
			secondworkDay = {};
			isSecondDaySelected = 0;
			isMoreThanTwoDaysSelected = 0;	
			rangeIsSet = 0;
			rangeIsBeingSet = 0;
		}
		
		$("#clearCalendar").click(function(e){
			
			e.preventDefault();
				
			resetVars();
			
			removeActiveDaysFormatting();
			$("#times").empty();
			
			$("#timesContainer").hide(200);
			
			
		})
		
		var firstworkDay = {};
		var secondworkDay = {};
		var isSecondDaySelected = 0;
		var isMoreThanTwoDaysSelected = 0;
		var rangeIsSet = 0;
		var rangeIsBeingSet = 0;
		
		
		$("#calendar").datepicker({
			numberOfMonths: 1,
			 onSelect: function(dateText, inst) {
				 
				 	//**********************************************
				 	//**********************************************
				 	//Clean this up and make comments
				 	//**********************************************
				 	//**********************************************
				 	

				 
					 	
					 	
			            
					 	//Set the selected day
					 	var workDay = {}
					 	workDay.date = new Date(dateText);
			            workDay.date = workDay.date.getTime();
			            
			            if(workDays.length == 3){
			            	rangeIsSet = 1;
			            }
			            
			            //If the day was already selected, remove the date
	// 		            if($.inArray(workDay.date, workDays) > -1){
						if(isWorkDayAlreadyAdded(workDay.date, workDays)){
			            	
			            	workDays = removeWorkDay(workDay.date, workDays);
			            	
	// 		            	workDays = $.grep(workDays, function(value){
	// 		            		return value != workDay;
	// 		            	})
			            }
			            else{
	// 		            	workDays.push(workDay);
							addWorkDay(workDay, workDays);
			            }
			            
			            if(workDays.length == 0){
			            	resetVars();
			            }
		            	else if(workDays.length == 1){
		            		firstworkDay = workDays[0];
		            		rangeIsSet = 0
		            		isSecondDaySelected = 0;
		            		
		            	}
		            	else if(workDays.length == 2){
	
		            		secondworkDay = workDays[1];
		            		
		            		isSecondDaySelected = 1;
		            		rangeIsBeingSet = 1;
		            		
		            		if(secondworkDay.date < firstworkDay.date){
		            			resetVars();
		            		}
		            		
		            	}
			            else{
			            	rangeIsSet = 1;
			            }
			            
					 	if($("#calendar").hasClass("invalid") == 1){
					 		validateDates();	
					 	}
			            
	// 		            }else if(rangeIsSet == 0){
	// 			            if(workDays.length == 1 ){
	// 			            	firstworkDay = workDay;
	// 			            }
	// 			            else if(workDays.length == 2){
	// 			            	isSecondDaySelected = 1;
	// 			            	secondworkDay = workDay;
	// 			            	rangeIsSet = 1;
	// 			            }
	// // 			            else{
	// // 			            	isMoreThanTwoDaysSelected = 1;
	// // 			            	isSecondDaySelected = 0;
	// // 			            }	            	
	// 		            }
				 	
		        },
		        
		        // This is run for every day visible in the datepicker.
		        beforeShowDay: function (date) {

		        	
		        	if($("#calendar").attr("data-is-showing-job") == 1){
// 			        	if($.inArray(date.getTime(), workDays) > -1){
						if(isWorkDayAlreadyAdded(date.getTime(), workDays)){
			        		return [true, "active111"]; 
			        	}
			        	else{
			        		return [true, ""];
			        	}
		        	}
	        		else if(rangeIsSet == 1){
		        		
// 			        	if($.inArray(date.getTime(), workDays) > -1){
						if(isWorkDayAlreadyAdded(date.getTime(), workDays)){
			        		return [true, "active111"]; 
			        	}
			        	else{
			        		return [true, ""];
			        	}

		        	}
		        	else if(isSecondDaySelected && !rangeIsSet){
		        		
		        		
		        		if(date.getTime() >= firstworkDay.date && date.getTime() <= secondworkDay.date){
		        			
// 		        			if($.inArray(date.getTime(), workDays) == -1){
							if(!isWorkDayAlreadyAdded(date.getTime(), workDays)){
		        				var workDay = {};
		        				workDay.date = date.getTime()
// 								workDays.push(workDay);
		        				addWorkDay(workDay, workDays);
		        			}
		        			
		        			if(workDays.length == 2){
// 		        				rangeIsSet = 1;
		        			}
		        			return [true, "active111"];
		        		}
		        		else{
		        			return [true, ""];
		        		}	  
		        		
		        		
		        		
		        	}else{
// 			        	if($.inArray(date.getTime(), workDays) > -1){
						if(isWorkDayAlreadyAdded(date.getTime(), workDays)){
			        		return [true, "active111"]; 
			        	}
			        	else{
			        		return [true, ""];
			        	}
			        		
		        	}

		        }
		    });
	
		$('#allStartTimes').timepicker({
			'scrollDefault' : '7:00am'
		});
		$('#allEndTimes').timepicker({
			'scrollDefault' : '5:00pm'
		});
		
 		setPopovers();
 		setDateRange();
		setStates();
		
		//Load the seed category's sub categories
		var i;
		var seedCategoryIds = [];
		$("#categoryTree li").each(function(){
			seedCategoryIds.push($(this).attr("data-cat-id"));
		})		
		getSubCategories(seedCategoryIds);
		
		
		
		
		
	})
	
	function removeActiveDaysFormatting(){
		var activeDays = $("#calendar").find(".active111");			
		$(activeDays).each(function(){
			$(this).removeClass("active111");
		})
	}
	
	function addWorkDay(workDay, workDays){
		
		var newTimeDiv = null;
		var $insertBeforeE;
		var formattedDate = new Date();
		
		//Show the times container
// 		$("#selectAllContainer").show();
		$("#timesContainer").show(200);
		
		//Add the work day JSON object to the array
		workDays.push(workDay);
		
		//Format the date
		
		formattedDate.setTime(workDay.date);
		formattedDate = $.datepicker.formatDate("D M d", formattedDate);
		
		//Build the html
		var html = "<div class='time' data-date='" + workDay.date + "'>"
					+ "<span>" + formattedDate + "</span>"
					+ "<div class='time-container'><input class='form-control start-time' type='text'></div>"
					+ "<div class='time-container'><input class='form-control end-time' type='text'></div>"
		     		+ "</div>";
		
		//By the logic established for calendar date selection,
		//the second date must be later than the first date.
		//Therefore, the correct placement does not need to be determined for the first two dates.
		if(workDays.length < 2){
			$("#times").append(html);	
		}
		else{
			
			//Determine which time div element this new date will be place before
			$insertBeforeE = getInsertBeforeElement(workDay.date);
			
			//If the new date is in the middle of the already-added days
			if($insertBeforeE != null){
				$(html).insertBefore($insertBeforeE);
			}
			//Else the new day is the latest thus far
			else{
				$("#times").append(html);	
			}
			
		}
				
		//Set the time picker
		newTimeDiv = $("#times").find("div[data-date='" + workDay.date + "']");
		$($(newTimeDiv).find("input.start-time")[0]).timepicker({
			'scrollDefault' : '7:00am'
		});
		$($(newTimeDiv).find("input.end-time")[0]).timepicker({
			'scrollDefault' : '5:00pm'
		});
		
		
	}
	
	function getInsertBeforeElement(newDate){
		
		var $e = null;
		
		//loop through all days added
		$("#times").find(".time").each(function(){
			
			var thisDate = parseInt($(this).attr("data-date"));
			
			//If the date is larger
			if(thisDate > newDate){
				
				//Insert before this time
				$e = $(this);
				
				//break from .each loop
				return false;
			}
		})
	
		if($e != null){
			return $e;
		}else{
			//The new date is greater than all dates already added
			return null;	
		}
		
		
	
	}
	
	
	function isWorkDayAlreadyAdded(date, workDays){
		
		var arr = [];
		
		arr = $.grep(workDays, function(workDay){
			return workDay.date == date;
		})
		
		if(arr.length > 0){
			return true;
		}
		else{
			return false;
		}
		
		
	}
	
	function removeWorkDay(dateToRemove, workDays){
		
    	
    	//Delete the html for the date's set-time-functionality
    	var e = $("#times").find("div[data-date='" + dateToRemove + "']")[0];
    	$(e).remove();
				
		//Remove JSON object form array
		return workDays = $.grep(workDays, function(workDay){
			
			//Return the work day if its date does NOT equal the date to remove
    		return workDay.date != dateToRemove;
    	})		

	}


	function select(date, obj){
		
		var days = $("#" + obj.id ).find("td[data-month=" + obj.currentMonth + "] a");
		
		days.each(function(){
			if($(this).text() == obj.currentDay){
				var par = $(this).parents()[0];
				$(par).addClass("active111");
			}
		})
		
	}
	function setDateRange(){
		
		today = new Date();
		var $e = $('#dateRange'); 
		$e.daterangepicker({
			minDate: today,
			autoUpdateInput: false,
			endDate: "",
			locale : {
				format : 'MM/DD/YYYY',		
				
			}											
		});		
		
		  $('#dateRange').on('apply.daterangepicker', function(ev, picker) {
		      $(this).val(picker.startDate.format('MM/DD/YYYY') + ' - ' + picker.endDate.format('MM/DD/YYYY'));
		      
		      //This event handles all changes for the date range picker.
		      //Therefore, the validation is also here so, if necessary, invalid css sytling
		      //can be removed when the user selects a valid date range.
		      validateInputExistence($(this), $(this).val());
		  });

	}
	
	
		
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
	
	function clearPostJobInputs(){
		document.getElementsByName('name')[0].value = "";		
		document.getElementsByName('streetAddress')[0].value = "";
		document.getElementsByName('city')[0].value = "";
		document.getElementsByName('state')[0].value = "";
		document.getElementsByName('zipCode')[0].value = "";
		document.getElementsByName('description')[0].value = "";
		
		
		$("#clearCalendar").trigger("click");
		
		
		$("#selectedCategories").empty();
	}	
	
	function showAddedJob(buttonElement){
		var i;
		var jobId = $(buttonElement).attr("data-job-id");
// 		removeInvalidFormControlStyles();
// 		disableFromControls(true);
		
//		var jobId = $(e).val();		
		var job = {};
		job = getJobById(jobId, jobs);
// salert(job)
		//Store the selected jobs id in case future action is taken
//		$("#saveChanges").val(jobId);
//		$("#deleteJob").val(jobId);
		$("#activeJobId").val(jobId)

		
		//Update elements' value
		document.getElementsByName('name')[0].value = job.jobName;			
		document.getElementsByName('streetAddress')[0].value = job.streetAddress;
		document.getElementsByName('city')[0].value = job.city;
		document.getElementsByName('state')[0].value = job.state ;
		document.getElementsByName('zipCode')[0].value = job.zipCode;
		document.getElementsByName('description')[0].value = job.description;
		
		workDays = [];
		var workDay = {};
		$("#calendar").attr("data-is-showing-job", "1");
			
// 		//Show work days
		for(i = 0; i< job.workDays.length; i++){
			
			workDay.date = job.workDays[i].millisecondsDate;
			workDays.push(workDay);
			$('#calendar').datepicker("setDate", new Date(job.workDays[i].stringDate) );
	    	$('.ui-datepicker-current-day').click(); // rapresent the current selected day
// 			date = new Date;
// 			date.setTime($(this).attr("data-date"));		
// 			date = $.datepicker.formatDate("yy-mm-dd", date);
		}
		
		
		

		
// 		   $('#calendar').datepicker("setDate", new Date(2016,09,29) );
//     	$('.ui-datepicker-current-day').click(); // rapresent the current selected day

//     	$('#calendar').datepicker("setDate", new Date(2016,09,27) );
//     	$('.ui-datepicker-current-day').click(); // rapresent the current selected day
    	

		
    	$("#calendar").attr("data-is-showing-job", "0");
    	
    	
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