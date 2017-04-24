<%@ include file="../includes/Header.jsp" %>
<%-- <%@ include file="../includes/resources/DatePicker.jsp" %> --%>


<!-- Order is specific -->
<!-- ************************************************* -->
<script src="<c:url value="/static/javascript/Utilities/Checkboxes.js" />"></script>	
<script src="<c:url value="/static/javascript/availability_employee/Availability.js" />"></script>	
<!-- ************************************************* -->

<script src="<c:url value="/static/javascript/availability_employee/AjaxCall.js" />"></script>
<link rel="stylesheet" type="text/css"	href="/JobSearch/static/css/availability.css" />	



<div class="container">
	<div id="availableDays">
		<%@ include file="./AvailableDays_CurrentlySet.jsp" %>
	</div>
	<div id="monthsContainer" class="container checkbox-container">
		<h5>Select one or more months</h5>
		<div class="row">
			<div class="col-sm-12"><label><input id="bbb" class="select-all" type="checkbox" value="">Select all months</label></div>	
		</div>		
		<div>		
			<div class="row options">
				<div class="col-sm-3"><label><input id="j" type="checkbox" data-month="0" data-cal-id="januaryCal-TempDisabled">January</label></div>	
				<div class="col-sm-3"><label><input id="" type="checkbox" data-month="3" data-cal-id="aprilCal" >April</label></div>	
				<div class="col-sm-3"><label><input id="" type="checkbox" data-month="6" data-cal-id="julyCal">July</label></div>
				<div class="col-sm-3"><label><input id="" type="checkbox" data-month="9" data-cal-id="octoberCal">October</label></div>
			</div>	
			<div class="row options">
				<div class="col-sm-3"><label><input id="f" type="checkbox" data-month="1" data-cal-id="februaryCal">February</label></div>	
				<div class="col-sm-3"><label><input id="" type="checkbox" data-month="4" data-cal-id="mayCal">May</label></div>	
				<div class="col-sm-3"><label><input id="" type="checkbox" data-month="7" data-cal-id="augustCal">August</label></div>
				<div class="col-sm-3"><label><input id="" type="checkbox" data-month="10" data-cal-id="novemberCal">November</label></div>
			</div>
			<div class="row options">
				<div class="col-sm-3"><label><input id="m" type="checkbox" data-month="2" data-cal-id="marchCal"value="">March</label></div>	
				<div class="col-sm-3"><label><input id="" type="checkbox" data-month="5" data-cal-id="juneCal">June</label></div>	
				<div class="col-sm-3"><label><input id="" type="checkbox" data-month="8" data-cal-id="septemberCal">September</label></div>
				<div class="col-sm-3"><label><input id="" type="checkbox" data-month="11" data-cal-id="decemberCal">December</label></div>
			</div>			
		</div>	
		<div class="row">
			<div class="col-sm-12">
				<div id="setSaveCont">
<!-- 					<button id="setAvailability" class="sqr-btn not-clickable">Set Availability</button> -->
<!-- 					<button id="saveAvailability" class="sqr-btn not-clickable">Save Availability</button> -->
<!-- 					<button id="cancel" class="sqr-btn not-clickable">Cancel</button> -->
					<span id="setAvailability" class="accent not-clickable disabled">Set Availability</span>
