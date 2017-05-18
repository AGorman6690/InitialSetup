package com.jobsearch.utilities;

import com.jobsearch.job.service.Job;
import com.jobsearch.model.JobSearchUser;

public final class DistanceUtility {
	
	public final static int earthRadius_miles = 3959; 
	
//	http://stackoverflow.com/questions/3694380/calculating-distance-between-two-points-using-latitude-longitude-what-am-i-doi
	public static Double getDistance(double lat1, double lat2, double lon1,
	        double lon2, double el1, double el2) {

	    final int R = earthRadius_miles; // Radius of the earth

	    Double latDistance = Math.toRadians(lat2 - lat1);
	    Double lonDistance = Math.toRadians(lon2 - lon1);
	    Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
	            + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
	            * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
	    Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
	    
	    double distance = R * c;

	    double height = el1 - el2;

	    distance = Math.pow(distance, 2) + Math.pow(height, 2);

	    return Math.sqrt(distance);
	}
	
	
	public static Double getDistance(JobSearchUser user, Job job) {

		return getDistance(user.getHomeLat(), job.getLat(), user.getHomeLng(), job.getLng(), 0, 0);
	}

}
