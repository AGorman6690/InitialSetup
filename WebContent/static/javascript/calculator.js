var $input;
var number1 = null;
var number2 = null;
var operator = null;
var numberString = "";
$(document).ready(function(){
	$(".number").click(function(){
		var click = $(this).html();
		numberString += click;
		$input.val(numberString);		
	})
	$("#clear").click(function(){
		clear();
	})
	$(".operator").click(function() {
		number1 = numberString;
		operator = $(this).html();
		numberString = "";
	})
	$("#decimal").click(function() {
		numberString += ".";
	})
	$("#equals").click(function(){
		if(operator != null){
			number2 = numberString;
			var equation = evaluate();	
			save(equation);
			clear();
		}		
	})
	$input = $("#value");
	
	setInterval(getHistory, 2000) /* time in milliseconds (ie 2 seconds)*/
})
function evaluate(){	
	if($.isNumeric(number1) && $.isNumeric(number2) && operator != null){
		var equation = number1 + " " + operator + " " + number2;
		var result = parseFloat(number1);
		number2 = parseFloat(number2);
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
	}
}
function clear(){
	$input.val("");
	number1 = null;
	number2 = null;
	operator = null;
	numberString = "";	
	numberString = "";
}
function save(equation){
	$.ajax({
		type: "POST",
		url: "/JobSearch/equation/save",
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
		url: "/JobSearch/equation/history",
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
