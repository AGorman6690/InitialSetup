<%@ include file="../includes/Header.jsp"%>
<%@ include file="../includes/resources/TableFilter.jsp" %>
<%@ include file="../includes/resources/DatePicker.jsp" %>

<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/credentials/credentials.css" />
<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/credentials/calendar_availability.css" />
<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/table.css" />

<script src="<c:url value="/static/javascript/Utilities/Checkboxes.js" />"></script>	
<%-- <script src="<c:url value="/static/javascript/profile_employee/Calendar_Applications.js" />"></script> --%>
<script src="<c:url value="/static/javascript/credentials_employee/credentials_employee.js" />"></script>


<div class="container">
	<div id="personalInfoContainer">
	
		<div id="imageContainer">			
			<p>${userDto.user.firstName } ${userDto.user.lastName }</p>
			<img src="/JobSearch/static/images/profile_image_default.png" alt="Profile Image">
			
		</div>
		<div id="personalInfo">
			<div class="info">
				<c:if test="${isViewingOnesSelf }">
					<p id="make-edits" class="linky-hover">Edit</p>
					<p id="save-edits" class="">Save</p>
				</c:if>
				<div class="lbl">Home Location
<!-- 					<span class="glyphicon glyphicon-pencil"></span> -->
				</div>
				<div class="value ${isViewingOnesSelf ? 'editable' : 'not-editable' }">
					${userDto.user.homeCity}, ${userDto.user.homeState} ${userDto.user.homeZipCode }</div>
				<c:if test="${isViewingOnesSelf }">
					<div id="editHomeLocation" class="edit-container">
						<div><span class="lbl">City</span><input id="city" type="text" value="${userDto.user.homeCity }"></div>
						<div><span class="lbl">State</span><select id="state" data-init-value="${userDto.user.homeState }"></select></div>
						<div><span class="lbl">Zip Code</span><input id="zipCode" type="text" value="${userDto.user.homeZipCode }"></div>
<!-- 						<button id="saveHomeLocation" class="save-changes square-button-green">Save</button> -->
<!-- 						<div class="cancel-changes">Cancel</div> -->
					</div>
				</c:if>
			</div>
			<div class="info">
				<div class="lbl">Maximum distance willing to travel
<!-- 					<span class="glyphicon glyphicon-pencil"></span> -->
				</div>
				<div class="value ${isViewingOnesSelf ? 'editable' : 'not-editable' }">
					${userDto.user.maxWorkRadius } miles</div>
				
				<c:if test="${isViewingOnesSelf }">
					<div id="editMaxDistance" class="edit-container">
						<div><span class="lbl">Miles</span><input id="miles" type="text" value="${userDto.user.maxWorkRadius }"></div>
<!-- 						<button id="saveMaxDistance" class="save-changes square-button-green">Save</button> -->
<!-- 						<div class="cancel-changes">Cancel</div> -->
					</div>
				</c:if>				
			</div>
<!-- 			<div class="info"> -->
<!-- 				<div class="lbl">Minimum hourly wage -->
<!-- 				</div> -->
<%-- 				<div class="value ${isViewingOnesSelf ? 'editable' : 'not-editable' }"> --%>
<!-- 					$${userDto.user.stringMinimumDesiredPay } per hour</div> -->
				
<%-- 				<c:if test="${isViewingOnesSelf }"> --%>
<!-- 					<div id="editMinimumPay" class="edit-container"> -->
<%-- 						<div><span class="lbl">$ per hour</span><input id="dollarsPerHour" type="text" value="${userDto.user.stringMinimumDesiredPay }"></div> --%>
<!-- 					</div>					 -->
<%-- 				</c:if> --%>
<!-- 			</div> -->
			<div class="info">
				<div data-toggle-id="aboutContainer" class="lbl">About
					<span class="glyphicon glyphicon-menu-down"></span>
<!-- 					<span class="glyphicon glyphicon-pencil"></span> -->
				</div>
				<div id="aboutContainer" class="value">
				 	<p>Edit</p>
					<p>the more obscure Latin words, consectetur, from a Lorem Ipsum
					 passage, and going through the cites of the word in classical literature, discovered
					  the undoubtable source. Lorem Ipsum comes from sections 1.10.32 and 1.10.33 of "de
					   Finibus Bonorum et Malorum" (The Extremes of Good and Evil) by Cicero, written in
					    45 BC. This book is a treatise on the theory of ethics, very popular during the
					     Renaissance. The first line of Lorem Ipsum, "Lorem ipsum dolor sit amet..", comes
					      from a line in section 1.10.32.
	
						The standard chunk of Lorem Ipsum used since the 1500s is reproduced below for those interested. Sections 1.10.32 and 1.10.33 from "de Finibus Bonorum et Malorum" by Cicero are also reproduced in their exact original form, accompanied by English versions from the 1914 translation by H. Rackham.
					</p>
				 </div>
			</div>			
