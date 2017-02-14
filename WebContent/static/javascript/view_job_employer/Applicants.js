$(document).ready(function(){
	
	
	$("#table_headerAnswers td input.filter-answers").change(function(){
		
		var checkedValue;
		var disabledValue;
		var $tr = $(this).closest("tr");
		
		if($(this).is(":checked")){
//			checkedValue = true;
			disabledValue = false;
			$tr.find("td.question").eq(0).removeClass("show-disabled");
			$tr.find("td.answers").eq(0).removeClass("show-disabled");
		}
		else{
			disabledValue = true;
			checkedValue = true;
			$tr.find("td.question").eq(0).addClass("show-disabled");
			$tr.find("td.answers").eq(0).addClass("show-disabled");
		}
		
		
		// Check the answer checkboxes
		$tr.find("td.answers input").each(function(){
			$(this).prop("checked", checkedValue);
			$(this).prop("disabled", disabledValue);
		})
		
		
		
	})
	
})