var questions = [];
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

			showSelectedQuestion();		
			
			setClickableness_ForQuestionActions(true, false, true, true);
			$("#addedQuestionsContainer .question-actions-container").show();
			setDisplay_createQuestionContainer(true);
		}
		else{
			clearAllInputs($("#questionsContainer"));
			
			setClickableness_ForQuestionActions(true, true, false, false);
			enableAllInputFields($("#questionsContainer"));
			$("#addedQuestionsContainer .question-actions-container").hide();
		}

		clearInvalidContentAndStyle();
		
	})
	
//	$("body").on("click", "#newQuestion.clickable", function(){
	$("#create-new-question").click(function(){
		
		clearAllInputs($("#questionsContainer"));
		enableAllInputFields($("#questionsContainer"));
		$("#answerListContainer").hide();
		setClickableness_ForQuestionActions(true, true, false, false);
		deselectAddedQuestion();
		setDisplay_createQuestionContainer(true);
	})
	
	$("body").on("click", "#addQuestion.clickable", function(){
		addQuestion();	
	})
	
	
	$("body").on("click", "#deleteQuestion.clickable", function(){
		deleteQuestion();
		clearAllInputs($("#questionsContainer"));
		enableAllInputFields($("#questionsContainer"));
		setClickableness_ForQuestionActions(true, true, false, false);
		
	})
	
	
	$("body").on("click", "#editQuestion.clickable", function(){
		
		enableAllInputFields($("#questionsContainer"));
		$("#editQuestionResponses").show();
		setClickableness_ForAddedQuestions(false);
		setClickableness_ForQuestionActions(false, false, false, false);
	})	
	
	$("#cancelEditQuestionChanges").click(function(){
		
		showSelectedQuestion();	
			
		setClickableness_ForQuestionActions(true, false, true, true);
		setClickableness_ForAddedQuestions(true);
		$("#editQuestionResponses").hide();
	})
	
	$("#saveEditQuestionChanges").click(function(){
		
		if(areQuestionInputsValid){
			saveEditQuestionChanges();
			
			setClickableness_ForQuestionActions(true, false, true, true);
			setClickableness_ForAddedQuestions(true);
			
			$("#editQuestionResponses").hide();
			disableAllInputFields($("#questionsContainer"));
		}
	})
	
	
})

function deselectAddedQuestion(){
	$("#addedQuestions").find("a.selected").eq(0).removeClass("selected");
}

function resetEntireQuestionSection(){
	
	questions = [];		
	$("#addedQuestions").empty();	
	
	clearAllInputs($("#questionsContainer"));
	enableAllInputFields($("#questionsContainer"));
	$("#answerListContainer").hide();
	setClickableness_ForQuestionActions(true, true, false, false);
	
}

function clearInvalidContentAndStyle(){
	removeAllInvalidStyles($("#questionsContainer"));
	$("#invalidAddQuestion").hide();
}

