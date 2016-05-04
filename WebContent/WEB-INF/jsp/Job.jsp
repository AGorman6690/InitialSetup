<%@ include file="./includes/Header.jsp"%>
<head>
<script src="<c:url value="/static/javascript/Jobs.js" />"></script>
<script src="<c:url value="/static/javascript/Category.js" />"></script>
<script src="<c:url value="/static/javascript/User.js" />"></script>
<script src="<c:url value="/static/javascript/AppendHtml.js" />"></script>

</head>

<input type="hidden" id="jobId" value="${job.id}" />
<input type="hidden" id="userId" value="${user.userId}" />

<div class="container">

			<div class="job-options btn-group" role="group" aria-label="...">
				<c:choose>
					<c:when test="${user.getProfileId() == 2}">
						<a href="./edit" class="btn btn-default">Edit Job</a>
						<button onclick="markJobComplete(${job.getId()})" id="markJobComplete"
							class="btn btn-default">Mark Job Complete</button>
					</c:when>
					<c:when test="${user.getProfileId() == 1}">
<%-- 						<a href="../job/${job.getId()}/user/${user.getUserId()}/apply" class="btn btn-default">Apply</a> --%>
							<a onclick="apply()" class="btn btn-default">Apply</a>
					</c:when>
				</c:choose>
			</div>

	<div>
		<div style="width: 500px" class="panel panel-info">
			<div class="panel-heading">Job Name</div>
			<div id="jobName" class="panel-body">${job.getJobName() }</div>
		</div>

			<div style="width: 500px" class="panel panel-info">
			  <div class="panel-heading">
			    Job Location
			  </div>
			  <div id="jobLocation" class="panel-body"></div>

			  	<div class="job-location-container input-group">
				  <span style="width: 125px" class="job-location-label input-group-addon" id="sizing-addon2">Street Address</span>
				  <div id="jobStreetAddress" class="panel-body">${job.getStreetAddress() }</div>
				</div>
			  	<div class="job-location-container input-group">
				  <span style="width: 125px" class="job-location-label input-group-addon" id="sizing-addon2">City</span>
				  <div id="jobCity" class="panel-body">${job.getCity() }</div>
				</div>
			  	<div class="job-location-container input-group">
				  <span style="width: 125px" class="job-location-label input-group-addon" id="sizing-addon2">State</span>
				  <div id="jobState" class="panel-body">${job.getState() }</div>
				</div>
			  	<div class="job-location-container input-group">
				  <span style="width: 125px" class="job-location-label input-group-addon" id="sizing-addon2">Zip Code</span>
				  <div id="jobZipCode" class="panel-body">${job.getZipCode() }</div>
				</div>
			</div>

		<div style="width: 500px" class="panel panel-info">
			<div class="panel-heading">Job Description</div>
			<div id="jobDescription" class="panel-body">${job.getDescription() }</div>
		</div>

	</div>

	<c:choose>
		<c:when test="${user.getProfileId() == 2}">
			<br>
			<div id="container">

				<div style="width: 750px" class="panel panel-warning">
					<div class="panel-heading">Applicants</div>
					<div id="applicants" class="panel-body">
						<div id="applicantFilters" class="panel panel-success">
							<div class="panel-heading">Filters</div>
							<div class="panel-body">

								<div style="margin-bottom:5px">
									<label class="control-label">Ratings</label>
								</div>
								<div style="margin-bottom:5px">
									<label class="control-label">Endorsements</label>
									<select title="Select endorsements" class="hide-show-columns selectpicker" multiple data-style="btn-primary">

	 									<c:if test="${fn:length(job.getCategories()) > 1 }">
	 										<option>Total Endorsements</option>
	 									</c:if>
										<c:forEach items="${job.getCategories() }" var="category">
											<option value="${category.getId()}">${category.getName()} Endorsements</option>
										</c:forEach>
									</select>
								</div>
								<div style="margin-bottom:5px">
									<label class="control-label">Questions</label>
									<select title="Select questions" class="hide-show-columns selectpicker" multiple data-style="btn-primary">
										<c:forEach items="${job.getQuestions() }" var="question">
											<option value="${category.getQuestionId()}">${question.getQuestion() }</option>
										</c:forEach>
									</select>
								</div>
								<div style="margin-bottom:5px">
									<label class="control-label">Application Status</label>
									<select id="applicationStatus" title="Select status" class="selectpicker" multiple data-style="btn-primary">
