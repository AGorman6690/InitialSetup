<%@ include file="./includes/Header.jsp"%>

<head>
	<script src="<c:url value="/static/javascript/Utilities.js" />"></script>
	<script src="<c:url value="/static/External/ratings/star-rating.js" />"></script>
	<script src="<c:url value="/static/javascript/InputValidation.js" />"></script>
	<link rel="stylesheet" type="text/css" href="/JobSearch/static/External/ratings/star-rating.css" />
<!-- 	<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/rateEmployees.css" /> -->
	<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/table.css" />
	<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/ratings.css" />
	<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/inputValidation.css" />
</head>

<body>

	<input type="hidden" id="jobId" value="${jobDto.job.id}"/>

	<div class="container">
		<div>${vtJobInfo }</div>

	
<c:choose>         
	<c:when test="${haveJobRatingsBeenSubmitted == true }">
		<div>Job ratings have already been submitted</div>
	</c:when>
	<c:otherwise>
		<div id="buttonsContainer" class="row">
			<div class="col col-sm-12">
				<button id="submitRatings" class="square-button">Submit Ratings</button>
			</div>
		</div>
		
		
		<div id="ratingsContainer">
			<table id="ratings" class="main-table-style">
				<thead>
					<tr>
						<th id="employee">Employee</th>
						<th id="rating">Rating</th>
						<th id="endorsements">Endorsements</th>
						<th id="comment">Comment</th>
					</tr>
				</thead>
				<tbody>
				<c:forEach items="${jobDto.employeeDtos }" var="employeeDto">
					<tr class="employee-container">
						<td class="employee" data-id="${employeeDto.user.userId }">${employeeDto.user.firstName }</td>
						<td class="rate-criteria-container">
							<c:forEach items="${employeeDto.rating.rateCriteria }" var="rateCriterion">
								<div class="rate-criterion" data-id="${rateCriterion.rateCriterionId }"
										 data-value="${rateCriterion.value }">
									
									<c:set var="rateCriterionValue_modifed" value="${rateCriterion.value == -1 ? 0 : rateCriterion.value }" />
									<c:choose>
										
										<c:when test="${rateCriterion.rateCriterionId == 1 }">											
												<label for="work-ethic" class="control-label">Work Ethic</label>
												<input name="work-ethic" class="work-ethic rating-loading"
													value="${rateCriterionValue_modifed }" data-size="xs">																				
										</c:when>
										<c:when test="${rateCriterion.rateCriterionId == 2 }">
<!-- 											<div class="rate-criterion" data-id="2" data-value="0"> -->
												<label for="timeliness" class="control-label">Timeliness</label>
												<input name="timeliness" class="timeliness rating-loading"
													value="${rateCriterionValue_modifed }" data-size="xs">
<!-- 											</div>										 -->
										</c:when>
										<c:when test="${rateCriterion.rateCriterionId == 3 }">
<!-- 											<div class="rate-criterion" data-id="3" data-value="0"> -->
												<label for="hire-again" class="control-label">Hire Again?</label>
												<input name="hire-again" class="hire-again rating-loading"
													value="${rateCriterionValue_modifed }" data-size="xs">
<!-- 											</div>										 -->
										</c:when>	
		
									</c:choose>
								</div>																	
	
							</c:forEach>
						</td>
						<td>
							<div class="endorsementsContainer">
							<c:forEach items="${jobDto.categories }" var="category">
								
								<c:set var="isCategoryEndorsed" value="0" />
								<c:forEach items="${employeeDto.rating.endorsements }" var="endorsement">
									<c:if test="${endorsement.categoryId == category.id}">
										<c:set var="isCategoryEndorsed" value="1" />
									</c:if>
								</c:forEach>
								
								<div data-id="${category.id }" data-value="${isCategoryEndorsed }" class="endorsement">
									<span class="glyphicon ${isCategoryEndorsed == 1 ? 'glyphicon-ok' : 'glyphicon-remove' }"></span>
									${category.name }
								</div>
							</c:forEach>
							</div>
						</td>						
						<td><textarea class="comment" rows="3">${employeeDto.rating.comment }</textarea></td>
						
					</tr>
				</c:forEach>				
				</tbody>
			
			</table>		
		</div>
	</c:otherwise>
</c:choose>

	</div> <!-- end container -->			
</body>

<script>

