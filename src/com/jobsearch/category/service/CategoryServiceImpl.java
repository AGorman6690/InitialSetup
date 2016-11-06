package com.jobsearch.category.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jobsearch.category.repository.CategoryRepository;
import com.jobsearch.job.service.JobServiceImpl;
import com.jobsearch.user.repository.UserRepository;
import com.jobsearch.user.service.UserServiceImpl;

@Service
public class CategoryServiceImpl {

	@Autowired
	CategoryRepository repository;

	@Autowired
	JobServiceImpl jobService;

	@Autowired
	UserServiceImpl userService;

	@Autowired
	UserRepository userRepository;

	public void addCategoryToUser(int userId, int categoryId) {
		repository.addCategoryToUser(userId, categoryId);
	}

	public void deleteCategoriesFromUser(int userId) {
		repository.deleteCategoriesFromUser(userId);
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

	public List<Category> getSubCategories_CALL_THIS_SOMETHING_DIFFERENT(int categoryId) {

		List<Category> categories = repository.getSubCategories(categoryId);

		for (Category category : categories) {
			category.setJobCount(jobService.getJobCountByCategory(category.getId()));
			category.setSubJobCount(jobService.getSubJobCount(category.getId(), 0));
		}

		return categories;
	}

	public List<Category> getSubCategories(int categoryId) {

		return repository.getSubCategories(categoryId);
	}

	public Category getCategory(int categoryId) {

		return repository.getCategory(categoryId);
	}

	public List<SubCategoryRequestDTO> getSubCategoryDTOs(List<Integer> categoryIds) {

		if (categoryIds.size() > 0) {

			List<SubCategoryRequestDTO> dtos = new ArrayList<SubCategoryRequestDTO>();

			// Get sub category DTOs for each category Id
			for (int categoryId : categoryIds) {

				List<Category> subCategories = this.getSubCategories(categoryId);

				for (Category subCategory : subCategories) {

					SubCategoryRequestDTO dto = new SubCategoryRequestDTO();
					dto.setCategoryId(categoryId);
					dto.setSubCategoryId(subCategory.getId());
					dto.setSubCategoryName(subCategory.getName());
					dto.setSubSubCategoryCount(getSubCategories(subCategory.getId()).size());

					dtos.add(dto);
				}
			}

			return dtos;

		} else {
			return null;
		}

	}

	public List<Category> getCategories(List<Integer> categoryIds) {
		List<Category> categories = new ArrayList<Category>();

		for(int categoryId : categoryIds){
			categories.add(this.getCategory(categoryId));
		}
		return categories;
	}

}
