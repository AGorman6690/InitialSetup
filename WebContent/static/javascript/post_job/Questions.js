var questions = [];
var ongoingQuestionCount = 0;
var slideSpeed = 600;

$(document).ready(function(){
	
	$("#question-format").val("");
	
	$("#question-format").click(function(){
		var questionFormatId = $(this).find("option:selected").eq(0).attr("data-format-id");
		if(questionFormatId == 2 || questionFormatId == 3) slideDown($("#answer-list-container"), slideSpeed);				
		else slideUp($("#answer-list-container"), slideSpeed);				
	})

	$("#add-answer").click(function(){ 		
		addAnotherAnswerOption();
	})
	
	$(".delete-answer").click(function(){	
		deleteAnswerOption(this);
	})	

	$("#added-questions").on("click", ".glyphicon-pencil", function(){		
		var $addedQuestion = $(this).closest(".added-question");
		var questionId = $addedQuestion.attr("data-question-id");
		$("#questions-wrapper").attr("data-question-id-under-edit", questionId);
		showQuestion(getQuestion(questionId));		
		setControlsVisibility_forEditQuestion(true, $addedQuestion);		
		enableAllInputFields($("#create-question-container"));
	})
	
	$("#cancel-question-edits").click(function(){		
		setControlsVisibility_forEditQuestion(false);
	})
	
	$("#save-question-edits").click(function(){				
		if(validateQuestionInputs()){
			var questionId = getQuestionIdUnderEdit();
			saveEditQuestionChanges(questionId);
			setControlsVisibility_forEditQuestion(false);	
			disableAllInputFields($("#create-question-container"));
		}
	})

	$("#create-new-question").click(function(){		
		clearAllInputs($("#create-question-container"));
		enableAllInputFields($("#create-question-container"));
		$("#answer-list-container").hide();
		showCreateQuestionContainer(true);
	})
	
	$("body").on("click", "#add-question.clickable", function(){
		addQuestion();	
	})
		
	$("body").on("click", ".added-question .glyphicon-trash", function(){
		var questionId = $(this).closest(".added-question").attr("data-question-id");
		deleteQuestion(questionId);
		clearAllInputs($("#create-question-container"));
		enableAllInputFields($("#create-question-container"));
	})
	
})
function showCreateQuestionContainer(request){
	if(request){
		$("#create-question-container").slideDown(slideSpeed);
	}else{
		$("#create-question-container").slideUp(slideSpeed);
	}
}
function getQuestionIdUnderEdit(){
	return $("#questions-wrapper").attr("data-question-id-under-edit");
}
function setControlsVisibility_forEditQuestion(prepareForEdits, $clickedAddedQuestion) {
	if(prepareForEdits){
		
		$("#add-question-wrapper").hide();
		$("#edit-question-actions").show();		
		$("#copy-or-new-question").slideUp(slideSpeed);
		$(".added-question").each(function(i, addedQuestion) {
			if(addedQuestion != $clickedAddedQuestion.get(0))  $(addedQuestion).slideUp(slideSpeed);
		})
		$("#create-question-container").slideDown(slideSpeed);
	}else{
		$("#create-question-container").slideUp(slideSpeed);
		$("#edit-question-actions").slideUp(slideSpeed);		
		$("#add-question-wrapper").slideDown(slideSpeed);
		$("#copy-or-new-question").slideDown(slideSpeed);
		$(".added-question").each(function(i, addedQuestion) {
			$(addedQuestion).show();
		})
	}
}
function saveEditQuestionChanges(questionId){
	
	var newlyEditedQuestion = {};
	var selectedQuestion = {};
	
	selectedQuestion = getQuestion(questionId);
	newlyEditedQuestion = getQuestionFromDOM();
	
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
	var element = $("#added-questions").find("[data-question-id='" + questionId + "'] .question-text")[0];
	var elementText = getAddedQuestionText(questionText);
	$(element).html(elementText);	
}
function deleteQuestion(questionId){
	questions = removeArrayElementByIdProp(questionId, questions);	
	removeElementFromDOM($("#added-questions"), "data-question-id", questionId);	
	setDisplay_addedQuestions();	
}
function setDisplay_addedQuestions(){
	if(questions.length > 0) $("#addedQuestionsContainer").show();
	else $("#addedQuestionsContainer").hide();
}
function showQuestion(question){	
	showQuestionDto(question);	
	disableAllInputFields($("#create-question-container"));		
}
function showQuestionDto(questionDto){
	$("#question").val(questionDto.text);
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
function getQuestion(questionId){	
	var question = {};
	$.each(questions, function(){
		if(this.questionId == questionId){
			question = this;		
		}
	})	
	return question;	
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
function validateQuestionInputs(){
	return validateInputElements($("#create-question-container"));
}
function addQuestion(){
	var question = {};
	if(validateQuestionInputs()){
		question = getQuestionFromDOM();
		ongoingQuestionCount += 1;
		question.questionId = ongoingQuestionCount;
		questions.push(question);
		addQuestionToDOM(question);
		clearAllInputs($("#create-question-container"));
		slideUp($("#answer-list-container"), slideSpeed);
		showCreateQuestionContainer(false);
	}
}
function getQuestionFromDOM(){	
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
	html += "<span class='glyphicon glyphicon-trash enlarge'></span>";
	html += "<span class='glyphicon glyphicon-pencil enlarge'></span>";
	html += "<span class='question-text' data-question-id='" + question.questionId + "' class='no-hover clickable'>";
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