// 	var submitRatingDtos = [];
	
	
	$(document).ready(function(){
		
		$(".endorsement").click(function(){
			var value = $(this).attr("data-value");
			toggleClasses($(this).find(".glyphicon"), "glyphicon-remove", "glyphicon-ok");
			if(value == 0){
				$(this).attr("data-value", "1");
			}
			else{
				$(this).attr("data-value", "0");
			}
		})
		
		
	    $('.timeliness').rating({
	        step: 1,
	        starCaptions: {1: 'Never', 2: 'Rarely', 3: 'Occasionally', 4: 'Mostly', 5: 'Always'},
// 	        starCaptionClasses: {0: 'not-rated'},
	        starCaptionClasses: {1: 'rated', 2: 'rated', 3: 'rated', 4: 'rated', 5: 'rated'},
	        hoverOnClear: false,
	    });
	    
	    $('.work-ethic').rating({
	        step: 1,
	        starCaptions: {1: 'Horrible', 2: 'Poor', 3: 'Average', 4: 'Good', 5: 'Excellent'},
	        starCaptionClasses: {1: 'rated', 2: 'rated', 3: 'rated', 4: 'rated', 5: 'rated'},
	        hoverOnClear: false,
	    });

	    $('.hire-again').rating({
	        step: 1,
	        starCaptions: {1: 'Never', 2: 'Reluctantly', 3: 'Maybe', 4: 'Probably', 5: 'Absolutely'},
	        starCaptionClasses: {1: 'rated', 2: 'rated', 3: 'rated', 4: 'rated', 5: 'rated'},
	        hoverOnClear: false,
	    });

	    $('.timeliness, .work-ethic, .hire-again').on('rating.change', function(event, value, caption) {
	        setRateCriterionValue($(this), value);

	        
	      });
		
	    $('.timeliness, .work-ethic, .hire-again').on('rating.clear', function(event) {
	    	setRateCriterionValue($(this), "0");
	      });
		
	    
// 	    $(".work-ethic").rating("update", 1);
		
		
	    function setRateCriterionValue($rating, value){
	    	
	    	var rateCriterion = $rating.closest('.rate-criterion');
	    	$(rateCriterion).attr("data-value", value);
	    	
	    	$(rateCriterion).removeClass("invalid");
	    }
		
	    $("#submitRatings").click(function(){
	    	submitRatings();
	    })
		
	})
		
		
		

	function getSubmitRatingDtos(){
		
		var submitRatingDtos = [];
		var submitRatingDto;
		
		$.each($("table#ratings").find(".employee-container"), function(){
			
			submitRatingDto = getSubmitRatingDto($(this));
			
			submitRatingDtos.push(submitRatingDto);
				
		})
		
		return submitRatingDtos;
		
	}
	
	function getSubmitRatingDto($employeeContainer){
		var $employee;
		var $rateCriteriaContainer;
		var submitRatingDto = {};

		$employee = $($employeeContainer.find(".employee")[0]);
		$rateCriteriaContainer = $($employeeContainer.find(".rate-criteria-container")[0]);
		
		submitRatingDto.employeeId = $employee.attr("data-id");
		submitRatingDto.rateCriteria = getRateCriteria($rateCriteriaContainer);
		submitRatingDto.commentString = getComment($employeeContainer);
		submitRatingDto.endorsementCategoryIds = getEndorsementCategoryIds($employeeContainer);
	
		return submitRatingDto;
	}
	
	function getEndorsementCategoryIds($employeeContainer){
		
		var endorsementCategoryIds = [];
		var endorsements = $employeeContainer.find(".endorsement");
		$.each(endorsements, function(){
			if($(this).attr("data-value") == 1){
				endorsementCategoryIds.push($(this).attr("data-id"));	
			}			
		})
		return endorsementCategoryIds;
	}
	
	function getComment($employeeContainer){
		return $employeeContainer.find("textarea.comment").val();
	}
	
	function getRateCriteria($rateCriteriaContainer){
		
		var rateCriteria = [];
		var rateCriterion = {};
		
		$.each($rateCriteriaContainer.find(".rate-criterion"), function(){
			
			rateCriterion = {};
			rateCriterion.rateCriterionId = $(this).attr("data-id");
			rateCriterion.value = $(this).attr("data-value");
			
			rateCriteria.push(rateCriterion);
			
		})
		
		return rateCriteria;
		
	}
	
	function areInputsValid(){
		
		var invalidCount = 0;
		
// 		if(submitRatingDtos_wrapper.jobId == undefined){
// 			invalidCount += 1;
// 		}
		
		
		$.each($("table#ratings").find(".rate-criterion"), function(){
			
			if($(this).attr("data-value") == 0){
				invalidCount += 1;
				setInvalidCss($(this));
			}
			else{
				setValidCss($(this));
			}
			
		})
		
		if(invalidCount > 0) return false;
		else return true;
		
		
	}
	
	
	function submitRatings(){

		
		if(areInputsValid()){
			
			var submitRatingDtos_wrapper = {};
			submitRatingDtos_wrapper.jobId = $("#jobId").val();
			submitRatingDtos_wrapper.submitRatingDtos = getSubmitRatingDtos();
			
			
	
	
			$.ajax({
				type: "POST",
				url: '/JobSearch/user/rate',
				contentType : "application/json",
				headers : getAjaxHeaders(),
				data: JSON.stringify(submitRatingDtos_wrapper),
		        success: _success,
		        error: _error
		    });
	
			function _success(){
				window.location = "/JobSearch/user/profile";
			}
	
			function _error(){
	// 			alert("error rateEmpoyee");
			}
				
		}
	}
	

	
</script>

<%@ include file="./includes/Footer.jsp"%>