package com.jobsearch.category.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jobsearch.category.repository.CategoryRepository;

@Service
public class CategoryServiceImpl {
	
	@Autowired
	CategoryRepository repository;
	
	public void addCategoryToJob(int jobId, int categoryId) {
		repository.addCategoryToJob(jobId, categoryId);

	}

	public List<Category> getCategoriesByJobId(int jobId) {
		return repository.getCategoriesByJobId(jobId);
	}

	public List<Category> getCategories() {
		return repository.getCategories();
	}
	           
	public List<Category> getCategoriesByUserId(int userId) {
		return repository.getCategoriesByUserId(userId);
	}

	public Category getCategory(int categoryId) {
		return repository.getCategory(categoryId);
	}
	
	public List<Category> getAppCategories() {
		return repository.getAppCategories();
	}
	
	

}