<!-- 			<div class="info"> -->
<!-- 				<div data-toggle-id="availabilityCalendarContainer" class="lbl"> -->
<!-- 					Availability<span class="glyphicon glyphicon-menu-down"></span> -->
<!-- 				</div> -->
<!-- 				<div id="availabilityCalendarContainer" class="value calendar-container"> -->
<%-- 					<c:if test="${isViewingOnesSelf }"> --%>
<!-- 						<a id="editAvailability" class="accent" href="/JobSearch/availability">Edit</a> -->
<%-- 					</c:if> --%>
<!-- 					<div id="availabilityCalendar" class="calendar"></div> -->
<!-- 				</div>					 -->
<!-- 			</div> -->

		</div>
	
	</div>

<!-- 	<div class="availability-container calendar-container"> -->
<!-- 		<div class="header-container"> -->
<!-- 			<h3>Calendar<span class="glyphicon glyphicon-pencil"></span></h3> -->
<!-- 		</div>	 -->
<!-- 		<div class="days-of-week-container checkbox-container"> -->
<!-- 			<div><label><input class="select-all" type="checkbox">Select all</label></div> -->
<!-- 			<div class="options"> -->
<!-- 				<label><input type="checkbox" name="days-of-week" data-day-of-week="0">Su</label> -->
<!-- 				<label><input type="checkbox" name="days-of-week" data-day-of-week="1">Mo</label> -->
<!-- 				<label><input type="checkbox" name="days-of-week" data-day-of-week="2">Tu</label> -->
<!-- 				<label><input type="checkbox" name="days-of-week" data-day-of-week="3">We</label> -->
<!-- 				<label><input type="checkbox" name="days-of-week" data-day-of-week="4">Th</label> -->
<!-- 				<label><input type="checkbox" name="days-of-week" data-day-of-week="5">Fr</label> -->
<!-- 				<label><input type="checkbox" name="days-of-week" data-day-of-week="6">Sa</label> -->
<!-- 			</div> -->
<!-- 		</div> -->
<!-- 		<div class="calendar"></div> -->
		
		
<!-- 			<div id="applicationDetails">				 -->
<%-- 				<c:forEach items="${applicationDtos }" var="applicationDto"> --%>
<%-- 					<div class="application" data-id="${applicationDto.application.applicationId }" --%>
<%-- 							 data-job-name="${applicationDto.jobDto.job.jobName }" --%>
<%-- 							 data-job-id="${applicationDto.jobDto.job.id}" --%>
<%-- 							 data-job-status="${applicationDto.jobDto.job.status}">									 --%>
<%-- 						<c:forEach items="${applicationDto.jobDto.workDays }" var="workDay"> --%>
<%-- 							<div class="work-day" data-date="${workDay.stringDate }"></div> --%>
<%-- 						</c:forEach> --%>
<!-- 					</div> -->
<%-- 				</c:forEach>				 --%>
<!-- 			</div>	 -->
			
<!-- 			<div id="employmentDetails"> -->
<%-- 				<c:forEach items="${jobDtos_employment_currentAndFuture }" var="jobDto">				 --%>
<%-- 					<div class="job" data-job-id="${jobDto.job.id}" --%>
<%-- 									data-job-name="${jobDto.job.jobName }">											  --%>
<%-- 						<c:forEach items="${jobDto.workDays }" var="workDay"> --%>
<%-- 							<div class="work-day" data-date="${workDay.stringDate }"></div> --%>
<%-- 						</c:forEach>					 --%>
<!-- 					</div>						 -->
<%-- 				</c:forEach>				 --%>
<!-- 			</div>		 -->
			
<!-- 			<div id="availabilityDetails"> -->
<%-- 				<c:forEach items="${userDto.availableDays }" var="dateString">														  --%>
<%-- 					<div class="work-day" data-date="${dateString }"></div>					 --%>
<%-- 				</c:forEach>				 --%>
<!-- 			</div>				 -->
<!-- 	</div> -->
<c:if test="${!empty userDto.jobDtos_jobsCompleted }">
	<div id="workHistoryContainer">
		<div class="header-container">
			<h3>Work History</h3>
		</div>
		<div>
			<div id="topCategories">
				<table class="main-table-style">
					<thead>
						<tr>
							<th>Category</th>
							<th>Completed Jobs</th>
							<th>Rating</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${userDto.categoryDtos_jobsCompleted }" var="categoryDto">
							<tr>
								<td ><a class="accent category">${categoryDto.category.name }</a></td>
								<td>${categoryDto.count_jobsCompleted }</td>
								<td>${categoryDto.ratingValue_jobsCompleted }</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
			<div id="pastJobs">
				<table>
					<thead>						
					</thead>
					<tbody>
						<c:forEach items="${userDto.jobDtos_jobsCompleted }" var="jobDto">
							<tr data-rating="${jobDto.ratingDto.value }"
								data-end-date="${jobDto.milliseconds_endDate }"
								data-duration="${jobDto.durationDays }">
								
								<td class="past-job">
									<div>
										<a class="job-name accent" href="/JobSearch/job/${jobDto.job.id }?c=profile-complete&p=1">${jobDto.job.jobName }</a>
										<span>
	<!-- 									<div class="item categoryContainer"> -->
											<c:forEach items="${jobDto.categories }" var="category" varStatus="status">
												<span class="category">${category.name }${!status.last ? ',' : '' }</span>
											</c:forEach>
										</span>
									</div>							
									<div class="item">
										<div><span class="lbl">Rating</span>${jobDto.ratingDto.value }</div>
										<div><span class="lbl">Comment</span>${jobDto.ratingDto.comment }</div>
									</div>
									<div class="item">
										<div><span class="lbl">Completion Date</span>${jobDto.job.stringEndDate }</div>
										<div><span class="lbl">Duration</span>${jobDto.durationDays } ${jobDto.durationDays == 1 ? 'day' : 'days'}</div>	
									</div>
									<div class="item">
										<div><span class="lbl">Job Description</span>${jobDto.job.description }</div>	
									</div>								
								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
