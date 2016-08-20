<%@ include file="./includes/Header.jsp"%>

<head>
<%-- <script src="<c:url value="/static/javascript/Jobs.js" />"></script> --%>
<%-- <script src="<c:url value="/static/javascript/Category.js" />"></script> --%>
<%-- <script src="<c:url value="/static/javascript/User.js" />"></script> --%>
<%-- <script src="<c:url value="/static/javascript/AppendHtml.js" />"></script> --%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/employeeProfile.css" />

<script src="<c:url value="/static/javascript/Utilities.js" />"></script>
</head>
	
<body>

	<div class="container">
		<div id="openApplicationsContainer">
			<h4>Open Applications</h4>
		
			<table id="openApplications">
				<thead>
					<tr>
						<th>Job Name</th>
						<th>Application Status</th>
						<th>Desired Pay</th>
						<th>Counter Offer</th>
					</tr>
				</thead>
				<tbody>
					
					<c:forEach items="${applicationResponseDtos }" var="dto">
					<tr>
						<td>${dto.job.jobName }</td>
						<td>
							<c:choose>
								<c:when test="${dto.application.status == 0  }">Waiting for response</c:when>
								<c:when test="${dto.application.status == 1  }">Declined</c:when>
								<c:when test="${dto.application.status == 2  }">Being considered</c:when>
								<c:when test="${dto.application.status == 3  }">Accepted</c:when>
							</c:choose>							
						</td>
						<td>
							<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${dto.currentDesiredWage}"/>
						</td>
						<td>
						
						
						<c:choose>
							<c:when test="${dto.currentWageProposal.proposedByUserId != user.userId }">
<!-- 								If employer has made the last wage proposal -->
								<div id="${dto.currentWageProposal.id}" class="counter-offer-container">
									<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${dto.currentWageProposal.amount }"/>
									
									
									<div class="counter-offer-response">
									<button class="accept-counter">Accept</button>
									<button class="re-counter">Counter</button>
									
										<div class="re-counter-amount-container hide-element">
											<input class="re-counter-amount"></input>
											<button class="send-counter-offer">Send</button>
											<button class="cancel-counter-offer">Cancel</button>
										</div>
										
									</div>
									<div class="sent-counter-notification hide-element">
										(counter offer sent)
									</div>	
								</div>
							</c:when>
							<c:otherwise>
								<div class="sent-counter-notification">
									(
									<fmt:formatNumber type="number" minFractionDigits="2" maxFractionDigits="2" value="${dto.currentWageProposal.amount }"/>
									 counter offer has been sent)
								</div>									
							</c:otherwise>
						</c:choose>
						</td>					
					</tr>
					</c:forEach>
					
				</tbody>
			</table>
		</div>
	</div>
</body>

<script>
	$(document).ready(function(){
		
		$(".re-counter").click(function(){
			var $e = $($(this).siblings(".re-counter-amount-container")[0]); 
			toggleClasses($e, "hide-element", "show-block");
		})
		
	$(".cancel-counter-offer").click(function(){
		$(this).parent().hide();
		$($(this).siblings("input")[0]).val("");
	})
	
	$(".send-counter-offer").click(function(){
		
		//Read the DOM
		var counterOfferResponse = $(this).parents(".counter-offer-response")[0];
		var counterOfferContainer = $(this).parents(".counter-offer-container")[0];
		var counterAmount = $($(this).siblings("input")[0]).val();
		var recounterNotificaiton = $(counterOfferContainer).find(".sent-counter-notification")[0];
	
		//Create dto
		var wageProposalCounterDTO = {};
		wageProposalCounterDTO.wageProposalIdToCounter = $(counterOfferContainer).attr("id");
		wageProposalCounterDTO.counterAmount = counterAmount;

		//Make ajax call
		sendCounterOffer(wageProposalCounterDTO, function(){
			
			//After the counter has been made, hide the re-counter controls.			
			$(counterOfferResponse).hide();
			
			//Inform the user that the counter has been sent.
			$(recounterNotificaiton).html("(" + twoDecimalPlaces(counterAmount) + " counter offer has been sent)");
			$(recounterNotificaiton).show();

		})

	})		
		
		
		
		
		
		
		$('#availableDays').datepicker({
			toggleActive: true,
			clearBtn: true,
			todayHighlight: true,
			startDate: new Date(),
			multidate: true
		});

		//Display employee's availability.
		//This seems hackish...
		var dates = [];
		var str = $("#arrayDates").val();
		str = str.substring(1, str.length -1);
		dates = str.split(",");
		var formatedDates = [];
		for(var i = 0; i < dates.length; i++){
			var date = dates[i];
			date =  date.trim();
			var day = date.substring(8, 10);
			var month = date.substring(5, 7);
			var year = date.substring(0, 4);
			formatedDates.push(month + "-" + day + "-" + year);
		}

		$("#availableDays").datepicker('setDates', formatedDates);

	})
	

	function updateAvailability(){


		var availabilityDTO = {};
		availabilityDTO.userId = $("#userId").val();
		availabilityDTO.stringDays = $("#availableDays").datepicker('getDates');

		var headers = {};
		headers[$("meta[name='_csrf_header']").attr("content")] = $(
				"meta[name='_csrf']").attr("content");

		$.ajax({
			type : "POST",
			url : environmentVariables.LaborVaultHost + "/JobSearch/user/availability/update",
			headers : headers,
			contentType : "application/json",
			dataType : "application/json", // Response
			data : JSON.stringify(availabilityDTO)
		}).done(function() {
// 			$('#home')[0].click();
		}).error(function() {
// 			$('#home')[0].click();
		});

	}

</script>



<%@ include file="./includes/Footer.jsp"%>

