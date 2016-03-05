<%@ include file="./includes/Header.jsp" %>
<!-- <html xmlns="http://www.w3.org/1999/xhtml" > -->
	<head>
		<script src="<c:url value="/static/javascript/Profile.js" />"></script>
		<script src="<c:url value="/static/javascript/Jobs.js" />"></script>
		<script src="<c:url value="/static/javascript/Category.js" />"></script>
		<script src="<c:url value="/static/javascript/User.js" />"></script>
		<script src="<c:url value="/static/javascript/RateCriterion.js" />"></script>
		<script src="<c:url value="/static/javascript/Ratings.js" />"></script>
		<script src="<c:url value="/static/javascript/Display.js" />"></script>
		<script src="<c:url value="/static/javascript/Application.js" />"></script>

		<link rel="stylesheet" type="text/css" href="./static/css/expandingTable.css" />	
		<link rel="stylesheet" type="text/css" href="./static/css/ratings.css" />
	</head> 
	
	
	
	
<!-- 	Store the user's id in an element as opposed to accessing it through a model object.
	 Model objects are not within an external js file's scope-->
	<input id='userId' type='hidden' value="${user.userId}">
 
	<h1>Active jobs</h1>
	<select multiple id="activeJobs" style= "width: 200px">
	</select>
	
	<h1>Applicants for the selected job</h1>	
	<select multiple id="applicants" style="width: 200px">
	</select>
	
	<br>
	<button id ="hireApplicant" type="button">Extend offer to applicant</button>
	
	<br>
	<h1>Outstanding offers</h1>
	<select multiple id="offeredApplicants" style="width: 200px">
	</select>

	<br>
	<br>
	<h1>Job applicant summary</h1>
	
	<br>
	<div id="activeJobsTable_div"></div>
	

	
	<script>

		//Get and populate user's active jobs
		getJobsByUser($("#userId").val(), function(response){
			populateJobs(response, document.getElementById("activeJobs"), 1);
		});
		
		
		getActiveJobsByUser_AppCat($("#userId").val(), function(response){

			renderTable2(response, $("#activeJobsTable_div"), function(){
				
				 $(".job").click(function(){
	                $(this).next(".applicants").toggle();
// 	                $(this).find(".arrow").toggleClass("up");
	            });

				$(".applicants").hide();
			})
		});
		
		
	</script>

<!-- </html> -->
<%@ include file="./includes/Footer.jsp" %>
