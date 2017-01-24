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
	