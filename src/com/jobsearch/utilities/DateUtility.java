package com.jobsearch.utilities;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.jobsearch.model.WorkDay;

public final class DateUtility {
	
	public enum TimeSpanUnit{
		Hours,
		Days,
	}

	public static java.sql.Date getSqlDate(String input) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			java.util.Date date = sdf.parse(input);
			return new java.sql.Date(date.getTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	




	public static java.sql.Date getSqlDate(String input, String inputFormat) {

		if (input != null) {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat(inputFormat);
				java.util.Date date = sdf.parse(input);
				return new java.sql.Date(date.getTime());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}

	
	public static String formatSqlTime(java.sql.Time time, String format){
		// For all formats:
		// https://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html
		return new SimpleDateFormat(format).format(time);
	}
	
	public static String formatSqlDate(Date date, String format){
		// For all formats:
		// https://docs.oracle.com/javase/8/docs/api/java/time/format/DateTimeFormatter.html
		return new SimpleDateFormat(format).format(date);
	}

	public static Double getTimeSpan(Date startDate, Time startTime, Date endDate,
											Time endTime, TimeSpanUnit timeSpanUnit) {
		
		// https://www.mkyong.com/java/how-to-calculate-date-time-difference-in-java/
		
		String dateFormat;
		String timeFormat;
		
		String stringStartDate;
		String stringEndDate;
		String stringStartTime;
		String stringEndTime;
		
		// Dates
		if(startDate != null && endDate != null){
			dateFormat = "MM/dd/yyyy";
			stringStartDate = formatSqlDate(startDate, dateFormat);
			stringEndDate = formatSqlDate(endDate, dateFormat);			
		}
		else{
			dateFormat = "";
			stringStartDate = "";
			stringEndDate = "";
		}
		
		
		// Times
		if(startTime != null && endTime != null){
			timeFormat = "HH:mm";
			stringStartTime = formatSqlTime(startTime, timeFormat);
			stringEndTime = formatSqlTime(endTime, timeFormat);			
		}
		else{
			timeFormat = "";
			stringStartTime = "";
			stringEndTime = "";
		}		
		
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat + " " + timeFormat);

		// Calculate time span
		try {
			Date date1 = sdf.parse(stringStartDate + " " + stringStartTime);
			Date date2 = sdf.parse(stringEndDate + " " + stringEndTime);
			
			//in milliseconds
			Double diff = (double) (date2.getTime() - date1.getTime());		

			switch (timeSpanUnit) {
				case Days:				
					return  diff / (24 * 60 * 60 * 1000) + 1;
				
				case Hours:
					return diff / (60 * 60 * 1000) % 24;
			}
			
		
			
		} catch (Exception e) {
			// TODO: handle exception
			
		}
		
		return null;
		
	}
	
	public static LocalDate getLocalDate(Date date){
		//http://stackoverflow.com/questions/21242110/convert-java-util-date-to-java-time-localdate
		return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

	}
	
	public static LocalTime getLocalTime(Time time){
		//http://stackoverflow.com/questions/21242110/convert-java-util-date-to-java-time-localdate
		//return time.toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
		return time.toLocalTime();
	}	
	
	public static Double getTimeSpan(LocalDate startDate, LocalTime startTime, 
									LocalDate endDate, LocalTime endTime,
									TimeSpanUnit timeSpanUnit) {

		try {
	
		LocalDateTime start = LocalDateTime.of(startDate, startTime);
		LocalDateTime end = LocalDateTime.of(endDate, endTime);
		
		long difference_milliseconds = ChronoUnit.MILLIS.between(start, end);
		
		switch (timeSpanUnit) {
			case Days:				
			return  (double) (difference_milliseconds / (24 * 60 * 60 * 1000) + 1);
			
			case Hours:
			return (double) (difference_milliseconds / (60 * 60 * 1000) % 24);
		}		
		
		} catch (Exception e) {
		// TODO: handle exception
		
		}
		
		return null;
		
		}

	
	public static LocalDate getMinimumDate(List<WorkDay> workDays) {	
		
		// Refactor: eliminate this method. Use getMinimumDate2
		
		LocalDate minDate = LocalDate.MAX;		
		for(WorkDay workDay : workDays){			 
			if(workDay.getDate() == null) workDay.setDate(LocalDate.parse(workDay.getStringDate()));
			if(workDay.getDate().isBefore(minDate)) minDate = workDay.getDate();
		}		
		return minDate;
	}
	
	public static LocalDate getMinimumDate2(List<String> dates) {
		LocalDate minDate = LocalDate.MAX;		
		for(String date : dates){	
			LocalDate localDate = LocalDate.parse(date);
			if(localDate.isBefore(minDate)) minDate = localDate;
		}		
		return minDate;
	}

	
	public static LocalDate getMaximumDate(List<WorkDay> workDays) {		
		LocalDate maxDate = LocalDate.MIN;		
		for(WorkDay workDay : workDays){			
			if(workDay.getDate() == null) workDay.setDate(LocalDate.parse(workDay.getStringDate()));
			if(workDay.getDate().isAfter(maxDate)) maxDate = workDay.getDate();
		}		
		return maxDate;
	}
	
	public static LocalDate getMaxDate2(List<String> dates) {
		LocalDate maxDate = LocalDate.MIN;		
		for(String date : dates){	
			LocalDate localDate = LocalDate.parse(date);
			if(localDate.isAfter(maxDate)) maxDate = localDate;
		}		
		return maxDate;
	}

	public static int getMonthSpan(List<WorkDay> workDays) {
		
		// Refactor: eliminate this method. Use getMonthSpan2
		
		if(workDays == null || workDays.size() == 0){
			return 1;
		}else{
			LocalDate minDate = getMinimumDate(workDays);
			LocalDate maxDate = getMaximumDate(workDays);			
			// Get total months since year 0.
			// This handles jobs where more than one year is spanned.
			// NOTE : The native ChronoUnit and Period classes did not accomplish this. 
			long months_minDate = minDate.getYear() * 12 + minDate.getMonthValue();
			long months_maxDate = maxDate.getYear() * 12 + maxDate.getMonthValue();			
			return (int) (months_maxDate - months_minDate + 1);
		}		
	}
	
	public static int getMonthSpan2(List<String> dates) {
		if(dates == null || dates.size() == 0){
			return 1;
		}else{
			LocalDate minDate = getMinimumDate2(dates);
			LocalDate maxDate = getMaxDate2(dates);			
			// Get total months since year 0.
			// This handles jobs where more than one year is spanned.
			// NOTE : The native ChronoUnit and Period classes did not accomplish this. 
			long months_minDate = minDate.getYear() * 12 + minDate.getMonthValue();
			long months_maxDate = maxDate.getYear() * 12 + maxDate.getMonthValue();			
			return (int) (months_maxDate - months_minDate + 1);
		}		
	}
	
	public static int getMonthSpan_new(List<LocalDate> dates) {
		Optional<LocalDate> minDate = dates.stream().min(Comparator.comparing(LocalDate::toEpochDay));
		Optional<LocalDate> maxDate = dates.stream().max(Comparator.comparing(LocalDate::toEpochDay));;
		// Get total months since year 0.
		// This handles jobs where more than one year is spanned.
		// NOTE : The native ChronoUnit and Period classes did not accomplish this. 
		long months_minDate = minDate.get().getYear() * 12 + minDate.get().getMonthValue();
		long months_maxDate = maxDate.get().getYear() * 12 + maxDate.get().getMonthValue();		
		return (int) (months_maxDate - months_minDate + 1);
	}

	public static String getTimeInBetween(LocalDateTime start, String stringEndDate, String stringEndTime) {
		
		LocalDate endDate = LocalDate.parse(stringEndDate.replace("/", "-"));
		LocalTime endTime = LocalTime.parse(stringEndTime);
		LocalDateTime end = LocalDateTime.of(endDate, endTime);
		
		long days = ChronoUnit.DAYS.between(start, end);
		long hours =  ChronoUnit.HOURS.between(start, end) - days * 24 ;
		long minutes =  ChronoUnit.MINUTES.between(start, end) - ( days * 24 * 60 ) - ( hours * 60 );
		String result = "";

		if(days == 1) result += days + " day, ";
		else if(days > 1) result += days + " days, ";
		result += " " + hours + ":";
		if(minutes < 10) result += "0";
		result += minutes + " hrs";

		// For example, the result will be in the form "2 days 15:45 hrs"
		return result;			
	}

	public static Boolean isDateExpired(LocalDateTime date, LocalDateTime now) {		
		if(date != null){
			if(date.isBefore(now)) return true;
			else return false;	
		}else return null;		
	}



	public static LocalDateTime getLocalDateTime(String dateString) {
		
		if(dateString == null) return null;
		else {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");
			return LocalDateTime.parse(dateString, formatter);	
		}
	}

	public static LocalDateTime getLocalDateTime(String date, String time) {
		
		if(date == null || time == null){
			return null;
		}
		else {
			LocalDate localDate = LocalDate.parse(date.replace("/", "-"));
			LocalTime localTime = LocalTime.parse(time);
			if (localDate == null || localTime == null){
				return null;
			}else{
				return LocalDateTime.of(localDate, localTime);					
			}
		}
	}
	
	public static LocalDateTime getFutureDate(LocalDateTime fromDate, Integer daysFromDate,
			Integer hoursFromDate, Integer minutesFromDate) {
	
		LocalDateTime futureDate = fromDate;
		if(NumberUtility.isPositiveNumber(daysFromDate))
			futureDate = futureDate.plusDays(daysFromDate);
		if(NumberUtility.isPositiveNumber(hoursFromDate))
			futureDate = futureDate.plusHours(hoursFromDate);
		if(NumberUtility.isPositiveNumber(minutesFromDate))
			futureDate = futureDate.plusMinutes(minutesFromDate);
		return futureDate;
	}

	public static boolean isValidFutrueDate(LocalDateTime earlierDate,
			LocalDateTime laterDate) {
		
		if(earlierDate != null && laterDate != null){	
			if(earlierDate.isBefore(laterDate)){
				return true;
			}else return false;
		}else return false;	
	}





	public static LocalDateTime getPastDate(LocalDateTime futureDate, Integer days,
			Integer hours, Integer minutes) {
	
		LocalDateTime pastDate = futureDate;
		if(NumberUtility.isPositiveNumber(days))
			pastDate = futureDate.minusDays(days);
		if(NumberUtility.isPositiveNumber(hours))
			pastDate = futureDate.minusHours(hours);
		if(NumberUtility.isPositiveNumber(minutes))
			pastDate = futureDate.minusMinutes(minutes);
		return pastDate;
	}





	public static String getCurrentTimestamp() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
	}











	
}
