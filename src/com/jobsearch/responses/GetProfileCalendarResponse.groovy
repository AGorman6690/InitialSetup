package com.jobsearch.responses

import com.jobsearch.model.Application
import com.jobsearch.model.Job

public class GetProfileCalendarResponse {
	List<CalendarApplication> calendarApplications
	public GetProfileCalendarResponse(){
		calendarApplications = new ArrayList<>();
	}
	
	public static class CalendarApplication {
		Application application
		List<String> dates
		Job job
	}
}