<!-- 				<table class="main-table-style"> -->
<!-- 					<thead> -->
<!-- 						<tr> -->
<!-- 							<th>Job Name</th> -->
<!-- 							<th>Category</th> -->
<!-- 							<th class="dropdown-container" data-sort-attr="data-rating"> -->
<!-- 								<span data-toggle-id="sortRating" data-toggle-speed="2"> -->
<!-- 									Rating<span class="glyphicon glyphicon-menu-down"></span> -->
<!-- 								</span> -->
<!-- 								<div id="sortRating" class="sort-container dropdown-style"> -->
<!-- 									<label> -->
<!-- 										<input type="radio" name="sortRating" data-sort-ascending="0">High to Low -->
<!-- 									</label> -->
<!-- 									<label> -->
<!-- 										<input type="radio" name="sortRating" data-sort-ascending="1">Low to High -->
<!-- 									</label> -->
<!-- 								</div>	 -->
<!-- 							</th> -->
<!-- 							<th>Comment</th> -->
<!-- 							<th class="dropdown-container" data-sort-attr="data-end-date"> -->
<!-- 								<span data-toggle-id="sortEndDate" > -->
<!-- 									Completion Date<span class="glyphicon glyphicon-menu-down"></span> -->
<!-- 								</span> -->
<!-- 								<div id="sortEndDate" class="sort-container dropdown-style"> -->
<!-- 									<label> -->
<!-- 										<input type="radio" name="sortEndDate" data-sort-ascending="0">Most Recent First -->
<!-- 									</label> -->
<!-- 									<label> -->
<!-- 										<input type="radio"	name="sortEndDate" data-sort-ascending="1">Oldest First -->
<!-- 									</label> -->
<!-- 								</div>	 -->
<!-- 							</th> -->
<!-- 							<th class="dropdown-container" data-sort-attr="data-duration"> -->
<!-- 								<span data-toggle-id="sortDuration" data-toggle-speed="-2"> -->
<!-- 									Duration<span class="glyphicon glyphicon-menu-down"></span> -->
<!-- 								</span> -->
<!-- 								<div id="sortDuration" class="sort-container dropdown-style"> -->
<!-- 									<label> -->
<!-- 										<input type="radio" name="sortDuration" data-sort-ascending="0">Longest First -->
<!-- 									</label> -->
<!-- 									<label> -->
<!-- 										<input type="radio"	name="sortDuration" data-sort-ascending="1">Shortest First -->
<!-- 									</label> -->
<!-- 								</div>	 -->
<!-- 							</th> -->
<!-- 						</tr> -->
<!-- 					</thead> -->
<!-- 					<tbody> -->
<%-- 						<c:forEach items="${userDto.jobDtos_jobsCompleted }" var="jobDto"> --%>
<%-- 							<tr data-rating="${jobDto.ratingDto.value }" --%>
<%-- 								data-end-date="${jobDto.milliseconds_endDate }" --%>
<%-- 								data-duration="${jobDto.durationDays }"> --%>
								
<%-- 								<td><a class="accent" href="/JobSearch/job/${jobDto.job.id }?c=profile-complete&p=1">${jobDto.job.jobName }</a></td> --%>
<!-- 								<td> -->
<%-- 									<c:forEach items="${jobDto.categories }" var="category"> --%>
<%-- 										<div>${category.name }</div> --%>
<%-- 									</c:forEach>							 --%>
<!-- 								</td> -->
<%-- 								<td>${jobDto.ratingDto.value }</td> --%>
<%-- 								<td>${jobDto.ratingDto.comment }</td> --%>
<%-- 								<td>${jobDto.job.stringEndDate }</td> --%>
<%-- 								<td>${jobDto.durationDays } ${jobDto.durationDays == 1 ? 'day' : 'days'}</td> --%>
<!-- 							</tr> -->
<%-- 						</c:forEach> --%>
<!-- 					</tbody> -->
<!-- 				</table>		 -->
			</div>
		</div>
	</div>
</c:if>	
</div>

