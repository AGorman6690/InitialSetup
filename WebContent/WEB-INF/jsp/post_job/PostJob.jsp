<%@ include file="../includes/Header.jsp"%>
<%-- <%@ include file="../includes/resources/DatePicker.jsp" %> --%>

<%@ include file="../includes/resources/SelectPageSection.jsp" %>

<link rel="stylesheet" type="text/css"	href="/JobSearch/static/css/inputValidation.css" />				
<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/table.css" />
<link rel="stylesheet" type="text/css"	href="/JobSearch/static/css/post_job/post_job_new.css" />
<link rel="stylesheet" type="text/css"	href="/JobSearch/static/css/post_job/post_job_work_days_and_times.css" />
<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/post_job/questions.css" />			

<script src="<c:url value="/static/javascript/InputValidation.js" />"></script>
<script	src="<c:url value="/static/javascript/post_job/Questions.js" />"></script>
<script	src="<c:url value="/static/javascript/post_job/PostJob.js" />"></script>
<script	src="<c:url value="/static/javascript/post_job/PostJob_WorkDaysAndTimes.js" />"></script>
<script	src="<c:url value="/static/javascript/post_job/SubmitValidation.js" />"></script>
<script	src="<c:url value="/static/javascript/Utilities/FormUtilities.js" />"></script>


<c:choose>
	<c:when test="${sessionScope.jobs_needRating.size() > 0 }">
		<div class="center pad-top-2">
			<p>You have ${sessionScope.jobs_needRating.size() == 1 ? 'a job that requires' : 'completed jobs that require' } your rating.</p>
			<p>Please rate your past employees before posting another job.</p>
		</div>
	</c:when>
	<c:otherwise>
		<c:if test="${!empty postedJobs }">
			<div id="copy-or-new-container">
				<div id="copy-previous-post-container">
					<button id="copy-previous-post" class="sqr-btn"
						 data-toggle-id="previous-job-posts">Begin from a previous job posting</button>
					<div id="previous-job-posts" class="dropdown-style">
						<c:forEach items="${postedJobs }" var="job">
							<div data-posted-job-id="${job.id }">${job.jobName }</div>
						</c:forEach>
					</div>					 
				</div>
				<button id="startNewJob" class="sqr-btn">Start a new job posting</button>		
			</div>			
		</c:if>	
		
		<div id="postSections" class="select-page-section-container ${!empty postedJobs ? 'hide-on-load' : '' }">
			<span id="show-general" class="select-page-section selected" data-page-section-id="generalContainer">General</span>
			<span id="show-dates-section" class="select-page-section" data-page-section-id="datesContainer">Work Days</span>
			<span id="select-times" class="select-page-section" data-page-section-id="timesContainer">Times</span>
			<span id="show-positions" class="select-page-section" data-page-section-id="positionsContainer">Positions</span>
			<span id="show-location" class="select-page-section" data-page-section-id="locationContainer">Location</span>
<!-- 			<span class="select-page-section" data-page-section-id="categoriesContainer">Categories</span> -->
			<span id="show-questions" class="select-page-section" data-page-section-id="questionsContainer">Questions</span>
			<span id="show-skills" class="select-page-section" data-page-section-id="employeeSkillsContainer">Employee Skills</span>							
		</div>
		
		<div class=" ${!empty postedJobs ? 'hide-on-load' : '' }">
		
		
			<div id="preview-job-posting-container">
				<div id="edit-or-submit-container">
					<button id="editPosting" class="sqr-btn">Edit Job Posting</button>
					<button id="submitPosting_final" class="sqr-btn">Submit Job Posting</button>
				</div>	
				<div id="displayExample_jobInfo">
				
				</div>
			</div>
			<div id="post-job-container">
				<div id="previous-next-container">
					<button id="proceed-to-preview-job-posting" class="sqr-btn">Review Job Posting</button>
					<div class="error-message-container">
						<p id="invalid-address-error-message" class="error-message">Invalid address</p>
					</div>
		<!-- 			<span id="previous-section">Previous</span> -->
		<!-- 			<span id="next-section">Next</span> -->
				</div>
			
				<div id="generalContainer" class="page-section">
