<%@ include file="./includes/Header.jsp"%>

<head>
	<script src="<c:url value="/static/javascript/Utilities.js" />"></script>
	<script src="<c:url value="/static/javascript/Category.js" />"></script>
	<script src="<c:url value="/static/javascript/InputValidation.js" />"></script>
	<script src="<c:url value="/static/javascript/PostJob/Jobs.js"/>"></script>
	<script src="<c:url value="/static/javascript/PostJob/Questions.js"/>"></script>
	<script src="<c:url value="/static/javascript/PostJob/ChangeForm.js"/>"></script>

	<link rel="stylesheet" type="text/css" href="./static/css/categories.css" />
	<link rel="stylesheet" type="text/css" href="./static/css/postJob.css" />
	<link rel="stylesheet" type="text/css"	href="./static/css/inputValidation.css" />		
	
	<!-- Time picker -->
	<link rel="stylesheet" type="text/css" href="./static/External/jquery.timepicker.css" />
	<script	src="<c:url value="/static/External/jquery.timepicker.min.js" />"></script>
	
</head>


<body>

	<input name="userId" value="${user.userId}" type="hidden"></input>
	
	<div id="jobPostContainer">
		<div id="jobCartContainer" class="menu">
<!-- 			<div class="navbar navbar-default" role="navigation"> -->
				<div class="container" style="">		
<!-- 					<div class="navbar-header"> -->
						<div class="row" style="">					
							<div class="col-sm-12" style="">
								
								<div id="jobActionContainer" class="" style="">
									<input id="activeJobId" type="hidden">
								    <div class="btn-group">
										<button id="startNewJob" type="button" disabled
							            	class="btn btn-info dropdown-toggle">New</button>						
										<button id="addJob" type="button" class="btn btn-info"
											onclick="addJobToCart()">Add</button>								
										<button id="deleteJob" data-confirmed="0" type="button" disabled class="btn btn-info"
											data-toggle="modal" data-target="#confirmJobDelete">Delete</button>								
										<button id="copyJob" type="button" disabled
											class="btn btn-info">Copy</button>
										<button id="editJob" type="button" class="btn btn-info"
											disabled>Edit</button>									
			
								    </div>		
								    
								    <span id="editJobResponseContainer" class="" 
								    		style="margin-left: 5px; display: none">
								    	<span id="saveChanges" data-content="Save changes" data-placement="bottom"
								    		 class="popoverInstant edit-job-glyphicon glyphicon glyphicon-floppy-saved"
								    		 style="vertical-align:middle; color: #00802b"></span>
								    	<span id="cancelChanges" data-content="Cancel changes" data-placement="bottom"
								    		 class="popoverInstant edit-job-glyphicon glyphicon glyphicon-floppy-remove"
								    		 style="vertical-align:middle; color: #b32d00"></span>
								    </span>
									
									<div id="submitJobsContainer" style="display: none; float: right;">
										
										<button id="submitJobs" data-confirmed="0" type="button" class="btn btn-info"
											 data-toggle="modal" data-target="#confirmJobSubmit">Submit Jobs</button>
												
									</div>	
									
									<div style="border-bottom-style: outset; margin-top: 10px">
										<div id="addedJobsContainer" class="" style="border-top-style: inset; 
											display: none; margin: 10px 0px 10px 0px; outline-width: 0px" >
											<div class="" style="margin-bottom: 5px; outline-width: 0px">
												<h3 style="outline-width: 0px; margin-top: 10px" class="">Job Cart</h3></div>
											
											<div id="addedJobs" style="" 
												class="btn-group" role="group" aria-label="Basic example">
											</div>	
				
											<div id="jobsInProcessContainer" style="display: none">
												<h4>Jobs In Process:</h4>	
												<div id="jobsInProcess" class="btn-group" role="group" aria-label="Basic example">
								
												</div>					
											</div>
										</div>	
									</div>	
								</div>							
<!-- 							</div>	 -->
						</div><!-- end row -->
