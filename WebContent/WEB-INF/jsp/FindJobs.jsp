<%@ include file="./includes/Header.jsp" %>
	<script src="<c:url value="/static/javascript/Profile.js" />"></script>
	<script src="<c:url value="/static/javascript/Jobs.js" />"></script>
	

	<span>Search for Jobs</span>
	<ul>
		<c:forEach items="${categories}" var="category">
			<li class="co_jobCategory">${ category.getName()}
				<ul>
					<c:forEach items="${category.getCategories()}" var="subcategory">
						<li class="co_jobCategory">${ subcategory.getName()}</li>
					</c:forEach>
				</ul>
			</li>
		</c:forEach>
	</ul>

<%@ include file="./includes/Footer.jsp" %>