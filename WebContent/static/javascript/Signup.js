$(document).ready(function(){
	
	$("#createAccount").click(function(){
		
		

	
//		if(areInputsValid_signUp()){
			
		

			
			var user = {};
			user.firstName = $("#signup-firstName").val();
			user.lastName = $("#signup-lastName").val();
			user.emailAddress = $("#signup-email").val();
			user.matchingEmailAddress = $("#signup-confirm-email").val();
			user.password = $("#signup-password").val();
			user.matchingPassword = $("#signup-confirm-password").val();
			user.profileId = $("#signup-profiles").find("option:selected").eq(0).val();			
			
			executeAjaxCall_signUp(user);

//		}
	})
	
	$("#signup-password").on("focusout", function(){
		
		if($(this).val() != "") isValidPassword($(this).val());	
	})
	
	$("#signup-confirm-password").on("focusout", function(){
		if($(this).val() != "") isValidConfirmPassword($("#signup-password").val(), $("#signup-confirm-password").val());		
	})	
	
	$("#signup-confirm-email").on("focusout", function(){
		if($(this).val() != "") isValidConfirmEmail($("#signup-email").val(),
														$("#signup-confirm-email").val());		
	})
	
//	$("#signup-email").on("focusout", function(){		
//		if($(this).val() != "") isValidEmail($(this));		
//	})
	
})

function areInputsValid_signUp(){
	var invalidCount = 0;
	
	invalidCount += isValidName($("#signup-firstName").val(), $("#signup-firstName"));
	invalidCount += isValidName($("#signup-lastName").val(), $("#signup-lastName"));
	invalidCount += isValidPassword($("#signup-password").val());
	invalidCount += isValidEmail($("#signup-email").val());
	invalidCount += isValidConfirmEmail($("#signup-email").val(), $("#signup-confirm-email").val());
	invalidCount += isValidConfirmPassword($("#signup-password").val(), $("#signup-confirm-password").val()); 
	invalidCount += isValidProfileType();
		
	if(invalidCount == 0) return true;
	else return false;
}

function isValidProfileType(){
	
	var sdf = $("#signup-profiles").find("option:selected").eq(0).attr("value");
	if($("#signup-profiles").find("option:selected").eq(0).attr("value") == "-1"){
		
		setInvalidCss($("#signup-profiles"));
		return 1;
		
	}
	else{
		setValidCss($("#signup-profiles"));
		return 0;
	}
}

function isValidConfirmEmail(email_1, email_2){
	
	var $error = $("#invalidConfirmEmail");
	var $input = $("#signup-confirm-email");
	
	if(email_2 == ""){
		setInvalidCss($input);
		return 1;		
	}
	if(email_1 != email_2){
		$error.show();
		setInvalidCss($input);
		return 1;
	}
	else {
		$error.hide();
		setValidCss($input);
		return 0;
	}
}

function isValidConfirmPassword(password_1, password_2){
	
	var $error = $("#invalidConfirmPassword");
	var $input = $("#signup-confirm-password");
	
	if(password_2 == ""){
		setInvalidCss($input);
		return 1;		
	}
	if(password_1 != password_2){
		$error.show();
		setInvalidCss($input);
		return 1;
	}
	else {
		$error.hide();
		setValidCss($input);
		return 0;
	}
}

function isValidName(name, $input){
	
//	var name = $input.val();
	if(name != undefined && name != ""){
		setValidCss($input);
		return 0;
	}
	else{
		setInvalidCss($input);
		return 1;
	}
}

function isValidPassword(password){
	
	var $e = $("#invalidPassword");
	var $input = $("#signup-password");
	var length = $input.val().length;
	
	if(password == ""){
		setInvalidCss($input);
		return 1;		
	}
	else if(length < 6 || length > 20){
		$e.show();
		setInvalidCss($input);
		return 1;
	}
	else {
		$e.hide();
		setValidCss($input);
		return 0;
	}
}