<!-- 					</div> -->
				</div>
			</div> 
		</div><!-- end job cart container -->	

		<div id="jobInfoContainer">
			<div class="container" >
				<div class="row" style="margin-top: 10px;" >
					<div id="jobGeneralContainer" class="col-sm-6">
						<form>
							<div class="" >
							
								<div class="job-sub-info-container">
									<h3>General</h3>
									<div class="job-sub-info">
										<fieldset class="form-group">
											<label for="jobName" class="form-control-label">Name</label>
											<input name="jobName" type="text"
												class="post-job-input form-control" id="jobName"></input>
											<div id="invalidJobName" class="invalid-message" style="display: none">Job names must be unique</div>
										</fieldset>
											
										<fieldset class="form-group">
											<label for="jobDescription" class="form-control-label">Description</label>
											<textarea name="jobDescription" class="form-control"
												id="jobDescription" rows="5"></textarea>
										</fieldset>
									</div>
								</div>
							
								
								<div class="job-sub-info-container">
									<h3>Location</h3>
									
									<div class="job-sub-info">
										<fieldset class="form-group">
											<label for="streetAddress" class="form-control-label">Street Address</label>
											<input name="streetAddress" type="text"
												class="post-job-input form-control" id="streetAddress"></input>
										</fieldset>
										
										<fieldset class="form-group">
											<label for="city" class="form-control-label">City</label>
											<input name="city" type="text"
												class="post-job-input form-control" id="city"></input>
										</fieldset>
										
										<fieldset class="form-group">
											<label for="state" class="form-control-label">State</label>
												
											<div class="post-job-input post-job-select-container">
												<select style="display: block" id="state" name="state"
												 class="post-job-select post-job-input form-control"></select>	
			<!-- 									<input name="state" type="text" -->
			<!-- 										class="post-job-input form-control" id="state"></input> -->
											</div>
										</fieldset>
										
										<fieldset class="form-group">
											<label for="zipCode" class="form-control-label">Zip Code</label>
											<input name="zipCode" type="text"
												class="post-job-input form-control" id="zipCode"></input>
										</fieldset>
									</div>
								</div>
								
								<div class="job-sub-info-container">
									<h3>Date and Time</h3>					
									
									<div class="job-sub-info">
										<fieldset class="form-group">
											<label for="dateRange" class="form-control-label">Start and End Dates</label>
											<input style="width: 250px" class="form-control" type="text"
												id="dateRange" value=""/>
				
										</fieldset>								
										
										<fieldset class="form-group">
											<label for="startTime" class="form-control-label">Start Time</label>
											<input name="startTime" type="text"
												class="post-job-input form-control time ui-timepicker-input"
													autocomplete="off" id="startTime"></input>
										</fieldset>
					
										<fieldset class="form-group">
											<label for="endTime" class="form-control-label">End Time</label>
											<input name="endTime" type="text"
												class="post-job-input form-control time ui-timepicker-input"
													autocomplete="off" id="endTime"></input>
										</fieldset>
										
									</div>
								</div>
								
								
								
								<div id="categoryContainer" class="job-sub-info-container">
									<h3>Categories</h3>				
									<div id="invalidCategoryInput-None" class="invalid-message"
										style="display: none">At least one category must be selected</div>
									<div id="invalidCategoryInput-TooMany" class="invalid-message" 
										style="display: none">A maximum of five categories can be selected</div>										