<!-- 										<option value="0">Submitted</option> -->
										<option value="1">Declined</option>
										<option value="2">Considering</option>
<!-- 										<option value="3">Accepted</option> -->
									</select>
								</div>
<!-- 								<button onclick="filterApplicants()" type="button" class="btn btn-success">Filter</button> -->
							</div>
						</div>


						<c:choose>
							<c:when test="${job.getApplicants().size() > 0 }">
								<table id="applicantsTable" class="table table-hover table-striped
										table-bordered" cellspacing="0" width="100%">
									<thead>
										<tr>

											<th>Applicant Name</th>
											<th>Rating</th>
											<c:forEach items="${job.getCategories() }"
 		 										var="category">
 		 										<th>${category.getName()} Endorsements</th>
 		 									</c:forEach>
 		 									<c:if test="${fn:length(job.getCategories()) > 1 }">
 		 										<th>Total Endorsements</th>
 		 									</c:if>
 		 									<c:forEach items="${job.getQuestions() }" var="question">
 		 										<th>${question.getQuestion() }</th>
 		 									</c:forEach>
 		 									<th>Application Status</th>
										</tr>
									</thead>
									<tbody>
										<c:forEach items="${job.getApplicants() }" var="applicant">
											<c:choose>
												<c:when test="${applicant.getApplication().status < 3 }">
													<tr id="applicant_${applicant.getUserId() }">
														<td class="clickToWorkHistory">${applicant.getFirstName() } ${applicant.getLastName() }</td>


														<td class="clickToWorkHistory">${applicant.getRating() }</td>
														<c:set var="total" value = "${0 }" />
														<c:forEach items="${applicant.getEndorsements()}" var="endorsement">
															<td class="clickToWorkHistory">${endorsement.getCount() }</td>
															<c:set var="total" value="${total + endorsement.getCount() }" />
														</c:forEach>
			 		 									<c:if test="${fn:length(job.getCategories()) > 1 }">
			 		 										<td class="clickToWorkHistory">${total}</td>
			 		 									</c:if>
			 		 									<c:forEach items="${applicant.getAnswers() }" var="answer">
			 		 										<c:set var="questionFormatId" value="${answer.getQuestionFormatId() }" />

			 		 										<c:choose>
			 		 											<c:when test="${questionFormatId == 0 }">
			 		 												<c:choose>
			 		 													<c:when test="${answer.getAnswerBoolean() == 1 }">
			 		 														<td class="clickToWorkHistory">Yes</td>
			 		 													</c:when>
			 		 													<c:otherwise>
			 		 														<td class="clickToWorkHistory">No</td>
			 		 													</c:otherwise>
		 		 													</c:choose>
			 		 											</c:when>

			 		 											<c:when test="${questionFormatId == 1 }">
			 		 												<td class="clickToWorkHistory">${answer.getAnswerText()}</td>
			 		 											</c:when>

			 		 											<c:otherwise>
			 		 												<c:forEach items="${answer.getAnswers() }" var="multipleAnswer" varStatus="stat">
			 		 													<c:set var="out" value="${stat.first ? '' : out} ${multipleAnswer }" />
			 		 												</c:forEach>
			 		 												<td class="clickToWorkHistory">${out }</td>
			 		 											</c:otherwise>
			 		 										</c:choose>

			 		 									</c:forEach>
														<td>
															<div class="applicant-status">
																<input value="${applicant.getApplication().getStatus()}" class="applicant-status" type="hidden" />

																<c:choose>
																	<c:when test="${applicant.getApplication().getStatus() == 1 }">
																		<div>
																			<button value="0" class="update-application-status btn btn-info btn-sm"
																			onclick="updateApplicationStatus(${applicant.getApplication().getApplicationId()}, 0 )" >
																			Declined</button>

																			<button style="display: none" value="1" class="update-application-status btn btn-default btn-sm"
																			onclick="updateApplicationStatus(${applicant.getApplication().getApplicationId()}, 1 )">
																			Declined</button>
																		</div>
																	</c:when>
																	<c:otherwise>
																		<div>
																			<button style="display: none" value="0" class="update-application-status btn btn-info btn-sm"
																			onclick="updateApplicationStatus(${applicant.getApplication().getApplicationId()}, 0 )" >
																			Declined</button>

																			<button value="1" class="update-application-status btn btn-default btn-sm"
																			onclick="updateApplicationStatus(${applicant.getApplication().getApplicationId()}, 1 )">
																			Declined</button>
																		</div>
																	</c:otherwise>
																</c:choose>

																<c:choose>
																	<c:when test="${applicant.getApplication().getStatus() == 2 }">
																		<div>
																			<button value="0" class="update-application-status btn btn-info btn-sm"
																			onclick="updateApplicationStatus(${applicant.getApplication().getApplicationId()}, 0 )" >
																			Considering</button>

																			<button style="display: none" value="2" class="update-application-status btn btn-default btn-sm"
																			onclick="updateApplicationStatus(${applicant.getApplication().getApplicationId()}, 2 )">
																			Considering</button>
																		</div>
																	</c:when>
																	<c:otherwise>
																		<div>
																			<button style="display: none" value="0" class="update-application-status btn btn-info btn-sm"
																			onclick="updateApplicationStatus(${applicant.getApplication().getApplicationId()}, 0 )" >
																			Considering</button>

																			<button value="2" class="update-application-status btn btn-default btn-sm"
																			onclick="updateApplicationStatus(${applicant.getApplication().getApplicationId()}, 2 )">
																			Considering</button>
																		</div>
																	</c:otherwise>
																</c:choose>

															</div>
														</td>

													</tr>
												</c:when>
											</c:choose>
										</c:forEach>
									</tbody>
								</table>
							</c:when>
							<c:otherwise>
								<div id="noApplicants">
									<div>No applicants</div>
								</div>
							</c:otherwise>
						</c:choose>
					</div> <!-- end applicants panel body -->
				</div> <!-- end applicants panel -->

				<div style="width: 750px" class="panel panel-warning">
					<div class="panel-heading">Employees</div>
					<div id="employees" class="panel-body">
						<c:choose>
							<c:when test="${job.getEmployees().size() > 0 }">
								<table id="employeesTable" class="table table-hover table-striped
										table-bordered" cellspacing="0" width="100%">
									<thead>
										<tr>
											<th>Employee Name</th>
											<th>Rating</th>
											<c:forEach items="${job.getCategories() }"
 		 										var="category">
 		 										<th>${category.getName()} Endorsements</th>
 		 									</c:forEach>
 		 									<th>Total Endorsements</th>
										</tr>
									</thead>
									<tbody>
										<c:forEach items="${job.getEmployees() }" var="employee">
											<tr id="employee_${employee.getUserId() }">

												<td class="clickToWorkHistory">${employee.getFirstName() }
														${employee.getLastName() }</td>

												<td class="clickToWorkHistory">${employee.getRating() }</td>
												<c:set var="total" value = "${0 }" />
												<c:forEach items="${employee.getEndorsements()}" var="endorsement">
													<td class="clickToWorkHistory">${endorsement.getCount() }</td>
													<c:set var="total" value="${total + endorsement.getCount() }" />
												</c:forEach>
												<td class="clickToWorkHistory">${total }</td>
											</tr>
										</c:forEach>
									</tbody>
								</table>
							</c:when>
							<c:otherwise>
								<div id="noEmployees">
									<div>No Employees</div>
								</div>
							</c:otherwise>
						</c:choose>
					</div> <!-- end employees panel body -->
				</div> <!-- end employees panel -->
			</div> <!-- end container -->
		</c:when>

		<c:when test="${user.getProfileId() == 1}">

			<c:choose>
				<c:when test="${job.getQuestions().size() > 0}">

					<div class="panel panel-danger">
						<div class="panel-heading">Questions</div>
						<div id="questions" class="panel-body">
							<c:forEach items="${job.getQuestions() }" var="question">
								<div id="question-${question.getQuestionId() }" class="panel panel-warning question">
									<div class="panel-heading">Question</div>
									<div class="panel-body">
										<textarea class="form-control" rows="3">${question.question}</textarea>

										<div class="dropdown" style="margin-top: 10px">
											<input class="question-format-id" type="hidden" value=${question.getFormatId() }></input>

												<c:choose>
													<c:when test="${question.getFormatId() == 0 }">
														<select title="Select an answer" class="selectpicker" data-style="btn-primary">
