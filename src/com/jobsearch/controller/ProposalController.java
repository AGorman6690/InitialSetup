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

import com.jobsearch.json.JSON;
import com.jobsearch.model.WorkDayDto;
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
	@RequestMapping(value = "/respond", method = RequestMethod.POST)
	public String respondToProposal(@RequestBody RespondToProposalRequest request, HttpSession session) {
		proposalService.respondToProposal(request, session);
		return "";
	}

	@RequestMapping(value = "/decline/{proposalId}", method = RequestMethod.GET)
	public String declineProposal(@PathVariable(value = "proposalId") int proposalId, HttpSession session) {
		proposalService.declineProposal(proposalId, session);
		return "redirect:/user/";
	}
	

}
