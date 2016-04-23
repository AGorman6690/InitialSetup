<%@ include file="./includes/Header.jsp"%>

<head>
<script src="<c:url value="/static/javascript/Jobs.js" />"></script>
<script src="<c:url value="/static/javascript/Category.js" />"></script>
<script src="<c:url value="/static/javascript/AppendHtml.js" />"></script>
<link rel="stylesheet" type="text/css"
	href="./static/css/categories.css" />

<!-- Time picker -->
<link rel="stylesheet" type="text/css"
	href="./static/External/jquery.timepicker.css" />
<script
	src="<c:url value="/static/External/jquery.timepicker.min.js" />"></script>
<%-- <script src="<c:url value="/static/External/GruntFile.js" />"></script> --%>


<!-- Dropdown -->
<!-- <script src="/path/to/bootstrap-hover-dropdown.min.js"></script> -->


</head>



<body>

	<input name="userId" value="${user.userId}" type="hidden"></input>
	<%-- 	<input type="hidden" id="userId" value="${user.userId}" /> --%>
	<div style="display: none" class="container" id="submitJobsContainer">
		<div style="width: 750px" class="panel panel-success">
			<div class="panel-heading">Pending Job Submissions</div>

			<div class="color-panel panel-body">
				<div id="pendingJobSubmissions"></div>

				<div class="form-group row">
					<div class="col-sm-offset-2 col-sm-10">
						<button id="submitJob" type="submit" class="btn btn-secondary"
							onclick="submitJobs()">Submit Job(s)</button>
					</div>
				</div>
			</div>
		</div>
	</div>

	<div class="container">
		<div style="width: 750px" class="panel panel-success">

			<div class="panel-heading">Job Information</div>

			<div class="color-panel panel-body">
				<!-- 				<div> -->
				<div class="container">
					<div class="form-group row">
						<label for="jobName" class="post-job-label col-sm-2 form-control-label">Job
							Name</label>
						<div class="col-sm-10">
							<input name="jobName" type="text"
								class="post-job-input form-control" id="jobName"></input>
						</div>
					</div>
				</div>

				<div class="container">
					<div class="form-group row">
						<label for="jobStreetAddress"
							class="post-job-label col-sm-2 form-control-label">Street
							Address</label>
						<div class="col-sm-10">
							<input name="streetAddress" type="text"
								class="post-job-input form-control" id="jobStreetAddress"></input>
						</div>
					</div>
					<div class="form-group row">
						<label for="jobCity"
							class="post-job-label col-sm-2 form-control-label">City</label>
						<div class="col-sm-10">
							<input name="city" type="text"
								class="post-job-input form-control" id="jobCity"></input>
						</div>
					</div>
					<div class="form-group row">
						<label for="jobState"
							class="post-job-label col-sm-2 form-control-label">State</label>
						<div class="col-sm-10">
							<input name="state" type="text"
								class="post-job-input form-control" id="jobState"></input>
						</div>
					</div>
					<div class="form-group row">
						<label for="jobZipCode"
							class="post-job-label col-sm-2 form-control-label">Zip
							Code</label>
						<div class="col-sm-10">
							<input name="zipCode" type="text"
								class="post-job-input form-control" id="jobZipCode"></input>
						</div>
					</div>
				</div>

				<!-- 					***************************************************************** -->
				<!-- 					NOTE: Found the date rage picker here: http://www.daterangepicker.com/ -->
				<!-- 					***************************************************************** -->
				<div class="container">

					<div class="form-group row">
						<label for="startTime" class="post-job-label col-sm-2 form-control-label">Start
							Time</label>
						<div class="col-sm-10">
							<input id="startTime" type="text"
								class="post-job-input form-control time ui-timepicker-input"
								autocomplete="off">
						</div>
					</div>




					<div class="form-group row">
						<label for="endTime" class="post-job-label col-sm-2 form-control-label">Estimated
							End Time</label>
						<div class="col-sm-10">
							<input id="endTime" type="text"
								class="post-job-input form-control time ui-timepicker-input"
								autocomplete="off">
						</div>
					</div>

					<div class="form-group row">
						<label class="post-job-label col-sm-2 form-control-label"
							for="jobDescription">Job Start and End Dates</label>
						<div class="col-sm-10">
							<input style="width: 250px" class="form-control" type="text"
								id="dateRange" />
							<script type="text/javascript">
								$(function() {
									$('#dateRange').daterangepicker({
										locale : {
											format : 'MM/DD/YYYY'
										}
									});
								});
							</script>
						</div>
					</div>
				</div>

				<div class="container">
					<div style="margin-bottom: 25px" class="row">
						<label class="post-job-label col-sm-2 form-control-label" for="jobDescription">Job
							Description</label>
						<div class="post-job-description col-sm-10">
							<textarea name="description" class="form-control"
								id="jobDescription" rows="3"></textarea>
						</div>
					</div>
				</div>



				<!-- 					<div class="container"> -->
				<!-- 						<div class="form-group row"> -->
				<!-- 							<label for="jobDuration" class="post-job-label col-sm-2 form-control-label">Duration (days) -->
				<!-- 								</label> -->
				<!-- 							<div class="col-sm-10"> -->
				<!-- 								<input name="jobDuration" type="text" -->
				<!-- 									class="post-job-input form-control" id="jobDuration" -->
				<!-- 									placeholder="Job Duration (days)"></input> -->
				<!-- 							</div> -->
				<!-- 						</div> -->
				<!-- 					</div> -->



				<div class="container">
					<div class="row">
						<label class="post-job-label col-sm-2 form-control-label">Categories</label>
						<div style="min-height: 50px; display: inline"
							id="selectedCategories"></div>
					</div>
				</div>

				<div class="container">
					<div class="row">
						<input id="selectedCategory" type="hidden"></input>
						<div style="margin-left: 125px"
							class="category-list-container form-group col-sm-10">
							<div id='0T'></div>
						</div>

					</div>
				</div>


				<div class="panel panel-primary">
					<div class="panel-heading">Questions</div>
					<div class="panel-body">

						<div id="question" class="question" style="margin: 20px 0px 20px 0px">
						
							<div class="panel panel-warning">
								<div class="panel-heading">New Question</div>
								<div class="panel-body">

									<div class="dropdown" style="margin-bottom: 10px">
										<input type="hidden"></input> 									
										<a class="btn btn-primary dropdown-toggle" type="button"
											data-toggle="dropdown">Question Format <span
											class="caret"></span></a>
										<ul class="dropdown-menu">
											<li class="answer-format-item" value="0"><a>Yes/No</a></li>
											<li class="answer-format-item" value="1"><a>Short
													Answer</a></li>
											<li class="answer-format-item" value="2"><a>Select
													single answer from list</a></li>
											<li class="answer-format-item" value="3"><a>Select
													multiple answers from list</a></li>
										</ul>
									</div>
									
									
									<div>
										<textarea style="display: inline" name="question"
											class="form-control" rows="3"></textarea>
									</div>
	
									<div class="answer-list-input"></div>
									<button type="button" style='margin-top: 10px' class="btn btn-success add-question-button"
										onclick="addQuestion()">Add Question</button>
								</div>
							
							</div> <!-- end question panel -->
						</div> <!-- end question -->
								


						<div id="addedQuestions"></div>

					</div> <!-- end question panel body -->					
				</div> <!-- end question panel -->
				


				<div class="form-group row">
					<div class="col-sm-offset-2 col-sm-10">
						<button type="submit" class="btn btn-secondary"
							onclick="addJobToCart()">Add Job</button>
					</div>
				</div>

				<!-- 				</div> -->
			</div> <!-- end panel body -->			
		</div> <!-- end panel container -->		
	</div> <!-- end page container  -->
	

