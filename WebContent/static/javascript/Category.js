$(document).ready(function() {
	
	$("#categoryTree").on("click", ".add-category", function(){
		
		if($(this).hasClass("disabled-category") == 0){

			if(validateMaximumSelectedCategories() == 0){
				var clickedCatId = $($(this).parent()).attr("data-cat-id");
				
				//Check if category has already been added
				if($("#selectedCategories").find("[data-cat-id=" + clickedCatId + "]").length == 0){
					var clickedCatName = $($(this).siblings(".category-name")[0]).text();
					showCategory(clickedCatId, clickedCatName);
				}		
				
				validateMinimumSelectedCategories();
			}
		}

	})
	
	$("#selectedCategories").on("click", ".remove-category", function(){
		
		if($(this).hasClass("disabled-category") == 0){
			var clickedCatId = $($(this).parent()).attr("data-cat-id");
			$($("#selectedCategories").find("[data-cat-id=" + clickedCatId + "]")[0]).remove();
			validateMaximumSelectedCategories();
		}
	})
	
	$("#categoryTree").on("click", ".show-sub-categories", function(){
	
		if($(this).hasClass("disabled-category") == 0){

			var i;
			var li = $(this).parent()
			
			//Expand the sub categories
			if($(li).hasClass("expanded") == 0){
				
				var subCategories = $("#categoryTree").find("[data-super-cat-id='" + $(li).attr("data-cat-id") + "']");
				var subCategoryIds = [];
				for(i = 0; i < subCategories.length; i++){
					var subCategory = $(subCategories[i]); 
					$(subCategory).show();
					
					//Build a list of sub categories whose sub categories need to be set.
					//Only add the sub category if it's sub categories have NOT yet been set.
					//When a category is expanded, then closed, then expanded, the sub categories
					//must not be set again. 
					if($(subCategory).attr("data-sub-categories-set") == "0"){
						subCategoryIds.push($(subCategories[i]).attr("data-cat-id"));
					}
				}	
				
				//Get the clicked category's sub category's sub categories
				getSubCategories(subCategoryIds)	

				$(li).addClass("expanded");
				$(this).removeClass("glyphicon-menu-down");
				$(this).addClass("glyphicon-menu-up");
				
			//Hide **ALL** the sub categories
			}else{
				var clickedLevel = $(li).attr("data-level");
				var allBelowListItems = $(li).nextAll(".category-list-item");
				
				//While the list items' level is greater than the clicked level,
				//hide the list items.
				//As soon as a list item's level is less than or equal to the clicked level,
				//then exit - all the sub categories have been found.
				for(i = 0; i < allBelowListItems.length; i++){
					if($(allBelowListItems[i]).attr("data-level") > clickedLevel){
						var $subItem = $(allBelowListItems[i]);						

						if($subItem.hasClass("expanded") == 1){
							$subItem.removeClass("expanded");
							
							var $showSubItem = $($subItem.find(".show-sub-categories")[0]);
							$showSubItem.addClass("glyphicon-menu-down");
							$showSubItem.removeClass("glyphicon-menu-up");							
						}
						$subItem.hide();
					}else{
						//Exit for loop
						i = allBelowListItems.length;
					}
					
				}
				$(li).removeClass("expanded");
				$(this).addClass("glyphicon-menu-down");
				$(this).removeClass("glyphicon-menu-up");
			}
		}
	})
	
})

function showCategory(id, name){		
		$("#selectedCategories").append("<button disabled type='button' class='btn' data-cat-id="	
										+  id + ">" + name + "<span class='remove-category glyphicon"
										+ " glyphicon-remove'></span></button>");
	}


function getSubCategories(categoryIds) {

	if(categoryIds.length > 0){			
	
		var parameter = "?";
		for(var i = 0; i < categoryIds.length; i++){
			
			if(i > 0){
				parameter += "&";
			}
			parameter += "categoryId=" + categoryIds[i];	
		}

		$.ajax({
			type : "GET",
			url : environmentVariables.LaborVaultHost + '/JobSearch/categories/subCategories' + parameter,
			dataType : "json",
			success : _success,
			error : _error
		});

		function _success(subCategoryDTOs) {
// 				salert(subCategoryDTOs)
			for(i = 0; i < subCategoryDTOs.length; i++){
				
				var dto = subCategoryDTOs[i];
				var categorySelector = "[data-cat-id='" + dto.categoryId + "']"
				var $category = $($("#categoryTree").find(categorySelector)[0]);
				$category.attr("data-sub-categories-set", "1");

				//Clone
				var $eSubCategory = $($("#categoryListItemTemplate").clone(true, true));

				//Set the sub category's html				
				var $eSpan = $($eSubCategory.find(".indent")[0]); 	
				var subCategoryLevel = parseInt($category.attr("data-level")) + 1;
				$eSpan.text(dto.subCategoryName);		
				$eSubCategory.attr('id', "");	
				$eSubCategory.attr("data-cat-id", dto.subCategoryId);
				$eSubCategory.attr("data-super-cat-id", dto.categoryId);					
				$eSubCategory.attr("data-level", subCategoryLevel);
				$eSpan.addClass("level-" + subCategoryLevel);
				
				if(dto.subSubCategoryCount == 0){
					$($eSubCategory.find(".glyphicon-menu-down")[0]).remove();
				}

				//Insert
				$eSubCategory.insertAfter(categorySelector);					
			}
		}

		function _error() {
			alert("error getCategoriesBySuperCat 999");
		}
	
	}
	
}


function getCategoryIds(containerId){

	var elements = $("#" + containerId).children()
	var ids = new Array();
	for(var i=0; i<elements.length; i++){
		ids[i] = getCategoryId(elements[i].id);
	}

	return ids;
}

function getCategoriesBySuperCat(categoryId, callback) {

	$.ajax({
		type : "GET",
		url : environmentVariables.LaborVaultHost + '/JobSearch/category/' + categoryId
				+ '/subCategories',
		dataType : "json",
		success : _success,
		error : _error
	});

	function _success(response) {
		// alert("success getCategoriesBySuperCat for " + elementId );
		// alert(JSON.stringify(response))
		callback(response, categoryId);
	}

	function _error() {
		alert("error getCategoriesBySuperCat " + categoryId + " 999");
	}
}

