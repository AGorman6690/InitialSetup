<%@ include file="./includes/Header.jsp" %>
<%@ include file="./includes/Header_Employer.jsp" %>

	<head>
<%-- 		<script src="<c:url value="/static/javascript/Jobs.js" />"></script> --%>
		<script src="<c:url value="/static/javascript/Category.js" />"></script>
<%-- 		<script src="<c:url value="/static/javascript/User.js" />"></script> --%>
<%-- 		<script src="<c:url value="/static/javascript/RateCriterion.js" />"></script> --%>
<%-- 		<script src="<c:url value="/static/javascript/Display.js" />"></script> --%>
		<script src="<c:url value="/static/javascript/AppendHtml.js" />"></script>
		<link rel="stylesheet" type="text/css" href="./static/css/categories.css" />
		<link rel="stylesheet" type="text/css" href="./static/css/postJob.css" />
	</head>
	

	
	<body>	
		<input type="hidden" id="userId" value="${user.userId}"/>
		
		<div class="container">
			<div style="width: 750px" class="panel panel-success">
			  
			  <div class="panel-heading">
			  	Job Information
			  </div>
			  
			  <div class="color-panel panel-body">	
				<div>
					<form:form action="./createJob" method="post" commandName="job">
						<div class="container">
							<div class="form-group row">
								<label for="jobName" class="col-sm-2 form-control-label">Job Name</label>
								<div class="col-sm-10">
									<form:input path="jobName" type="text" class="post-job-input form-control" id="jobName" placeholder="Job Name"></form:input>
								</div>
							</div>
						</div>
						
						<div class="container">
							<div class="form-group row">
								<label for="jobLocation" class="col-sm-2 form-control-label">Location</label>
								<div class="col-sm-10">
									<form:input path="location" type="text" class="post-job-input form-control" id="jobLocation" placeholder="Location"></form:input>
								</div>
							</div>
						</div>
								
						<div class="container">
							<div class="row">
							  	<label class="col-sm-2 form-control-label" for="jobDescription">Job Description</label>
							  	<div class="post-job-description col-sm-10">
							  		<form:textarea path="description" class="form-control" id="jobDescription" rows="3" placeholder="Job Description"></form:textarea>
								</div>
							</div>
						</div>
							
						<br>				
						<div class="container">
							<div class="row">
								<label class="col-sm-2 form-control-label">Job Category</label>
								<form:input id="selectedCategory" type="hidden" path="categoryId"></form:input>
								<div class="category-list-container form-group col-sm-10">
									<div id='0T'>
									</div>
								</div>
							</div>
						</div>
						
						<form:input path="userId" value="${user.userId}" type="hidden"></form:input>
					  	
					  	
						<div class="form-group row">
							<div class="col-sm-offset-2 col-sm-10">
								<button type="submit" class="btn btn-secondary">Post Job</button>
							</div>
						</div>
						
					</form:form>	
				</div>
			  </div>
			</div>
		</div>
	</body>

<script>

	//******************************************************************
	//I'm thinking the seed categorie should be hardcoded so page load is more elegant
	//******************************************************************
	
	//Get the seed categories.
	//Seed categories are sub categories to a category with id=0
	getCategoriesBySuperCat('0', function(response, elementId){
		
		appendFirstLevelCategories_PostJob(elementId, response, function(){

			var cats = $('#' + elementId + 'T').find('li');
			for(var i = 0; i < cats.length; i++){
				
				getCategoriesBySuperCat(cats[i].id, function(response, elementId){
					appendFirstLevelCategories_PostJob(elementId, response, function(){

					})
				})
			}	
		})
	})	
	
</script>
	
<%@ include file="./includes/Footer.jsp" %>