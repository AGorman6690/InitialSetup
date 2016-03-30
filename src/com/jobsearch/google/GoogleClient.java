package com.jobsearch.google;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;

public class GoogleClient {
	
	public GeocodingResult[] getLatAndLng(String address){
		
		GeoApiContext context = new GeoApiContext().setApiKey("AIzaSyAXc_OBQbJCEfhCkBju2_5IfjPqOYRKacI");
		
		try {
			return GeocodingApi.geocode(context, address).await();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	//Found here: https://rosettacode.org/wiki/Haversine_formula#Java
    public static double getDistance(double lat1, double lon1, double lat2, double lon2) {
    	final double R = 6372.8; // In kilometers
    	double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);
 
        double a = Math.pow(Math.sin(dLat / 2),2) + Math.pow(Math.sin(dLon / 2),2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return R * c;
    }
}

