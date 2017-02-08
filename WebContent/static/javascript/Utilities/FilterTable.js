// th elements need a 'data-filter-attr'. It's value corresponds to 
// a data attribute on a tbody.tr element
// 



$(document).ready(function(){

	$("html").click(function(e){
		
		// Close all dropdowns if user clicked outside of a dropdown		
		if($(e.target).closest("th.header-dropdown").length == 0 &&
				!$(e.target).hasClass("dropdown-container")){
			
			// For some reason, if a datepicker's "prev" or "next" buttons are clicked,
			// the ".closest(".dropdown").length" will return 0 even though this element
			// has a .dropdown element as an ancestor...
//			if($(e.target).parent().hasClass("ui-datepicker-prev") == 0 &&
//				$(e.target).parent().hasClass("ui-datepicker-next") == 0 &&
//				
//				$(e.target).hasClass("ui-datepicker-prev") == 0 &&
//				$(e.target).hasClass("ui-datepicker-next") == 0){
				
				closeOtherDropdowns("");
//			}
			
		}
	})

	
	$("th[data-filter-attr] .glyphicon.approve-filter").click(function(){
		
//		closeOtherDropdowns($(this).closest(".dropdown-container").attr("id"));		
		
		var $th = $(this).closest("th[data-filter-attr]");
		var $filterContainer = $(this).closest(".filter-container");
		var $table = $(this).closest("table");
				
		var filters = [];
		filters= getFilters($table);
				
//		var filterValues = getFilterValues($filterContainer);
//		var filterAttrName = $th.attr("data-filter-attr");
		
		filterTableRows(filters, $table);
		
		$th.find("span[data-toggle-id]").eq(0).click();
	})
	
	$("th[data-sort-attr] input[type=radio]").change(function(){
		
//		closeOtherDropdowns($(this).closest(".dropdown-container").attr("id"));
		
		var $th = $(this).closest("th[data-sort-attr]");
		var $table = $(this).closest("table");		
		var sortAttr = $(this).closest("th[data-sort-attr]").attr("data-sort-attr");
		var doSortAscending = $(this).attr("data-sort-ascending");
		
		var sortedRows = getSortedRows(doSortAscending, sortAttr, $table);		
		sortTable(sortedRows, $table);
		
		
		resetOtherSortHeaders($th, $table);
		
		$th.find("span[data-toggle-id]").eq(0).click();
	})
	
	
})

	
function closeOtherDropdowns(dropdownIdToExclude){

	var iDropdownContainerId;
	var iDropdownContainer;
	
	$("html").find(".header-dropdown").each(function(){
		
		iDropdownContainer = $(this).find(".dropdown-container").eq(0);
		iDropdownId = $(iDropdownContainer).attr("id");
		if(iDropdownId != dropdownIdToExclude){
			if($(iDropdownContainer).is(":visible")){
				$(this).find("span[data-toggle-id]").eq(0).click();
			}
		}
	})
}


function resetOtherSortHeaders($thNotToReset, $table){

	$table.find("th[data-sort-attr]").each(function(){
		
		if(this != $thNotToReset[0]){
			$(this).find("input[type=radio]").each(function(){
				$(this).prop("checked", false);
			})
		}
	})
	
}

function sortTable(sortedRows, $table){
	
	var sortedHtml = "";
	
	$(sortedRows).each(function(){
		sortedHtml += this.html;
	})
		
	$table.find("tbody").eq(0).html(sortedHtml);
	
}

function getSortedRows(doSortAscending, sortAttr, $table){
		
	var sortedRows = [];
	var currentRow = {};
	var searchIndex;
	
	// for each table row
	$table.find("tbody tr").each(function(i, row){
		
		// Store row information
		currentRow = {};
		currentRow.value = $(row).attr(sortAttr);
		currentRow.html = $("<div />").append($(row).clone()).html(); // This will also copy the tr element. 
		
		if(i == 0) sortedRows.push(currentRow);
		else{
			
			// Search for the index to insert the row at 
			searchIndex = 0;			
			while(searchIndex < sortedRows.length ){
				
				if(doSortAscending == 1){
					if(currentRow.value > sortedRows[searchIndex].value) searchIndex += 1;
					else break;
				}
				else{
					if(currentRow.value < sortedRows[searchIndex].value) searchIndex += 1;
					else break;
				}
			} 

			// Insert the row at the specified index
			sortedRows.splice(searchIndex, 0, currentRow);
		}	
	})		
	
	return sortedRows;
	
}


function getFilters($table){
	
	var filters = [];
	var filter = {};
	
	var filterHeaders = $table.find("th[data-filter-attr]");
	var filterAttr;
	var filterValues = [];
	$(filterHeaders).each(function(){
		
		filterAttr = $(this).attr("data-filter-attr");
		
		filterValues = [];
		filterValues = getFilterValues($(this).find(".filter-container"));
		
		if(filterValues.length > 0){
			filter = {};
			filter.attr = filterAttr;
			filter.values = filterValues;
			filters.push(filter);	
		}
		
		
	})
	
	return filters;
	
}

function filterTableRows(filters, $table){
	
	var filterValue_currentRow;
	var doShowRow;
	
	$table.find("tbody tr:not(.no-filter)").each(function(i, row){
		
		doShowRow = true;
		$(filters).each(function(j, filter){
			
			filterValue_currentRow = $(row).attr(filter.attr);
			
			// If the row does not satisfy ALL APPLIED filters,
			// then the row will be hidden.
			if(!doesArrayContainValue(filterValue_currentRow, filter.values)){
				doShowRow = false;
				return false; // exit
			}
			
		})

		if(doShowRow) $(row).show();
		else $(row).hide();
		
	})	
}

function getFilterValues($filterContainer){
	
	var filterValues = [];
	var filterValue;
	
	$filterContainer.find("input:checked").each(function(){
		filterValue = $(this).attr("data-filter-attr-value");
		if(filterValue != undefined) filterValues.push(filterValue);
	})
	
	return filterValues;
	
}
	
