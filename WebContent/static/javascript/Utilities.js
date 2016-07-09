
function formatTime(time){
	//When converting from string to java.sqlTime on the server,
	//java.sql.Time.valueOf(someString) needs the parameter string to be in "hh:mm:ss" format.

	if( time == "" ){
		return "00:00:00";
	}else{

		var len = time.length;

		 //am or pm
		var dayHalf = time.substring(len - 2);

		var colon = time.indexOf(":");
		var hour = time.substring(0, colon);
		var minutes = time.substring(colon + 1, len - 2);

		if(dayHalf == "pm"){
			hour = parseInt(hour) + 12;
		}

		if(hour.length == 1){
			hour = "0" + hour;
		}

		return hour + ":" + minutes + ":00";
	}


}

function formatTimeTo12Hours(time){
	//Used to convert hh:mm:ss to h:mm[am or pm]
	
	if( time == "" ){
		return "0:00am";
	}else{

		var len = time.length;

		 //am or pm
		var dayHalf;

		var firstColon = time.indexOf(":");
		var secondColon = time.indexOf(":", firstColon + 1);
		var hour = time.substring(0, firstColon);
		var minutes = time.substring(firstColon + 1, secondColon);

		if(hour > 12){
			hour = parseInt(hour) - 12;
			dayHalf = "pm";
		}else{
			dayHalf = "am";
		}


		return Number(hour).toString() + ":" + minutes + dayHalf;
	}


}


function salert(array){
	alert(JSON.stringify(array))
}