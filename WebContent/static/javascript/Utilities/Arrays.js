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
	
	if($.inArray(value, array) == -1) return false;
	else return true;
}