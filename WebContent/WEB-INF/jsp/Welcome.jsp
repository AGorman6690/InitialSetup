<%@ include file="./includes/Header.jsp"%>

<head>
<link rel="stylesheet" type="text/css" href="./static/css/welcome.css" />

</head>

<div class="container">

	<div class="main">
		<div class="main-item">
			<a href="/JobSearch/jobs/find">Looking For Work</a>
		</div>
		<div class="main-item">
			<h1>Labor Vault</h1>
		</div>
		<div class="main-item">
			<a href="/JobSearch/employees/find">Looking To Hire</a>
		</div>
	</div>
	

<!-- 	<a href="./dummyData">Set Dummy Data</a> -->
	
	
	
	<br>
	<br>
	<br>
	<br>
	<br>
	<br>
	<br>
	<br>
	<br>
	<br>
	<br>
	<br>
	<br>
	<br>
	<br>
	<br>
	<br>	
	<br>
	<br>
	<br>
	<br>
	<br>
	<br>

						<button id="debug1">Sign in as employer (UserId = 1)</button>
						<button id="debug2">Sign in as employee 1</button>
						<button id="debug3">Sign in as employee 2</button>
	

</div>

<script>
	$(document).ready(function() {
		$("#debug1").click(function() {
			
			
// 			$("#nav_logOut").trigger("click");
// 			$("#password").val('jg');
// 			$("#userName").val('gorma080@d.umn.edu');
// 			$("#loginContainer input[type=submit]")[0].click();
			
			
			$("#nav_logOut").trigger("click");
			$("#password").val('jg');
			$("#userName").val('gorma080@d.umn.edu');
			$("#loginContainer input[type=submit]").click();
			
			

		})

		$("#debug2").click(function() {
			
			$("#nav_logOut").trigger("click");
			$("#password").val('2');
			$("#userName").val('2');
			$("#loginContainer input[type=submit]").click();

		})
		
		$("#debug3").click(function() {
			
			$("#nav_logOut").trigger("click");
			$("#password").val('jg');
			$("#userName").val('justin.gorman@wilsontool.com');
			$("#loginContainer input[type=submit]")[0].click();

		})		
		
		$("#login").click(function(){
			$("div.login").show();
			$("div.sign-up").hide();
		})
		
		$("#signUp").click(function(){
			$("div.login").hide();
			$("div.sign-up").show();
		})

	})

	function createAccount() {
		$("#createAccountContainer").toggle();

	}
</script>

<%@ include file="./includes/Footer.jsp"%>