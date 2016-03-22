package com.jobsearch.model;

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
}

