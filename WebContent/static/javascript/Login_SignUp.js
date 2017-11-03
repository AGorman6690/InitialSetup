var slideSpeed_loginSignUp = 600;
$(document).ready(function(){
	
	$("body").on("click", "#do-login", function() {
//		login();
	})
	
	$("body").on("click", "#do-sign-up", function() {
		if (validateInputElements($("#sign-up-wrapper"))){
			signUp();	
		}
	})
	
	$(".show-login-sign-up-mod").click(function(){
		ajaxGet_loginSignUpPage($(this).attr("data-context"));
	})
	
	$("body").on("click", ".context-item", function() {
		setContextDisplay($(this).attr("id"));		
	})
	
	setContextDisplay($("#page-wrapper").attr("data-context"), function(){
		$("").show();
	});
	
	$("body").on("click", "#debug1", function() {		
		$("#nav_logOut").trigger("click");
		$("#login-password").val('jg');
		$("#login-email-address").val('gorma080@d.umn.edu');
		$("#do-login").click();
	})
	$("body").on("click", "#debug2", function() {		
		$("#nav_logOut").trigger("click");
		$("#login-password").val('2');
		$("#login-email-address").val('2a');
		$("#do-login").click();
	})	
	$("body").on("click", "#debug3", function() {		
		$("#nav_logOut").trigger("click");
		$("#login-password").val('jg');
		$("#login-email-address").val('justin.gorman@wilsontool.com');
		$("#do-login")[0].click();
	})	
	
	$("#create-account").click(function(){

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
function signUp(){
	var createUserRequest = {};
	createUserRequest.firstName = $("#signup-first-name").val();
	createUserRequest.lastName = $("#signup-last-name").val();
	createUserRequest.emailAddress = $("#signup-email-address").val();
	createUserRequest.confirmEmailAddress = $("#signup-confirm-email-address").val();
	createUserRequest.password = $("#signup-password").val();
	createUserRequest.confirmPassword = $("#signup-confirm-password").val();
	createUserRequest.profileId = $("#signup-profiles").find("option:selected").eq(0).val();
	$.ajax({
		type : "POST",
		url: "/JobSearch/user/",
		data: JSON.stringify(createUserRequest),
		contentType: "application/json",
		headers : getAjaxHeaders(),
		dataType : "json"
	}).done(function(createUserResponse) {
		broswerIsWaiting(false);
		if (createUserResponse.userId !== null){
			$("#sign-up-input").slideUp(slideSpeed_loginSignUp);
			$("#successful-sign-up").slideDown(slideSpeed_loginSignUp);
		}else{
			var $emailInUseError = $("#sign-up-fail-email-in-use");
			var $invalidEmailAddress = $("#sign-up-fail-invalid-email");
			if (createUserResponse.emailInUse){
				$emailInUseError.show();
			}else{
				$emailInUseError.hide();
			}
			
			if (createUserResponse.invalidEmail){
				$invalidEmailAddress.show();
			}else{
				$invalidEmailAddress.hide();
			}			                        
			showErrorMessageCont(true, $("#do-sign-up-container"));
		}
	
	})
}
function login(){
	
	var loginRequest = {};
	loginRequest.emailAddress = $("#login-email-address").val();
	loginRequest.password = $("#login-password").val();
	broswerIsWaiting(true);
	$.ajax({
		type : "POST",
		url: "/JobSearch/user/login",
		data: JSON.stringify(loginRequest),
		contentType: "application/json",
		headers : getAjaxHeaders(),
		dataType : "json"
	}).done(function(loginResponse) {
		broswerIsWaiting(false);
		if(loginResponse.success){
			redirectToProfile();
		}

	})	
}
function ajaxGet_loginSignUpPage(context){
	
	broswerIsWaiting(true);
	$.ajax({
		type : "GET",
		url: "/JobSearch/login-sign-up/" + context,
		headers : getAjaxHeaders(),
		dataType : "html"
	}).done(function(html) {
		broswerIsWaiting(false);
		$("#login-sign-up-wrapper").html(html).promise().done(function() {
			setContextDisplay(context);
			$("#login-sign-up-mod").show();			
		});

	})
}
function setContextDisplay(context){
	
	if(context === "login"){
		$("#login-wrapper").slideDown(slideSpeed_loginSignUp);
		$("#sign-up-wrapper").slideUp(slideSpeed_loginSignUp);
	}else{
		$("#login-wrapper").slideUp(slideSpeed_loginSignUp);
		$("#sign-up-wrapper").slideDown(slideSpeed_loginSignUp);
	}
	
	highlightArrayItem($("#login-sign-up-context .context-item[id=" + context + "]"), $("#login-sign-up-context .context-item"), "clicked");
}

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

