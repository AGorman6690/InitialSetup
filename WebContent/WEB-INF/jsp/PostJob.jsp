<%@ include file="./includes/Header.jsp" %>
<%@ include file="./includes/Header_Employer.jsp" %>

	<head>
<%-- 		<script src="<c:url value="/static/javascript/Profile.js" />"></script> --%>
<%-- 		<script src="<c:url value="/static/javascript/Jobs.js" />"></script> --%>
		<script src="<c:url value="/static/javascript/Category.js" />"></script>
<%-- 		<script src="<c:url value="/static/javascript/User.js" />"></script> --%>
<%-- 		<script src="<c:url value="/static/javascript/RateCriterion.js" />"></script> --%>
<%-- 		<script src="<c:url value="/static/javascript/Display.js" />"></script> --%>
		<script src="<c:url value="/static/javascript/Lists.js" />"></script>
		<link rel="stylesheet" type="text/css" href="./static/css/categories.css" />
		<link rel="stylesheet" type="text/css" href="./static/css/postJob.css" />
	</head>
	

	
	<body>	
		<input type="hidden" id="userId" value="${user.userId}"/>
	
		<div class="post-job-container">
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
					<div class="form-group row">
				  		<label class="col-sm-2">Job Openings</label>
						<div class="col-sm-10" >
						  	<form:select path="openings" class="post-job-openings c-select" >
								<option value="1">1</option>
								<option value="2">2</option>
								<option value="3">3</option>
								<option value="4">4</option>
								<option value="5">5</option>
								<option value="6">6</option>
								<option value="7">7</option>
								<option value="8">8</option>
								<option value="9">9</option>
								<option value="10">10</option>
							</form:select>
						</div>
					</div>
				</div>
				
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
				
				<div class="container">
					<div class="row">
					  	<label class="col-sm-2 form-control-label" for="jobDescription">Job Description</label>
					  	<div class="post-job-description col-sm-10">
					  		<form:textarea path="description" class="form-control" id="jobDescription" rows="3"></form:textarea>
						</div>
					</div>
				</div>
				
				<form:input path="userId" value="${user.userId}" type="hidden"></form:input>
			  	
			  	<br>
				<div class="form-group row">
					<div class="col-sm-offset-2 col-sm-10">
						<button type="submit" class="btn btn-secondary">Post Job</button>
					</div>
				</div>
				
			</form:form>	
		</div>

	</body>

<script>

	//******************************************************************
	//I'm thinking the seed categorie should be hardcoded so page load is more elegant
	//******************************************************************
	
	//Get the seed categories.
	//Seed categories are sub categories to a category with id=0
	getCategoriesBySuperCat('0', function(response, elementId){
		
	//	alert('callback1 ' + elementId)
		appendFirstLevelCategories_PostJob(elementId, response, function(){
// 			alert('appending to ' + elementId)
			//alert(JSON.stringify(response))
			
			var cats = $('#' + elementId + 'T').find('li');
			for(var i = 0; i < cats.length; i++){
				getCategoriesBySuperCat(cats[i].id, function(response, elementId){
// 					alert('callback2 ' + elementId)
					appendFirstLevelCategories_PostJob(elementId, response, function(){

					})
				})
			}	
		})
	})	
	
</script>
	
<%@ include file="./includes/Footer.jsp" %>