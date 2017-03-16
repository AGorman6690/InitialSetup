<%@ include file="../includes/Header.jsp"%>
<%@ include file="../includes/resources/DatePicker.jsp"%>
<%@ include file="../includes/resources/Modal.jsp"%>
<%@ include file="../includes/resources/InputValidation.jsp"%>


<script src="/JobSearch/static/javascript/Utilities/FormUtilities.js" type="text/javascript"></script>
<link href="/JobSearch/static/css/Templates/forms.css" rel="stylesheet" />
<link href="/JobSearch/static/css/table.css" rel="stylesheet" />

<script src="<c:url value="/static/javascript/find_employees/FindEmployees.js" />"></script>
<link href="/JobSearch/static/css/find_employees/findEmployees.css" rel="stylesheet" />
<link href="/JobSearch/static/css/find_employees/make_offer_modal.css" rel="stylesheet" />


<div class="container">
	
	<div id="filtersContainer">
		<c:if test="${!empty jobDtos_current}">
			<div id="loadCurrentJobContainer" class="filter">
				<h3>Current Jobs</h3>
				<div class="filter-value">
					<select>
						<option selected disabled>Select a job</option>
						<c:forEach items="${jobDtos_current }" var="jobDto">
							<option data-job-id="${jobDto.job.id }">${jobDto.job.jobName }</option>
						</c:forEach>
					</select>
				</div>
			</div>
		</c:if>
		<div id="locationFilterContainer" class="filter">
			<h3>Location</h3>
			<div id="location" class="filter-value">
				<input id="street" type="text" placeholder="Street">
				<input id="city" type="text" placeholder="City">
				<select id="state"></select>
				<input id="zipCode" type="text" placeholder="Zip Code" value="55119">
			</div>
		</div>
		<div id="availabilityFilterContainer" class="filter">
			<h3>Availability</h3>
<!-- 			<div class="filter-value"> -->
<!-- 				<label><input id="partialAvailabilityAllowed" type="checkbox">Partial Availability Allowed</label> -->
<!-- 			</div> -->
			<div class="calendar-container filter-value">
				<div id="availabilityCalendar" class="calendar">
				</div>
				<button class="clear-calendar">Clear</button>
			</div>
		</div>
		<div><button id="findEmployees">Get Employees</button></div>
		<div id="categoriesFilterContainer" class="filter">
			<h3>Categories</h3>
			<div  class="filter-value"></div>
		</div>	
		
	</div>
	<div id="resultsContainer">
		<h3>Results</h3>
		<div id="results">
		
		</div>
	</div>
</div>	


<%@ include file="./MakeOfferModal.jsp"%>

<%@ include file="../includes/Footer.jsp"%>