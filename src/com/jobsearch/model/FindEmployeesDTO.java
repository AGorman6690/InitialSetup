package com.jobsearch.model;

import java.util.ArrayList;
import java.util.List;

import com.jobsearch.google.Coordinate;

public class FindEmployeesDTO {

	private String fromAddress;
	private double radius;
	private List<String> days;
	private double rating;
	private List<Integer> categoryIds;
	private Coordinate coordinate;
	
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
	public Coordinate getCoordinate() {
		return coordinate;
	}
	public void setCoordinate(Coordinate coordinate) {
		this.coordinate = coordinate;
	}
	public String getFromAddress() {
		return fromAddress;
	}
	public void setFromAddress(String fromAddress) {
		this.fromAddress = fromAddress;
	}
	public double getRadius() {
		return radius;
	}
	public void setRadius(double radius) {
		this.radius = radius;
	}
	public List<String> getDays() {
		return days;
	}
	public void setDays(List<String> days) {
		this.days = days;
	}
	public double getRating() {
		return rating;
	}
	public void setRating(double rating) {
		this.rating = rating;
	}
	public List<Integer> getCategoryIds() {
		return categoryIds;
	}
	public void setCategoryIds(List<Integer> categoryIds) {
		this.categoryIds = categoryIds;
	}
	
}
