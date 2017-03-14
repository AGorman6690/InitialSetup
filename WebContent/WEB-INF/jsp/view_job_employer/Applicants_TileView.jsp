<%@ include file="../includes/TagLibs.jsp" %>

<table>
	<thead>
		<tr>
			<th class="lbl">Filter</th>
		</tr>
		<tr>
			<th class="lbl">Sort</th>
			<th>Rating</th>
			<th>Days Available</th>
			<th class="header-dropdown" data-sort-attr="data-wage-proposal-amount">
					
				<span data-toggle-id="sortWageProposalOffer_tile" >
					Current Offer
				</span>
				<div id="sortWageProposalOffer_tile" class="dropdown-container sort-container">
<!-- 							<span class="approve-sort glyphicon glyphicon-ok"></span> -->
					
					<label>
						<input id="" type="radio"
							name="wage-prpoposal-current-offer"
							data-sort-ascending="0">High to Low
					</label>
					<label>
						<input id="" type="radio"
							name="wage-prpoposal-current-offer"
							data-sort-ascending="1">Low to High
					</label>					
				</div>					
			
			
			</th>
		</tr>
	</thead>
	<tbody>
		<c:forEach items="${jobDto.applicationDtos }" var="applicationDto">
			<tr class="" data-application-status="${applicationDto.application.status }"
				data-applicant-rating="${applicationDto.applicantDto.ratingValue_overall}"
				data-application-id="${applicationDto.application.applicationId }"
				data-is-old="${applicationDto.application.hasBeenViewed }"
				data-wage-proposal-amount="${applicationDto.currentWageProposal.amount }"
				data-wage-proposal-status="${applicationDto.currentWageProposal.status }"
				data-is-sent-proposal="${applicationDto.currentWageProposal.proposedToUserId ==
											 applicationDto.applicantDto.user.userId ? '1' : '0'}"
				data-answer-option-ids-seleted="${applicationDto.answerOptionIds_Selected }">
				
				<td colspan="4">
					<div class="image-container">
						<img src="/JobSearch/static/images/profile_image_default.png" alt="Profile Image">
					</div>
					<div>
						<p class="name">${applicationDto.applicantDto.user.firstName }</p>
						<p>
							<input name="input-1" class="rating-loading"
									value="${applicationDto.applicantDto.ratingValue_overall }	">
							${applicationDto.applicantDto.ratingValue_overall }						
						</p>
						<div class="proposal-container">
							<h3>Proposal</h3>
							<p>$ ${applicationDto.currentWageProposal.amount }</p>
							<p>${fn:length(applicationDto.dateStrings_availableWorkDays) } of ${fn:length(applicationDto.jobDto.workDays) } days</p>
						</div>
					</div>
				</td>
			</tr>
		</c:forEach>
	</tbody>
	
</table>