<!-- 															<option value="-1" selected="selected">Select an answer</option> -->
															<option value="1">Yes</option>
															<option value="0">No</option>
														</select>
													</c:when>
													<c:when test="${question.getFormatId() == 1 }">
														<textarea class="form-control answer-text" rows="3" placeholder="Answer"></textarea>
													</c:when>
													<c:when test="${question.getFormatId() == 2 }">
														<select title="Select an answer" class="selectpicker" data-style="btn-primary">
<!-- 															<option value="-1" selected="selected">Select an answer</option> -->
															<c:forEach items="${question.getAnswerOptions() }" var="option">
																<option value="${option.getAnswerOptionId()}">${option.getAnswerOption() }</option>
															</c:forEach>
														</select>
													</c:when>
													<c:when test="${question.getFormatId() == 3 }">
														<select title="Select an answer" class="selectpicker" multiple data-style="btn-primary">
<!-- 															<option value="-1" selected="selected">Select an answer</option> -->
															<c:forEach items="${question.getAnswerOptions() }" var="option">
																<option value="${option.getAnswerOptionId()}">${option.getAnswerOption() }</option>
															</c:forEach>
														</select>
													</c:when>
												</c:choose>
<!-- 											</ul> -->
										</div>
									</div>
								</div>
							</c:forEach>
						</div>

					</div>
				</c:when>
				<c:otherwise>
					<div class="panel panel-danger">
						<div class="panel-heading">Questions</div>
						<div class="panel-body">
							No Questions
						</div>

					</div>
				</c:otherwise>

				</c:choose>

		</c:when>
	</c:choose>
