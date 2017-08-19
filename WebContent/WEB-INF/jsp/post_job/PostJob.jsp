<%@ include file="../includes/Header.jsp"%>
<%@ include file="../includes/resources/StarRatings.jsp"%>

<link rel="stylesheet" type="text/css"
	href="/JobSearch/static/css/post_job/post_job.css" />

<script src="<c:url value="/static/javascript/post_job/Questions.js" />"></script>
<script src="<c:url value="/static/javascript/post_job/PostJob.js" />"></script>
<script
	src="<c:url value="/static/javascript/post_job/SubmitValidation.js" />"></script>
<script
	src="<c:url value="/static/javascript/Utilities/FormUtilities.js" />"></script>


<c:choose>
	<c:when test="${sessionScope.jobs_needRating.size() > 0 }">
		<div class="center pad-top-2">
			<p>You have ${sessionScope.jobs_needRating.size() == 1 ? 'a job that requires' : 'completed jobs that require' }
				your rating.</p>
			<p>Please rate your past employees before posting another job.</p>
		</div>
	</c:when>
	<c:otherwise>
	
<%-- 
		<c:if test="${!empty postedJobs }">
			<div id="copy-or-new-container">
				<div id="copy-previous-post-container">
					<button id="copy-previous-post" class="sqr-btn"
						data-toggle-id="previous-job-posts">Begin from a previous
						job posting</button>
					<div id="previous-job-posts" class="dropdown-style">
						<c:forEach items="${postedJobs }" var="job">
							<div data-posted-job-id="${job.id }">${job.jobName }</div>
						</c:forEach>
					</div>
				</div>
				<button id="startNewJob" class="sqr-btn">Start a new job
					posting</button>
			</div>
		</c:if>
