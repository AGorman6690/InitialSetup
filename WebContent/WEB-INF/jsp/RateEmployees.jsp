<%@ include file="./includes/Header.jsp"%>

<head>
	<script src="<c:url value="/static/javascript/Utilities.js" />"></script>
<%-- 	<script src="<c:url value="/static/javascript/User.js" />"></script> --%>
<!-- 	<link rel="stylesheet" type="text/css" href="http://localhost:8080/JobSearch/static/css/ratings.css" /> -->
	<link rel="stylesheet" type="text/css" href="/JobSearch/static/css/rateEmployees.css" />
</head>

<body>

	<input type="hidden" id="jobId" value="${job.id}"/>

	<div class="container">
		<div id="buttonsContainer" class="row">
			<div class="col col-sm-12">
				<button id="submitRatings" class="square-button">Submit Ratings (not built yet)</button>
			</div>
		</div>
		<div id="main" class="row">
			<div class="col col-sm-4">
				<div id="employees" class="column">	
					<div class="header bottom-border-thin">Employees</div>
					<div class="content">
						<ul class="select-one">
						<c:forEach items="${job.employees }" var="employee">
							<li class="not-clicked" value="${employee.userId }" data-on-time="-1" data-work-ethic="-1"
								 data-hire-again="-1" data-endorsements="" data-comment="">${employee.firstName }</li>
						</c:forEach>
						<li class="not-clicked" value="-1" data-on-time="-1" data-work-ethic="-1"
								 data-hire-again="-1" data-endorsements="">Test</li>
						</ul>
					</div>
				</div>			
			</div>
			<div class="col col-sm-4">
				<div id="ratings" class="column">	
					<div class="header bottom-border-thin">Ratings</div>
					<div class="content">
						<div class="criterion-container"><div class="criterion-name">On Time</div>
						
							<ul id="onTime" class="rating-list select-one criterion-list">
								<li class="not-clicked" data-value="0">Never</li>
								<li class="not-clicked" data-value="2.5">Occasionally</li>
								<li class="not-clicked" data-value="5">Always</li>
							</ul>
						</div>
						<div class="criterion-container"><div class="criterion-name">Work Ethic</div>
							<ul id="workEthic" class="rating-list select-one criterion-list">
								<li class="not-clicked" data-value="0">Poor</li>
								<li class="not-clicked" data-value="2.5">Average</li>
								<li class="not-clicked" data-value="5">Exceptional</li>
							</ul>
						</div>
						<div class="criterion-container"><div class="criterion-name">Hire Again?</div>
							<ul id="hireAgain" class="rating-list select-one criterion-list">
								<li class="not-clicked" data-value="0">No</li>
								<li class="not-clicked" data-value="2.5">Maybe</li>
								<li class="not-clicked" data-value="5">Yes</li>
							</ul>
						</div>
					</div>
				</div>			
			</div>
			<div class="col col-sm-4">
				<div id="endorsements" class="column">	
					<div class="header bottom-border-thin">Endorsements</div>
					<div class="content"><div class="criterion-name">Categories</div>
						<ul id="categories" class="select-multiple rating-list">
						<c:forEach items="${job.categories }" var="category">
							<li class="not-clicked" class="category" value="${category.id }">${category.name }</li>
						</c:forEach>
						</ul>
					</div>
				</div>			
			</div>			
		</div>
		
		<!-- Comment -->
		<div id="main" class="row">
			<div class="col col-sm-4"></div>
			<div  class="col col-sm-8">
				<div class="header bottom-border-thin">Comment</div>
				<div class="content">
					<textarea id="comment" rows="3"></textarea>
					<button id="saveComment">Save</button>
				</div>
			</div>
		</div>
		
	</div> <!-- end container -->
</body>

