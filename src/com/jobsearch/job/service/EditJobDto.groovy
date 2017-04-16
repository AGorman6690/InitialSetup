package com.jobsearch.job.service

import java.awt.TexturePaintContext.Int;
import java.util.List

import org.springframework.aop.aspectj.RuntimeTestWalker.ThisInstanceOfResidueTestVisitor;

import com.jobsearch.application.service.Application
import com.jobsearch.application.service.ApplicationDTO
import com.jobsearch.category.service.Category
import com.jobsearch.model.EmploymentProposalDTO;
import com.jobsearch.model.JobSearchUser;
import com.jobsearch.model.JobSearchUserDTO
import com.jobsearch.model.Question
import com.jobsearch.model.Skill
import com.jobsearch.model.WorkDay
import com.jobsearch.model.WorkDayDto;
import com.jobsearch.model.application.ApplicationInvite;
import com.jobsearch.user.rate.RatingDTO

public class EditJobDto {
	
	int jobId
	List<WorkDay> newWorkDays
	EmploymentProposalDTO proposalDto

}