function saveEditQuestionChanges(){
	
	var newlyEditedQuestion = {};
	var selectedQuestion = {};
	
	selectedQuestion = getSelectedQuestion();
	newlyEditedQuestion = getQuestion();
	
	// When editing a question, the id must remain the same.
	// Otherwise the anchor's [data-question-id] needs to be updated.
	newlyEditedQuestion.questionId = selectedQuestion.questionId;
	
	// Remove the old selected question
	questions = removeArrayElementByIdProp(selectedQuestion.questionId, questions);
	
	// Add the new edited question
	questions.push(newlyEditedQuestion);
	
	updateAddedQuestionText(newlyEditedQuestion.questionId, newlyEditedQuestion.text);
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


function setClickableness_ForQuestionActions(doSetNew_asClickable, doSetAdd_asClickable,
												doSetDelete_asClickable, doSetEdit_asClickable){
	
	if(doSetEdit_asClickable) $("#editQuestion").addClass("clickable");
	else $("#editQuestion").removeClass("clickable");
	
	if(doSetDelete_asClickable) $("#deleteQuestion").addClass("clickable");
	else $("#deleteQuestion").removeClass("clickable");	
	
	if(doSetAdd_asClickable) $("#addQuestion").addClass("clickable");
	else $("#addQuestion").removeClass("clickable");
	
	if(doSetNew_asClickable) $("#newQuestion").addClass("clickable");
	else $("#newQuestion").removeClass("clickable");
	
}

function deleteQuestion(){

	var selectedQuestion = getSelectedQuestion();
	questions = removeArrayElementByIdProp(selectedQuestion.questionId, questions);
	
	//Remove the question from the cart
	removeElementFromDOM($("#addedQuestions"), "data-question-id", selectedQuestion.questionId);
	
	setDisplay_addedQuestions();
	
}

function setDisplay_addedQuestions(){
	if(questions.length > 0) $("#addedQuestionsContainer").show();
	else $("#addedQuestionsContainer").hide();
}
function showSelectedQuestion(){
	
	var selectedQuestion = getSelectedQuestion();
	
	showQuestionDto(selectedQuestion);

	
	disableAllInputFields($("#questionsContainer"));			
	
}

function showQuestionDto(questionDto){
	$("#question").val(questionDto.text);
	
	// Select the question format
	$("#questionFormat option[data-format-id='" + parseInt(questionDto.formatId) + "']").prop("selected", "selected");
	$("#questionFormat").trigger("click");
	
	
	if(questionDto.answerOptions.length > 0){
		
		resetAnswerListContainer();
		
		// Add an answer option for every answer option other than the first 2 
		for(i=0; i<questionDto.answerOptions.length - 2; i++){
			$("#addAnswer").trigger("click");
		}
		
		// Populate the answer option inputs
		$("#answerList").find(".answer-container input").each(function(i, e){
			$(this).val(questionDto.answerOptions[i].text);
		})
	}
}

function resetAnswerListContainer(){
	$("#answerList").find(".answer-container").each(function(i, e){
		if(i > 1) $(e).remove();
	})
}

function getSelectedQuestion(){
	
	var selectedQuestionId = $("#addedQuestions").find("a.selected").attr("data-question-id");
	selectedQuestionId = parseInt(selectedQuestionId);
	
	var selectedQuestion = {};
	$.each(questions, function(){
		if(this.questionId == selectedQuestionId){
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
	else{
		$(clickedAnswerOption).siblings("input").eq(0).val("");
	}
}


function addQuestion(){
	var question = {};
	if(areQuestionInputsValid()){
		question = getQuestion();
		ongoingQuestionCount += 1;
		question.questionId = ongoingQuestionCount;
		questions.push(question);
		addQuestionToDOM(question);
		clearAllInputs($("#questionsContainer"));
		slideUp($("#answerListContainer"), 400);
		setDisplay_addedQuestions();
		setDisplay_createQuestionContainer(false);
	}
}
function getQuestion(){
	
	var question = {};
	var answerOptionsInputs = []
	var answerOptions = [];
	var answerOption = {};
	
	question.text = $("#question").val();
	question.formatId = $("#questionFormat").find("option:selected").eq(0).attr("data-format-id");

	//If necessary, set the answer options
	if(doesQuestionHaveAnAnswerList(question)){

		$.each($("#answerList").find(".answer-container input"), function(){
			answerOption = {};
			answerOption.text = $(this).val();
			answerOptions.push(answerOption);
		})
		
		question.answerOptions = answerOptions;
	
	}else{
		question.answerOptions = [];
	}
	
	return question;
}

function doesQuestionHaveAnAnswerList(question){
	if(question.formatId == 2 || question.formatId == 3){
		return true;
	}else{
		return false;
	}
}

function addQuestionToDOM(question){
	
	var html = "<a data-question-id='" + question.questionId + "' class='no-hover clickable'>";
	
	var buttonText = getAddedQuestionText(question.text);
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