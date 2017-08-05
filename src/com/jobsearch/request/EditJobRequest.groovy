package com.jobsearch.request

import java.util.List

import org.springframework.aop.aspectj.RuntimeTestWalker.ThisInstanceOfResidueTestVisitor;

import com.jobsearch.model.Application
import com.jobsearch.dtos.ApplicationDTO
import com.jobsearch.category.service.Category
import com.jobsearch.model.EmploymentProposalDTO;
import com.jobsearch.model.JobSearchUser;
import com.jobsearch.model.JobSearchUserDTO
import com.jobsearch.model.Question
import com.jobsearch.model.Skill
import com.jobsearch.model.WorkDay
import com.jobsearch.model.WorkDayDto;

public class EditJobRequest {	
	int jobId
	List<WorkDay> newWorkDays
}
