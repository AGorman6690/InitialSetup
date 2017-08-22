package com.jobsearch.model

import java.time.LocalDate

public class CalendarDay {
	
	LocalDate date
	List<CalendarDayJobDto> jobDtos
	
	public static class CalendarDayJobDto{
		Job job
		WorkDay workDay
	}
	
}
 