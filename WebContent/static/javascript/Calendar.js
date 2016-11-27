
	
	function removeDate(date, days){
		return $.grep(days, function(day){
			return day != date;
		})
	}
	
	function isDayAlreadyAdded(date, days){
		
		var arr = [];
		
		arr = $.grep(days, function(day){
			return day == date;
		})
		
		if(arr.length > 0){
			return true;
		}
		else{
			return false;
		}
		
		
	}
	
	
//	function removeActiveDaysFormatting(){
//		var activeDays = $("#calendar").find(".active111");			
//		$(activeDays).each(function(){
//			$(this).removeClass("active111");
//		})
//	}