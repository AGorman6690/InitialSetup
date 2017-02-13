function attemptToAddValueToArray(value, array){	
	if(!doesArrayContainValue(value, array)) array.push(value);
	return array;
}

function removeValueFromArray(value, array){
	if($.inArray(value, array) != -1){
		array.push(value);
	}
	return $.grep(array, function(element, i){
		return (element != value);
	})
}

function doesArrayContainValue(value, array){
	
	
	// Trim white space
	value = value.replace(/^\s+|\s+$/g,'')
	
	if($.inArray(value, array) == -1) return false;
	else return true;
}

function doesArrayContainAtLeastOneValue(values, array){
	
	var doesContainAtleastOneValue = false;
	
	$(values).each(function(i, value){
		if(doesArrayContainValue(value, array)){
			doesContainAtleastOneValue = true;
			return false;
		}
	})
	
	return doesContainAtleastOneValue;
	
	
}


function isStringACommaSeperatedArray(string){
	
	var array;
	
	// If necessary, remove the leading and trailing square brackets.
	string = string.replace(/[\[\]]/g, "");
		
	array = string.split(",");
	
	if(array.length > 0) return true;
	else return false;
}

function getArrayFromString(string){

	var array;

	
	// If necessary, remove the leading and trailing square brackets.
	string = string.replace(/[\[\]]/g, "");
		
	array = string.split(",");
	
	return array;
	
}