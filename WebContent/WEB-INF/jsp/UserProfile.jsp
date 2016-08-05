
<%@ include file="./includes/Header.jsp"%>

<head>
<script src="<c:url value="/static/javascript/Jobs.js" />"></script>
<script src="<c:url value="/static/javascript/Category.js" />"></script>
<script src="<c:url value="/static/javascript/User.js" />"></script>
<script src="<c:url value="/static/javascript/AppendHtml.js" />"></script>
<link rel="stylesheet" type="text/css" href="./static/css/ratings.css" />
<link rel="stylesheet" type="text/css"
	href="./static/css/categories.css" />
<link rel="stylesheet" type="text/css" href="./static/css/ratings.css" />
</head>


<body>

	<c:choose>
		<c:when test="${redirectUrl != null}">
			<c:redirect url="${redirectUrl}" />
		</c:when>
	</c:choose>

	<input type="hidden" id="userId" value="${user.userId}" />
	<div class="container">
		<h1>Edit Profile</h1>
		<button type="button" class="btn btn-warning" id="save">Save</button>

		<div style="width: 750px" class="panel panel-info">
			<div class="panel-heading">Home Location</div>
			<div class="panel-body">
				<div class="form-group row">
					<label for="homeCity"
						class="post-job-label col-sm-2 form-control-label">City</label>
					<div class="col-sm-10">
						<input name="city" type="text" class="post-job-input form-control"
							id="homeCity" value="${user.getHomeCity() }"></input>
					</div>
				</div>
				<div class="form-group row">
					<label for="homeState"
						class="post-job-label col-sm-2 form-control-label">State</label>
					<div class="col-sm-10">
						<input name="state" type="text"
							class="post-job-input form-control" id="homeState"
							value="${user.getHomeState() }"></input>
					</div>
				</div>
				<div class="form-group row">
					<label for="homeZipCode"
						class="post-job-label col-sm-2 form-control-label">Zip
						Code</label>
					<div class="col-sm-10">
						<input name="zipCode" type="text"
							class="post-job-input form-control" id="homeZipCode"
							value="${user.getHomeZipCode() }"></input>
					</div>
				</div>
			</div>
		</div>
		<!-- end home location panel -->

		<c:if test="${user.getProfileId() == 1 }">
			<div class="panel panel-info">
				<div class="panel-heading">Maximum Distance Willing To Work
					From Home Location</div>
				<div class="panel-body">
					<div class="form-group row">
						<label for="maxWorkRadius"
							class="post-job-label col-sm-2 form-control-label">Distance
							from home location</label>
						<div class="col-sm-10">
							<c:choose>
								<c:when test="${user.getMaxWorkRadius() == -1 }">
									<input name="state" type="text"
										class="post-job-input form-control" id="maxWorkRadius"></input>
								</c:when>
								<c:otherwise>
									<input name="state" type="text"
										class="post-job-input form-control" id="maxWorkRadius"
										value="${user.getMaxWorkRadius() }"></input>
								</c:otherwise>
							</c:choose>
						</div>
					</div>
				</div>
			</div>
			<!-- end max work radius panel -->
		</c:if>

		<div class="panel panel-info">
			<div class="panel-heading">Categories</div>
			<div class="panel-body">
				<div style="display: inline" id="selectedCategories">
					<c:forEach items="${user.getCategories() }" var="category">
						<button type="button" class="btn btn-success"
							id="${category.getId() }-selected"
							onclick="removeCategoryFromSelection(this)">
							${category.getName() } <span style="margin: 5px 5px 5px 5px"
								class="glyphicon glyphicon-remove"> </span>
						</button>
					</c:forEach>
				</div>
				<div id="addCategories" style="display: none"></div>
				<div id="removeCategories" style="display: none"></div>
				<br>
				<div id="0T"></div>
			</div>
		</div>
		<!-- end categories panel -->


		<form:form enctype="multipart/form-data" action="/JobSearch/upload/resume"
			method="POST">
			<table>
				<tr>
					<td><input type="file" name="file"/></td>
				</tr>
				<tr>
					<td><input id="" type="submit" value="Upload" /></td>
				</tr>
				<tr>
					<td><input type="hidden" name="${_csrf.parameterName}"
						value="${_csrf.token}" /></td>

				</tr>
			</table>
		</form:form>

	</div>
	<!-- end container -->
</body>



<script>
	$(document)
			.ready(
					function() {
						$("#save")
								.click(
										function() {

											var editProfileDTO = {};
											editProfileDTO.userId = $("#userId")
													.val();
											editProfileDTO.homeCity = $(
													"#homeCity").val();
											editProfileDTO.homeState = $(
													"#homeState").val();
											editProfileDTO.homeZipCode = $(
													"#homeZipCode").val();
											editProfileDTO.maxWorkRadius = $(
													"#maxWorkRadius").val();

											editProfileDTO.categoryIds = [];
											var categories = $(
													'#selectedCategories')
													.find("button");
											for (var i = 0; i < categories.length; i++) {
												var id = categories[i].id;
												editProfileDTO.categoryIds
														.push(id
																.substring(
																		0,
																		id
																				.indexOf("-")));
											}

											var headers = {};
											headers[$(
													"meta[name='_csrf_header']")
													.attr("content")] = $(
													"meta[name='_csrf']").attr(
													"content");

											$
													.ajax(
															{
																type : "POST",
																url : environmentVariables.LaborVaultHost + "/JobSearch/user/profile/edit",
																headers : headers,
																contentType : "application/json",
																dataType : "application/json", // Response
																data : JSON
																		.stringify(editProfileDTO)
															}).done(function() {

													}).error(function() {

													});

										})
					})

	var pageContext = "profile";

// 	getCategoriesBySuperCat('0', function(response, categoryId) {
// 		appendCategories(categoryId, "T", response, function() {
// 		});
// 	})

	// 	getCategoriesByUser($("#userId").val(), function(usersCategories){

	// 		getCategoriesBySuperCat('0', function(response, categoryId){

	// 			//Append seed categories
	// 			appendCategories(categoryId, "T", response);
	// 		})
	// 	})
</script>


<%@ include file="./includes/Footer.jsp"%>