<!-- 					<div class="item"> -->
<!-- 						<p>Employment Type</p> -->
<!-- 						<div class="radio-container"> -->
<!-- 							<div class="radio"> -->
<!-- 							  <label><input value="0" id="employmentType_employee" type="radio" name="employmentType">Employee</label> -->
<!-- 							</div> -->
<!-- 							<div class="radio"> -->
<!-- 							  <label><input value="1" id="employmentType_contractor" type="radio" name="employmentType">Contractor</label> -->
<!-- 							</div> -->
<!-- 						</div> -->
<!-- 					</div> -->
					<div class="item">
						<p>Job Name</p>
						<input id="name" name="name" type="text" class=""	value=""></input>						
					</div>
					<div class="item">
						<p>Description</p>
						<textarea id="description" name="description" class="" rows="6"></textarea>							
					</div>
				</div>
				
				<div id="datesContainer" class="page-section">
		
					<div class="row">
						<div class="item">
							<p>Can applicants apply for particular work days?</p>
<!-- 							<div class=" button-group"> -->
							<div class="radio-container">
								<label data-show-id-on-click="work-days-calendar"><input id="yes-partial" type="radio" name="partial-availability">
								Yes, they can apply for one or more work days</label>
								<label data-show-id-on-click="work-days-calendar"><input id="no-partial" type="radio" name="partial-availability">
								No, they have to apply for all work days</label>
<!-- 								<button id="yes-partial" class="sqr-btn gray-2" data-show-id-on-click="work-days-calendar"> -->
<!-- 									Yes, they can apply for one or more work days</button> -->
<!-- 								<button id="no-partial" class="sqr-btn gray-2" data-show-id-on-click="work-days-calendar"> -->
<!-- 									No, they have to apply for all work days</button> -->
							</div>
						</div>					
						<div id="work-days-calendar" class="item calendar-container teal-navigation v2 post-job">
							<p>Work Days</p>
							<div class="pad-top">
								<button class="" id="clearCalendar">Clear</button>
							</div>
							<div id="workDaysCalendar_postJob" class="calendar" data-is-showing-job="0">
							</div>											
							
						</div>
					</div>			
				</div>		
				
				<div id="timesContainer" class="page-section">
				
					<p id="no-dates-selected" class="linky-hover pad-top">Please select one or more work days</p>
					<div id="initial-time-question" class="item pad-top">
						<p>Is each work day's start time and end time the same?</p>
						<div class="radio-container">
							<label data-show-id-on-click="set-one-start-and-end-time"
								data-hide-id-on-click="times-cont"><input id="same-times" type="radio" name="same-times">
							Yes, each work day starts and ends at the same time</label>
							<label data-show-id-on-click="times-cont"
								data-hide-id-on-click="set-one-start-and-end-time"><input id="different-times" type="radio" name="same-times">
							No, at least work one day starts or ends at a different time</label>
						</div>
					</div>							
					<div id="set-one-start-and-end-time" class="pad-top">
						<div>
							<p>Start Time</p>
							<select id="single-start-time" class="time start-time"></select>
						</div>
						<div>
							<p>End Time</p>
							<select id="single-end-time" class="time end-time"></select>
						</div>
							
					</div>
					<div id="times-cont">
						
						<div class="radio-container pad-top">
							<label><input id="select-all-dates" type="radio" name="set-times">Select all dates</label>
							<label><input id="deselect-all-dates" type="radio" name="set-times">Deselect all dates</label>
						</div>
						<div id="multiple-time-cont">
							<div>
								<p>Start Time</p>
								<select id="multiple-start-times" class="time start-time"></select>
							</div>
							<div>
								<p>End Time</p>
								<select id="multiple-end-times" class="time end-time"></select>
							</div>
							<div>
								<button id="apply-multiple-times" class="sqr-btn gray-3">Apply</button>
							</div>					
						</div>				
						<div class="item calendar-container teal-navigation v2 post-job hide-unused-rows">
							<div id="select-times-cal" class="calendar">
							</div>											
						</div>	
			
					</div>
				</div>	
				<div id="positionsContainer" class="page-section">
					<div class="item">
						<p>Positions</p>
					</div>		
					<div>
						<p>How many positions are you filling per day?</p>
						<input id="positions-per-day" type="text" class="positive-number" value="">
					</div>								
				</div>			
							
				<div id="locationContainer" class="page-section">
					<div class="item">
						<p>Street Address</p>						
						<input id="street" type="text" class=""	value=""></input>						
					</div>
					<div class="item">
						<p>City</p>					
						<input id="city" type="text" class=""	value=""></input>						
					</div>
					<div class="item">
						<p>State</p>						
						<select id="state" class=""	></select>						
					</div>	
					<div class="item">
						<p>Zip Code</p>						
						<input id="zipCode" type="text" value=""></input>						
					</div>						
				</div>	
								
