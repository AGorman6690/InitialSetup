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

		ratingService.setModel_ViewRateEmployer(jobId, model, session);
		return "/ratings/RateEmployer";
	}
	
	@RequestMapping(value = "/job/{jobId}/rate-employees", method = RequestMethod.GET)
	public String viewRateEmployees(@PathVariable(value = "jobId") int jobId, Model model, HttpSession session) {

		ratingService.setModel_ViewRateEmployees(jobId, model, session);
		return "/ratings/RateEmployees";
	}
	
	@RequestMapping(value = "/rating/user/{userId}", method = RequestMethod.GET)
	public String getRatings_byUser(Model model, @PathVariable(value = "userId") int userId) {		
		ratingService.setGetRatingByUserResponse(model, userId);		
		return "/ratings/RatingsByUser";
	}
	
	@RequestMapping(value = "/ratings/rate/employees", method = RequestMethod.PUT)
	@ResponseBody
	public String rateEmployees(HttpSession session,
									@RequestBody List<SubmitRatingRequest> requests) {

		ratingService.rateEmployees(requests, session);
		return "";
	}
	
	@RequestMapping(value = "/ratings/rate/employer", method = RequestMethod.PUT)
	@ResponseBody
	public String rateEmployer(HttpSession session,
									@RequestBody SubmitRatingRequest request) {

		ratingService.rateEmployer(request, session);
		return "";
	}
	
}
