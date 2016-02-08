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
		<style>
			div.arrow { background:transparent url(arrows.png) no-repeat scroll 0px -16px; width:16px; height:16px; display:block;}
		</style>	
		
		
	    <script type="text/javascript">  
// 	        $(document).ready(function(){
// 	            $("#renderTable #report tr:odd").addClass("odd");
// 	            $("#renderTable #report tr:not(.odd)").hide();
// 	            $("#renderTable #report tr:first-child").show();
	            
// 	            $("#renderTable #report tr.odd").click(function(){
// 	                $(this).next("tr").toggle();
// // 	                $(this).find(".arrow").toggleClass("up");
// 	            });
// 	        });
	    </script> 		
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
	<h1>Summary of job offers</h1>
	<div id="renderTable"></div>
	
	
	<table id='report2'>
	  <tr>
	    <th>Job Name</th>
	    <th>User Name</th>
	    <th>Status</th>
	  </tr>
  	  <tr>
	    <td>1</td>
	    <td>2</td>
	    <td>3</td>
	  </tr>
	   <tr>
	    <td>1</td>
	    <td>2</td>
	    <td>3</td>
	  </tr>
	</table>

	
	<br>
	<div id="applications"></div>
	
	<br>
	<div id="activeJobsTable"></div>
	

	
	<script>

		//Get and populate user's active jobs
		getJobsByUser($("#userId").val(), function(response){
			populateJobs(response, document.getElementById("activeJobs"), 1);
		});
		
		getApplicationsByEmployer($("#userId").val(), function(response){
			//alert(JSON.stringify(response));
			renderTable(response, $("#renderTable"), function(){
				
				$("#report tr:odd").addClass("odd");
		            $("#report tr:not(.odd)").hide();
		            $("#report tr:first-child").show();
		            
		            $("#report tr.odd").click(function(){
		                $(this).next("tr").toggle();
//	 	                $(this).find(".arrow").toggleClass("up");
		            });
			})
		})
		
		getActiveJobsByUser_AppCat($("#userId").val(), function(response){
			alert(JSON.stringify(response));
			renderTable2(response, $("#activeJobsTable"), function(){
				
			})
		});
		
		
	</script>

<!-- </html> -->
<%@ include file="./includes/Footer.jsp" %>
