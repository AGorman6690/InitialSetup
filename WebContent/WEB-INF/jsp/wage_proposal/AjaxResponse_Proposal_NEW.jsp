<%@ include file="../includes/TagLibs.jsp"%>

<div class="mod simple-header proposal-container" data-session-user-is-employer=${user.profileId == 2 ? '1' : '0' }
	data-application-id="${applicationDto.application.applicationId }"
	data-employer-is-making-first-offer="${isEmployerMakingFirstOffer ? '1' : '0'}">
	<div class="mod-content" >
		<div class="mod-header"></div>	
		<div class="mod-body">		
			<div class="respond-to-proposal">
				<%@ include file="./ReplyToCurrentProposal/CurrentProposal.jsp" %>
			</div>		
<!-- 			<div class="confirm-response-to-proposal hide-on-load"> -->
<%-- 				<%@ include file="./ReplyToCurrentProposal/ReviewResponseToCurrentProposal.jsp" %>				 --%>
<!-- 			</div>		 -->
		</div>
	</div>
</div>
<div id="json_workDayDtos" class="hide">${json_workDayDtos }</div>