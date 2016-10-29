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
				<button id="submitRatings" class="square-button">Submit Ratings (no validation yet)</button>
			</div>
		</div>
		<div id="main" class="row">
			<div class="col col-sm-4">
				<div id="employees" class="column">	
					<div class="header bottom-border-thin">Employees</div>
					<div class="content">
						<ul class="select-one">
						<c:forEach items="${job.employees }" var="employee">
							<li data-user-id="${employee.userId }">${employee.firstName }</li>
						</c:forEach>
						</ul>
					</div>
				</div>			
			</div>
			<div class="col col-sm-4">
				<div id="ratings" class="column">	
					<div class="header">Ratings</div>
					<div class="content">
						<div class="criterion-container">
							<div class="criterion-name">On Time</div>						
							<ul id="onTime" data-rate-criterion-id="2" class="rating-list select-one criterion-list">
								<li class="" data-value="0">Never</li>
								<li class="" data-value="2.5">Occasionally</li>
								<li class="" data-value="5">Always</li>
							</ul>
						</div>
						<div class="criterion-container">
							<div class="criterion-name">Work Ethic</div>
							<ul id="workEthic" data-rate-criterion-id="1" class="rating-list select-one criterion-list">
								<li class="" data-value="0">Poor</li>
								<li class="" data-value="2.5">Average</li>
								<li class="" data-value="5">Exceptional</li>
							</ul>
						</div>
						<div class="criterion-container">
							<div class="criterion-name">Hire Again?</div>
							<ul id="hireAgain" data-rate-criterion-id="3" class="rating-list select-one criterion-list">
								<li class="" data-value="0">No</li>
								<li class="" data-value="2.5">Maybe</li>
								<li class="" data-value="5">Yes</li>
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
						<c:forEach items="${categories }" var="category">
							<li class="category" data-category-id="${category.id }">${category.name }</li>
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
<!-- 					<button id="saveComment" class="square-button-green">Save</button> -->
				</div>
			</div>
		</div>
		
	</div> <!-- end container -->
</body>

