package com.jobsearch.category.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jobsearch.category.repository.CategoryRepository;
import com.jobsearch.job.service.JobServiceImpl;
import com.jobsearch.user.repository.UserRepository;

@Service
public class CategoryServiceImpl {

	@Autowired
	CategoryRepository repository;

	@Autowired
	JobServiceImpl jobService;

	@Autowired
	UserRepository userRepository;

	public void addCategoryToJob(int jobId, int categoryId) {
		repository.addCategoryToJob(jobId, categoryId);
	}

	public void addCategoryToUser(int userId, int categoryId) {

		if (!userRepository.hasCategory(userId, categoryId)) {
			repository.addCategoryToUser(userId, categoryId);
		}
	}

	public void removeCategoryFromUser(int userId, int categoryId) {
		repository.removeCategoryFromUser(userId, categoryId);

	}

	public List<Category> getCategoriesByJobId(int jobId) {
		return repository.getCategoriesByJobId(jobId);
	}

	public Category getCategoryByJobId(int jobId) {
		return repository.getCategoryByJobId(jobId);
	}

	public List<Category> getCategoriesByUserId(int userId) {
		return repository.getCategoriesByUserId(userId);
	}

	public List<Category> getSubCategories(int superCat) {
		
		List<Category> categories = repository.getSubCategories(superCat);
		
		for (Category category : categories) {
			category.setJobCount(jobService.getJobCountByCategory(category.getId()));
			category.setSubJobCount(jobService.getSubJobCount(category.getId(), 0));
		}
		
		return categories;
	}
}
