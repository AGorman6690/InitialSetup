package com.jobsearch.user.web

import java.util.List

import com.fasterxml.jackson.annotation.JsonProperty

public class EditProfileRequestDTO {
	@JsonProperty
	int userId

	@JsonProperty
	List<Integer> categoryIds

	@JsonProperty
	Float homeLat

	@JsonProperty
	Float homeLng

	@JsonProperty
	String homeCity

	@JsonProperty
	String homeState

	@JsonProperty
	String homeZipCode

	@JsonProperty
	int maxWorkRadius

	@JsonProperty
	double minPay
}
