<%@ include file="./includes/Header.jsp"%>


	<script src="<c:url value="/static/javascript/Category.js" />"></script>
	<script src="<c:url value="/static/javascript/Utilities.js" />"></script>
	<script src="<c:url value="/static/javascript/InputValidation.js" />"></script>
	<script src="<c:url value="/static/javascript/DatePickerUtilities_generalized.js" />"></script>
<%-- 	<script src="<c:url value="/static/javascript/FindJobs.js" />"></script> --%>


	<script src="<c:url value="/static/javascript/find_jobs/Filters.js" />"></script>
	<script src="<c:url value="/static/javascript/find_jobs/AjaxCall.js" />"></script>
	<script src="<c:url value="/static/javascript/find_jobs/Map.js" />"></script>
	<script src="<c:url value="/static/javascript/find_jobs/MapInteraction.js" />"></script>
	<script src="<c:url value="/static/javascript/Utilities/FormUtilities.js" />"></script>
	
	<link rel="stylesheet" type="text/css"	href="../static/css/inputValidation.css" />
	<link rel="stylesheet" type="text/css"	href="/JobSearch/static/css/findJobs.css" />
	<link rel="stylesheet" type="text/css"	href="../static/css/datepicker.css" />
	<link rel="stylesheet" type="text/css"	href="../static/css/calendar.css" />
	
	<link rel="stylesheet" type="text/css"	href="../static/css/find_jobs/sortBy.css" />

	<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js" integrity="sha256-T0Vest3yCU7pafRw9r+settMBX6JkKN06dqBnpQ8d30="   crossorigin="anonymous"></script>

</head>

<body>
	<div class="container">
		<div id="filtersContainer">
			<div><%@ include file="./find_jobs/Filters.jsp" %></div>
		</div>
		<div id="mainBottom">
			<div class="row" >
				<div id="filteredJobsContainer" class="col-sm-4">
					<div id="sortByContainer"><%@ include file="./find_jobs/SortBy.jsp" %></div>
					<div id="filteredJobs"></div>					
				</div>
				<div id="mapContainer" class="col-sm-8">				
					<div id="map" class="right-border"></div>				
				</div>		
			</div>
		</div>
	</div>

		
