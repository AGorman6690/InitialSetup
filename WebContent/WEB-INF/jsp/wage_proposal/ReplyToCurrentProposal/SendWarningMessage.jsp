<%@ include file="../../includes/TagLibs.jsp" %>

<div class="accepting-offer-context">
	<p class=" ">You are about to accept the
		 ${sessionScope.user.profileId == 1 ? "employer's" : "applicant's" } offer.</p>			
	<p class="  context-employee">Once you do, you will be employed for this job.</p>
</div>
<div class="proposing-new-offer-context ">
<%--
	<p class=" ">
		<c:choose>
			<c:when test="${context == 'employer-make-initial-offer' }">
				You are about to make an offer to 
					${user_makeOfferTo.firstName } ${user_makeOfferTo.lastName }
			</c:when>
			<c:otherwise>
				You are about to send a new proposal to the
					${sessionScope.user.profileId == 1 ? " employer" : " applicant" }.
			</c:otherwise>
		</c:choose>
	</p>
 --%>
</div>
<p class="context-employer proposing-new-offer-context ">
	The applicant will have the option to counter this offer,
	 however must do so within the time period you specify.</p>
<p class="context-employer proposing-new-offer-context ">
	If the applicant accepts this offer, they will become an employee for this job.</p>	
<p class="context-employer accepting-offer-context ">
	The applicant must confirm your acceptance within the time period you specify.</p>	
<p class="context-employee proposing-new-offer-context ">
	You are about to send the employer a new proposal.</p>	