<!-- 									Eventaully render this with jstl -->
<!-- 									//********************************************************************************* -->
<!-- 									//********************************************************************************* -->
									<div class="job-sub-info">
										<div id="selectedCategories" style="margin-bottom: 5px">
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
<!-- 									//********************************************************************************* -->
<!-- 									//********************************************************************************* -->

								</div>				
							</div>
						
						
						
						</form>
					</div> <!-- end job general container -->
					
					<div id="jobQuestionsContainer" class="col-sm-6">
					
						<div style="margin-left: 50px">
							<div class="job-questions-label"  style="border-top-style: inset"><h3>Questions</h3>						
							</div>
								
							<div class="job-sub-info" style="margin-left: 50px;">
								
								<div id="new-question-container-container">
									<div id="new-question-container">					
										<div class="question-formats-container post-job-select-container dropdown" style="margin-bottom: 10px">									 
				
											<select class="question-formats form-control" title="">
											  <option selected value="-1" style="display: none">Select a question format</option>	
											  <option class="answer-format-item" value="0">Yes or No</option>
											  <option class="answer-format-item" value="1">Short Answer</option>
											  <option class="answer-format-item" value="2">Single Answer</option>
											  <option class="answer-format-item" value="3">Multiple Answer</option>
											</select>
				
										</div>									
										
										<div class="new-question-text">
											<textarea id="newQuestionText" style="display: inline; resize: none" name="question"
												class="form-control" rows="2"></textarea>
										</div>
									
										<div class="answer-option-list" style="display: none">
											<h4>Answer List</h4>
											<ul class="list-group" style="width: 225px; margin: 10px 0px 5px 0px">
												<li class="list-group-item">
													<span style="font-size: 1.5em" class="delete-answer-item glyphicon glyphicon-remove"></span>
													<input style="margin-left: 10px; display:inline; width: 150px" class="form-control answer-option">
												</li>
												<li class="list-group-item">
													<span style="font-size: 1.5em" class="delete-answer-item glyphicon glyphicon-remove"></span>
													<input style="margin-left: 10px; display:inline; width: 150px" class="form-control answer-option">
												</li>
												<li style="display:table; text-align:center" class="add-new-answer-item list-group-item">
													<span style="display:table-cell; vertical-align:middle; height: 5px
														font-size: 1em" class="glyphicon glyphicon-chevron-right"></span>
												</li>
											</ul>		 				
						 				
										</div>	
									
										<div>
											<span id="addNewQuestion" style="margin:10px 0px 10px 10px; font-size: small" 
											data-content="Add question" class="popover1 glyphicon glyphicon-plus"></span>
										</div>
									</div><!-- end job-questions-new -->
								</div>
							
								<div id="addedQuestions">													
								</div>
							
							</div> 
					
						</div>
					</div> <!-- end job questions container -->
				</div><!-- end row -->
			</div><!-- end container -->
		</div><!-- end job info container -->
	</div><!-- end post job container -->
	
<!-- 			This is the template html for added questions -->
<!-- ********************************************************************************************* -->
<div style="display: none" id="added-question-template-container">
	<div class="added-question-template" style="margin: 10px 10px 10px 10px">	
		<div style="margin-top: 10px">
			
		
<!-- 				<span style="width: 100%; font-size: 20px; color: black; vertical-align: top; margin: 0px 0px 10px 10px" data-content="Hide question" -->
<!-- 				 class="border-blue hide-question popover1 glyphicon glyphicon-resize-small" aria-hidden="true"></span> -->
								
						
						
	
			<span style="font-size: 25px; vertical-align: top; margin: 10px 10px 10px 10px"
			data-content="Disable question for current job posting" 
			class="popover1 toggle-question-activeness enable-question glyphicon glyphicon-ok" aria-hidden="true"></span>
	
		
			<textarea style="display: inline; width: 75%; resize: none" name="question"
				class="form-control question-text" rows="3"></textarea>
				
			<span style="font-size: 20px; color: black; vertical-align: top; margin: 10px 0px 10px 10px" data-content="Delete question"
			 class="delete-question popover1 glyphicon glyphicon-remove" aria-hidden="true"></span>

		</div>
		
		<div>
			<span style="margin: 10px 0px 10px 10px" class="toggle-question-info-container glyphicon glyphicon-menu-right" aria-hidden="true"></span>
			<div class="question-info-container" style="margin-left: 50px; display:none">

			</div>
		</div>						
	</div>
</div>	
<!-- ********************************************************************************************* -->


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




<script>

	var pageContext = "postJob";
	var jobs = [];	
	var jobCount = 0;
	var questions = [];
	var questionCount = 0;
	var questionContainerIdPrefix = 'question-';
	
		

	$(document).ready(function() {
	
		$('#startTime').timepicker({
			'scrollDefault' : '7:00am'
		});
		$('#endTime').timepicker({
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