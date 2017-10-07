<%@ include file="../includes/TagLibs.jsp"%>

<div class="mod simple-header proposal-container current-proposal-response"
	data-session-user-is-employer=${user.profileId == 2 ? '1' : '0' }
	data-proposal-id="${response.currentProposal.proposalId }"
	data-application-id="${response.currentProposal.applicationId }"
	data-employer-is-making-first-offer="${context == 'employer-make-initial-offer' ? '1' : '0'}">
	<div class="mod-content" >
		<div class="mod-header"></div>	
		<div class="mod-body">		
			<div class="respond-to-proposal">
				<%@ include file="./ReplyToCurrentProposal/CurrentProposal.jsp" %>
			</div>		
		</div>
	</div>
</div>
<div id="json_workDayDtos" class="hide">${json_workDayDtos }</div>