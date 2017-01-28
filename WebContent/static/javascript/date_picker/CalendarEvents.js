function getTdByDayMonthYear($calendar, day, month, year){
	
//	var a = $calendar.find("td[data-year=" + year + "][data-month=" + month + "] a:contains(" + day + ")");
	var anchors = $calendar.find("td[data-year=" + year + "][data-month=" + month + "]")
	var td;
	$(anchors).each(function(){
		if(this.innerText == day) td = this; 
	});
	
	return td;
	
	
	
}