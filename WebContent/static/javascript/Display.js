
function showChildren(elementId){

	$('#' + elementId + ' > div').each(function(){		
		$(this).attr('class', 'show');	
	});	
}

function hideDescendants(elementId){
	
	$('#' + elementId + ' div').each(function(){
		$(this).attr('class', 'hide');		
	});	
}


function toggleChildrenDisplay(elementId){
	
	//For each child div
	$('#' + elementId + '> div').each(function(){
	
		//If the div is currently shown
		if (this.className.localeCompare('show') == 0){		
			
			//Hide the child div
			this.className= 'hide';
			
			//Hide all the child's descendants
			$('#' + this.id + ' div').removeClass();
			$('#' + this.id + ' div').addClass('hide');	
		}
		//Else if it's hidden
		else {			
			this.className = 'show';
		}
	});
}

function toggleAllChildrenDisplay(elementId){
	
	alert("toggleChildrenDisplay for " + elementId);
	$('#' + elementId + ' div').each(function(){
		
		alert($(this).attr('id'));
	
		if (this.className.localeCompare('show') == 0)
			this.className= 'hide';
		else {			
			this.className = 'show';
		}
		});
}



function createChildDivs(arr, elementId, callback){
	
	var parentE = document.getElementById(elementId);
	
	//Get the parent's level
	var parentLevel = getLevel(parentE.id);
	
	//The new level is 1 level deeper than its parent level
	var newLevel = parseInt(parentLevel) + 1;
	
	var i;	
	for(i=0; i<arr.length; i++){
		var newE = document.createElement("div");
		
		//The inner html will be a series of spaces and the category name
		newE.innerHTML = getIndentation(newLevel) + arr[i].name;	

		//The new div id will be {categoryId}-{level}
		newE.id = arr[i].id + '-' + newLevel;;
		newE.className = 'hide';
		
		//alert('new id ' + newE.id);
		parentE.appendChild(newE);
	}
	
	//return the parent's id
	callback(elementId);
}


function addClickEvent(elementId, callback){

	//For each category 1 level deep	
	$('#' + elementId + '> div').each(function(){

		//The click event will toggle the children divs' display property
		$(this).click(function(event){
			
			//Don't fire the parents' div click event
			event.stopPropagation();

			//The the categories two levels deep
			setSecondLevel(this.id, function(elementId){
				toggleChildrenDisplay(elementId);
			});			
		});				
	});
	callback(elementId);
}

function setFirstLevel(elementId, callback){
	//This will:
	//1) Get the sub categories 1 level deep.
	//2) Create div elements, for the sub categories, within the super category div
	//3) Add a click event for each sub category div
	
	//alert("1 getFirstLevel " + elementId);
	
	//Get categories that are 1 level deep relative to the element pass as the parameter
	getCategoriesBySuperCat(elementId, function(response, elementId){

		//For the element passed in, create children divs containing the categories that are 1 level deep
		createChildDivs(response, elementId, function(elementId){
			
			//Add the click event for the children divs
			addClickEvent(elementId, function(){
				callback(elementId);
			});	
		});				
	});	
}
	

function setSecondLevel(elementId, callback){
	
	//The presence of done key signifies the element's second level
	//has been set
	var doneKey = 'done';
	
	//If the element Id does not contain the "done key", then
	//proceed to set the div's sub categories.
	//If the element already has set it's sub categories, then skip.
	//This will eliminate re-adding the div's sub categories if the user 
	//expands and re-expands the div. 
	if (elementId.indexOf(doneKey) == -1){
	//	alert("setting setSecondLevel for " + elementId);

		//For each category 1 level deep
		$('#' + elementId + '> div').each(function(){		
		//	alert("set first level for " + $(this).attr('id'));
			//Set the categories 1 level deep
			setFirstLevel(this.id, function(){});
		});
		
		//Append the "done key" to the element's id to signify its
		//second level has been set. 		
		$('#' + elementId).attr('id', elementId + doneKey);
		elementId += doneKey;
		//alert("after all divs");	
		
		//alert("COMPLETE setting setSecondLevel for " + elementId);
		callback(elementId);
	}else{
		callback(elementId);
	}	
}


function getLevel(id){
	//PURPOSE: This will take an Id an parse it to find the level.
	//The div tag's id syntax is "categoryId"-"level".	
	//For example, if id = "12345-56", this will return 56. 
	
	var levelStart = id.indexOf("-") + 1;
	return id.substring(levelStart, id.length);
}

function getCategoryId(id){
	//PURPOSE: This will take an Id an parse it to find the id.
	//The div tag's id syntax is "categoryId"-"level".	
	//For example, if id = "12345-56", this will return 12345. 
	
	var idEnd = id.indexOf("-");
	return id.substring(0, idEnd);
}

function getIndentation(level){
	//PURPOSE: This will take in a level and return spaces.
	//The spaces will give the effect of sub categories (i.e. indentation)
  	var i;
    var temp = "";
    for(i=0; i<level; i++){
    	temp = temp + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
    }
    return temp;
  }
