package com.jobsearch.google;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.AddressComponent;
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
    public static double getDistance(double lat1, double lng1, double lat2, double lng2) {
    	final double R = 3959; //6372.8 (kilometers), 3595 (miles)
    	double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lng2 - lng1);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);
 
        double a = Math.pow(Math.sin(dLat / 2),2) + Math.pow(Math.sin(dLon / 2),2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return R * c;
    }

	public static String getAddressComponent(AddressComponent[] addressComponents, String name) {
		
		//************************************************
		//This not finished.
		//The thought was to export to the database the address components returned from Google,
		//as opposed to the user's input.
		//This would do away with typos and make addresses, cities and states uniform.
		//Address this later.
		//************************************************
				
		//Address components (i.e. zip code, city, state, ect.) are not always returned in the same order.
		//Therefore the entire AddressComponet array must be searched for a particular address component.
	
		for (AddressComponent cmp : addressComponents){
			
			//If the component returned more than one type, then
			//the address is ambiguous (i.e. apparently Google maps cannot tell
			//whether the address component is a city or state).
			//Do not add this job.
			if(cmp.types.length == 1){
				if(cmp.types[0].name() == name){
					return cmp.shortName;
				}
			}else{
				//address component is ambiguous
				return null;
			}
			
		}
		
		return null;
	}
}

