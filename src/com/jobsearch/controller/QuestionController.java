package com.jobsearch.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jobsearch.category.service.CategoryServiceImpl;
import com.jobsearch.google.GoogleClient;
import com.jobsearch.json.JSON;
import com.jobsearch.model.Question;
import com.jobsearch.repository.ProposalRepository;
import com.jobsearch.service.ApplicationServiceImpl;
import com.jobsearch.service.JobServiceImpl;
import com.jobsearch.service.ProposalServiceImpl;
import com.jobsearch.service.QuestionServiceImpl;
import com.jobsearch.service.RatingServiceImpl;
import com.jobsearch.service.UserServiceImpl;
import com.jobsearch.service.WorkDayServiceImpl;
import com.jobsearch.utilities.VerificationServiceImpl;

@Controller
public class QuestionController {
	
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
	@Autowired
	QuestionServiceImpl questionService;
	
	@ResponseBody
	@RequestMapping(value = "/post-job/previous-question/load", method = RequestMethod.GET)
	public String loadPreviousPostedQuestion(@RequestParam(name = "questionId", required = true) int questionId,
			HttpSession session) {

		// *****************************************************
		// *****************************************************
		// Can the getJobDto methond be used in place of this????
		// The getJobDTO_DisplayJobInfo() should also be responsible
		// for setting the questions.
		// This is redundant.
		// *****************************************************
		// *****************************************************

		Question postedQuestion = questionService.getQuestion_PreviousPostedQuestion(session, questionId);

		return JSON.stringify(postedQuestion);
	}
}
