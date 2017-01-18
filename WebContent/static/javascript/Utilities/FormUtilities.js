function clearAllInputs($container){
	
	$container.find("input[type=text]").each(function(){		
		$(this).val("");
	})
	
	$container.find("select").each(function(){
		$(this).val("");
	})
	
	$container.find("input[type=radio]").each(function(){		
		$(this).prop("checked", false);
	})	
	
	$container.find(".calendar-single-date .active111").each(function(){		
		$(this).removeClass("active111");
	})	
}

function areInputsValid_Container($container){
	
	var option;
	var isInvalid = 0;
	var name = "";
	
	// A container might have multiple radio groups.
	// Check all of them.
	$container.find("input[type=radio]").each(function(){
		name = $(this).attr("name");		
		if($container.find("input[type=radio][name=" + name + "]:checked").length == 0){
			isInvalid += 1;
		}
			
	})
	
	// Text inputs
	$container.find("input[type=text]").each(function(){
		if($(this).val() == "") isInvalid += 1;
		
	})
	
	// Selects
	$container.find("select").each(function(){		
		
		if($(this).val() == "") isInvalid += 1;
		
		if($(this).find("option:selected").length == 0) isInvalid += 1;
	})
	
	// Single date calendar
	$container.find(".calendar-single-date").each(function(){	
		if($(this).find(".active111").length == 0) isInvalid += 1;		
	})	
	
	if(isInvalid > 0 ) return false;
	else return true;
}

function removeInvalidCss($container){
	$container.find(".invalid").each(function(){
		$(this).removeClass("invalid");
	})
}