function isValidEmail(email){
	
	var re = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
	
	if(email == ""){
		 setInvalidCss($("#signup-email"));
		 $("#invalidEmail_format").hide();
		 return 1;
	}
	else {
		$("#invalidEmail_format").hide();
		setValidCss($("#signup-email"));
		return 0;
	}
//	else{
//		$("#invalidEmail").show();
//		setInvalidCss($("#signup-email"));
//		return 1;
//	}
}

//function isUserValid(user){
//	
//	var invalidCount = 0;
//	
////	if(user.firstName == ""{
////		invalidCount += 1;	
////	}) 
//	
//	if(user.lastName == "") invalidCount += 1;
//	
//	if(user.emailAddress == "") invalidCount += 1;
//	
//	if(user.password == "") invalidCount += 1;
//	
//	if(user.matchingPassword == "") invalidCount += 1;
//	
//	if(user.profileId == "") invalidCount += 1;
//	
//	if(invalidCount == 0) return true;
//	else return false;
//}

function executeAjaxCall_signUp(user){
	
	broswerIsWaiting(true);
	$.ajax({
		type : "POST",
		url: '/JobSearch/user/sign-up',
		headers : getAjaxHeaders(),
		contentType : "application/json",
		data : JSON.stringify(user),
		dataType : "json",		
		success : _success,
		error : _error,
		cache: true
	});

	function _success(newUserDto) {
		
		broswerIsWaiting(false);	
		
		if(newUserDto.isInvalidNewUser){
			showInvalidNewUser(newUserDto);
		}
		else{
			showEmailVerification();
		}

	}	

	function _error() {
		broswerIsWaiting(false);
		alert('DEBUG: error executeAjaxCall_saveFindJobFilter')		
	}
	
}

function showEmailVerification(){
	$("#verifyEmail").show();
	$("#signUpGroup").hide();
	$("#loginGroup").hide();
}


function showInvalidNewUser(newUserDto){
	
//	if(newUserDto.isInvalidFirstName){
//		setInvalidCss($("#signup-firstName"));
//	}
//	
//	
//	if(newUserDto.isInvalidLastName){
//		setInvalidCss($("#signup-lastName"));
//	}
//	
//	
//	if(newUserDto.isInvalidEmail_format){
//		$("#invalidEmail_format").show();
//		setInvalidCss($("#signup-email"));
//	}
//	else if(newUserDto.isInvalidEmail_duplicate){
//		$("#invalidEmail_duplicate").show();
//		setInvalidCss($("#signup-email"));
//	}
//	else{
//		$("#invalidEmail_format").hide();
//		$("#invalidEmail_duplicate").hide();
//		setValidCss($("#signup-email"));
//	}
//	
//	
//	if(newUserDto.isInvalidPassword){
//		$("#invalidPassword").show();
//	}
	
//	$("#signup-firstName").val(newUserDto.user.firstName);
//	$("#signup-lastName").val(newUserDto.user.lastName);
//	$("#signup-email").val(newUserDto.user.emailAddress);
//	$("#signup-password").val(newUserDto.user.password);
//	$("#signup-confirm-password").val(newUserDto.user.matchingPassword);
//	$("#signup-profiles").find("option[value=" + newUserDto.user.profileId + "]").prop("selected", true);
//	
//	areInputsValid_signUp();
	
	if(newUserDto.isInvalidEmail_format){
		$("#invalidEmail_format").show();
		setInvalidCss($("#signup-email"));
	}
	else if(newUserDto.isInvalidEmail_duplicate){
		$("#invalidEmail_duplicate").show();
		setInvalidCss($("#signup-email"));
	}
	else{
		$("#invalidEmail_format").hide();
		$("#invalidEmail_duplicate").hide();
		setValidCss($("#signup-email"));
	}	
	
//	isValidName(newUserDto.user.firstName, $("#signup-firstName"));
//	isValidName(newUserDto.user.lastName, $("#signup-lastName"));
//	isValidPassword($("#signup-password").val());
//	isValidEmail($("#signup-email").val());
//	isValidConfirmPassword($("#signup-password").val(), $("#signup-confirm-password").val()); 
//	isValidProfileType();
	
}



