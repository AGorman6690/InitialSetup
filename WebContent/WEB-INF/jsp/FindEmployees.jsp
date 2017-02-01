<%@ include file="./includes/Header.jsp" %>
<head>
	<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/findEmployees.css" />
<!-- 	<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/calendar.css" /> -->
	<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/datepicker.css" />
	<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/table.css" />
	<link rel="stylesheet" type="text/css"	href="/JobSearch/static/css/inputValidation.css" />
		
	<script src="<c:url value="/static/javascript/InputValidation.js" />"></script>	
	<script src="<c:url value="/static/javascript/Utilities.js" />"></script>
	<script   src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"   integrity="sha256-T0Vest3yCU7pafRw9r+settMBX6JkKN06dqBnpQ8d30="   crossorigin="anonymous"></script>
</head>

<body>
	<div class="container">
		<div class="section">
			<div class="header">
				<span data-toggle-id="findEmployeesContainer" class="glyphicon glyphicon-menu-down"></span>
				<span class="header-text">Find Employees</span>
			</div>
			<div id="findEmployeesContainer" >
				<div id="getEmployeesContainer" class="section-body">
					<button id="getEmployees" class="square-button-green">Find Employees</button>
				</div>
				<div class="section-body">
					<div class="sub-header">
						<span data-toggle-id="locationContainer" class="glyphicon glyphicon-menu-down"></span>
						<h4>Location</h4>
						<div class="bottom-border-thinner">
							<div class="body-element-container " id="locationContainer">
								<input id="miles" placeholder="Number of" class="form-control" value="">
								<span id="milesFromText">Miles From</span>
								<input id="city" placeholder="City" class="form-control">
								<input id="state" placeholder="State" class="form-control">
								<input id="zipCode" placeholder="Zip Code" class="form-control" value="">
							</div>
						</div>
					</div>
	
				</div>
				<div class="section-body">
					<div class="sub-header">
						<span data-toggle-id="availabilityContainer" class="glyphicon glyphicon-menu-down"></span>
						<h4>Availability</h4>	
						<div class="bottom-border-thinner">
							<div class="body-element-container" id="availabilityContainer">	
								<div id="calendar"></div>
								<div id="clearCalendarContainer">
									<button class="square-button" id="clearCalendar">Clear</button>
								</div>
							</div>
						</div>
					</div>
				</div>
				
				<div class="section-body">
					<div class="sub-header">
						<span data-toggle-id="ratingContainer" class="glyphicon glyphicon-menu-down"></span>
						<h4>Rating</h4>	
						<div class="bottom-border-thinner">
							<div id="ratingContainer" class="body-element-container">	
								<div id="invalidRating" class="invalid-message" data-message-for="rating"></div>
<!-- 								<input id="rating" placeholder="" class="form-control"> -->
									<label for="rating"	class="form-control-label">Greater Than Or Equal To </label>									
									<select id="rating" name="state" class="form-control">
										<option selected value="0">0</option>
										<option value="1">1</option>
										<option value="2">2</option>
										<option value="3">3</option>
										<option value="4">4</option>
									</select>	
							</div>
						</div>
					</div>
				</div>			
				
				<div class="section-body">
					<div class="sub-header">
						<span data-toggle-id="categoriesContainer" class="glyphicon glyphicon-menu-down"></span>
						<h4>Categories</h4>
						<div class="bottom-border-thinner">
							<div class="body-element-container" id="categoriesContainer">
							</div>
						</div>
					</div>
	
				</div>			
			
			</div>
		</div>
		

		<div id="resultsContainer" class="section">
			<div class="header">
				<span class="header-text">Results</span>
			</div>
			<div id="results" class="section-body">
				
			</div>
		</div>
		
		
		
	</div><!-- end container -->



</body>


