package com.jobsearch.google;

import org.springframework.stereotype.Component;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import com.jobsearch.model.Job;
import com.jobsearch.model.JobSearchUser;


@Component
public class GoogleClient {

	public GeocodingResult[] getLatAndLng(String address) {		
		GeoApiContext context = new GeoApiContext().setApiKey("AIzaSyAXc_OBQbJCEfhCkBju2_5IfjPqOYRKacI");
		try {
			return GeocodingApi.geocode(context, address).await();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;	
	}

	// Found here: https://rosettacode.org/wiki/Haversine_formula#Java
	public static double getDistance(double lat1, double lng1, double lat2, double lng2) {
		final double R = 3959; // 6372.8 (kilometers), 3595 (miles)
		double dLat = Math.toRadians(lat2 - lat1);
		double dLon = Math.toRadians(lng2 - lng1);
		lat1 = Math.toRadians(lat1);
		lat2 = Math.toRadians(lat2);

		double a = Math.pow(Math.sin(dLat / 2), 2) + Math.pow(Math.sin(dLon / 2), 2) * Math.cos(lat1) * Math.cos(lat2);
		double c = 2 * Math.asin(Math.sqrt(a));
		return R * c;
	}

	private static boolean isValidResult(GeocodingResult[] results) {
		if (results == null || results.length != 1) return false;
		else return true;
	}

	public static String buildAddress(String streetAddress, String city, String state, String zipCode) {
	
		String address = "";		
		if(streetAddress != null) address += streetAddress;
		if(city != null) address += " " + city;
		if(city != null && state != null) address += ",";
		if(state != null) address += " " + state;
		if(zipCode != null) address += " " + zipCode;		
		return address;
	}

	public static boolean isValidAddress(JobSearchUser user) {		
		String address = buildAddress(null, user.getHomeCity(), user.getHomeState(), user.getHomeZipCode());
		return isValidAddress(address);
	}
	
	public static boolean isValidAddress(String address) {
		GoogleClient maps = new GoogleClient();
		GeocodingResult[] results = maps.getLatAndLng(address);
		if (isValidResult(results))	return true;
		else return false;
	}
	
	public static boolean isValidAddress(Coordinate coordinate) {
		if(coordinate == null) return false;
		else return true;
	}

	public static Coordinate getCoordinate(JobSearchUser user) {	
		String address = buildAddress(null, user.getHomeCity(), user.getHomeState(), user.getHomeZipCode());
		return getCoordinate(address);
	}
	
	public static Coordinate getCoordinate(String address) {

		GoogleClient maps = new GoogleClient();
		GeocodingResult[] results = maps.getLatAndLng(address);

		if(isValidResult(results)){
			Coordinate coordinate = new Coordinate();
			coordinate.setLatitude((float) results[0].geometry.location.lat);
			coordinate.setLongitude((float) results[0].geometry.location.lng);
			return coordinate;
		}else return null;
	}
	
	public static Coordinate getCoordinate(Job job) {	
		String address = buildAddress(job.getStreetAddress(), job.getCity(), job.getState(), job.getZipCode());
		return getCoordinate(address);
	}

	public static GeocodingResult getGeocodingResult(Job job) {
		
		String address = buildAddress(job.getStreetAddress(),
				job.getCity(), job.getState(), job.getZipCode());
		
		GoogleClient maps = new GoogleClient();
		GeocodingResult[] results = maps.getLatAndLng(address);

		return results[0];
	}

}