<!-- 				<div id="categoriesContainer" class="page-section"> -->
<!-- 					<div class="header row"> -->
<!-- 						<p>Categories</p> -->
<!-- 					</div>		 -->
<!-- 					<div class="row"> -->
		
<!-- 					</div>								 -->
<!-- 				</div>	 -->
				
				<div id="questionsContainer" class="page-section">
					<div id="addedQuestionsContainer" class="item">			
						<p>Questions</p>
						<div class="question-actions-container">
							<button id="deleteQuestion" class="btn-sqr">Delete</button>
							<button id="editQuestion" class="btn-sqr">Edit</button>	
							<span id="editQuestionResponses">
								<span id="saveEditQuestionChanges" class="glyphicon glyphicon-ok"></span>
								<span id="cancelEditQuestionChanges" class="glyphicon glyphicon-remove"></span>
							</span>
					</div>				
					<div id="addedQuestions"></div>
				</div>		
				<div id="copy-or-new-question" class="item">
					<c:if test="${postedQuestions.size() > 0 }">
						<div id="copy-question-container">
							<button id="copy-previous-question" class="sqr-btn" data-toggle-id="postedQuestions">
								Begin with a previous question</button>
								<div id="postedQuestions" class="dropdown-style">
									<c:forEach items="${postedQuestions }" var="question">
										<div data-question-id="${question.questionId }">${question.text }</div>
									</c:forEach>
								</div>					
							</div>
						</c:if>
						<button id="create-new-question" class="sqr-btn">Create a new question</button>						
					</div>
				
		
					<div id="create-question-container" class="item">

						<div class="">
							<p>Question Format</p>						
							<select id="questionFormat" class="question-formats">
							  <option class="answer-format-item" data-format-id="0">Yes or No</option>
<!-- 							  <option class="answer-format-item" data-format-id="1">Short Answer</option> -->
							  <option class="answer-format-item" data-format-id="2">Select one answer</option>
							  <option class="answer-format-item" data-format-id="3">Select one or more answers</option>
							</select>					
						</div>	
						
						<div class="item">
							<p>Question</p>						
							<textarea id="question" class="" rows="3"></textarea>				
						</div>		
						
						<div class="item" id="answerListContainer">
								<p>Answers</p>						
								<div id="answerList">
									<div class="list-item answer-container">
										<span class="delete-list-item delete-answer glyphicon glyphicon-remove"></span>
										<input type="text" class="answer-option">
									</div>
									<div class="list-item answer-container">
										<span class="delete-list-item delete-answer glyphicon glyphicon-remove"></span>
										<input type="text" class="answer-option">
									</div>
								</div>
								<span id="addAnswer" class="add-list-item glyphicon glyphicon-plus"></span>			
						</div>
						<div id="questionActions" class="question-actions-container pad-top">
							<button id="addQuestion" class="clickable btn-sqr">Add</button>
							<div id="invalidAddQuestion" class="invalid-message">Please fill in all required fields</div>
						</div>																	
						
					</div>
				</div>
				
				<div id="employeeSkillsContainer" class="page-section">
					<div class="item">
							<p>Required Employee Skills (optional)</p>					
							<div id="requiredSkillsContainer" class="list-items-container skills-container">
								<div class="list-item">
									<span class="delete-list-item glyphicon glyphicon-remove"></span>
									<span class="">
										<input type="text" >
									</span>
								</div>							
							</div>		
							<span class="add-list-item glyphicon glyphicon-plus"></span>								
					</div>		
					<div class="item">
						<p>Desired Employee Skills (optional)</p>						
						<div id="desiredSkillsContainer" class="list-items-container skills-container">
							<div class="list-item">
								<span class="delete-list-item glyphicon glyphicon-remove"></span>
								<span class="answer-option-container">
									<input type="text">
								</span>
							</div>								
						</div>		
						<span class="add-list-item glyphicon glyphicon-plus"></span>								
					</div>											
				</div>
				<div id="prev-or-next" class="pad-top-2 center button-container">
					<span id="previous-section" class="sqr-btnz greenz">Previous</span>
					<span id="next-section" class="sqr-btnz greenz">Next</span>
				</div>
			</div>
		</div>	
	</c:otherwise>
</c:choose>



<script async defer src="https://maps.googleapis.com/maps/api/js?key=AIzaSyAXc_OBQbJCEfhCkBju2_5IfjPqOYRKacI&amp">
</script>

<%@ include file="../includes/Footer.jsp"%>
<%@ include file="../includes/resources/JobInformation.jsp" %>