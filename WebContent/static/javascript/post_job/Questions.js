var postQuestionDtos = [];
var ongoingQuestionCount = 0;


$(document).ready(function(){
	
	$("#questionFormat").val("");
	
	$("#questionFormat").click(function(){
		var questionFormatId = $(this).find("option:selected").eq(0).attr("data-format-id");
		if(questionFormatId == 2 || questionFormatId == 3) slideDown($("#answerListContainer"), 400);				
		else slideUp($("#answerListContainer"), 400);				
	})

	$("#addAnswer").click(function(){ 		
		addAnotherAnswerOption();
	})
	
	$(".delete-answer").click(function(){	
		deleteAnswerOption(this);
	})
	

	$("#addedQuestions").on("click", "a.clickable", function(){
		
		// Add or remove "selected" class from clicked anchor
		if($(this).hasClass("selected")) $(this).removeClass("selected")
		else highlightArrayItem(this, $("#addedQuestions").find("a"), "selected");
		
		if(isAQuestionSelected()){
//			var selectedPostQuestionDto = getSelectedPostQuestionDto();
			showSelectedPostQuestionDto();			
			
			// Turn "Delete" and "Edit" to clickable
			setClickableness_ForQuestionActions(true, false, true, true);
			
			// Turn "Add" to un-clickable
			setClickableness_ForQuestionActions(false, true, false, false);
		}
		else{
			clearAllInputs($("#questionsContainer"));
			setClickableness_ForQuestionActions(false, false, true, true);			
			setClickableness_ForQuestionActions(true, true, false, false);
			enableAllInputFields($("#questionsContainer"));
		}

		clearInvalidContentAndStyle();
		

	})
	
	$("body").on("click", "#addQuestion.clickable", function(){
		addQuestion();
	
	})
	
	
	$("body").on("click", "#deleteQuestion.clickable", function(){
		deleteQuestion();
		clearAllInputs($("#questionsContainer"));
		enableAllInputFields($("#questionsContainer"));
		setClickableness_ForQuestionActions(false, false, true, true);
		
	})
	
	
	$("body").on("click", "#editQuestion.clickable", function(){
		
		enableAllInputFields($("#questionsContainer"));
		$("#editQuestionResponses").show();
		setClickableness_ForQuestionActions(false, true, true, true);
		setClickableness_ForAddedQuestions(false);
	})	
	
	$("#cancelEditQuestionChanges").click(function(){
		
		showSelectedPostQuestionDto();	
			
		setClickableness_ForQuestionActions(true, true, true, true);
		setClickableness_ForQuestionActions(false, true, false, false);
		setClickableness_ForAddedQuestions(true);
		$("#editQuestionResponses").hide();
	})
	
	$("#saveEditQuestionChanges").click(function(){
		
		if(areQuestionInputsValid){
			saveEditQuestionChanges();
			
			setClickableness_ForQuestionActions(true, true, true, true);
			setClickableness_ForQuestionActions(false, true, false, false);
			setClickableness_ForAddedQuestions(true);
			
			$("#editQuestionResponses").hide();
			disableAllInputFields($("#questionsContainer"));
		}
	})
	
	
})

function clearInvalidContentAndStyle(){
	removeAllInvalidStyles($("#questionsContainer"));
	$("#invalidAddQuestion").hide();
}

function saveEditQuestionChanges(){
	
	var newlyEditedQuestion = {};
	var selectedPostQuestionDto = {};
	
	selectedPostQuestionDto = getSelectedPostQuestionDto();
	newlyEditedQuestion = getPostQuestionDto();
	
	// When editing a question, the id must remain the same.
	// Otherwise the anchor's [data-question-id] needs to be updated.
	newlyEditedQuestion.id = selectedPostQuestionDto.id;
	
	// Remove the old selected question
	postQuestionDtos = removeArrayElementByIdProp(selectedPostQuestionDto.id, postQuestionDtos);
	
	// Add the new edited question
	postQuestionDtos.push(newlyEditedQuestion);
	
	updateAddedQuestionText(newlyEditedQuestion.id, newlyEditedQuestion.text);
}

function updateAddedQuestionText(questionId, questionText){
	
	var element = $("#addedQuestions").find("[data-question-id='" + questionId + "']")[0];
	
	var elementText = getAddedQuestionText(questionText);
	
	$(element).html(elementText);
	
}

function isAQuestionSelected(){
	if($("#addedQuestions").find("a.selected").length == 0) return false;
	else return true;
}

function setClickableness_ForAddedQuestions(doSetAsClickable){
	
	$("#addedQuestions").find("a").each(function(){
		if(doSetAsClickable) $(this).addClass("clickable");
		else $(this).removeClass("clickable");
	})
	
}

function setClickableness_ForQuestionActions(doSetAsClickable, doSetAdd, doSetDelete, doSetEdit){
	
	if(doSetAsClickable){
		if(doSetEdit) $("#editQuestion").addClass("clickable");
		if(doSetDelete) $("#deleteQuestion").addClass("clickable");
		if(doSetAdd) $("#addQuestion").addClass("clickable");
	}
	else{
		if(doSetEdit) $("#editQuestion").removeClass("clickable");
		if(doSetDelete) $("#deleteQuestion").removeClass("clickable");	
		if(doSetAdd) $("#addQuestion").removeClass("clickable");
	}
	
}

