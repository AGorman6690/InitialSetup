<%@ include file="../includes/Header.jsp"%>
<%@ include file="../includes/resources/TableFilter.jsp" %>

<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/credentials/credentials.css" />
<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/table.css" />

<%-- <script src="<c:url value="/static/javascript/profile_employee/Calendar_Applications.js" />"></script> --%>


<div class="container">
	<div id="imageContainer">
		
		<div id="image">
			<img src="/JobSearch/static/images/profile_image_default.png" alt="Profile Image">
			<p>${userDto.user.firstName } ${userDto.user.lastName }</p>
		</div>
	</div>
	
	<div id="aboutMeContainer">
		<textarea id="aboutMe" rows="6"></textarea>
	</div>
		
	<div id="workHistoryContainer">
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
							<td>${categoryDto.category.name }</td>
							<td>${categoryDto.count_jobsCompleted }</td>
							<td>${categoryDto.ratingValue_jobsCompleted }</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
		<div id="pastJobs">
			<table class="main-table-style">
				<thead>
					<tr>
						<th>Job Name</th>
						<th>Category</th>
						<th class="dropdown-container" data-sort-attr="data-rating">
							<span data-toggle-id="sortRating" >
								<span class="glyphicon glyphicon-menu-down"></span>Rating
							</span>
							<div id="sortRating" class="sort-container dropdown-style">
								<label>
									<input type="radio" name="sortRating" data-sort-ascending="0">High to Low
								</label>
								<label>
									<input type="radio" name="sortRating" data-sort-ascending="1">Low to High
								</label>
							</div>	
						</th>
						<th>Comment</th>
						<th class="dropdown-container" data-sort-attr="data-end-date">
							<span data-toggle-id="sortEndDate" >
								<span class="glyphicon glyphicon-menu-down"></span>Completion Date
							</span>
							<div id="sortEndDate" class="sort-container dropdown-style">
								<label>
									<input type="radio" name="sortEndDate" data-sort-ascending="0">Most Recent First
								</label>
								<label>
									<input type="radio"	name="sortEndDate" data-sort-ascending="1">Oldest First
								</label>
							</div>	
						</th>
						<th class="dropdown-container" data-sort-attr="data-duration">
							<span data-toggle-id="sortDuration" >
								<span class="glyphicon glyphicon-menu-down"></span>Duration
							</span>
							<div id="sortDuration" class="sort-container dropdown-style">
								<label>
									<input type="radio" name="sortDuration" data-sort-ascending="0">Longest First
								</label>
								<label>
									<input type="radio"	name="sortDuration" data-sort-ascending="1">Shortest First
								</label>
							</div>	
						</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${userDto.jobDtos_jobsCompleted }" var="jobDto">
						<tr data-rating="${jobDto.ratingDto.value }"
							data-end-date="${jobDto.endDate_milliseconds }"
							data-duration="${jobDto.durationDays }">
							
							<td><a class="accent" href="/JobSearch/job/${jobDto.job.id }?c=profile-complete&p=1">${jobDto.job.jobName }</a></td>
							<td>
								<c:forEach items="${jobDto.categories }" var="category">
									<div>${category.name }</div>
								</c:forEach>							
							</td>
							<td>${jobDto.ratingDto.value }</td>
							<td>${jobDto.ratingDto.comment }</td>
							<td>${jobDto.job.stringEndDate }</td>
							<td>${jobDto.durationDays } ${jobDto.durationDays == 1 ? 'day' : 'days'}</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>		
		</div>
	</div>

</div>