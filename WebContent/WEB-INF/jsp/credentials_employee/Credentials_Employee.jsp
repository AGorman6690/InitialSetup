<%-- <%@ include file="../includes/Header.jsp"%> --%>
<%@ include file="../includes/resources/TableFilter.jsp" %>
<%-- <%@ include file="../includes/resources/DatePicker.jsp" %> --%>
<%@ include file="../includes/resources/StarRatings.jsp" %>

<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/credentials/credentials.css" />
<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/credentials/calendar_availability.css" />
<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/table.css" />

<script src="<c:url value="/static/javascript/Utilities/Checkboxes.js" />"></script>	
<%-- <script src="<c:url value="/static/javascript/profile_employee/Calendar_Applications.js" />"></script> --%>
<script src="<c:url value="/static/javascript/credentials_employee/credentials_employee.js" />"></script>


<div class="container111">
	<div id="personalInfoContainer">	
		<div id="imageContainer">		
			<p>${userDto.user.firstName } ${userDto.user.lastName }</p>	
			<img src="/JobSearch/static/images/profile_image_default.png" alt="Profile Image">		
	
		</div>
		<div id="personalInfo" class="pad-top">
		
					<p id="make-edits" class="linky-hover">Edit</p>
			<p id="save-edits" class="linky-hover">Save</p>	
			<c:if test="${userDto.user.profileId == 1 }">
				<div class="info">	
					<div class="lbl">Home Location
					</div>
					<div class="value ${isViewingOnesSelf ? 'editable' : 'not-editable' }">
						${userDto.user.homeCity}, ${userDto.user.homeState} ${userDto.user.homeZipCode }</div>
					<c:if test="${isViewingOnesSelf }">
						<div id="editHomeLocation" class="edit-container">
							<div><span class="lbl">City</span><input id="city" type="text" value="${userDto.user.homeCity }"></div>
							<div><span class="lbl">State</span><select id="state" data-init-value="${userDto.user.homeState }"></select></div>
							<div><span class="lbl">Zip Code</span><input id="zipCode" type="text" value="${userDto.user.homeZipCode }"></div>
						</div>
					</c:if>
				</div>
				<div class="info">
					<div class="lbl">Work Radius
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
			</c:if>
			<div class="info">
				<div class="lbl">About
					
<!-- 					<span class="glyphicon glyphicon-pencil"></span> -->
				</div>
				<div id="aboutContainer" class="value">
					<p>${userDto.user.about }</p>
				 </div>
				 <div class="edit-container">
				 	<textarea id="about">${userDto.user.about }</textarea>
				 </div>
			</div>			
			
		</div>
		<div id="ratings" class="pad-top">
			
			<%@ include file="../ratings/RatingsByUser.jsp" %>
		</div>	
	</div>
</div>

<%-- <%@ include file="../includes/Footer.jsp"%> --%>