function deleteQuestion(){

	var selectedPostQuestionDto = getSelectedPostQuestionDto();
	postQuestionDtos = removeArrayElementByIdProp(selectedPostQuestionDto.id, postQuestionDtos);
	
	//Remove the question from the cart
	removeElementFromDOM($("#addedQuestions"), "data-question-id", selectedPostQuestionDto.id);
	
}

function showSelectedPostQuestionDto(){
	
	var selectedPostQuestionDto = getSelectedPostQuestionDto();
	
	
	$("#question").val(selectedPostQuestionDto.text);
	
	// Select the question format
	$("#questionFormat option[data-format-id='" + parseInt(selectedPostQuestionDto.formatId) + "']").prop("selected", "selected");
	$("#questionFormat").trigger("click");
	
	
	if(selectedPostQuestionDto.answerOptions.length > 0){
		
		resetAnswerListContainer();
		
		// Add an answer option for every answer option other than the first 2 
		for(i=0; i<selectedPostQuestionDto.answerOptions.length - 2; i++){
			$("#addAnswer").trigger("click");
		}
		
		// Populate the answer option inputs
		$("#answerList").find(".answer-container input").each(function(i, e){
			$(this).val(selectedPostQuestionDto.answerOptions[i]);
		})
	}
	
	
	disableAllInputFields($("#questionsContainer"));			
	
}

function resetAnswerListContainer(){
	$("#answerList").find(".answer-container").each(function(i, e){
		if(i > 1) $(e).remove();
	})
}

function getSelectedPostQuestionDto(){
	
	var selectedQuestionId = $("#addedQuestions").find("a.selected").attr("data-question-id");
	selectedQuestionId = parseInt(selectedQuestionId);
	
	var selectedQuestion = {};
	$.each(postQuestionDtos, function(){
		if(this.id == selectedQuestionId){
			selectedQuestion = this;		
		}
	})
	
	return selectedQuestion;
	
}

function addAnotherAnswerOption(){
	var anAnswerContainer = $("#answerList").find(".answer-container")[0];			
	var clone = $(anAnswerContainer).clone(true);			
	
	//Clear the input
	$(clone).find("input").val("");
	
	$("#answerList").append(clone);
}

function deleteAnswerOption(clickedAnswerOption){
	
	// There must remain at least two answer options
	if($("#answerList").find(".answer-container").length > 2){
		$(clickedAnswerOption).closest(".answer-container").remove();
	}
}


function addQuestion(){
	var postQuestionDto = {};
	if(areQuestionInputsValid()){
		postQuestionDto = getPostQuestionDto();
		ongoingQuestionCount += 1;
		postQuestionDto.id = ongoingQuestionCount;
		postQuestionDtos.push(postQuestionDto);
		addQuestionToDOM(postQuestionDto);
		clearAllInputs($("#questionsContainer"));
		slideUp($("#answerListContainer"), 400);
	}
}
function getPostQuestionDto(){
	
	var postQuestionDto = {};
	var answerOptionsInputs = []
	var answerOptions = [];
	
	
	postQuestionDto.text = $("#question").val();
	postQuestionDto.formatId = $("#questionFormat").find("option:selected").eq(0).attr("data-format-id");

	//If necessary, set the answer options
	if(doesQuestionHaveAnAnswerList(postQuestionDto)){

		$.each($("#answerList").find(".answer-container input"), function(){
			answerOptions.push($(this).val());
		})
		
		postQuestionDto.answerOptions = answerOptions;
	
	}else{
		postQuestionDto.answerOptions = [];
	}
	
	return postQuestionDto;
}

function doesQuestionHaveAnAnswerList(postQuestionDto){
	if(postQuestionDto.formatId == 2 || postQuestionDto.formatId == 3){
		return true;
	}else{
		return false;
	}
}

function addQuestionToDOM(postQuestionDto){
	
	var html = "<a data-question-id='" + postQuestionDto.id + "' class='accent no-hover clickable'>";
	
	var buttonText = getAddedQuestionText(postQuestionDto.text);
	html += buttonText;			
	html += "</a>";
				
	$("#addedQuestions").append(html);
		
}

function getAddedQuestionText(questionText){
	
	//If the qustion is longer than 20 characters, then only show the first 20.
	if(questionText.length > 50) return questionText.substring(0, 19) + "..."	
	else return questionText;
	
}

function areQuestionInputsValid(){
	
	var $e;
	var selectedQuestionFormatId;
	var invalidCount = 0;
	
	//Question format
	if($("#questionFormat").find("option:selected").length == 0){
		invalidCount += 1;
		setInvalidCss($("#questionFormat"));
	}
	else{
		setValidCss($("#questionFormat"));
	}

	
	//If multiple choice question, verify at least two answers have been given
	selectedQuestionFormatId = $("#questionFormat").find("option:selected").eq(0).attr("data-format-id")
	if(selectedQuestionFormatId == 2 || selectedQuestionFormatId ==3){
		
		$.each($("#answerList").find(".answer-container input"), function(i, e){
			
			if(i <= 2){
				if($(e).val() == ""){
					invalidCount += 1;
					setInvalidCss($(e));
				}
				else setValidCss($(e));
			}
				
		})
	}
		
	//Verify question
	if($("#question").val() == ""){
		invalidCount += 1;
		setInvalidCss($("#question"));
	}
	else setValidCss($("#question"))

	
	$e = $("#invalidAddQuestion");
	if(invalidCount > 0){
		$e.show();
		return false;
	}else{
		$e.hide();
		return true;
	}
}