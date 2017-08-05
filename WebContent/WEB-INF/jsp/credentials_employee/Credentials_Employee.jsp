<%@ include file="../includes/TagLibs.jsp"%>
<%@ include file="../includes/resources/TableFilter.jsp" %>
<%-- <%@ include file="../includes/resources/StarRatings.jsp" %> --%>

<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/credentials/credentials.css" />
<!-- <link rel="stylesheet" type="text/css" href="/JobSearch/static/css/credentials/calendar_availability.css" /> -->
<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/table.css" />

<script src="<c:url value="/static/javascript/Utilities/Checkboxes.js" />"></script>	
<script src="<c:url value="/static/javascript/credentials_employee/credentials_employee.js" />"></script>

<div id="personal-info-container" class="personal-info-section">	
	<div id="image">		
		<p id="user-name">${response.profileInfoDto.user.firstName } ${response.profileInfoDto.user.lastName }</p>	
		<img src="/JobSearch/static/images/profile_image_default.png" alt="Profile Image">	
	</div>
	<div id="personal-info">
		<div class="info">	
			<c:if test="${response.profileInfoDto.user.profileId == 1 }">				
				<div class="info-item">
					<label>Home Location</label>						
					<span class="value ${isViewingOnesSelf ? 'editable' : '' }">
						<c:choose>
							<c:when test="${empty response.profileInfoDto.user.homeCity &&
											 empty response.profileInfoDto.user.homeState &&
											 empty response.profileInfoDto.user.homeZipCode }">
								<span class="not-set">Add your home location so employers can find you</span>
							</c:when>
							<c:otherwise>
								${response.profileInfoDto.user.homeCity},
								 ${response.profileInfoDto.user.homeState}
								  ${response.profileInfoDto.user.homeZipCode }
								
							</c:otherwise>
						</c:choose>
					</span>				
					<c:if test="${isViewingOnesSelf }">
						<div id="edit-home-location" class="edit-container">
							
							<div>
								<label>City</label>
								<input id="city" class="select-all" type="text" value="${response.profileInfoDto.user.homeCity }">
							</div>
							<div>
								<label>State</label>
								<select id="state" data-do-show-placeholder="0" data-init-value="${response.profileInfoDto.user.homeState }"></select>
							</div>
							<div>
								<label>Zip Code</label>
								<input id="zipCode" class="select-all" type="text" value="${response.profileInfoDto.user.homeZipCode }">
							</div>
							<p class="edit-wrapper"><span id="save-home-location" class="save-profile-info">Save</span></p>
						</div>
					</c:if>						
				</div>
				<div class="info-item">
					<label>Maximum travel distance</label>
					<span class="value ${isViewingOnesSelf ? 'editable' : 'not-editable' }">							
						<c:choose>
							<c:when test="${empty response.profileInfoDto.user.maxWorkRadius ||
										response.profileInfoDto.user.maxWorkRadius == 0}">
								<span class="not-set">Add your maximum traveling distance so employers can find you</span>
							</c:when>
							<c:otherwise>
								${response.profileInfoDto.user.maxWorkRadius } miles
							</c:otherwise>
						</c:choose>
					</span>				
					<c:if test="${isViewingOnesSelf }">
						<div id="edit-max-distance" class="edit-container">
							
							<label>Miles</label>
							<input id="miles" class="select-all" type="text" value="${response.profileInfoDto.user.maxWorkRadius }">
							<p class="edit-wrapper"><span id="save-max-distance" class="save-profile-info">Save</span></p>
						</div>
					</c:if>			
				</div>						
			</c:if>
			<div class="info-item">
				<label>About</label>
				<span class="value ${isViewingOnesSelf ? 'editable' : 'not-editable' }">							
					<c:choose>
						<c:when test="${response.profileInfoDto.user.about == '' }">
							<span class="not-set">Write something so
							${sessionScope.user.profileId == 1 ? ' employers' : ' applicants' } know a little bit about you</span>
						</c:when>
						<c:otherwise>
							${response.profileInfoDto.user.about }
						</c:otherwise>
					</c:choose>
				</span>				
				<c:if test="${isViewingOnesSelf }">
					<div id="edit-aout" class="edit-container">
						
						<textarea id="about" class="select-all">${response.profileInfoDto.user.about }</textarea>
						<p class="edit-wrapper"><span id="save-about" class="save-profile-info">Save</span></p>
					</div>
				</c:if>							
			</div>	
		</div>
	</div>
</div>