--%>
<div id="page-wrapper">
		<div id="side-bar">
			
			<div id="show-general" class="incomplete"><span>General</span></div>
			<div id="show-work-days" class="incomplete"><span>Work Days</span></div>
			<div id="show-location" class="incomplete"><span>Location</span></div>
			<div id="show-questions" class="optional incomplete"><span>Questions</span></div>
			<div id="show-skills" class="optional incomplete"><span>Skills</span></div>
			<p id="proceed-to-preview-job-posting" class="pointer">Review then submit</p>	
			<div class="error-message-container">
				<p id="invalid-address-error-message" class="error-message">Invalid
					address</p>
			</div>				
		</div>

		<div id="post-job-info" class=" ${!empty postedJobs ? 'hide-on-load-d' : '' }">
				


				<div id="general-wrapper" class="section">
					<h3>General</h3>
					<div class="item-wrapper">
						<div class="item">
							<label>Job Name</label>
							<input id="name" name="name" type="text" class="" value=""></input>
						</div>
						<div class="item">
							<label>Job Description</label>
							<textarea id="description" name="description" class="" rows="6"></textarea>
						</div>
						<div class="item">
							<label>How many positions are you filling per day?</label>
							<input id="positions-per-day" type="text" class="positive-number"
								value="">
						</div>					
					</div>
				</div>

				<div id="dates-wrapper" class="section">
					<h3>Work Days</h3>					
					<div class="item-wrapper">
						<button class="" id="clear-calendar">Clear</button>
						<div id="work-days-calendar" class="item calendar-container post-job">							
							<div id="workDaysCalendar_postJob" class="calendar v2"
								data-is-showing-job="0"></div>
						</div>
						<div id="is-partial-availability-allowed-question" class="item show-with-multiple-work-days">
							<p class="question-to-job-poster">This job has more than one work day. Can applicants apply for 1
								or more work days?</p>
							<!-- 							<div class=" button-group"> -->
							<div class="radio-container">
								<label data-show-id-on-click="work-days-calendar">
									<input id="yes-partial" type="radio" name="partial-availability">
									Yes</label>
								 <label data-show-id-on-click="work-days-calendar">
								 	<input id="no-partial" type="radio" name="partial-availability">
									No, they have to apply for all work days</label>
							</div>
						</div>							
						<div id="supreme-times-wrapper" class="item">
							<div id="set-all-times-wrapper" class="show-with-multiple-work-days time-wrapper">
								
								<label for="set-all-times"><input type="checkbox" id="set-all-times"></input>Set all times</label>
								<select id="set-all-start-times"></select>
								<select id="set-all-end-times"></select>
							</div>			
	
							<div id="appendges">						
							</div>
						</div>
					</div>
				</div>

			
				<div id="location-wrapper" class="section">
					<h3>Location</h3>					
					<div class="item-wrapper">
						<div class="item">
							<label>Street Address *</label>
							<input id="street" type="text" class="" value=""></input>
						</div>
						<div class="item">
							<label>City *</label>
							<input id="city" type="text" class="" value=""></input>
						</div>
						<div class="item">
							<label>State *</label>
							<select id="state" class=""></select>
						</div>
						<div class="item">
							<label>Zip Code</label>
							<input id="zip-code" type="text" value=""></input>
						</div>
					</div>
				</div>

				<div id="questions-wrapper" class="section">
					<h3>Questions <span>(optional)</span></h3>					
					<div class="item-wrapper">
						<p class="optional-item-explantion">You can propose questions to the
							applicants. Ask about their experience, whether they have their
							own tools, ect. This is optional, but it can help you decide which
							applicant to hire.</p>

						<div id="copy-or-new-question" class="item">
							<c:if test="${postedQuestions.size() > 0 }">
								<div id="copy-question-container">
									<button id="copy-previous-question"
										data-toggle-id="posted-questions">Begin with a previous
										question</button>
									<div id="posted-questions" class="dropdown-style">
										<c:forEach items="${postedQuestions }" var="question">
											<p class="pointer" data-question-id="${question.questionId }">${question.text }</p>
										</c:forEach>
									</div>
								</div>
							</c:if>
							<button id="create-new-question">Create a
								new question</button>
						</div>
						<div id="added-questions" class="item">							
						</div>					
						<div id="edit-question-actions">
							<span id="save-question-edits" class="pointer">Save</span>
							<span id="cancel-question-edits" class="pointer">Cancel</span>
						</div>
						<div id="create-question-container" class="item">	
							<div class="item">
								<label>Question Format</label>
								<select id="question-format" class="question-formats">
									<option class="answer-format-item" data-format-id="0">Yes
										or No</option>
									<!-- 							  <option class="answer-format-item" data-format-id="1">Short Answer</option> -->
									<option class="answer-format-item" data-format-id="2">Select
										one answer</option>
									<option class="answer-format-item" data-format-id="3">Select
										one or more answers</option>
								</select>
							</div>	
							<div class="item">
								<label>Question</label>
								<textarea id="question" class="" rows="4"></textarea>
							</div>	
							<div class="item" id="answer-list-container">
								<label>Answers</label>
								<div id="answer-list">
									<div class="list-item answer-container">
										<input type="text" class="answer-option">
										<span class="delete-list-item delete-answer glyphicon glyphicon-remove"></span>
									</div>
									<div class="list-item answer-container">
										<input type="text" class="answer-option">
										<span
											class="delete-list-item delete-answer glyphicon glyphicon-remove"></span>
										
									</div>
									<span id="add-answer"
									class="add-list-item glyphicon glyphicon-plus"></span>
								</div>
								
							</div>
							<div id="add-question-wrapper">
								<button id="add-question" class="clickable btn-sqr">Add</button>
								<div id="invalidAddQuestion" class="invalid-message">Please
									fill in all required fields</div>
							</div>
	
						</div>
					</div>
				</div>

				<div id="skills-wrapper" class="section">
					<h3>Skills <span>(optional)</span></h3>					
					<div class="item-wrapper">
						<p class="optional-item-explantion">List the skills that each
							applicant should have. This is optional, but can help applicants
							decide if they are a good match for your job.</p>
	
						<div class="item">
							<p>Required Employee Skills</p>
							<div id="required-skills-container"
								class="list-items-container skills-container">
								<div class="list-item">									
									<input type="text">									
									<span class="delete-list-item glyphicon glyphicon-remove"></span>
								</div>
							</div>
							<span class="add-list-item glyphicon glyphicon-plus"></span>
						</div>
						<div class="item">
							<p>Desired Employee Skills</p>
							<div id="desired-skills-container"
								class="list-items-container skills-container">
								<div class="list-item">
									<input type="text">
									<span class="delete-list-item glyphicon glyphicon-remove"></span>
								</div>
							</div>
							<span class="add-list-item glyphicon glyphicon-plus"></span>
						</div>
					</div>
				</div>
				
				
		</div>
</div>
	</c:otherwise>
</c:choose>


<div id="clone-start-and-end-times" class="hide-on-load">
	
	<div class="time-wrapper">
		<label class="date"></label>
		<select class="start-time"></select>
		<select class="end-time"></select>
	</div>
	
</div>

<script async defer
	src="https://maps.googleapis.com/maps/api/js?key=AIzaSyAXc_OBQbJCEfhCkBju2_5IfjPqOYRKacI&amp">
	
</script>

<%@ include file="../includes/Footer.jsp"%>
