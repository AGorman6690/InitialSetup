package com.jobsearch.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jobsearch.application.service.ApplicationServiceImpl;
import com.jobsearch.category.service.CategoryServiceImpl;
import com.jobsearch.google.GoogleClient;
import com.jobsearch.json.JSON;
import com.jobsearch.model.WorkDayDto;
import com.jobsearch.proposal.repository.ProposalRepository;
import com.jobsearch.proposal.service.ProposalServiceImpl;
import com.jobsearch.service.JobServiceImpl;
import com.jobsearch.service.RatingServiceImpl;
import com.jobsearch.service.WorkDayServiceImpl;
import com.jobsearch.user.service.UserServiceImpl;
import com.jobsearch.utilities.VerificationServiceImpl;

@Controller
@RequestMapping( value = "/workdays")
public class WorkDayController {
	
	@Autowired
	ProposalRepository repository;
	@Autowired
	JobServiceImpl jobService;
	@Autowired
	ApplicationServiceImpl applicationService;
	@Autowired
	WorkDayServiceImpl workDayService;
	@Autowired
	CategoryServiceImpl categoryService;
	@Autowired
	UserServiceImpl userService;
	@Autowired
	VerificationServiceImpl verificationService;
	@Autowired
	GoogleClient googleClient;
	@Autowired
	RatingServiceImpl ratingService;
	@Autowired
	ProposalServiceImpl proposalService;


	@ResponseBody
	@RequestMapping(value = "/{jobId}", method = RequestMethod.GET)
	public String getWorkDayDtos_jobInfo(@PathVariable(value = "jobId") int jobId, HttpSession session) {

		List<WorkDayDto> workDayDtos = jobService.getWorkDayDtos(jobId);

		return JSON.stringify(workDayDtos);
	}
	
	@ResponseBody
	@RequestMapping(value = "/job/{jobId}/employee/{userId}/work-days", method = RequestMethod.GET)
	public String getWorkDayDtos_proposedWorkDays(@PathVariable(value = "jobId") int jobId,
			@PathVariable(value = "userId") int userId, HttpSession session) {

		return JSON.stringify(proposalService.getWorkDayDtos_proposedWorkDays(jobId, userId, session));
	}
	
}
