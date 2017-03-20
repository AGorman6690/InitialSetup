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


function removeDateFromArray(date, array){
	
	return $.grep(array, function(_date, i){
		return _date.getTime() != date.getTime();
	})
	
	return array;
}


function doesArrayContainValue(value, array){
	
	
	// Trim white space
	value = value.replace(/^\s+|\s+$/g,'')
	
	if($.inArray(value, array) == -1) return false;
	else return true;
}

function doesDateArrayContainDate(dateToCheck, dateArray){
	
	var arr = [];	
	arr = $.grep(dateArray, function(date, days){
		return date.getTime() == dateToCheck.getTime();
	})
	
	if(arr.length > 0) return true;
	else return false;
	
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

function doesArrayContainAllValues(values, array){
	
	var doesContainAllValues = true;
	
	$(values).each(function(i, value){
		if(!doesArrayContainValue(value, array)){
			doesContainAllValues = false;
			return false;
		}
	})
	
	return doesContainAllValues;
	
	
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