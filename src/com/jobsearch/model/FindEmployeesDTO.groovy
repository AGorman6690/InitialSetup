package com.jobsearch.model

import java.util.ArrayList;
import java.util.List;

import com.jobsearch.google.Coordinate;

class FindEmployeesDTO {

	String fromAddress
	double radius
	List<String> days
	double rating
	List<Integer> categoryIds
	Coordinate coordinate

	public FindEmployeesDTO(String fromAddress2, double radius2, double rating2, List<String> days2, List<Integer> categoryIds2) {
		this.setFromAddress(fromAddress2);
		this.setRadius(radius2);
		this.setRating(rating2);

		if(days2.get(0).matches("-1")){
			this.setDays(new ArrayList<String>());
		}
		else{
			this.setDays(days2);
		}

		this.setCategoryIds(categoryIds2);
	}
}
