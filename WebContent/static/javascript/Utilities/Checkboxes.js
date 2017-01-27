
$(document).ready(function(){

	$(".checkbox-container input.select-all").change(function(){
	
		var doSelectAll;
		if($(this).is(":checked")) doSelectAll = true;
		else doSelectAll = false;
		
		changeSelectAllCheckbox($(this), doSelectAll, true);
		
		
	})
	
	$(".checkbox-container .options input[type=checkbox]").change(function(){
		
		
		var totalCheckboxes_unchecekd;
		var checkbox_selectAll = $(this).closest(".checkbox-container").find(".select-all").eq(0);
		// If un-checking
		if(!$(this).is(":checked")){
			
			// De-select the "select all" checkbox
			$(checkbox_selectAll).prop("checked", false);
		}
		else{
			
			totalCheckboxes_unchecked = $(this).closest(".checkbox-container")
												.find(".options input[type=checkbox]:not(:checked)");
			
			if(totalCheckboxes_unchecked.length == 0) $(checkbox_selectAll).prop("checked", true);
			
		}
	})
	
	
})

function changeSelectAllCheckbox($selectAllCheckbox, doSelectAll, doExecuteChangeEvent){
	
	
	$selectAllCheckbox.prop("checked", doSelectAll);
	
	var checkboxesToChange =  [];	
	if(doSelectAll){		
		checkboxesToChange = $selectAllCheckbox.closest(".checkbox-container")
										.find(".options input[type=checkbox]:not(:checked)");
	}
	else{
		checkboxesToChange = $selectAllCheckbox.closest(".checkbox-container")
										.find(".options input[type=checkbox]:checked");
	}

	$(checkboxesToChange).each(function(){		
		if(doExecuteChangeEvent) $(this).prop("checked", doSelectAll).change();
		else $(this).prop("checked", doSelectAll);
	})	

}

function disableCheckboxes($checkboxContainer){
	
	$checkboxContainer.addClass("not-checkable");
	
	$checkboxContainer.find(" input[type=checkbox]").each(function(){
		$(this).attr("disabled", "disabled");
	})
}

function enableCheckboxes($checkboxContainer){
	$checkboxContainer.find(" input[type=checkbox]").each(function(){
		$(this).removeAttr("disabled");
	})
}
