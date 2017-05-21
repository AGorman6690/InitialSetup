<%@ include file="../includes/TagLibs.jsp"%>
	

		<table class="main-table-style shadow">
			<thead>
				<tr class="header-2">
					<th id="job-name" class="left-edge"></th>

					
					<th id="start-date" class="header-dropdown hide-with-calendar" data-sort-attr="data-job-start-date">
						<span data-toggle-id="sort_start_date" >
							Start Date<span class="glyphicon glyphicon-menu-down"></span>
						</span>
						<div id="sort_start_date" class="dropdown-container sort-container">
							<label>
								<input type="radio"	name="sort_start_date"
									data-sort-ascending="1">Earliest first
							</label>
							<label>
								<input type="radio"	name="sort_start_date"
									data-sort-ascending="0">Latest first
							</label>					
						</div>					
					</th>	
					<th id="end-date" class="header-dropdown hide-with-calendar" data-sort-attr="data-job-end-date">
						<span data-toggle-id="sort_end_date" >
							End Date<span class="glyphicon glyphicon-menu-down"></span>
						</span>
						<div id="sort_end_date" class="dropdown-container sort-container">
							<label>
								<input type="radio"	name="sort_end_date"
									data-sort-ascending="1">Earliest first
							</label>
							<label>
								<input type="radio"	name="sort_end_date"
									data-sort-ascending="0">Latest first
							</label>					
						</div>					
					</th>									
<!-- 					<th class="header-dropdown hide-with-calendar" data-sort-attr="data-job-duration-days"> -->
<!-- 						<span data-toggle-id="sort_duration_days" > -->
<!-- 							Duration<span class="glyphicon glyphicon-menu-down"></span> -->
<!-- 						</span> -->
<!-- 						<div id="sort_duration_days" class="dropdown-container sort-container"> -->
<!-- 							<label> -->
<!-- 								<input type="radio"	name="sort_duration_days" -->
<!-- 									data-sort-ascending="1">Shortest to longest -->
<!-- 							</label> -->
<!-- 							<label> -->
<!-- 								<input type="radio"	name="sort_duration_days" -->
<!-- 									data-sort-ascending="0">Longest to shortest -->
<!-- 							</label>					 -->
<!-- 						</div>					 -->
<!-- 					</th> -->
					<th id="location" class="header-dropdown hide-with-calendar" data-sort-attr="data-job-distance">
						<span data-toggle-id="sort_distance" >
							Location<span class="glyphicon glyphicon-menu-down"></span>
						</span>
						<div id="sort_distance" class="dropdown-container sort-container">
							<label>
								<input type="radio"	name="sort_distance"
									data-sort-ascending="1">Closest to farthest
							</label>
							<label>
								<input type="radio"	name="sort_distance"
									data-sort-ascending="0">Farthest to closest
							</label>					
						</div>					
					</th>	
					<th class=" first proposal">Proposal</th>
<!-- 					<th>Expiration</th> -->
<!-- 					<th id="action-th" class="left-edge">Status</th>					 -->
<!-- 					<th class="header-dropdown" data-sort-attr="data-employment-proposal-amount"> -->
<!-- 						<span data-toggle-id="sort_proposed-amount" > -->
<!-- 							Amount<span class="glyphicon glyphicon-menu-down"></span> -->
<!-- 						</span> -->
<!-- 						<div id="sort_proposed-amount" class="dropdown-container sort-container"> -->
<!-- 							<label> -->
<!-- 								<input type="radio"	name="sort_proposed-amount" -->
<!-- 									data-sort-ascending="0">Highest to lowest -->
<!-- 							</label> -->
<!-- 							<label> -->
<!-- 								<input type="radio"	name="sort_proposed-amount" -->
<!-- 									data-sort-ascending="1">Lowest to highest -->
<!-- 							</label>					 -->
<!-- 						</div>					 -->
<!-- 					</th>						 -->
																	 
				</tr>
			</thead>					

			<tbody>
			
				<c:forEach items="${applicationDtos }" var="applicationDto">
					<c:if test="${applicationDto.application.flag_applicantAcknowledgedAllPositionsAreFilled == 0 }">
						<tr class="application"
							data-application-status="${applicationDto.application.status }"
							data-application-id="${applicationDto.application.applicationId }"
							data-employment-proposal-amount="${applicationDto.employmentProposalDto.amount }"
							data-job-start-date="${applicationDto.jobDto.milliseconds_startDate }"
							data-job-end-date="${applicationDto.jobDto.milliseconds_endDate }"
							data-job-duration-days="${applicationDto.jobDto.workDays.size() }"
							data-job-distance="${applicationDto.jobDto.distance }"
							>
										
							<td class="job-name">
								<a class="accent ${applicationDto.application.status == 3 ? 'accepted' : ''}"
								   href="/JobSearch/job/${applicationDto.jobDto.job.id }?c=profile-incomplete&p=1">
									${applicationDto.jobDto.job.jobName }
								</a>
							</td>						
							<td class="hide-with-calendar">${applicationDto.jobDto.job.stringStartDate }</td>
							<td class="hide-with-calendar">${applicationDto.jobDto.job.stringEndDate }</td>	
<%-- 							<td class="hide-with-calendar">${applicationDto.jobDto.workDays.size() } ${applicationDto.jobDto.workDays.size() <= 1 ? 'day' : 'days' }</td>	 --%>
							<td class="hide-with-calendar">${applicationDto.jobDto.job.city_formatted }, ${applicationDto.jobDto.job.state }</td>

<%-- 							<td>${applicationDto.employmentProposalDto.time_untilEmployerApprovalExpires }</td> --%>
							<td>
								<c:set var="jobDto" value="${applicationDto.jobDto }" />
								<%@ include file="../wage_proposal/Proposal_Main.jsp" %>
							</td>
						</tr>
					</c:if>
				</c:forEach>
										
			</tbody>
	
</table>