<!-- 		<script async defer -->
	<script
		src="https://maps.googleapis.com/maps/api/js?key=AIzaSyAXc_OBQbJCEfhCkBju2_5IfjPqOYRKacI&callback=initMap">
	</script>

	<script>
	
	
	
		$(document).ready(function(){
			
			setTimeOptions($("#startTimeOptions"), 60);
			setTimeOptions($("#endTimeOptions"), 60);
	
	// 		$("#clearSession").click(function(){
	// 			sessionStorage.clear();
	// 		})
	
			
	// 		if(sessionStorage.doStoreFilteredJobs == "1"){
	// 			$("#filteredJobs").html(sessionStorage.filteredJobs);
	// 			setMap();				
	// 			$("#mainBottom").show();
	// 			$("#filterContainer").empty();
	// 			$("#filterContainer").append(sessionStorage.filters);
	// 		}
	// 		else{
	// 		}
	
		})
	
	
			
		
	
		
		function setTimeOptions($eSelect, increment){
			
			if(increment > 0){
					
				$eSelect.empty();
				$eSelect.append('<option value="" selected style="display: none"></option>');
				
				var hourCount;
				var hour;
				var minute;
				var modifiedMinute;
				var amPm;
				var time;
				//Hour
				hour = 12;
				for(hourCount = 1; hourCount < 25; hourCount++){
	
					//Am or pm
					if(hourCount <= 12){
						amPm = "am";
					}else{
						amPm = "pm"
					}
					
					//Minute
					for(minute = 0; minute < 60; minute += increment){
	// 					alert("minute: " + minute + " " + increment)
						if(minute < 10){
							modifiedMinute = "0" + minute;	
						}else{
							modifiedMinute = minute;
						}	
						
						time = hour + ":" + modifiedMinute + amPm;
						$eSelect.append("<option data-filter-value='" + formatTime(time) + "'>"
											+ time + "</option>");
					}
					
					//Incerment the hour
					if(hour == 12){
						hour = 1;
					}else{
						hour ++;
					}				 
				}
			}
		}
	
		
		function getFilterParameters(){
			
			var params = "";
			var filterValue;
			var sortByRadio;
			
			
			//Distance filter			
			var address = $.trim($("#city").val() + " "
									+ $("#state").val() + " " + $("#zipCode").val());	
			
			params += "?radius=" + $("#radius").val();;
			params += "&fromAddress=" + address;
	
			
			//Loop through each additional filter
			$(".additional-filter").each(function(){
				
				//Check select filters
				$(this).find(".select-container select").each(function(){
					//Get the value of the selected option
					filterValue = $($(this).find("option:selected")[0]).data("filter-value");
					if(filterValue != null){
						params += "&" + $(this).data("filter-dto-prop");
						params += "=" + filterValue;
					}
				})
				
				//Check radio
				$(this).find("input[type=radio]").each(function(){
					if($(this).is(":checked")){
						params += "&" + $(this).data("filter-dto-prop");
						params += "=" + $(this).data("filter-value");
					}
				})
				
				//Check text inputs
				$(this).find("input[type=text]").each(function(){
					filterValue = $(this).val();
					if(filterValue != ""){
						params += "&" + $(this).data("filter-dto-prop");
						params += "=" + filterValue;
					}
				})		
				
			})
			
	// 		//Check if data should be sorted
	// 		sortByRadio = $("input[name=sort]").filter(":checked")[0];
	// 		if(sortByRadio != null){
	// 			params += "&" + getSortByParam();
	// 		}
			
			
			
				//Category ids
	// 			var categoryIds = getCategoryIds("selectedCategories");
	// 			if (categoryIds.length > 0){		
	// 				for (i = 0; i < categoryIds.length; i++) {
	// 					params += '&categoryId=' + categoryIds[i];
	// 				}
	// 			}else params += "&categoryId=-1";
			params += "&categoryId=-1";
				
	// 			// Working days
	// 			var days = [];
	// 			var days = $("#workingDays").datepicker('getDates');;
	// 			if (days.length > 0){		
	// 				for (i = 0; i < days.length; i++) {
	// 					params += '&day=' + days[i];
	// 				}
	// 			}else params += "&day=-1";
			params += "&day=-1";
			
			return params;
		}
		
		function getSortByParam($sortByRadio){
			var sortByParam;
			sortByParam = "sortBy=" + $sortByRadio.data("col");
			sortByParam += "&isAscending=" + $sortByRadio.data("is-ascending");
			
			return sortByParam;
		}
		
		
		function appendFilteredJobs() {
			
			var params = getFilterParameters();
			params += "&isAppendingJobs=1";
		
			$.ajax({
				type : "GET",
					url: environmentVariables.LaborVaultHost + '/JobSearch/jobs/filter' + params,
					success : _success,
					error : _error
				});
	
				function _success(response) {
					//This will return a velocity template
					
					var doAppend = 0;
					
					//If the returned html is the "No Jobs" message 
					if(response.indexOf('id="noJobs"') > -1){
						
						
						//AND some jobs have already been posted, then do not show
						//the "No Jobs" message again.
						if($("#filteredJobs").find(".job").length > 0){
							doAppend = 0;
						}else{
							doAppend = 1;
						}
						
					//Else new jobs were returned
					}else{
						doAppend = 1;
					}
		
					if(doAppend){
						$("#filteredJobs").append(response);
						setMap();	
					}
						
				}	
	
				function _error(response) {
	// 				alert('DEBUG: error append filter jobs')
				}
		}
		
		function sortLoadedJobs(){
			
			var $sortByRadio = $($("input[name=sort]").filter(":checked")[0]);
			var params = "?" + getSortByParam($sortByRadio);
			params += "&" + getRequestedLatAndLngParams();
			
			$.ajax({
				type : "GET",
					url: environmentVariables.LaborVaultHost + '/JobSearch/jobs/sort' + params,
					success : _success,
					error : _error,
					cache: true
				});
	
				function _success(response) {
					//This will return a velocity template
					
					$("#filteredJobs").html(response);		
										
				}	
	
				function _error(response) {
	// 				alert('DEBUG: error set filter jobs')
				}
		}
		
		
	
		
		
	</script>

 

<%@ include file="./includes/Footer.jsp"%>