<script>

	var submitRatingDtos = [];
	
	
	$(document).ready(function(){
		
// 		function initializeSubmitRatingDtos(){
			
			//Initialize SubmitRatingDtos
			var rateCriterion;
			var submitRatingDto;	
			var i;
			$.each($("#employees").find("li"), function(){
				submitRatingDto = {};
				submitRatingDto.employeeId = $(this).attr("data-user-id");
				submitRatingDto.rateCriteria = []
				submitRatingDto.commentString = "";
// 				submitRatingDto.endorsements = [];
				submitRatingDto.endorsementCategoryIds = [];
				for(i = 1; i <= 3; i++){
					rateCriterion = {}
					rateCriterion.rateCriterionId = i;
					rateCriterion.value = -1;
					submitRatingDto.rateCriteria.push(rateCriterion);
				}
				submitRatingDtos.push(submitRatingDto);
			})			
// 		}
		
		
		
		$("#workEthic li").click(function(){
			
			updateRateCriterionValue(1, $(this).attr("data-value"));
			highlightArrayItem($(this), $("#workEthic").find("li"), "clicked");
		})
		
		$("#hireAgain li").click(function(){
			
			updateRateCriterionValue(3, $(this).attr("data-value"));
			highlightArrayItem($(this), $("#hireAgain").find("li"), "clicked");
		})
		
		$("#onTime li").click(function(){
			
			updateRateCriterionValue(2, $(this).attr("data-value"));
			highlightArrayItem($(this), $("#onTime").find("li"), "clicked");
			
		})
		
		function updateRateCriterionValue(rateCriterionId, value){
			var selectedEmployeeId = getSelectedEmployeeId();			
			var submitRatingDto = getSubmitRatingDto(selectedEmployeeId);
			var rateCriterion = getRateCriterionById(rateCriterionId, submitRatingDto);
			rateCriterion.value = value;
		}
		
		function getRateCriterionById(rateCriterionId, submitRatingDto){
			
			var rateCriterion = {};
			$.each(submitRatingDto.rateCriteria, function(){
				if(this.rateCriterionId == rateCriterionId){
					rateCriterion = this;
				}
			})
			return rateCriterion;
			
		}
		
		function getSelectedEmployeeId(){
			return $($("#employees").find("li.clicked")[0]).attr("data-user-id");
		}
		
		function showSubmitRatingDto(submitRatingDto){
			
			$.each(submitRatingDto.rateCriteria, function(){				
				showSelectedRateCriterion(this);
			})
			
			removeClassFromArrayItems($("#endorsements").find("li"), "clicked");
			$.each(submitRatingDto.endorsementCategoryIds, function(){
				showSelectedCategory(this);
			})
			
			$("#comment").val(submitRatingDto.commentString);
			
		}
		
		function showSelectedCategory(categoryId){
			var categoryLi = $("#endorsements").find("li[data-category-id='" + categoryId + "']")[0];
			$(categoryLi).addClass("clicked");
		}
		
		function showSelectedRateCriterion(rateCriterion){
			var ul = $("#ratings").find("ul[data-rate-criterion-id='" + rateCriterion.rateCriterionId + "']")[0];
			var listItems = $(ul).find("li");
			var rateCriterionValue = rateCriterion.value;
			highlightArrayItemByAttributeValue("data-value", rateCriterionValue, listItems, "clicked");
	
		}
		
		$("#employees li").click(function(){
			
			var clickedListItem = this;
			
			updateCurrentEmployeesComment(function(){
				highlightClickedEmployee(clickedListItem);
			})
		
			var submitRatingDto = getCurrentEmployeesSubmitRatingDto();
			showSubmitRatingDto(submitRatingDto);

		})
		
		$("#endorsements li").click(function(){
			
			
			
			toggleClass($(this), "clicked");
			
			var submitRatingDto = getCurrentEmployeesSubmitRatingDto();
			var categoryId = $(this).attr("data-category-id");
			
			updateCategoryEndorsements(categoryId, submitRatingDto);	
		
			
		})
		
		function highlightClickedEmployee(listItem){
			highlightArrayItem($(listItem), $("#employees").find("li"), "clicked");
		}
		
		function updateCategoryEndorsements(categoryId, submitRatingDto){
			
			if($.inArray(categoryId, submitRatingDto.endorsementCategoryIds) > -1){
				submitRatingDto.endorsementCategoryIds = removeArrayElementValue(categoryId,
															submitRatingDto.endorsementCategoryIds);
			}
			else{
				submitRatingDto.endorsementCategoryIds.push(categoryId);
			}
			
		}
		
		function updateCurrentEmployeesComment(callback){
			var submitRatingDto = getCurrentEmployeesSubmitRatingDto();
			submitRatingDto.commentString = $("#comment").val();;
			
			callback();
		}
		
		function getCurrentEmployeesSubmitRatingDto(){
			
			var clickedEmployeeLi = $("#employees").find("li.clicked")[0];
			var clickedEmployeeId = $(clickedEmployeeLi).attr("data-user-id");
			return getSubmitRatingDto(clickedEmployeeId);
			
		}
		
		
		
		function getSubmitRatingDto(userId){
			
			var submitRatingDto = {};
			$.each(submitRatingDtos, function(){
				if(this.employeeId == userId){
					submitRatingDto = this;
				}
			})
			return submitRatingDto;				
		}
			
		
		
		
		
	
		$("#submitRatings").click(function(){
			
			updateCurrentEmployeesComment(function(){
				submitRatings();
			});
									
			
		})
		

	
		
// 		$("#saveComment").click(function(){
// 			var submitRatingDto = getCurrentEmployeesSubmitRatingDto();
// 			submitRatingDto.commentString = $("#comment").val();;	
// 		})
			

		
		
		
	})
	


	
	
	
	function submitRatings(){

		var submitRatingDtos_wrapper = {};
		submitRatingDtos_wrapper.jobId = $("#jobId").val();
		submitRatingDtos_wrapper.submitRatingDtos = submitRatingDtos;


		$.ajax({
			type: "POST",
			url: '/JobSearch/user/rate',
			contentType : "application/json",
			headers : getAjaxHeaders(),
			data: JSON.stringify(submitRatingDtos_wrapper),
	        success: _success,
	        error: _error
	    });

		function _success(){
			window.location = "/JobSearch/user/profile";
		}

		function _error(){
// 			alert("error rateEmpoyee");
		}
				
	}
	

	
</script>

<%@ include file="./includes/Footer.jsp"%>