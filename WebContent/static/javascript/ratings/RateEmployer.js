$(document).ready(function(){
	
	
    $('.timeliness').rating({
        step: 1,
        starCaptions: {1: 'Horribly', 2: 'Poorly', 3: 'OK', 4: 'Adequately', 5: 'Perfectly'},       
// 	        starCaptionClasses: {0: 'not-rated'},
        starCaptionClasses: {1: 'rated', 2: 'rated', 3: 'rated', 4: 'rated', 5: 'rated'},
        hoverOnClear: false,
    });
    
    $('.work-ethic').rating({
        step: 1,
        starCaptions: {1: 'No', 2: 'Yes'},
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
//        setRateCriterionValue($(this), value);
        
      });
	
    $('.timeliness, .work-ethic, .hire-again').on('rating.clear', function(event) {
//    	setRateCriterionValue($(this), "0");
      });


	
})