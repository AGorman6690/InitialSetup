var $input;
var number1;
var number2;
var operator;
var numberString;
var hasDecimal;
$(document).ready(function(){
	$(".number").click(function(){
		var click = $(this).html();
		numberString += click;
		showNumber(numberString);			
	})
	$("#clear").click(function(){
		clear();
	})
	$(".operator").click(function() {
		var $e = $(this);
		if(isBuildingNumber2()){
			return;
		}
		
		if(number1 === null && $.isNumeric(numberString)){
			number1 = parseFloat(numberString);
		}
		
		if(number1 !== null){
			operator = $e.html();
			clearSelectedOperator();
			$e.addClass("selected");
			numberString = "";
		}
	})
	$("#decimal").click(function() {
		if(!hasDecimal){
			numberString += ".";
			showNumber(numberString);
			hasDecimal = true;
		}		
	})
	$("#equals").click(function(){
		if(operator !== null && $.isNumeric(number1)){
			number2 = parseFloat(numberString);
			if($.isNumeric(number2)){
				var equation = buildEquation();	
				if(equation !== null){
					save(equation);
					clear();	
				}							
			}
		}	
	})
	$input = $("#value");
	clear();
	setInterval(getHistory, 2000)
})
function isBuildingNumber2(){
	if(operator !== null && number1 !== null && numberString !== ""){
		return true;
	}else{
		return false;
	}
}
function showNumber(numberString){
	$input.val(numberString);	
}
function buildEquation(){	
	if($.isNumeric(number1) && $.isNumeric(number2) && operator !== null){
		var equation = number1 + " " + operator + " " + number2;
		var result = number1;
		number2 = number2;
		switch (operator) {
		case "+":
			result += number2;
			break;
		case "-":
			result -= number2;
			break;
		case "x":
			result *= number2;
			break;
		case "/":
			result /= number2;
			break;
		}	
		equation += " = " + result;
		$input.val(equation);
		return equation;
	}else{
		return null;
	}	
}
function clear(){
	$input.val("");
	number1 = null;
	number2 = null;
	operator = null;
	numberString = "";	
	hasDecimal = false;
	clearSelectedOperator();
}
function clearSelectedOperator(){
	$(".operator.selected").removeClass("selected");
}
function save(equation){
	$.ajax({
		type: "POST",
		url: "/equation/save",
		headers : getAjaxHeaders(),
		contentType: "application/json",
		data: JSON.stringify(equation),
		dataType: "html"
	}).done(function(html){
		$("#history").html(html);
	})
}
function getHistory(){
	$.ajax({
		type: "GET",
		url: "/equation/history",
		dataType: "html"
	}).done(function(html){
		$("#history").html(html);
	})	
}
function getAjaxHeaders(){	
	var headers = {};
	headers[$("meta[name='_csrf_header']").attr("content")] = $(
			"meta[name='_csrf']").attr("content");	
	return headers;
}