<script>

	var days = [];
	$(document).ready(function(){
		var dateToday = new Date();
		$("#calendar").datepicker({
  	      numberOfMonths: 2,
  	      minDate: dateToday,
//   	      showButtonPanel: true,
//   	      multidate: true,
			  onSelect:function(dateText){
				var date = new Date(dateText);
				
				if(isDayAlreadyAdded(date.getTime(), days)){
					days = removeDate(date.getTime(), days); 
	        	}
	        	else{
	        		days.push(date.getTime());  
	        	}
				
				
			  },
  	      beforeShowDay:function(date){
  	    	  
				if(isDayAlreadyAdded(date.getTime(), days)){
	        		return [true, "active111"]; 
	        	}
	        	else{
	        		return [true, ""];
	        	}
      	
  	    	  

  	      },
    	
  	      
  	    });
		
		$("#getEmployees").click(function(){
			getEmployees();
		})
		
		$("#clearCalendar").click(function(){
			removeActiveDaysFormatting();
			days = [];
		})

	}) 

	function getListParameter(paramName, arr){
	
		var result = "";
		var i;
		
		if(arr.length == 0){
			result = paramName + "=-1";
		}
		else{		
			for(i=0; i<arr.length; i++){
				
				if(i > 0){
					result += "&" ;
				}			
				result += paramName + "=" + arr[i];
			}
		}
		return result;
		
	}
	
	function covertDatesFromMillisecondsToString(dates){
		var date;
		var newArr = [];
		$.each(dates, function(){
			date = new Date;
			date.setTime(this);		
			date = $.datepicker.formatDate("yy-mm-dd", date);
			newArr.push(date);
		})
		
		return newArr;
		
	}
	
	function getEmployees(){
		
		var stringDates = [];
		var param;
		var findEmployeesDto = {}
		findEmployeesDto.radius = $("#miles").val();
		findEmployeesDto.fromAddress = $("#city").val() + " " + $("#state").val() + $("#zipCode").val();
		findEmployeesDto.rating = $("#rating").val();
		
		//Convert the dates from millisecond to a string.
		//The database needs dates in yy-mm-dd format.
		stringDates = covertDatesFromMillisecondsToString(days);
		findEmployeesDto.days = stringDates;
		
		findEmployeesDto.categoryIds = [];
		findEmployeesDto.categoryIds[0] = 1;
		findEmployeesDto.categoryIds[1] = 2;
		findEmployeesDto.categoryIds[2] = 3;
		
		if(areInputsValid(findEmployeesDto) == 1){
			
			param = "?fromAddress=" + findEmployeesDto.fromAddress;
			param += "&radius=" + findEmployeesDto.radius;
			param += "&rating=" + findEmployeesDto.rating; 
			param += "&" + getListParameter("day", findEmployeesDto.days);
			param += "&" + getListParameter("categoryId", findEmployeesDto.categoryIds);			
			
			$("html").addClass("waiting");
			$.ajax({
				type : "GET",
				url : environmentVariables.LaborVaultHost + "/JobSearch/search/employees" + param,
				headers : getAjaxHeaders(),
				contentType : "application/json",
// 				data : JSON.stringify(findEmployeesDto)
			}).done(function(html) { 		
// 				alert(html)
				$("#results").empty();
				$("#results").append(html);
				scrollToElement("resultsContainer", 500);
				$("html").removeClass("waiting");
			}).error(function() {
				$("html").removeClass("waiting");
			});			
		}
			
	}
	
	function areInputsValid(findEmployeesDto){
		
		var $errorMessage;
		var $e;
		var value;
		var result = 1;
		
		//*****Required*****
		//Radius
		$e = $("#miles")
		value = $e.val()
		if($.isNumeric(value) == 0){
			result = 0;
			setInvalidCss($e);
		}
		else if(value < 0){
			result = 0;
			setInvalidCss($e);
		}
		else{
			setValidCss($e)
		}
		
		//*****Required*****
		//Address
		if(findEmployeesDto.fromAddress.trim().length == 0){
			result = 0;
			setInvalidCss($("#city"));
			setInvalidCss($("#state"));
			setInvalidCss($("#zipCode"));
		}else{
			setValidCss($("#city"));
			setValidCss($("#state"));
			setValidCss($("#zipCode"));
		}
		
		//*****Optional*****
		//Rating		
		value = findEmployeesDto.rating;
		$e = $("#rating")
		$errorMessage = $("#invalidRating")
		//If the use suppled a rating 
		if(value != ""){
			
			if($.isNumeric(value) == 0){
				result = 0;
				setInvalidCss($e);
				$errorMessage.html("Rating must be numeric")
				show($errorMessage);
			}
			else if(value < 0){
				result = 0;
				setInvalidCss($e);
				$errorMessage.html("Rating must be greater than zero")
				show($errorMessage);
			}
			else{
				setValidCss($e);
				hide($errorMessage);
			}
				
		}
		else{
			setValidCss($e);
			hide($errorMessage);
		}
		return result;
		
	}

</script>



<%@ include file="./includes/Footer.jsp" %>