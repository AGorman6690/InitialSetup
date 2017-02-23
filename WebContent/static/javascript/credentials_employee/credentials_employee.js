$(document).ready(function(){
	$("#saveHomeLocation").click(function(){
		var user_edited = {};
		user_edited.homeCity = $("#city").val();;
		user_edited.homeState = $("#state option:selected").val();;
		user_edited.homeZipCode = $("#zipCode").val();
		
		executeAjaxCall_updateUserSettings(user_edited);
	})
	
	$("#saveMaxDistance").click(function(){
		var user_edited = {};
		user_edited.maxWorkRadius = $("#miles").val();;
		
		executeAjaxCall_updateUserSettings(user_edited);
	})
	
	$("#saveMinimumPay").click(function(){
		var user_edited = {};
		user_edited.minimumDesiredPay = $("#dollarsPerHour").val();;
		
		executeAjaxCall_updateUserSettings(user_edited);
	})	
	
	$(".cancel-changes").click(function(){
		$(this).closest(".edit-container").find("input, select").each(function(){
			$(this).val("");
		})
		
		$(this).closest(".edit-container").siblings("[data-toggle-id]").eq(0).click();
	})
})

function executeAjaxCall_updateUserSettings(user_edited){
	
	broswerIsWaiting(true);
	$.ajax({
		type : "POST",
		url: '/JobSearch/user/settings/edit',
		headers : getAjaxHeaders(),
		contentType : "application/json",
		data : JSON.stringify(user_edited),
//			dataType : "json",		
		success : _success,
		error : _error,
		cache: true
	});

	function _success() {
		broswerIsWaiting(false);	
		location.reload();
	}	

	function _error() {
		broswerIsWaiting(false);	
	}
}