package com.jobsearch.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jobsearch.request.SubmitRatingRequest;
import com.jobsearch.service.RatingServiceImpl;
import com.jobsearch.session.SessionContext;

@Controller
//@RequestMapping( value = "/ratings")
public class RatingController{

	@Autowired
	RatingServiceImpl ratingService;


	@RequestMapping(value = "/job/{jobId}/rate-employer", method = RequestMethod.GET)
	public String viewRateEmployer(@PathVariable(value = "jobId") int jobId, Model model, HttpSession session) {

		if (ratingService.setModel_ViewRateEmployer(jobId, model, session))
			return "/ratings/RateEmployer";
		else
			return SessionContext.get404Page();
	}
	
	

	@RequestMapping(value = "/job/{jobId}/rate-employees", method = RequestMethod.GET)
	public String viewRateEmployees(@PathVariable(value = "jobId") int jobId, Model model, HttpSession session) {

		if (ratingService.setModel_ViewRateEmployees(jobId, model, session))
			return "/ratings/RateEmployees";
		else
			return SessionContext.get404Page();
	}
	
	@RequestMapping(value = "/user/{userId}/ratings", method = RequestMethod.GET)
	public String getRatings_byUser(Model model, @PathVariable(value = "userId") int userId) {		
		ratingService.setGetRatingByUserResponse(model, userId);		
		return "/ratings/RatingsByUser";
	}
	
	@RequestMapping(value = "/user/rate/employees", method = RequestMethod.POST)
	@ResponseBody
	public String rateEmployees(HttpSession session,
									@RequestBody List<SubmitRatingRequest> submitRatingDtos) {

		ratingService.insertRatings(submitRatingDtos, session);
		return "";
	}

	
}
