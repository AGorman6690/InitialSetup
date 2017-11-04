package com.jobsearch.utilities;

import java.time.LocalDateTime;

import com.jobsearch.model.WorkDay;

public final class WorkDayUtil {
	public static boolean isComplete(WorkDay workDay){
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime endTime = DateUtility.getLocalDateTime(workDay.getStringDate(), workDay.getStringEndTime());
		if (endTime.isBefore(now)){
			return true;
		}else{
			return false;
		}
	}

}
