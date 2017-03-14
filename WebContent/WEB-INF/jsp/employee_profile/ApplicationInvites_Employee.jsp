<%@ include file="../includes/Header.jsp"%>
	

<div class="container">
	<table class="main-table-style">
		<thead>				
			<tr>
				<th id="" class="left-edge">Job Name</th>																	 
			</tr>
		</thead>					
		
		<tbody>
		
			<c:forEach items="${jobDtos_applicationInvites }" var="jobDto">
					<tr>
						<td>
							<a class="accent"
							   href="/JobSearch/job/${jobDto.job.id }?c=find&p=1">
								${jobDto.job.jobName }
							</a>
						</td>					
					</tr>
			</c:forEach>
									
		</tbody>
	</table>
</div>	

<%@ include file="../includes/Footer.jsp"%>