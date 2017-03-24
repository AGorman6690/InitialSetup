<%@ include file="../includes/TagLibs.jsp"%>
	

		
			<thead>
				<tr class="header-1">		
					<th id="jobName" class="left-edge" colspan="1"></th>
					<th id="wageProposal" class="span left-edge right-edge" colspan="2">Wage Proposal</th>
					<th id="startDate" class="left-edge hide-with-calendar" colspan="1"></th>
					<th id="endDate" class="left-edge hide-with-calendar" colspan="1"></th>	
					<th id="location" class="left-edge hide-with-calendar" colspan="1"></th>	
					<th id="location" class="left-edge hide-with-calendar" colspan="1"></th>					
				</tr>
				
				<tr class="header-2">
					<th id="" class="left-edge">Job Name</th>
					<th id="action-th" class="left-edge">Status</th>					
					<th class="header-dropdown" data-sort-attr="data-employment-proposal-amount">
						<span data-toggle-id="sort_proposed-amount" >
							Amount<span class="glyphicon glyphicon-menu-down"></span>
						</span>
						<div id="sort_proposed-amount" class="dropdown-container sort-container">
							<label>
								<input type="radio"	name="sort_proposed-amount"
									data-sort-ascending="0">Highest to lowest
							</label>
							<label>
								<input type="radio"	name="sort_proposed-amount"
									data-sort-ascending="1">Lowest to highest
							</label>					
						</div>					
					</th>
					<th class="header-dropdown hide-with-calendar" data-sort-attr="data-job-start-date">
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
					<th class="header-dropdown hide-with-calendar" data-sort-attr="data-job-end-date">
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
					<th class="header-dropdown hide-with-calendar" data-sort-attr="data-job-duration-days">
						<span data-toggle-id="sort_duration_days" >
							Duration<span class="glyphicon glyphicon-menu-down"></span>
						</span>
						<div id="sort_duration_days" class="dropdown-container sort-container">
							<label>
								<input type="radio"	name="sort_duration_days"
									data-sort-ascending="1">Shortest to longest
							</label>
							<label>
								<input type="radio"	name="sort_duration_days"
									data-sort-ascending="0">Longest to shortest
							</label>					
						</div>					
					</th>
					<th class="header-dropdown hide-with-calendar" data-sort-attr="data-job-distance">
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
																	 
				</tr>
			</thead>					

			<tbody>
			
				<c:forEach items="${applicationDtos }" var="applicationDto">
<%-- 					<c:if test="${applicationDto.application.status <= 3 }"> --%>
						<tr class="application"
							data-application-status="${applicationDto.application.status }"
							data-application-id="${applicationDto.application.applicationId }"
							data-employment-proposal-amount="${applicationDto.currentWageProposal.amount }"
							data-job-start-date="${applicationDto.jobDto.milliseconds_startDate }"
							data-job-end-date="${applicationDto.jobDto.milliseconds_endDate }"
							data-job-duration-days="${applicationDto.jobDto.workDays.size() }"
							data-job-distance="${applicationDto.jobDto.distance }"
							>
							<td>
								<a class="accent ${applicationDto.application.status == 3 ? 'accepted' : ''}"
								   href="/JobSearch/job/${applicationDto.jobDto.job.id }?c=profile-incomplete&p=1">
									${applicationDto.jobDto.job.jobName }
								</a>
							</td>

							<td>
								<%@ include file="../wage_proposal/WageProposal_NEW.jsp" %>
							</td>
							<td>
								<%@ include file="../wage_proposal/History_WageProposals.jsp" %>
							</td>										
						
							<td class="hide-with-calendar">${applicationDto.jobDto.job.stringStartDate }</td>
							<td class="hide-with-calendar">${applicationDto.jobDto.job.stringEndDate }</td>	
							<td class="hide-with-calendar">${applicationDto.jobDto.workDays.size() } ${applicationDto.jobDto.workDays.size() <= 1 ? 'day' : 'days' }</td>	
							<td class="hide-with-calendar">${applicationDto.jobDto.job.city }, ${applicationDto.jobDto.job.state }</td>
												
						</tr>
<%-- 					</c:if> --%>
				</c:forEach>
										
			</tbody>
	

