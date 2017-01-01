
<%@ include file="./includes/Header.jsp"%>

<head>
<link rel="stylesheet" type="text/css"
	href="/JobSearch/static/css/employeeSettings.css" />
<link rel="stylesheet" type="text/css"
	href="/JobSearch/static/css/inputValidation.css " />

<script src="<c:url value="/static/javascript/InputValidation.js" />"></script>

<script src="<c:url value="/static/javascript/User.js" />"></script>


<!--     <link href="/JobSearch/static/External/jquery-ui.min.css" rel="stylesheet"> -->
<!--     <link href="/JobSearch/static/External/jquery.comiseo.daterangepicker.css" rel="stylesheet"> -->
<!--     <script src="/JobSearch/static/External/jquery-ui.min.js"></script> -->
<!--     <script src="/JobSearch/static/External/moment.min.js"></script> -->
<!--     <script src="/JobSearch/static/External/jquery.comiseo.daterangepicker.js"></script> -->


<!-- Time picker -->
<link rel="stylesheet" type="text/css"
	href="./static/External/jquery.timepicker.css" />
<script
	src="<c:url value="/static/External/jquery.timepicker.min.js" />"></script>

</head>


<body>

	<div class="container">
		<div id="header" class="bottom-border-thin">
			<h3>Settings</h3>
			<span id="updateAvailabilityContainer">
				<button id="saveChanges" class="square-button">Save</button> <!-- 				<button class="square-button">Cancel</button> -->
			</span>
			<p>These settings will be used by employers to find you easier.</p>
		</div>
		<div class="content-container" id="homeLocation">
			<h4>Home location</h4>
			<div class="form-group input-container bottom-border-thinner">
				<div class="input">
					<label for="homeCity" class="form-control-label">City</label> <input
						name="homeCity" type="text" class="form-control" id="homeCity"
						value="${user.homeCity }"></input>
				</div>
				<div class="input">
					<label for="homeState" class="form-control-label">State</label> <input
						name="homeState" type="text" class="form-control" id="homeState"
						value="${user.homeState }"></input>
				</div>
				<div class="input">
					<label for="homeCity" class="form-control-label">Zip Code</label> <input
						name="homeZipCode" type="text" class="form-control"
						id="homeZipCode" value="${user.homeZipCode }"></input>
				</div>

			</div>
		</div>
		<div class="content-container" id="maxDistanceContainer">
			<h4>Max distance willing to work from home location</h4>
			<div class="form-group bottom-border-thinner">
				<div class="input-container">
					<label for="maxDistance" class="form-control-label">Distance</label>

					<c:choose>
						<c:when test="${user.maxWorkRadius == -1 }">
							<c:set var="maxWorkRadius" value="" />
						</c:when>
						<c:otherwise>
							<c:set var="maxWorkRadius" value="${user.maxWorkRadius}" />
						</c:otherwise>
					</c:choose>

					<input name="maxDistance" type="text" class="form-control"
						placeholder="miles" id="maxDistance" value="${maxWorkRadius }"></input>
				</div>
			</div>
		</div>
		<div class="content-container" id="minPayContainer">
			<h4>Minimum desired pay</h4>
			<div class="form-group input-container bottom-border-thinner">
				<div class="input">
					<label for="minPay" class="form-control-label">Amount</label>
					<c:choose>
						<c:when test="${user.minimumDesiredPay == 0 }">
							<c:set var="minDesiredPay" value="" />
						</c:when>
						<c:otherwise>
							<c:set var="minDesiredPay" value="${user.minimumDesiredPay}" />
						</c:otherwise>
					</c:choose>

					<input name="minPay" type="text" class="form-control"
						placeholder="$ per hour" id="minPay" value="${minDesiredPay }"></input>
					<!-- 					<div class="checkbox" id="allowLessThanMinimumContainer"> -->
					<!-- 						<label><input id="allowLessThanMinimum" type="checkbox" value="">Allow employers to offer you less</label> -->
					<!-- 					</div>				 -->
				</div>
			</div>
		</div>
		<div class="content-container" id="availabilityContainer">
			<h4>Availability</h4>
			(Not built yet)
			<div class="input-container">
				<div class="container" id="daysContainer">
					<h5>Days</h5>
					<div class="row select-all checkbox date-time-container">
						<div class="col-sm-12">
							<label><input id="january" type="checkbox" value="">Select
								all</label>
						</div>
					</div>
					<div class="row checkbox date-time-container">
						<div class="col-sm-3">
							<label><input id="january" type="checkbox" value="">Sunday</label>
						</div>
						<div class="col-sm-3">
							<label><input id="february" type="checkbox" value="">Monday</label>
						</div>
						<div class="col-sm-3">
							<label><input id="march" type="checkbox" value="">Tuesday</label>
						</div>
						<div class="col-sm-3">
							<label><input id="march" type="checkbox" value="">Wednesday</label>
						</div>
					</div>
					<div class="row checkbox date-time-container">
						<div class="col-sm-3">
							<label><input id="january" type="checkbox" value="">Thursday</label>
						</div>
						<div class="col-sm-3">
							<label><input id="february" type="checkbox" value="">Friday</label>
						</div>
						<div class="col-sm-3">
							<label><input id="march" type="checkbox" value="">Saturday</label>
						</div>
						<div class="col-sm-3"></div>
					</div>

				</div>

				<div class="container" id="monthsContainer">
					<h5>Months</h5>
					<div class="row select-all checkbox date-time-container">
						<div class="col-sm-12">
							<label><input id="january" type="checkbox" value="">Select
								all</label>
						</div>
					</div>
					<div class="row checkbox date-time-container">
						<div class="col-sm-3">
							<label><input id="january" type="checkbox" value="">January</label>
						</div>
						<div class="col-sm-3">
							<label><input id="february" type="checkbox" value="">April</label>
						</div>
						<div class="col-sm-3">
							<label><input id="march" type="checkbox" value="">July</label>
						</div>
						<div class="col-sm-3">
							<label><input id="march" type="checkbox" value="">October</label>
						</div>
					</div>
					<div class="row checkbox date-time-container">
						<div class="col-sm-3">
							<label><input id="january" type="checkbox" value="">February</label>
						</div>
						<div class="col-sm-3">
							<label><input id="february" type="checkbox" value="">May</label>
						</div>
						<div class="col-sm-3">
							<label><input id="march" type="checkbox" value="">August</label>
						</div>
						<div class="col-sm-3">
							<label><input id="march" type="checkbox" value="">November</label>
						</div>
					</div>
					<div class="row checkbox date-time-container">
						<div class="col-sm-3">
							<label><input id="january" type="checkbox" value="">March</label>
						</div>
						<div class="col-sm-3">
							<label><input id="february" type="checkbox" value="">June</label>
						</div>
						<div class="col-sm-3">
							<label><input id="march" type="checkbox" value="">September</label>
						</div>
						<div class="col-sm-3">
							<label><input id="march" type="checkbox" value="">December</label>
						</div>
					</div>
				</div>

				<div class="container" id="yearsContainer">
					<h5>Years</h5>
					<div class="checkbox date-time-container">
						<div>
							<label><input id="january" type="checkbox" value="">2016</label>
						</div>
						<div>
							<label><input id="february" type="checkbox" value="">2017</label>
						</div>
					</div>
				</div>
				<div class="container" id="startTimeContainer">
					<h5>Start Time</h5>
					<div class="checkbox date-time-container">
						<div>
							<label><input id="january" type="checkbox" value="">All
								day</label>
						</div>
						<div class="specify-time radio">
							<div>
								<label><input id="january" type="radio" name="startTime">Before</label>
							</div>
							<div>
								<label><input id="february" type="radio"
									name="startTime">After</label>
							</div>
							<input name="startTime" type="text"
								class="form-control time ui-timepicker-input" autocomplete="off"
								id="startTime"></input>
						</div>
					</div>
				</div>
				<div class="container" id="endTimeContainer">
					<h5>End Time</h5>
					<div class="checkbox date-time-container">
						<div>
							<label><input id="january" type="checkbox" value="">All
								day</label>
						</div>
						<div class="specify-time radio">
							<div>
								<label><input id="january" type="radio" name="endTime">Before</label>
							</div>
							<div>
								<label><input id="february" type="radio" name="endTime">After</label>
							</div>
							<input name="endTime" type="text"
								class="form-control time ui-timepicker-input" autocomplete="off"
								id="endTime"></input>
						</div>
					</div>
				</div>

				<div class="input-container" id="calendarContainer">
					<div id="calendar" class="input-group date"></div>
				</div>
			</div>
		</div>
		<!-- close availability container -->

		<div class="content-container" id="categoryContainer">
			<h4>Notify me when a job is added in following categories</h4>
			<div class="form-group bottom-border-thinner">
				<div class="input-container">
					<label for="maxDistance" class="form-control-label">Categories</label>
					<c:forEach items="${categories}" var="category">
						<br>
						<label><input type="checkbox" value="${category.getId()}" name="category" class="categoryIds" /> ${category.getName()}</label>
					</c:forEach>
				</div>
			</div>
		</div>
	</div>
	<!-- close page container -->

</body>



<script>
	$(document)
			.ready(
					function() {

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



					})

	function areValidSettings(editProfileDTO) {

		var $e;
		var result = 1;

		//Verify max radius
		$e = $("#maxDistance");
		if ($.isNumeric(editProfileDTO.maxWorkRadius) == 0) {
			result = 0;
			setInvalidCss($e);
		} else if (editProfileDTO.maxWorkRadius <= 0) {
			result = 0;
			setInvalidCss($e);
		} else {
			setValidCss($e);
		}

		//Verify min pay
		$e = $("#minPay");
		if ($.isNumeric(editProfileDTO.minPay) == 0) {
			result = 0;
			setInvalidCss($e);
		} else if (editProfileDTO.minPay < 0) {
			result = 0;
			setInvalidCss($e);
		} else {
			setValidCss($e);
		}

		return result;

	}
</script>


<%@ include file="./includes/Footer.jsp"%>
