/**
 * 
 */

$(document).ready(function(){
	// '#' gets an html element by its id
	// '.' gets an html element by one of its classes
	// each element can only have one id and it has to be unique
	// each element can have multiple class and do not have to be unique
	$("#userGetRequest").click(function(){
		
		// $ is shorthand for jQuery
		// $.something is the same as jQuery.something
		
		var userId = $("#userId").val();
		
		$.ajax({
	        url: 'http://localhost:8080/JobSearch/getUser?userId=' + userId,
	        contentType: "application/json", // Request
	        dataType: "json", // Response
	        success: _success,
	        error: _error
	    });
		
		//Executes if the ajax call is successful
		function _success(response){
			var html = "<div>"+ response.firstName+" "+ response.lastName + " was returned from ajax call</div>";
			$("#userFromAjaxRequest").append(html);
		}
		
		//Executs if the ajax call errors out
		function _error(response){
			
		}
	})
	
	
	
	$("#userSetRequest").click(function(){
		
		var user = {};				
		user.firstName = document.getElementById('firstName').value;
		user.lastName = document.getElementById('lastName').value;
		user.emailAddress = document.getElementById('emailAddress').value;
		
		$.ajax({
			type: "POST",
	        url: 'http://localhost:8080/JobSearch/setUser', 
	        contentType: "application/json", // Request
	        dataType: "json", // Response
	        cache: false, // Force requested pages not to be cached by the browser
	        processData: false, // Avoid making query string instead of JSON
	        data: JSON.stringify(user),// "{'userID': '9', 'userName': 'j'}",
	        success: _success,
	        error: _error
	    });
		
		//Executes if the ajax call is successful
		function _success(response){
			var html = "<div> success was returned from ajax call</div>";
			$("#userFromAjaxRequest").append(html);
		}
		
		//Executs if the ajax call errors out
		function _error(response){
			var html = "<div> failure was returned from ajax call</div>";
			$("#userFromAjaxRequest").append(html);
			
		}
	})
	
	
	$("#setCategories").click(function(){
		var user = {};				
		user.firstName = document.getElementById('firstName').value;
		user.lastName = document.getElementById('lastName').value;
		
		$.ajax({
			type: "GET",
			url: 'http://localhost:8080/JobSearch/setCategories',
			contentType: "application/json",
			data: JSON.stingify(user)
			
			
		})
	})
	
});