</body>

<script>
	var pageContext = "postJob";
	var jobs = [];
	var jobCount = 1;
	var questions = [];
	var questionCount = 0;

	getCategoriesBySuperCat('0', function(response, categoryId) {
		appendCategories(categoryId, "T", response, function() {
		});
	})

	$(document).ready(function() {
		$('#startTime').timepicker({
			'scrollDefault' : '7:00am'
		});
		$('#endTime').timepicker({
			'scrollDefault' : '5:00pm'
		});

		assignAnswerFormatItemClickEvent();

	})

	function assignAnswerFormatItemClickEvent() {

		$(".answer-format-item")
				.click(
						function(event) {

							var selectedAnswerFormatText = $(this).text();
							var anchorElement = $(this).parent().siblings('a')[0];
							$(anchorElement).html(selectedAnswerFormatText);
							$(anchorElement)
									.append(
											"<span style='margin-left: 5px' class='caret'></span>");

							var selectedAnswerFormatId = $(this).val();
							var inputElement = $(this).parent().siblings(
									'input')[0];

							$(inputElement).val(selectedAnswerFormatId);
							if (selectedAnswerFormatId == 2
									|| selectedAnswerFormatId == 3) {
								showAnswerListInput(this);
							} else {
								emptyAnswerListInput(this);
							}

						})
	}

	function showAnswerListInput(event) {

		var ancestorDiv = $(event).closest('div');
		var answerListInputDiv = $(ancestorDiv).siblings('.answer-list-input')[0];

		var j = -1;
		var r = [];

		r[++j] = '<ul class="list-group" style="width: 225px; margin: 10px 0px 5px 0px">';
		r[++j] = '<button style="margin-bottom: 5px" onclick="addItem(this)" type="button" class="btn btn-warning">Add answer option</button>';
		r[++j] = getItem();
		r[++j] = getItem();
		r[++j] = '</ul>';

		$(answerListInputDiv).empty();
		$(answerListInputDiv).append(r.join(''));

	}

	function deleteItem(event) {
		$(event).parent().remove();
	}

	function getItem(){
		return  '<li class="list-group-item">'
		+ '<span style="font-size: 1.5em" onclick="deleteItem(this)" class="glyphicon glyphicon-remove-circle"></span>'
		+ '<input style="margin-left: 10px; display:inline; width: 150px" class="form-control answer-option">'
		+ '</li>';
	}
	
	function addItem(event) {
		var list = $(event).parent().append(getItem());
	}

	function emptyAnswerListInput(event) {

		var ancestorDiv = $(event).closest('div');
		var answerListInputDiv = $(ancestorDiv).siblings('.answer-list-input')[0];
		$(answerListInputDiv).empty();
	}

	function addQuestion() {

		var j = -1;
		var r = [];

		$("#question").clone().appendTo($("#addedQuestions"));

		var newId = 'question-' + questionCount;
		$("#addedQuestions").find("#question").attr('id', newId);
		$("#" + newId).find(".panel-body")
				.append(
						"<button style='margin-top: 10px' class='btn btn-danger' type='button' onclick='deleteQuestion(this)'>Delete Question</button>")

		var questionLabels = $("#addedQuestions").find(".panel-heading");
		var questionLabel = questionLabels[questionLabels.length - 1];
		$(questionLabel).text('Question');
		
		
		$("#addedQuestions").find(".add-question-button").remove();
		assignAnswerFormatItemClickEvent();

		questionCount += 1;
	}

	function deleteQuestion(event) {
		$(event).closest(".question").remove();
	}
</script>

<%@ include file="./includes/Footer.jsp"%>