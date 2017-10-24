package com.jobsearch.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jobsearch.request.AcceptProposalRequest;
import com.jobsearch.request.MakeInitialOfferByEmployerRequest;
import com.jobsearch.request.RespondToProposalRequest;
import com.jobsearch.service.ProposalServiceImpl;

@Controller
@RequestMapping(value = "/proposal")
public class ProposalController {

	@Autowired
	ProposalServiceImpl proposalService;

	@RequestMapping(value = "/{proposalId}", method = RequestMethod.GET)
	public String getProposal(@PathVariable(value = "proposalId") int proposalId, Model model, HttpSession session) {

		proposalService.setCurrentProposalResponse(model, session, proposalId);
		return "/wage_proposal/AjaxResponse_Proposal_NEW";
	}

	@ResponseBody
	@RequestMapping(value = "/", method = RequestMethod.POST)
	public String respondToProposal(@RequestBody RespondToProposalRequest request, HttpSession session) {
		proposalService.offerNewProposal(request, session);
		return "";
	}
	
	@ResponseBody
	@RequestMapping(value = "/accept", method = RequestMethod.POST)
	public String acceptProposal(@RequestBody RespondToProposalRequest request, HttpSession session) {
		proposalService.acceptProposal(request, session);
		return "";
	}

	@RequestMapping(value = "/decline/{proposalId}", method = RequestMethod.GET)
	public String declineProposal(@PathVariable(value = "proposalId") int proposalId, HttpSession session) {
		proposalService.declineProposal(proposalId, session);
		return "redirect:/user/";
	}
	
	@RequestMapping(value = "/employer-make-initial-proposal/user/{userId}", method = RequestMethod.GET)
	public String initiateContact_byEmployer(
			@PathVariable(value = "userId") int userId,
			@RequestParam(name = "jobId", required = false) Integer jobId,
			Model model,
			HttpSession session) {

		proposalService.setModel_employerToMakeInitialProposal(userId, jobId, model, session);		
		return "/wage_proposal/AjaxResponse_Proposal_NEW";
	}

	@RequestMapping(value = "/initial-proposal", method = RequestMethod.POST)
	@ResponseBody
	public String makeOffer(
			HttpSession session, 
			@RequestBody MakeInitialOfferByEmployerRequest request) {
		proposalService.insertInitialProposalByEmployer(request, session);
		return "";
	}
}
