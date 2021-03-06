package com.jobsearch.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jobsearch.google.GoogleClient;
import com.jobsearch.json.JSON;
import com.jobsearch.model.WorkDayDto;
import com.jobsearch.repository.ProposalRepository;
import com.jobsearch.service.ApplicationServiceImpl;
import com.jobsearch.service.JobServiceImpl;
import com.jobsearch.service.ProposalServiceImpl;
import com.jobsearch.service.RatingServiceImpl;
import com.jobsearch.service.UserServiceImpl;
import com.jobsearch.service.WorkDayServiceImpl;
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

		List<WorkDayDto> workDayDtos = workDayService.getWorkDayDtos(jobId);

		return JSON.stringify(workDayDtos);
	}

}
