$(document).ready(function() {
	
	var $clonedNewQuestionContainer = $($("#new-question-container").clone(true, true));
	
	$("#new-question-container-container").on("click", "#addNewQuestion", function(event){
		addNewQuestion(this, event, $clonedNewQuestionContainer);
	})
	
	//This must come AFTER the "$("#addNewQuestion").on(...)" event has been bound to the
	//add new question element.
// 		var $cloned = $("#questionFormats").clone(true, true);

	$("body").on("click", ".add-new-answer-item", function(e){
// 			e.stopPropagation();
		if($(this).hasClass("disabled") == 0){
			var $clonedClone = $clonedAnswerListItem.clone(true, true);
			$clonedClone.insertBefore($(this));
		}
	})		
	var $clonedAnswerListItem = $($($("#new-question-container").find(".list-group-item")[0]).clone(true, true));

	$("body").on("click", ".delete-question", function(){
		if($(this).hasClass("disabled") == 0){
			$(this).closest(".added-question").remove();	
		}
	})
	
	$("body").on("click", ".delete-answer-item", function(){
		
		if($(this).hasClass("disabled") == 0){
			$(this).parent().remove();
		}
	})
	
	$("#addedQuestions").on("click", ".toggle-question-info-container", function(){
	
		var infoContainer = $(this).siblings(".question-info-container")[0];
	
		if( $(infoContainer).is(":visible") == true){
			$(this).removeClass("glyphicon-menu-down");
			$(this).addClass("glyphicon-menu-right");
		}else{			
			$(this).removeClass("glyphicon-menu-right");
			$(this).addClass("glyphicon-menu-down");
		}
			
		$(infoContainer).toggle();

	})

	
	$('#addedQuestions').on('click', '.toggle-question-activeness', function(){
		if($(this).hasClass("disabled") == 0){
			if($(this).hasClass("enable-question") == 1){
				$(this).removeClass("enable-question");
				$(this).addClass("disable-question");
				this.setAttribute("data-content", "Enable question for current job posting");
				
			}else{
				$(this).removeClass("disable-question");
				$(this).addClass("enable-question");
				this.setAttribute("data-content", "Disable question for current job posting");
			}
		}
		
		$(".popover1").popover('hide');
	})
	
	$("#jobQuestionsContainer").on("change", "select", function(event) {
		
		event.stopPropagation();
		
		var selectedAnswerFormatValue = $($(this).find(":selected")).val();
		var $answerList = $($(this).parent().siblings(".answer-option-list")[0]);
		
		if (selectedAnswerFormatValue == 2
				|| selectedAnswerFormatValue == 3) {
			
			$answerList.show();
		} else {
			$answerList.hide();
		}

		//If necessary, remove "invalid" css
		validateSelectInput($(this).parent(), $(this).find(":selected").val());

	})
	
	$("body").on("change", ".post-job-select-container.invalid-select-input", function(){
		validateSelectInput($(this), $(this).find('.post-job-select').val());
	}) 	
	
	$("body").on("change", ".invalid-input-existence", function(){
		validateInputExistence($(this), $(this).val());
	}) 	 		
	
})


function addNewQuestion(e, event, $clonedNewQuestionContainer){
	
	if($(e).hasClass("disabled") == 0){
		if(validateAddQuestionInputs() == 1){

			var selectedAnswerFormatValue = $($("#new-question-container").find("select")[0]).find(":selected").val();
			var questionText = $("#newQuestionText").val();

			if (selectedAnswerFormatValue > -1 && questionText != "" ){
			
	// 			event.stopPropagation();
				event.preventDefault();
				
				var newId = questionContainerIdPrefix + questionCount;
				
				//Clone added-question html template for new question
				$("#added-question-template-container .added-question-template").clone().appendTo($("#addedQuestions"));			
				var $addedQuestion = $($("#addedQuestions").find(".added-question-template")[0]);
				$addedQuestion.attr('id', newId);
				$addedQuestion.data('questionId', questionCount);
				
				//Add the added-question class, remove the template class.
				//This identifies an added-question.
				$addedQuestion.removeClass("added-question-template");
				$addedQuestion.addClass("added-question");
				
				//Set the added question's text					
				$addedQuestion.find('.question-text').val(questionText);
				
				//From the new question container, clone the question formats and answer list
				//to the just added question 
				var $addedQuestionInfoContainer = $($addedQuestion.find(".question-info-container")[0]);
				$("#new-question-container .question-formats-container").clone().appendTo($addedQuestionInfoContainer);
				$("#new-question-container .answer-option-list").clone(true, true).appendTo($addedQuestionInfoContainer);
				
				//Select the proper question format
				$($addedQuestion.find("select")[0]).val(selectedAnswerFormatValue).change();
													
				//Reset the new question html
				$("#new-question-container").remove();		
				$clonedNewQuestionContainer.clone(true, true).appendTo($("#new-question-container-container"));

				//Add question to the array
				var question = {};	
				question.questionId = $addedQuestion.data('questionId');
				
				//Set question text and question format					
				question.text = $addedQuestion.find('.question-text').val();
				question.formatId = $addedQuestion.find('select').find(":selected").val();

				if(question.formatId == 0 || question.formatId == 2 || question.formatId == 3){

					//Set answer options
					question.answerOptions = [];
					var answerOptions = $addedQuestion.find('.answer-option');
					for(var j = 0; j < answerOptions.length; j++){
						var answerOption = {};
						answerOption.answerOption = $(answerOptions[j]).val();
						if(answerOption.answerOption != "") {
							question.answerOptions.push(answerOption);
						}
					}	
				}	
				
				questions.push(question);
				questionCount += 1;
	
				setPopovers();
			}				
		}
	}
}