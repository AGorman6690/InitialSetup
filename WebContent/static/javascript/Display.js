//
//function showChildren(elementId){
//
//	$('#' + elementId + ' > div').each(function(){	
//		//alert(' showing element ' + this.id)
//		$(this).attr('class', 'show');	
//	});	
//}
//
//function hideDescendants(elementId){
//	
//	$('#' + elementId + ' div').each(function(){
//		$(this).attr('class', 'hide');		
//	});	
//}
//
//
//function toggleChildrenDisplay(elementId, callback){
////alert('about to toggle for ' + elementId)
//	//For each child div (i.e. sub category)
//	$('#' + elementId + '> div').each(function(){
//
//		//If the div is currently shown
//		if (this.className.localeCompare('show') == 0){		
//			//alert('hiding')
//			//Hide it
//			this.className= 'hide';
//			
//			//Hide all its descendants.
//			//This will close all sub-categories that the user expanded
//			$('#' + this.id + ' div').removeClass();
//			$('#' + this.id + ' div').addClass('hide');	
//
//		}
//		//Else if it's hidden
//		else {		
//			//alert('showing')
//			//Show the child div.
//			this.className = 'show border';
//		}
//	});
//	
//	callback(elementId)
//}
//
//
//function toggleCheckMark(elementId, callback){
//	var grey = "./static/css/pictures/greyCheck.png";
//	var green = "./static/css/pictures/greenCheck.png";
//	
//	//If the check mark is currently grey
//	if($('#' + elementId).attr('src').localeCompare(grey) == 0){
//		
//		//Loop through all image elements and reset a green check, if one exists. 
//		$('#0-0--done img').each(function(){
//			if($(this).attr('src').localeCompare(green) == 0){
//				$(this).attr('src', grey);
//			}
//			
//		});
//		//Set the clicked grey arrow to green
//		$('#' + elementId).attr('src', green);
//	}else {
//		$('#' + elementId).attr('src', grey);
//	}
//	
//	//Return the imaages source
//	callback($('#' + elementId).attr('src'), elementId);
//}
//
//function toggleAllChildrenDisplay(elementId){
//	
//	alert("toggleChildrenDisplay for " + elementId);
//	$('#' + elementId + ' div').each(function(){
//		
//		alert($(this).attr('id'));
//	
//		if (this.className.localeCompare('show') == 0)
//			this.className= 'hide';
//		else {			
//			this.className = 'show';
//		}
//		});
//}
//
//
//
//function createChildDivs(arr, elementId, callback){
//	
////These child divs will take the following form:
////****************************************************************
////****************************************************************
////	<div id= ***PARENT***> (This is the div element Id passed in. This div is a container for 
////								the parent category's sub categories and sub-sub categories and so forth. 
////		<span> (this will contain the parent category name)
////			<img></img> (check mark image)
////			<img></img> (down arrows image)
////		</span>
////	
////		<div id= ***FIRST SUB CATEGORY***>
////			<span>	(this will contain the sub cateogry's name)
////				<img></img>
////				<img></img> 
////			</span>
////			
////			(***SUB-SUB CATEGORY DIVS WILL GO HERE***)
////		</div>
////		
////		<div id= ***SECOND SUB CATEGORY***>
////			<span>
////				<img></img>
////				<img></img> 
////			</span>
////	
////			(***SUB-SUB CATEGORY DIVS WILL GO HERE***)
////		</div>
////	
////		(and so on for other sub categories...)
////	
////	</div>
////****************************************************************
////****************************************************************
//
//
//	//Parent div
//	var parentE = document.getElementById(elementId);
//	
//	//Parse the parent's level from its id
//	var parentLevel = getLevel(parentE.id);
//	
//	//The new level is 1 level deeper than its parent level
//	var newLevel = parseInt(parentLevel) + 1;
//	
//	//For each sub category create a new child div
//	var i;	
//	for(i=0; i<arr.length; i++){
//		
//		//New elements to append to the parent
//		var newD = document.createElement("div");
//		var newS = document.createElement("span");
//		var newI = document.createElement("img");
//		var newI2 = document.createElement("img");
//		
//		//The new div's inner html will be a series of spaces to give the effect of sub items.
//		//The new span's inner html will be the sub category name.
//		newD.innerHTML = getIndentation(newLevel);
//		newS.innerHTML = arr[i].name;
//		
//		//The new div id will be {categoryId}-{level}--.
//		//The text at the end of the span and images is used as a key to later identify the element
//		//and add things like click events, etc.
//		newD.id = arr[i].id + '-' + newLevel + '--';
//		newS.id = newD.id + 's';
//		newI.id = newD.id + 'iGC'; //GC for grey check
//		newI2.id = newD.id + 'iDA'; //DA for down arrow
//
//		//Set default classes
//		newD.className = 'hide margin-vert';
//		newS.className = '';
//		newI.className = 'margin-hori';
//	
//		//Set image sources
//		newI.src = "./static/css/pictures/greyCheck.png";
//		newI2.src = "./static/css/pictures/downArrows.png";
//
//		//Set the images' height. Eventually this should not be hard coded. 
//		newI.height = '12';//newS.height;
//		newI2.height = '12';//newS.height;
//
//		//Append the images to the span,
//		//append the span to the div.
//		//append the div to the parent.
//		newS.appendChild(newI);
//		newS.appendChild(newI2);
//		newD.appendChild(newS);
//		parentE.appendChild(newD);
//	}
//	
//	//return the parent's id
//	callback(elementId);
//}
//
//
//function createChildDivs_2(arr, elementId, callback){
//	//alert('creating child divs for ' + elementId);
//		//Parent div
//		//alert($('#0-0--').attr('id'))
//		//var parentE = document.getElementById('0-00--');
//
//		var parentE = document.getElementById(elementId);
//		//Parse the parent's level from its id
//		var parentLevel = getLevel(elementId);
//		
//		
//		//The new level is 1 level deeper than its parent level
//		var newLevel = parseInt(parentLevel) + 1;
//		
//		//For each sub category create a new child div
//		var i;	
//		for(i=0; i<arr.length; i++){
//			
//			//New elements to append to the parent
//			var newD = document.createElement("div");
//			var newS = document.createElement("span");
//			var newI = document.createElement("img");
//			var newS2 = document.createElement("span");
//			var newS3 = document.createElement("span");
//			
//			//The new div's inner html will be a series of spaces to give the effect of sub items.
//			//The new span's inner html will be the sub category name.
//			newD.innerHTML = getIndentation(newLevel);
//			newS.innerHTML = arr[i].name + " ";
//			newS3.innerHTML =  Math.floor((Math.random() * 10) + 1);
//
//			//The new div id will be {categoryId}-{level}--.
//			//The text at the end of the span and images is used as a key to later identify the element
//			//and add things like click events, etc.
//			newD.id = arr[i].id + '-' + newLevel + '--';
//			newS.id = newD.id + 'sCN'; //CN for category name
//			newI.id = newD.id + 'iDA';  //DA for down arrow
//			newS2.id = newD.id + 'sJC'; //JC for job count
//
//			//Set default classes
//			newD.className = 'hide margin-vert border';
//			newS.className = 'border';
//			newS2.className = 'border';
//			newS3.className = 'border';
//			newI.className = 'margin-hori border';
//		
//			//Set image sources
//			newI.src = "./static/css/pictures/downArrows.png";
//			//newS2.src = "./static/css/pictures/downArrows.png";
//
//			//Set the images' height. Eventually this should not be hard coded. 
//			newI.height = '12';//newS.height;
//			//newS2.height = '12';//newS.height;
//
//			//Append the images to the span,
//			//append the span to the div.
//			//append the div to the parent.		
//			
//			newS.appendChild(newS2);
//			newS.appendChild(newI);
////			newS3.prependChild(newI);
//			newS.appendChild(newS3);
//			newD.appendChild(newS);			
//			parentE.appendChild(newD);
//			
//
//		}
//		
//		//return the parent's id
//		callback(elementId);
//	}
//
//function setFindJobs(arr, parentId, callback){
//	
//	
//	//Parse the parent's level from its id
//	var parentLevel = getLevel(parentId);
//	
//	//For each category
//	for(var i=0; i<arr.length; i++){
//
//		
//		//The new level is 1 level deeper than its parent level
//		var newLevel = parseInt(parentLevel) + 1;
//		//alert(arr[i].name)
//		var text = '[{"name":"innerHTML","value":"' + arr[i].name + '"}]';
//		var childId = arr[i].id + '-' + newLevel + '--';
//		createChildE('0-0--', 'div', childId, text);
//
//	}
//	
//
////	var text = '[{"name":"src","value":"./static/css/pictures/greyCheck.png"}]';
////	createChildE('0-0--', 'img', "a", text);
//}
//
//
//
//
//function createChildE(parentId, childType, childId, text){
//	
//	//Create the new element.
//	var childE = document.createElement(childType);
//	childE.id = childId;
//	
//	//Set Attributes
//	var attrs = JSON.parse(text);	
////	alert(4445)
//	for(var i = 0; i < attrs.length; i++){
//
//		
//		if(attrs[i].name.localeCompare("innerHTML") == 0){
//			childE.innerHTML = attrs[i].value;
//		}else{
//			childE.setAttribute(attrs[i].name, attrs[i].value);	
//		}
//		
////		$('#' + childId).attr(attrs[i].name, attrs[i].value);
//		
//	}
//	
//	
//	//childE.setAttribute('value', 'asdf');
//	//Append the element
//	var e = document.getElementById(parentId);
//	e.appendChild(childE);	
//}
//
//
//
//function addClickEvent(elementId, callback){
////	alert('addClickEvent')
//	//For each div 1 level deep (i.e. sub category)	
//	$('#' + elementId + '> div').each(function(){
////		alert('add click event for ' + this.id)
//		//The down arrows' click event will toggle the div's children divs' (i.e. sub categories) display property
//		//and set the div' sub-sub categories (i.e. second level)
//		$('#' + this.id + 'iDA').click(function(event){
//			//Don't fire the parent elements' click event
//			event.stopPropagation();
//			//alert("down arrow click")
//			
//			//Set the div id of the image's parent span.
//			var divParentId = $(this).parent().parent().attr('id')
//	
////			alert('down arrow click. parent parent is ' + divParentId)
//			//Toggle the display for the parent div's children div(s) (i.e. sub categories)
//			toggleChildrenDisplay(divParentId, function(divParentId){
//	
//				//Set the categories two levels deep (i.e. the category's sub-sub categories).
//				//By setting two levels deep, lag will hopefully be eliminated because when user continues to expand
//				//the categories and the sub-sub categories need to be displayed, they will already be loaded. 
//				//Pass the image's parent's div id.			
//				setSecondLevel(divParentId, function(elementId){
//				});	
//				
//			});
//			
//		});
//		
//		//The green arrow click event will set the selected category Id
//		$('#' + this.id + 'iGC').click(function(event){
//			//Don't fire the parents' div click event
//			event.stopPropagation();
//			
//			//Toggle the check mark image.
//			//If necessary, clear a previously checked category.
//			toggleCheckMark($(this).attr('id'), function(src, elementId){
//				
//				//If the image source is now the green arrow
//				if (src.indexOf("green") !== -1){
//					//Parse the category id from the image's id
//					var categoryId = getCategoryId(elementId);
//					
//					//Store the selected category id
//					$('#selectedCategory').val(categoryId);
//				}else{
//					//Clear the selected category
//					$('#selectedCategory').val("");
//				}
//			});
//		});		
//	});
//	
//	callback(elementId);
//}
//
//
//
//function addDownArrowClickEvent(elementId, callback){
////alert('adding click event for ' + elementId)
//	//The down arrows' click event will toggle the div's children divs' (i.e. sub categories) display property
//	//and set, only once, the div' sub-sub categories (i.e. second level)
//	$('#' + elementId + 'iDA').click(function(event){
//		//Don't fire the parent elements' click event
//		event.stopPropagation();
//
//		//Set the div id of the image's parent span.
//		var divParentId = $(this).parent().parent().attr('id')
//
//		
//		//Toggle the display for the parent div's children div(s) (i.e. sub categories)
//		toggleChildrenDisplay(divParentId, function(divParentId){
//		
//			//If the category's second level has not been set, then
//			//set the categories two levels deep (i.e. the category's sub-sub categories).
//			//By setting two levels deep, lag will hopefully be eliminated because when user continues to expand
//			//the categories and the sub-sub categories need to be displayed, they will already be loaded. 									
//			if (IsSecondLevelSet(divParentId) == 0){	
//				
//				//For each child div (i.e. sub category)
//				$('#' + divParentId + ' > div').each(function(){
//					
//					getCategoriesBySuperCat(this.id, function(response, elementId){
//						
//						createChildDivs_2(response, elementId, function(elementId){
//						
//							addDownArrowClickEvent(elementId, function(){
//							});
//							setChildJobCount(elementId, function(){});
//						})
//					})
//				})
//				
//				//Append the "done" key to the id.
//				//This is hardcoded. There must be a better way to signify that the category's 
//				//second level has been set...
//				$('#' + elementId).attr('id', elementId + 'done');
//			}
//			
//		})
//		
//	})
//	
//	
//	callback(elementId);
//}
//
//
//
//
//function setFirstLevel(elementId, callback){
//	//This will:
//	//1) Get the categories 1 level deep (i.e. sub categories).
//	//2) Create div elements, for the sub categories, within the super category div.
////			*** See createChildDivs() method for further detail. ***
//	//3) Add a click event for the image elements within each sub category div
//		
//	//Get categories that are 1 level deep relative to the element pass as the parameter
//	getCategoriesBySuperCat(elementId, function(response, elementId){
//		
//		//For the element passed in, create children divs containing the categories that are 1 level deep
//		createChildDivs(response, elementId, function(elementId){
//			
//			//Add the click event for the children divs
//			addClickEvent(elementId, function(){
//				callback(elementId);
//			});	
//		});				
//	});	
//}
//
//function setFirstLevel_2(elementId, callback){
//	//This will:
//	//1) Get the categories 1 level deep (i.e. sub categories).
//	//2) Create div elements, for the sub categories, within the super category div.
////			*** See createChildDivs() method for further detail. ***
//	//3) Add a click event for the image elements within each sub category div
//		
//	alert('setting first level for ' + elementId)
//	//Get categories that are 1 level deep relative to the element pass as the parameter
//	getCategoriesBySuperCat(elementId, function(response, elementId){
//		
//		//For the element passed in, create children divs containing the categories that are 1 level deep
//		createChildDivs_2(response, elementId, function(elementId){
//			callback(elementId);
////			//Add the click event for the children divs
////			addClickEvent(elementId, function(){
////				callback(elementId);
////			});	
//		});				
//	});	
//}
//	
//function IsSecondLevelSet(elementId){
//	var doneKey = 'done';
//	if (elementId.indexOf(doneKey) == -1){
//		return 0;
//	} else{
//		return 1;
//	}
//}
//function setSecondLevel(elementId, callback){
//	
//	//alert('setting the second level for ' + elementId)
//	
//	//The presence of done key signifies the element's second level
//	//has been set
//	var doneKey = 'done';
//	
//	//If the element Id does not contain the "done key", then
//	//proceed to set the div's sub-sub categories.
//	//If the div has already set it's sub-sub categories, then skip.
//	//This will eliminate re-adding the div's sub-sub categories if the user 
//	//expands and re-expands the div. 
//	if (elementId.indexOf(doneKey) == -1){
//
//		//For each category 1 level deep
//		$('#' + elementId + '> div').each(function(){		
//
//			//Set the categories that are 1 level deep (i.e. two level deep relative to the 
//			//element passed in)
//			setFirstLevel(this.id, function(){});
//		});
//		
//		//Append the "done key" to the element's id to signify its
//		//second level has been set. 		
//		$('#' + elementId).attr('id', elementId + doneKey);
//		elementId += doneKey;
//		
//		callback(elementId);
//	}else{
//		callback(elementId);
//	}	
//}
//
//function setChildJobCount(eId){
////	alert('set child job count for ' + eId)
//	$('#' + eId + ' > div').each(function(){
//		setJobCount(this.id);
////		setSubJobCount(this.id);
//	})
//}
//
//function setJobCount(eId){
//
//	//Parse the category id
//	var categoryId = getCategoryId(eId);
//
//	//Get the category's job count
//	getJobCountByCategory(categoryId, function(response){
//		
//		//Set the span's inner html
//		var spanJobCount = document.getElementById(eId + 'sJC');
//		spanJobCount.innerHTML = response;
//	})
//
//}
//
//function setSubJobCount(eId){
//
//	//Parse the category id
//	var categoryId = getCategoryId(eId);
//
//	//Get the category's job count
//	getSubJobCountByCategory(categoryId, function(response){
//		
//		//Set the span's inner html
//		var spanJobCount = document.getElementById(eId + 'sJC');
//		spanJobCount.innerHTML = response;
//	})
//
//}
//
//function setSecondLevel_2(elementId, callback){
//	
//	alert('setting the second level for ' + elementId)
//
//	//The presence of done key signifies the element's second level
//	//has been set
//	var doneKey = 'done';
//	
//	//If the element Id does not contain the "done key", then
//	//proceed to set the div's sub-sub categories.
//	//If the div has already set it's sub-sub categories, then skip.
//	//This will eliminate re-adding the div's sub-sub categories if the user 
//	//expands and re-expands the div. 
//	if (elementId.indexOf(doneKey) == -1){
//
//		//For each category 1 level deep
//		$('#' + elementId + '> div').each(function(){		
////			alert('inside setSecondLevel. setting first level for ' + this.id )
//			//Set the categories that are 1 level deep (i.e. two level deep relative to the 
//			//element passed in)
//			setFirstLevel_2(this.id, function(response){});
//		});
//		
//		//Append the "done key" to the element's id to signify its
//		//second level has been set. 		
//		$('#' + elementId).attr('id', elementId + doneKey);
//		elementId += doneKey;
//		
//		callback(elementId);
//	}else{
//		callback(elementId);
//	}	
//	
//	alert('calling back from setSecondLevel_2')
//}
//
//
//function getLevel(id){
//	//PURPOSE: This will take an Id an parse it to find the level.
//	//The div tag's id syntax is {categoryId}-{level}--	
//	//For example, if id = "12345-56--", this will return 56. 
//	
//	var levelStart = id.indexOf("-") + 1;
//	var levelEnd = id.indexOf("--");
//	
//	return id.substring(levelStart, levelEnd);
//}
//
//function getCategoryId(id){
//	//PURPOSE: This will take an Id an parse it to find the id.
//	//The div tag's id syntax is {categoryId}-{level}--.	
//	//For example, if id = "12345-56--", this will return 12345. 
//	
//	var idEnd = id.indexOf("-");
//	return id.substring(0, idEnd);
//}
//
//function getIndentation(level){
//	//PURPOSE: This will take in a level and return spaces.
//	//The spaces will give the effect of sub categories (i.e. indentation)
//  	var i;
//    var temp = "";
//    for(i=0; i<level; i++){
//    	temp = temp + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
//    }
//    return temp;
//  }
