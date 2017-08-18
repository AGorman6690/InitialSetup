var questions = [];
var ongoingQuestionCount = 0;


$(document).ready(function(){
	
	$("#question-format").val("");
	
	$("#question-format").click(function(){
		var questionFormatId = $(this).find("option:selected").eq(0).attr("data-format-id");
		if(questionFormatId == 2 || questionFormatId == 3) slideDown($("#answer-list-container"), 400);				
		else slideUp($("#answer-list-container"), 400);				
	})

	$("#add-answer").click(function(){ 		
		addAnotherAnswerOption();
	})
	
	$(".delete-answer").click(function(){	
		deleteAnswerOption(this);
	})
	

	$("#added-questions").on("click", ".glyphicon-pencil", function(){
		
		var $addedQuestion = $(this).closest(".added-question");
		hideOtherConrolsVisibilityForQuestionEdit(true, $addedQuestion);
		showSelectedQuestion();		
		
		
		
		
		// Add or remove "selected" class from clicked anchor
		if($(this).hasClass("selected")) $(this).removeClass("selected")
		else highlightArrayItem(this, $("#added-questions").find("a"), "selected");
		
		if(isAQuestionSelected()){

			showSelectedQuestion();		
			
			setClickableness_ForQuestionActions(true, false, true, true);
			$("#addedQuestionsContainer .question-actions-container").show();
			setDisplay_createQuestionContainer(true);
		}
		else{
			clearAllInputs($("#create-question-container"));
			
			setClickableness_ForQuestionActions(true, true, false, false);
			enableAllInputFields($("#create-question-container"));
			$("#addedQuestionsContainer .question-actions-container").hide();
		}

		clearInvalidContentAndStyle();
		
	})
	
//	$("body").on("click", "#newQuestion.clickable", function(){
	$("#create-new-question").click(function(){
		
		clearAllInputs($("#create-question-container"));
		enableAllInputFields($("#create-question-container"));
		$("#answer-list-container").hide();
		setClickableness_ForQuestionActions(true, true, false, false);
		deselectAddedQuestion();
		setDisplay_createQuestionContainer(true);
	})
	
	$("body").on("click", "#add-question.clickable", function(){
		addQuestion();	
	})
	
	
	$("body").on("click", ".added-question .glyphicon-trash", function(){
		var questionId = $(this).closest(".added-question").attr("data-question-id");
		deleteQuestion(questionId);
		clearAllInputs($("#create-question-container"));
		enableAllInputFields($("#create-question-container"));
		setClickableness_ForQuestionActions(true, true, false, false);
		
	})
	
	
	$("body").on("click", "#editQuestion.clickable", function(){
		
		enableAllInputFields($("#create-question-container"));
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
			disableAllInputFields($("#create-question-container"));
		}
	})
	
	
})
function hideOtherConrolsVisibilityForQuestionEdit(request, $clickedAddedQuestion){
	
	
	if(request){
		$("#copy-or-new-question").hide();
		$(".added-question").each(function(i, addedQuestion) {
			if(addedQuestion != $clickedAddedQuestion.get(0))  $(addedQuestion).hide();
		})
	}else{
		$("#copy-or-new-question").show();
		$(".added-question").each(function(i, addedQuestion) {
			$(addedQuestion).show();
		})
	}
	
}
function deselectAddedQuestion(){
	$("#added-questions").find("a.selected").eq(0).removeClass("selected");
}

function resetEntireQuestionSection(){
	
	questions = [];		
	$("#added-questions").empty();	
	
	clearAllInputs($("#create-question-container"));
	enableAllInputFields($("#create-question-container"));
	$("#answer-list-container").hide();
	setClickableness_ForQuestionActions(true, true, false, false);
	
}

function clearInvalidContentAndStyle(){
	removeAllInvalidStyles($("#create-question-container"));
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
	questions = $.grep(questions, function(question, i) {
		return question.questionId != selectedQuestion.questionId;
	})
	// Add the new edited question
	questions.push(newlyEditedQuestion);
	
	updateAddedQuestionText(newlyEditedQuestion.questionId, newlyEditedQuestion.text);
}

