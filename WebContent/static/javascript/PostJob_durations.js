
function showAddedQuestions(request){
	if(request){
		slideDown($("#cartContainer"), 500);
		$("#editQuestion").show();
		$("#deleteQuestion").show();
	}
	else{
		slideUp($("#cartContainer"), 500);
		$("#editQuestion").hide();
		$("#deleteQuestion").hide();
	}
}
		
function updateAddedQuestionText(questionId, questionText){
	
	var element = $("#addedQuestions").find("[data-question-id='" + questionId + "']")[0];
	
	var elementText = getAddedQuestionText(questionText);
	
	$(element).html(elementText);
	
}

function getAddedQuestionText(questionText){
	
	//If the qustion is longer than 20 characters, then only show the first 20.
	if(questionText.length > 50){
		return questionText.substring(0, 19) + "..."
	}else{
		return questionText;
	}
}

function addQuestionToDOM(postQuestionDto){
	var html = "<a data-question-id='" + postQuestionDto.id + "' class='accent no-hover clickable'>";
	
	var buttonText = getAddedQuestionText(postQuestionDto.text);
	html += buttonText;			
	html += "</a>";
				
	$("#addedQuestions").append(html);
	
	clearPostQuestionInputs();
		
}

function clearPostQuestionInputs(){
	$("#question").val("");
	$("#questionFormat option[value='-1']").prop("selected", "selected");
	
	$("#answerList").find(".answer-container input").each(function(){
		$(this).val("");
	});
	
	clearInvalidCss();
}

function buttonIsCurrentlySelected(button){
	if($(button).hasClass("selected") == 1){
		return true;
	}
	else{
		return false;
	}
}

function getSelectedQuestion(){
	var selectedQuestionId = $($("#questionCart").find("a.selected")[0]).attr("data-question-id");
	selectedQuestionId = parseInt(selectedQuestionId);
	
	var selectedQuestion = {};
	$.each(postQuestionDtos, function(){
		if(this.id == selectedQuestionId){
			selectedQuestion = this;		
		}
	})
	
	return selectedQuestion;
	
}

function clearPostQuestionInputs(){
	$("#question").val("");
	$("#questionFormat option[value='-1']").prop("selected", "selected");
	
	$("#answerList").find(".answer-container input").each(function(){
		$(this).val("");
	});
	
	clearInvalidCss();
}

function deleteQuestion(){

	var selectedQuestion = getSelectedQuestion();
	postQuestionDtos = removeArrayElementByIdProp(selectedQuestion.id, postQuestionDtos);
	
	//Remove the question from the cart
	removeElementFromDOM($("#questionCart"), "data-question-id", selectedQuestion.id);
	
}	

function showSelectedQuestion(){
	var selectedQuestion = getSelectedQuestion();
	var i;
	$("#question").val(selectedQuestion.text);
	$("#questionFormat option[value='" + parseInt(selectedQuestion.formatId) + "']").prop("selected", "selected");
	$("#questionFormat").trigger("click");
	
	//If a single or multiple answer
	if(doesQuestionHaveAnAnswerList(selectedQuestion)){		
		
		//Clear all the answer option inputs except the first 2.
		//By definition there must be at least two answer options.
		//Plus, at least 1 .answer-container needs to remain in order to
		//have something to clone in the "#addAnswer" click event.
		i = 0;
		$("#answerList").find(".answer-container").each(function(){
			if(i > 1){
				$(this).remove();
			}					
			i += 1;
		})
		
		//Add an answer option for every answer option other than the first 2 
		for(i=0; i<selectedQuestion.answerOptions.length - 2; i++){
			$("#addAnswer").trigger("click");
		}
		
		//Populate the answer option inputs
		var answerInputs = $("#answerList").find(".answer-container input");
		i = 0;
		$.each(answerInputs, function(){
			$(this).val(selectedQuestion.answerOptions[i]);
			i += 1;
		})
	}
}