</div>


<script type="text/javascript">

	$(document).ready(function(){
		$('#applicantsTable').DataTable();
		$('#employeesTable').DataTable();

		$(".clickToWorkHistory").click(function(){
			elementId = $(this).parent().attr('id');
			var idBegin = elementId.indexOf("_") + 1;
			var userId =  elementId.substring(idBegin);
			window.location = "../jobs/completed/employee/?userId=" + userId +
								'&jobId=' + $("#jobId").val() + '&c=0';
		})

		$("#applicationStatus").change(function(){

			var i = 0;
			var j = 0;
			var statusesToShow = $(this).find(":selected")
									.map(function(){ return $(this).val() }).get();


			var statuses = $("#applicantsTable").find("tbody input.applicant-status")

			if(statusesToShow.length == 0){
				for(i = 0; i < statuses.length; i++){
					var status = statuses[i];
					var rows = $(status).parents('tr');
					var row = rows[0];
					$(row).hide();
				}
			}else if(statusesToShow.length == 2){
				for(i = 0; i < statuses.length; i++){
					var status = statuses[i];
					var rows = $(status).parents('tr');
					var row = rows[0];
					$(row).show();
				}
			}else{
				for(i = 0; i < statuses.length; i++){
					var status = statuses[i];
					var rows = $(status).parents('tr');
					var row = rows[0];

					for(j = 0; j < statusesToShow.length; j++){

						if($(status).val() == statusesToShow[j]){
							$(row).show();
							j = statusesToShow.length;
						}else if(j == statusesToShow.length - 1){
							$(row).hide();
						}
					}
				}
			}
		})

		$(".hide-show-columns").change(function(){
			filterApplicants();
		})


		$(".update-application-status").click(function(){

			//Store the value of the application status
			$(this).parent().siblings('input.applicant-status').val($(this).val());

			//Show all default buttons
			var buttons = $(this).parents('div.applicant-status').find('button');
			for(var i = 0; i < buttons.length; i++){
				var button = buttons[i];

				if($(button).hasClass("btn-info")){
					$(button).hide();
				}else if($(button).hasClass("btn-default")){
					$(button).show();
				}
			}

			//Toggle the clicked button
			$(this).hide();
			var otherButton = $(this).siblings('button')[0];
			$(otherButton).show();

		})

	});

	function filterApplicants(){

		var headers = $("#applicantsTable").find("thead tr th");
		var headerNames = [];
		var i = 0;
		var j = 0;
		var headersToShow = $(".hide-show-columns").find(":selected")
								.map(function(){ return $(this).html() }).get();

// 		alert(endorsementsToShow)

		for(i = 0; i < headers.length; i++){
			var header = headers[i];
			var headerName = $(header).text();

			if(headerName != "Applicant Name" && headerName != "Rating"
					&& headerName != "Application Status"){

				var showColumn = 0;
				for(j = 0; j < headersToShow.length; j++){
					if(headerName == headersToShow[j]){
						showColumn = 1;
					}
				}

				if(showColumn == 1){
					$("#applicantsTable td:nth-child(" + parseInt(i + 1) + ")").show();
					$("#applicantsTable th:nth-child(" + parseInt(i + 1) + ")").show();
				}else{
					$("#applicantsTable td:nth-child(" + parseInt(i + 1) + ")").hide();
					$("#applicantsTable th:nth-child(" + parseInt(i + 1) + ")").hide();
				}
			}
		}
	}

	function apply(){

		var userId = $("#userId").val();
		var applicationDTO = {};

		applicationDTO.jobId = $("#jobId").val();
		applicationDTO.userId = userId;
		applicationDTO.answers = [];

		var questions = $("#questions").find(".question");
		var invalidAnswer = 0;

		for(var i = 0; i < questions.length; i++){

			var questionElement = questions[i];
			var questionFormatId = $(questionElement).find(".question-format-id").val();

			var answer = {};
			answer.answerText = "";
			answer.answerBoolean = -1;
			answer.answerOptionId = -1;
			answer.answerOptionIds = [];

			if(questionFormatId == 0){
				answer.answerBoolean = $(questionElement).find('select').find(":selected").val();
				if(answer.answerBoolean == "") invalidAnswer = 1;
			}else if(questionFormatId == 1){
				answer.answerText = $(questionElement).find('.answer-text').val()
				if(answer.answerText == "") invalidAnswer = 1;
			}else if(questionFormatId == 2){
				answer.answerOptionId = $(questionElement).find('select').find(":selected").val();
				if(answer.answerOptionId == "") invalidAnswer = 1;
			}else if(questionFormatId == 3){
				answer.answerOptionIds = $(questionElement).find('select').find(":selected").map(function(){ return this.value }).get();
				if(answer.answerOptionIds == "") invalidAnswer = 1;
			}

			var questionElementId = $(questionElement).attr('id');
			answer.questionId = questionElementId.substring(questionElementId.indexOf("-") + 1);
			answer.userId = userId;

			applicationDTO.answers.push(answer);
		}

// 		alert(JSON.stringify(applicationDTO))

		if(invalidAnswer == 0){
			var headers = {};
			headers[$("meta[name='_csrf_header']").attr("content")] = $(
					"meta[name='_csrf']").attr("content");
			$.ajax({
				type : "POST",
				url : 'http://localhost:8080/JobSearch/job/apply',
				headers : headers,
// 				dataType : "application/json",
				contentType : "application/json",
				data : JSON.stringify(applicationDTO),
			}).done(function() {
				$('#home')[0].click();
			}).error(function() {
				$('#home')[0].click();
			});
		}
	}




</script>

<%@ include file="./includes/Footer.jsp"%>