function updateAddedQuestionText(questionId, questionText){
	
	var element = $("#added-questions").find("[data-question-id='" + questionId + "']")[0];
	
	var elementText = getAddedQuestionText(questionText);
	
	$(element).html(elementText);
	
}

function isAQuestionSelected(){
	if($("#added-questions").find("a.selected").length == 0) return false;
	else return true;
}

function setClickableness_ForAddedQuestions(doSetAsClickable){
	
	$("#added-questions").find("a").each(function(){
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

function deleteQuestion(questionId){
	questions = removeArrayElementByIdProp(questionId, questions);
	
	//Remove the question from the cart
	removeElementFromDOM($("#added-questions"), "data-question-id", questionId);
	
	setDisplay_addedQuestions();
	
}

function setDisplay_addedQuestions(){
	if(questions.length > 0) $("#addedQuestionsContainer").show();
	else $("#addedQuestionsContainer").hide();
}
function showSelectedQuestion(){
	
	var selectedQuestion = getSelectedQuestion();
	
	showQuestionDto(selectedQuestion);

	
	disableAllInputFields($("#create-question-container"));			
	
}

function showQuestionDto(questionDto){
	$("#question").val(questionDto.text);
	
	// Select the question format
	$("#question-format option[data-format-id='" + parseInt(questionDto.formatId) + "']").prop("selected", "selected");
	$("#question-format").trigger("click");
	
	
	if(questionDto.answerOptions.length > 0){
		
		resetAnswerListContainer();
		
		// Add an answer option for every answer option other than the first 2 
		for(i=0; i<questionDto.answerOptions.length - 2; i++){
			$("#add-answer").trigger("click");
		}
		
		// Populate the answer option inputs
		$("#answer-list").find(".answer-container input").each(function(i, e){
			$(this).val(questionDto.answerOptions[i].text);
		})
	}
}

function resetAnswerListContainer(){
	$("#answer-list").find(".answer-container").each(function(i, e){
		if(i > 1) $(e).remove();
	})
}

function getSelectedQuestion(){
	
	var selectedQuestionId = $("#added-questions").find("a.selected").attr("data-question-id");
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
	var anAnswerContainer = $("#answer-list").find(".answer-container")[0];			
	var clone = $(anAnswerContainer).clone(true);			
	
	//Clear the input
	$(clone).find("input").val("");
	
	$("#answer-list").find(".list-item").last().after(clone);
}

function deleteAnswerOption(clickedAnswerOption){
	
	// There must remain at least two answer options
	if($("#answer-list").find(".answer-container").length > 2){
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
		clearAllInputs($("#create-question-container"));
		slideUp($("#answer-list-container"), 400);
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
	question.formatId = $("#question-format").find("option:selected").eq(0).attr("data-format-id");

	//If necessary, set the answer options
	if(doesQuestionHaveAnAnswerList(question)){

		$.each($("#answer-list").find(".answer-container input"), function(){
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
	var questionText = getAddedQuestionText(question.text);
	var html = "<div class='added-question' data-question-id=" + question.questionId + ">";
	html += "<span class='glyphicon glyphicon-trash'></span>";
	html += "<span class='glyphicon glyphicon-pencil'></span>";
	html += "<span data-question-id='" + question.questionId + "' class='no-hover clickable'>";
	html += questionText;			
	html += "</span>";
	html += "</div>";
				
	$("#added-questions").append(html);
		
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
	if($("#question-format").find("option:selected").length == 0){
		invalidCount += 1;
		setInvalidCss($("#question-format"));
	}
	else{
		setValidCss($("#question-format"));
	}

	
	//If multiple choice question, verify at least two answers have been given
	selectedQuestionFormatId = $("#question-format").find("option:selected").eq(0).attr("data-format-id")
	if(selectedQuestionFormatId == 2 || selectedQuestionFormatId ==3){
		
		$.each($("#answer-list").find(".answer-container input"), function(i, e){
			
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