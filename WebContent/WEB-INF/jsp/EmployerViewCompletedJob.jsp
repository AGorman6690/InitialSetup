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

	<input type="hidden" id="jobId" value="${job.id}"/>

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
						<th id="comment">Comment</th>
					</tr>
				</thead>
				<tbody>
				<c:forEach items="${job.employees }" var="employee">
					<tr class="employee-container">
						<td class="employee" data-id="${employee.userId }">${employee.firstName }</td>
						<td class="rate-criteria-container">
							<div class="rate-criterion" data-id="2" data-value="0">
								<label for="timeliness" class="control-label">Timeliness</label>
								<input name="timeliness" class="timeliness rating-loading" data-size="xs">
							</div>
							<div class="rate-criterion" data-id="1" data-value="0">
								<label for="work-ethic" class="control-label">Work Ethic</label>
								<input name="work-ethic" class="work-ethic rating-loading" data-size="xs">
							</div>		
							<div class="rate-criterion" data-id="3" data-value="0">
								<label for="hire-again" class="control-label">Hire Again?</label>
								<input name="hire-again" class="hire-again rating-loading" data-size="xs">
							</div>	
						</td>
						<td><textarea class="comment" rows="3"></textarea></td>
						
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
	        starCaptions: {1: 'Never', 2: 'Reluctantly', 3: 'Potentially', 4: 'Probably', 5: 'Absolutely'},
	        starCaptionClasses: {1: 'rated', 2: 'rated', 3: 'rated', 4: 'rated', 5: 'rated'},
	        hoverOnClear: false,
	    });

	    $('.timeliness, .work-ethic, .hire-agian').on('rating.change', function(event, value, caption) {
	        setRateCriterionValue($(this), value);

	        
	      });
		
	    $('.timeliness, .work-ethic').on('rating.clear', function(event) {
	    	setRateCriterionValue($(this), "0");
	      });
		
		
		
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
		submitRatingDto.endorsementCategoryIds = [];
	
		return submitRatingDto;
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