<!-- 					<span id="saveAvailability" class="accent not-clickable disabled">Save</span> -->
<!-- 					<span id="cancel" class="accent not-clickable disabled">Cancel</span> -->

				</div>
			</div>
		</div>	
	</div>	
	
	<div id="availabilityCont">
		<div id="daysContainer" class="container checkbox-container">
			<h5>(this is set by the month checkbox change event)</h5>
			<div id="saveCancel">
				<span id="saveAvailability" class="accent not-clickable disabled">Save</span>
				<span id="cancel" class="accent not-clickable disabled">Cancel</span>
			</div> 
			<div class="row">
				<div class="col-sm-12"><label><input id="removeCurrentAvailability" class="" type="checkbox" value="">Remove saved availability</label></div>	
			</div>				
			<div class="row">
				<div class="col-sm-12"><label><input id="january" class="select-all" type="checkbox" value="">Select all days</label></div>	
			</div>		
		<!-- 	<div>		 -->
		<!-- 		<div class="row options"> -->
		<!-- 			<div class="col-sm-3"><label><input id="sundays" type="checkbox" data-day-of-week="0">Sunday</label></div>	 -->
		<!-- 			<div class="col-sm-3"><label><input id="february" type="checkbox" data-day-of-week="1">Monday</label></div>	 -->
		<!-- 			<div class="col-sm-3"><label><input id="march" type="checkbox" data-day-of-week="2">Tuesday</label></div> -->
		<!-- 			<div class="col-sm-3"><label><input id="march" type="checkbox" data-day-of-week="3">Wednesday</label></div> -->
		<!-- 		</div>	 -->
		<!-- 		<div class="row options"> -->
		<!-- 			<div class="col-sm-3"><label><input id="january" type="checkbox" data-day-of-week="4">Thursday</label></div>	 -->
		<!-- 			<div class="col-sm-3"><label><input id="february" type="checkbox" data-day-of-week="5">Friday</label></div>	 -->
		<!-- 			<div class="col-sm-3"><label><input id="march" type="checkbox" data-day-of-week="6">Saturday</label></div> -->
		<!-- 			<div class="col-sm-3"></div> -->
		<!-- 		</div> -->
		<!-- 	</div> -->
			<div>		
				<div class="row options">
					<div class=""><label><input id="sundays" type="checkbox" data-day-of-week="0">Sundays</label></div>	
					<div class=""><label><input id="february" type="checkbox" data-day-of-week="1">Mondays</label></div>	
					<div class=""><label><input id="march" type="checkbox" data-day-of-week="2">Tuesdays</label></div>
					<div class=""><label><input id="march" type="checkbox" data-day-of-week="3">Wednesdays</label></div>
					<div class=""><label><input id="january" type="checkbox" data-day-of-week="4">Thursdays</label></div>	
					<div class=""><label><input id="february" type="checkbox" data-day-of-week="5">Fridays</label></div>	
					<div class=""><label><input id="march" type="checkbox" data-day-of-week="6">Saturdays</label></div>
				
				</div>	
			</div>
			<div id="legendContainer">
				<div>
					<span id="legend_unsaved" class="legend"></span><span>Unsaved Availability</span>
				</div>
				<div>
					<span id="legend_saved" class="legend"></span>Saved Availability
				</div>
			</div>			
		</div>			

		<div id="calendarContainers" class="container">
<!-- 			<div id="januaryCal" class="calendar-container" > -->
<!-- 				<div class="calendar" data-min-date="01/01/2017"></div> -->
<!-- 			</div> -->
			<div id="februaryCal" class="calendar-container" >
				<div class="calendar" data-min-date="02/01/2017"></div>
			</div>	
			<div id="marchCal" class="calendar-container" >
				<div class="calendar" data-min-date="03/01/2017"></div>
			</div>		
			<div id="aprilCal" class="calendar-container" >
				<div class="calendar" data-min-date="04/01/2017"></div>
			</div>	
			<div id="mayCal" class="calendar-container" >
				<div class="calendar" data-min-date="05/01/2017"></div>
			</div>	
			<div id="juneCal" class="calendar-container" >
				<div class="calendar" data-min-date="06/01/2017"></div>
			</div>		
			<div id="julyCal" class="calendar-container" >
				<div class="calendar" data-min-date="07/01/2017"></div>
			</div>		
			<div id="augustCal" class="calendar-container" >
				<div class="calendar" data-min-date="08/01/2017"></div>
			</div>		
			<div id="septemberCal" class="calendar-container" >
				<div class="calendar" data-min-date="09/01/2017"></div>
			</div>		
			<div id="octoberCal" class="calendar-container" >
				<div class="calendar" data-min-date="10/01/2017"></div>
			</div>	
			<div id="novemberCal" class="calendar-container" >
				<div class="calendar" data-min-date="11/01/2017"></div>
			</div>		
			<div id="decemberCal" class="calendar-container" >
				<div class="calendar" data-min-date="12/01/2017"></div>
			</div>	
						
		</div>
	</div>
</div>