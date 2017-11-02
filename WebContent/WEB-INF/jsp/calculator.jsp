<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
	
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">


<html>
	<head>
		<meta name="_csrf" content="${_csrf.token}" />
		<meta name="_csrf_header" content="${_csrf.headerName}" />
		<script
		src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.4/jquery.min.js"
		type="text/javascript"></script>
	</head>
	<body>
		<div id="container">
			<div id="calculator">
				<div class="row">
					<input id="value" type="text" disabled>
					<button id="clear">C</button>
				</div>
				<div class="row">
					<button class="number">7</button>
					<button class="number">8</button>
					<button class="number">9</button>
					<button id="multiply" class="operator">x</button>
				</div>
				<div class="row">
					<button class="number">4</button>
					<button class="number">5</button>
					<button class="number">6</button>
					<button id="divide" class="operator">/</button>
				</div>	
				<div class="row">
					<button class="number">1</button>
					<button class="number">2</button>
					<button class="number">3</button>
					<button id="add" class="operator">+</button>
				</div>		
				<div class="row">
					<button id="decimal">.</button>
					<button class="number">0</button>
					<button id="equals">=</button>
					<button id="subtract" class="operator">-</button>
				</div>											
			</div>
			<div id="history">
				<%@ include file="./calculator_history.jsp" %>
			</div>
		</div>
	
	</body>
	<link rel="stylesheet" type="text/css" href="static/css/calculator.css" />
	<script src="/static/javascript/calculator.js" type="text/javascript"></script>
</html>
