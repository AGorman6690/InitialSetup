
<%@ include file="./includes/Header.jsp"%>

<head>
	<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/employeeSettings.css" />
	<link rel="stylesheet" type="text/css"	href="/JobSearch/static/css/inputValidation.css " />
	
	<script src="<c:url value="/static/javascript/InputValidation.js" />"></script>
	
			
		
<!--     <link href="/JobSearch/static/External/jquery-ui.min.css" rel="stylesheet"> -->
<!--     <link href="/JobSearch/static/External/jquery.comiseo.daterangepicker.css" rel="stylesheet"> -->
<!--     <script src="/JobSearch/static/External/jquery-ui.min.js"></script> -->
<!--     <script src="/JobSearch/static/External/moment.min.js"></script> -->
<!--     <script src="/JobSearch/static/External/jquery.comiseo.daterangepicker.js"></script> -->

    
 	<!-- Time picker -->
	<link rel="stylesheet" type="text/css" href="./static/External/jquery.timepicker.css" />
	<script	src="<c:url value="/static/External/jquery.timepicker.min.js" />"></script>   
    
</head>


<body>

	<div class="container">
		<div id="header" class="bottom-border-thin">
			<h3>Settings</h3>			
			<span id="updateAvailabilityContainer">
				<button id="saveChanges" class="square-button">Save</button>
<!-- 				<button class="square-button">Cancel</button> -->
			</span>
			<p>These settings will be used by employers to find you easier.</p>
		</div>
		<div class="content-container" id="homeLocation">
			<h4>Home location</h4>
			<div class="form-group input-container bottom-border-thinner">
				<div class="input">
					<label for="homeCity"
						class="form-control-label">City</label>
					<input name="homeCity" type="text" class="form-control"
						id="homeCity" value="${user.homeCity }"></input>
				</div>
				<div class="input">
					<label for="homeState"
						class="form-control-label">State</label>
					<input name="homeState" type="text" class="form-control"
						id="homeState" value="${user.homeState }"></input>
				</div>
				<div class="input">
					<label for="homeCity"
						class="form-control-label">Zip Code</label>
					<input name="homeZipCode" type="text" class="form-control"
						id="homeZipCode" value="${user.homeZipCode }"></input>
				</div>
				
			</div>			
		</div>
		<div class="content-container" id="maxDistanceContainer">
			<h4>Max distance willing to work from home location</h4>
			<div class="form-group bottom-border-thinner">
				<div class="input-container">
					<label for="maxDistance"
						class="form-control-label">Distance</label>
						
					<c:choose>
						<c:when test="${user.maxWorkRadius == -1 }">
							<c:set var="maxWorkRadius" value="" />	
						</c:when>
						<c:otherwise>
							<c:set var="maxWorkRadius" value="${user.maxWorkRadius}" />	
						</c:otherwise>
					</c:choose>
						
					<input name="maxDistance" type="text" class="form-control" placeholder="miles"
						id="maxDistance" value="${maxWorkRadius }"></input>
				</div>	
			</div>	
		</div>
		<div class="content-container" id="minPayContainer">
			<h4>Minimum desired pay</h4>
			<div class="form-group input-container bottom-border-thinner">
				<div class="input">
					<label for="minPay"
						class="form-control-label">Amount</label>
					<c:choose>
						<c:when test="${user.minimumDesiredPay == 0 }">
							<c:set var="minDesiredPay" value="" />	
						</c:when>
						<c:otherwise>
							<c:set var="minDesiredPay" value="${user.minimumDesiredPay}" />	
						</c:otherwise>
					</c:choose>
											
					<input name="minPay" type="text" class="form-control" placeholder="$ per hour"
						id="minPay" value="${minDesiredPay }"></input>
<!-- 					<div class="checkbox" id="allowLessThanMinimumContainer"> -->
<!-- 						<label><input id="allowLessThanMinimum" type="checkbox" value="">Allow employers to offer you less</label> -->
<!-- 					</div>				 -->
				</div>	
			</div>	
		</div>		
		
		</div> <!-- close page container -->

</body>



<script>
$(document).ready(function() {
						
	$('#startTime').timepicker({
		'scrollDefault' : '7:00am'
	});					
		  
       
//       	$("#calendar").datepicker({
//       	      numberOfMonths: 3,
//       	      showButtonPanel: true,
//       	      multidate: true,
      	      
//       	    });
        	
//     $("#toggleWeekends").click(function(){				    	
//     	$("#availability").datepicker("setDates", ["09-09-2016", "09-10-2016"])
//     })
        
       
//    	$("#calendar").datepicker().on("changeDate", function(e){
   		
//    		})
	
	$("#saveChanges").click(function() {
			
			var editProfileDTO = {};
			editProfileDTO.homeCity = $("#homeCity").val();
			editProfileDTO.homeState = $("#homeState").val();
			editProfileDTO.homeZipCode = $("#homeZipCode").val();
			editProfileDTO.maxWorkRadius = parseInt($("#maxDistance").val());
			editProfileDTO.minPay = $("#minPay").val();
			
			//Verify
			if(areValidSettings(editProfileDTO) == 1){
				
			

// 			editProfileDTO.categoryIds = [];
// 			var categories = $('#selectedCategories').find("button");
// 			for (var i = 0; i < categories.length; i++) {
// 				var id = categories[i].id;
// 				editProfileDTO.categoryIds
// 						.push(id
// 								.substring(
// 										0,
// 										id
// 												.indexOf("-")));
// 			}
	
				var headers = {};
				headers[$(
						"meta[name='_csrf_header']")
						.attr("content")] = $(
						"meta[name='_csrf']").attr(
						"content");
	
				$.ajax(
						{
							type : "POST",
							url : environmentVariables.LaborVaultHost + "/JobSearch/user/settings/edit",
							headers : headers,
							contentType : "application/json",
							dataType : "application/json", // Response
							data : JSON
									.stringify(editProfileDTO)
						}).done(function() {
	
						}).error(function() {
	
						});

			}
		})
		
		
		
		
	
	})
	
	function areValidSettings(editProfileDTO){
	
		var $e;
		var result = 1;
		
		//Verify max radius
		$e = $("#maxDistance");
		if($.isNumeric(editProfileDTO.maxWorkRadius) == 0 ){
			result = 0;
			setInvalidCss($e);
		}else if (editProfileDTO.maxWorkRadius <= 0){
			result = 0;
			setInvalidCss($e);
		}else{
			setValidCss($e);
		}
		
		//Verify min pay
		$e = $("#minPay");
		if($.isNumeric(editProfileDTO.minPay) == 0 ){
			result = 0;
			setInvalidCss($e);
		}else if (editProfileDTO.minPay < 0){
			result = 0;
			setInvalidCss($e);
		}else{
			setValidCss($e);
		}
		
		return result;
	
	}


</script>


<%@ include file="./includes/Footer.jsp"%>
