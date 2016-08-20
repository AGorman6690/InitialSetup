package com.jobsearch.job.service;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.apache.velocity.Template;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.maps.model.GeocodingResult;
import com.jobsearch.application.service.ApplicationServiceImpl;
import com.jobsearch.category.service.CategoryServiceImpl;
import com.jobsearch.google.GoogleClient;
import com.jobsearch.job.repository.JobRepository;
import com.jobsearch.model.JobSearchUser;
import com.jobsearch.model.Question;
import com.jobsearch.user.service.UserServiceImpl;

@RunWith(MockitoJUnitRunner.class)
public class JobServiceTest {

	@InjectMocks
	JobServiceImpl jobServiceImpl;

	@Mock
	JobRepository repository;
	@Mock
	CategoryServiceImpl categoryService;
	@Mock
	ApplicationServiceImpl applicationService;
	@Mock
	UserServiceImpl userService;
	@Mock
	Template filterJobsTemplate;
	@Mock
	GoogleClient googleClient;

	@Before
	public void setUp() {

	}

	@Test
	public void test_addPosting() {
		SubmitJobPostingRequestDTO submitJobPostingRequestDto = new SubmitJobPostingRequestDTO();
		List<JobInfoPostRequestDTO> jobs = new ArrayList<>();
		List<Question> questions = new ArrayList<>();

		JobInfoPostRequestDTO job1 = new JobInfoPostRequestDTO();
		JobInfoPostRequestDTO job2 = new JobInfoPostRequestDTO();
		jobs.add(job1);
		jobs.add(job2);

		Question question1 = new Question();
		Question question2 = new Question();
		questions.add(question1);
		questions.add(question2);

		JobSearchUser user = new JobSearchUser();

		submitJobPostingRequestDto.setJobs(jobs);

		jobServiceImpl.addPosting(submitJobPostingRequestDto, user);
		GeocodingResult[] result = new GeocodingResult[1];
		result[0] = new GeocodingResult();

		when(googleClient.getLatAndLng(anyString())).thenReturn(result);

		Mockito.verify(repository, Mockito.times(1)).addJob((JobInfoPostRequestDTO) anyObject(),
				(JobSearchUser) anyObject());

		assertThat(true).isTrue();
	}
}
