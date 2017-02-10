package com.jobsearch.utilities;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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

	public static int getMinimumMonth(List<WorkDay> workDays) {
		
		List<LocalDate> dates = getLocalDates(workDays);		
		
		int minMonth = 12;
		
		for(LocalDate date : dates){
			if(date.getMonthValue() < minMonth) minMonth = date.getMonthValue();
		}
		
		return minMonth;
	}
	
	public static int getMaximumMonth(List<WorkDay> workDays) {
		
		List<LocalDate> dates = getLocalDates(workDays);
		
		int maxMonth = 1;
		
		for(LocalDate date : dates){
			if(date.getMonthValue() > maxMonth) maxMonth = date.getMonthValue();
		}
		
		return maxMonth;
	}

	public static LocalDate getMinimumDate(List<WorkDay> workDays) {
	
		LocalDate minDate = LocalDate.MAX;
		List<LocalDate> dates = getLocalDates(workDays);
		
		for(LocalDate date : dates){
			if(date.isBefore(minDate)) minDate = date;
		}
		
		return minDate;
	}
	
	public static LocalDate getMaximumDate(List<WorkDay> workDays) {
		
		LocalDate maxDate = LocalDate.MIN;
		List<LocalDate> dates = getLocalDates(workDays);
		
		for(LocalDate date : dates){
			if(date.isAfter(maxDate)) maxDate = date;
		}
		
		return maxDate;
	}

	private static List<LocalDate> getLocalDates(List<WorkDay> workDays) {
		
		return workDays.stream()
						.map(WorkDay::getDate)
						.collect(Collectors.toList());
	}

	public static int getMonthSpan(List<WorkDay> workDays) {

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
