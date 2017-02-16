<%@ include file="../includes/Header.jsp"%>

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
		
		</div>
	</div>
	
	<div id="aboutMeContainer">
	
	</div>
</div>