function getPostQuestionDto(){
	
	var postQuestionDto = {};
	var answerOptionsInputs = []
	var answerOptions = [];
	
	
	postQuestionDto.text = $("#question").val();
	postQuestionDto.formatId = $("#questionFormat").find("option:selected").val();

	//If necessary, set the answer options
	if(doesQuestionHaveAnAnswerList(postQuestionDto)){
		answerInputs = $("#answerList").find(".answer-container input");
		$.each(answerInputs, function(){
			answerOptions.push($(this).val());
		})
		
		postQuestionDto.answerOptions = answerOptions;
	
	}else{
		postQuestionDto.answerOptions = [];
	}
	
	return postQuestionDto;
}

function showSaveAndCancelEditQuestionChanges(request){
	if(request){
		$("#saveEditQuestionChanges").show();
		$("#cancelEditQuestionChanges").show();
	}
	else{
		$("#saveEditQuestionChanges").hide();
		$("#cancelEditQuestionChanges").hide();
	}
}

function unselectQuestion(){
	var q = $("#addedQuestions").find(".selected")[0];
	$(q).removeClass("selected");
	
	clearPostQuestionInputs();
}

function setQuestionActionsAsClickable(request){
	

	if(request){
		setActionAsClickable(true, $editQuestion);
		setActionAsClickable(true, $deleteQuestion);
	}
	else{
		setActionAsClickable(false, $editQuestion);
		setActionAsClickable(false, $deleteQuestion);
	}
	
}

function setActionAsClickable(request, $e){
	if(request){
		$e.addClass("clickable");
		$e.removeClass("not-clickable");
	}
	else{
		$e.removeClass("clickable");
		$e.addClass("not-clickable");
	}
}

function getSubCartId(childElement){
	var subCart = $(childElement).parents(".sub-cart")[0];
	return $(subCart).attr("id");

}


function resetAddedQuestionContainer(){
	setActionAsClickable(false, $editQuestion);
	setActionAsClickable(false, $deleteQuestion);
	setActionAsClickable(true, $addQuestion);
	
	showSaveAndCancelEditQuestionChanges(false);
	// unselectQuestion();
	
	
}

function hideSectionContainers(sectionContainerId){
	$.each($("#sectionContainers").find(".section-container"), function(){
		if($(this).attr("id") != sectionContainerId){
			//slideUp($(this), 500);
			$(this).hide();
		}
		
	})
}

function isButtonClickable($e){
	if($e.hasClass("clickable") == 1){
		return true;
	}else{
		return false;
	}
		
}

function disableInputFields(request, containerId){
	
	var $eContainer = $("#" + containerId);
	var inputs;
	var requestedDisabledValue;
	var datepickerContainer;
	
	//Per the request, set the value for the "disabled" property
	if(request == true){
		requestedDisabledValue = "disabled";
	}else{
		requestedDisabledValue = "";
	}
		
	//Build an array of all the "input" elements
	inputs = $eContainer.find("input");
	$.merge(inputs, $eContainer.find("textarea"));
	$.merge(inputs, $eContainer.find("select"));
	
	//Set the disabled property for the input elements
	$.each(inputs, function(){
		$(this).prop("disabled", requestedDisabledValue);
	})
	
	
	//Set the date picker's "enabledness"
	datepickerContainer = $("#" + containerId).find("#calendar");
	if(request == true){
		$(datepickerContainer).datepicker("disable");
	}else{
		$(datepickerContainer).datepicker("enable");
	}
}

function clearInvalidCss(){
	$("#postingContainer").find(".invalid").each(function(){
		$(this).removeClass("invalid");
	})
	
	$("#postingContainer").find(".invalid-message").each(function(){
		$(this).hide();
	})
}

function doesQuestionHaveAnAnswerList(postQuestionDto){
	if(postQuestionDto.formatId == 2 || postQuestionDto.formatId == 3){
		return true;
	}else{
		return false;
	}
}


function showSectionContainer(sectionContainerId){
	var sectionContainer = $("#sectionContainers").find("#" + sectionContainerId)[0];
	
	//slideDown($(sectionContainer), 500);
	$(sectionContainer).show();
	
}

function showContractorContent(request){
	
	$.each($("#sectionContainers").find(".contractor-content"), function(){
		if(request)$(this).show();	
		else $(this).hide();
	})
	
	
}