<script>

	$(document).ready(function(){
	
		$("#submitRatings").click(function(){
			
			submitRatings();						
			
		})
		
		$("#ratings li").click(function(){
		
			var selectedEmployeeListItem;
			var parentUl;
			var ratingValue 
			
			//If an employee is currently selected
			if(isEmployeeSelected()){
				
				toggleListItemsColors($(this));
				
				//**AFTER** the list item's color has been toggled, the rating value
				ratingValue = getRatingValue($(this));
				
				//Get the employee list item
				selectedEmployeeListItem = getSelectedEmployeeListItem();
				
				//Get the clicked list item's parent ul
				parentUl = $(this).parents("ul")[0];
			
				//Determine which rating criteria is being set
				//and update the appropriate employee list item's attribute
				if($(parentUl).attr("id") == "onTime"){
					$(selectedEmployeeListItem).attr("data-on-time", ratingValue);
				}else if($(parentUl).attr("id") == "workEthic"){
					$(selectedEmployeeListItem).attr("data-work-ethic", ratingValue);
				}else if($(parentUl).attr("id") == "hireAgain"){
					$(selectedEmployeeListItem).attr("data-hire-again", ratingValue);
				}

			}

		})
		

		
		$("#endorsements li").click(function(){
			
			
			if(isEmployeeSelected()){
				//Multiple endorsements can be given.
				//Therefore, do not clear other selected list items.
				//Just toggle the clicked list item.
				toggleClasses($(this), "clicked", "not-clicked");
				
				updateEmployeesEndorsementAttribute($(this).val());
			}
			
		})
		
		
			
		
// 		$("#ratings li").click(function(){
// 			//The "rating-list" class excludes the employee list
			
// 			var parentUl;
// 			var currentClickedLi;
			
// 			if(isEmployeeSelected()){				
				
// 			}
			
// 		})
		
		$("#employees li").click(function(){
			//The "rating-list" class excludes the employee list
			
			toggleListItemsColors($(this));
			
			if(isEmployeeSelected()){
				
				//Hightlight the employee's ratings and endorsements
				showEmployeesRatings($(this));
				showEmployeesEndorsements($(this));
				ShowEmployeeComment($(this));
			}else{
				
				//Else clear all list items colors
				clearListItemsColors($("#onTime"));
				clearListItemsColors($("#workEthic"));
				clearListItemsColors($("#hireAgain"));
				clearListItemsColors($("#categories"));
				$("#comment").val("");
			}
			
		})		
		
		$("#saveComment").click(function(){
			if(isEmployeeSelected()){
				saveComment();	
			}		
		})
			

		
		
		
	})
	
	function saveComment(){
		var selectedEmployeeListItem = getSelectedEmployeeListItem();
		$(selectedEmployeeListItem).attr("data-comment", $("#comment").val());
	}
	
	function ShowEmployeeComment($employeeListItem){
		var employeesComment = ($employeeListItem).attr("data-comment"); 
		$("#comment").val(employeesComment);
	}
	
	
	function showEmployeesEndorsements($employeeListItem){

		var categoryIds = getEmployeeEndorsementCategoryIds($employeeListItem);
		var i;
		var categoryId;
		var categoryListItem;
		
		//Clear all category list item's colors
		clearListItemsColors($("#categories"));
		
		//Select all the categories that the employee has an endorsement for
		for(i = 0; i < categoryIds.length; i++){
			categoryId = categoryIds[i];
			
			//Get the category list item
			categoryListItem = $("#categories").find("li[value = '" + categoryId + "']");
			
			$(categoryListItem).addClass("clicked");
			
		}
		

	}
	
	function getEmployeeEndorsementCategoryIds($employeeListItem){
		var endorsements = $employeeListItem.attr("data-endorsements");
		var categoryIds = endorsements.split("*");
		
		//Remove the first element. It is a blank
		categoryIds.splice(0, 1);
		
		return categoryIds;
	}
	
	function showEmployeesRatings($employeeListItem){
		
		//Get the employees rating values
		var onTimeValue = $employeeListItem.attr("data-on-time");
		var workEthicValue = $employeeListItem.attr("data-work-ethic");
		var hireAgainValue = $employeeListItem.attr("data-hire-again");

		//Select the list item matching the employee's rating
		selectListItem($("#onTime"), onTimeValue);
		selectListItem($("#workEthic"), workEthicValue);
		selectListItem($("#hireAgain"), hireAgainValue);
					
	}
	
	function selectListItem($ul, ratingValue){
		
		var $listItem;			
		clearListItemsColors($ul);
		
		//If the rating value has yet been set for this employee
		if(ratingValue != -1){	
			
			//Get list item matching the user's rating value
			$listItem = getListItemByValue($ul, ratingValue);
			
			$listItem.addClass("clicked");
		}			
	}
	
	function clearListItemsColors($ul){
		var selectedLis = $ul.find("li.clicked")
		var selectedLi;
		var i;
		
		for(i=0; i<selectedLis.length; i++){
			selectedLi = selectedLis[i];
			$(selectedLi).removeClass("clicked");
			$(selectedLi).removeClass("not-clicked");	
		}
		
		
	}
	
	function getListItemByValue($ul, dataValue){			
		return $($ul.find("li[data-value='" + dataValue + "']")[0]);			
	}
	
	function toggleListItemsColors($listItem){
		
		//If this list item is currently clicked
		if($listItem.hasClass("clicked") == 1){
			toggleClasses($listItem, "clicked", "not-clicked");
			
		//Else another list item may be selected
		}else{
			//Get parent ul
			parentUl = $listItem.parents("ul")[0];
			
			//Remove the clicked class from a potentially selected list item.
			currentClickedLi = $($(parentUl).find("li.clicked")[0]);
			toggleClasses($(currentClickedLi), "clicked", "not-clicked")
			
 			//Add class to clicked list item
			toggleClasses($listItem, "clicked", "not-clicked");
		}
	}

		
	function isEmployeeSelected(){
		
		var clickedEmployeeListItems = $("#employees").find("li.clicked");
		
		if(clickedEmployeeListItems.length > 0){	
			return true;
		}else{
			return false;
		}
	}
	
	function getSelectedEmployeeListItem(){
		var clickedEmployeeListItems = $("#employees").find("li.clicked");
		return clickedEmployeeListItems[0];		
	}
	
		
	
	function toggleEndorsement(event){
// 		alert($(event).attr('id'))
		
		if($(event).hasClass("btn-secondary")){
			$(event).removeClass("btn-secondary")
			$(event).addClass("btn-success")
		}else{
			$(event).addClass("btn-secondary")
			$(event).removeClass("btn-success")
		}
		
	}
	
	
	function submitRatings(){
		//****************************************
		//****************************************
		//Need validation here
		//****************************************		
		//****************************************
		
		var ratingRequestDTOs_wrapper = {};
		ratingRequestDTOs_wrapper.ratingRequestDTOs = getRatingRequestDTOs();	
		
// 		salert(ratingRequestDTOs_wrapper )
		var headers = {};
		
		headers[$("meta[name='_csrf_header']").attr("content")] = $(
				"meta[name='_csrf']").attr("content");

		$.ajax({
			type: "POST",
			url: '/JobSearch/user/rate',
			contentType : "application/json",
			headers : headers,
			data: JSON.stringify(ratingRequestDTOs_wrapper),
	        success: _success,
	        error: _error
	    });

		function _success(){
			window.location = "/JobSearch/user/profile";
		}

		function _error(response, errorThrown){
			alert("error rateEmpoyee");
		}
				
	}
	
	
	function getRatingRequestDTOs(){
		
		var ratingRequestDTOs = [];
		
		//Create a ratingDTO for each rating, for each employee
		$("#employees").find("li").each(function(){

			ratingRequestDTOs.push(getRatingRequestDTO($(this)));
// 			ratingRequestDTOs.push(getRatingRequestDTO($(this), "data-on-time", 2));
// 			ratingRequestDTOs.push(getRatingRequestDTO($(this), "data-hire-again", 3));

		})
		
		return ratingRequestDTOs;
		
	}
	
	function getRatingRequestDTO($employeeListItem){
		
		var ratingRequestDTO = {}
		
		//Set the rate criteria which is a list of rate criterion objects
		ratingRequestDTO.rateCriteria = [];
		ratingRequestDTO.rateCriteria.push(getRateCriterion($employeeListItem, "data-work-ethic", 1));
		ratingRequestDTO.rateCriteria.push(getRateCriterion($employeeListItem, "data-on-time", 2));
		ratingRequestDTO.rateCriteria.push(getRateCriterion($employeeListItem, "data-hire-again", 3));		
		
		//Set the endorsements
		ratingRequestDTO.endorsements = {};
		ratingRequestDTO.endorsements = getEndorsementDTOs($employeeListItem);
		
		//Set the comment
		ratingRequestDTO.comment = {};
		ratingRequestDTO.comment = getCommentDTO($employeeListItem);
		
		return ratingRequestDTO;
	}
	
	
	function getCommentDTO($employeeListItem){
		
		var commentDTO = {};
		
		commentDTO.userId = $employeeListItem.val();
		commentDTO.jobId = $("#jobId").val();
		commentDTO.comment = $("#comment").val();
		
		return commentDTO;
	}
	
	
	function getEndorsementDTOs($employeeListItem){
		
		var endorsementDTOs = [];
		var endorsementDTO;
		
		//Get the category ids that the employee was endorsed for
		var categoryIds = getEmployeeEndorsementCategoryIds($employeeListItem);
		
		$.each(categoryIds, function(i, val){
			endorsementDTO = {};
			
			endorsementDTO.userId = $employeeListItem.val();
			endorsementDTO.jobId = $("#jobId").val();
			endorsementDTO.categoryId = val;	
			
			endorsementDTOs.push(endorsementDTO);
		})
		
		return endorsementDTOs;
	}
	
	function getRateCriterion($employeeListItem, attributeName, rateCriterionId){
		
		var rateCriterion = {}
		
		rateCriterion.employeeId = $employeeListItem.val();
		rateCriterion.jobId = $("#jobId").val();		
		rateCriterion.rateCriterionId = rateCriterionId;
		rateCriterion.value = $employeeListItem.attr(attributeName);	
		
		return rateCriterion;
	}
	

	function getRatingValue($ratingListItem){
		
		//If the list item is clicked, return the list items value data attribute
		if($ratingListItem.hasClass("clicked") == 1){
			return $ratingListItem.attr("data-value");	
			
		//Else the clicked rating is being removed and
		//set the employee's rating value back to the default
		}else{
			return "-1"
		}
			
		
	}
	
	function updateEmployeesEndorsementAttribute(categoryId){
		
		//Within the endorsements attribute, category ids begin with a "*" for identity purposes.
		var categoryIdKey = "*" + categoryId;
		
		//Get the selected employee list item
		var employeeListItem = getSelectedEmployeeListItem();			
		var endorsements = $(employeeListItem).attr("data-endorsements");
		
		//If the empoyee does not yet have the category id in it's endorsement attribute.			
		if(endorsements.indexOf(categoryIdKey) == -1){
			//Add it
			endorsements += categoryIdKey;
		}else{
			//Else remove it
			endorsements = endorsements.replace(categoryIdKey, "");
		}
		
		//Update the employee's endorsement attribute
		$(employeeListItem).attr("data-endorsements", endorsements);
	}
	
	
</script>

<%@ include file="./includes/Footer.jsp"%>