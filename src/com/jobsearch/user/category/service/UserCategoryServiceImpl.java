package com.jobsearch.user.category.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jobsearch.user.category.repository.UserCategoryRepositoryImpl;

@Service
public class UserCategoryServiceImpl {

	@Autowired
	UserCategoryRepositoryImpl userCategoryRepository;

	public void addUserCategories(List<Integer> categoryIds, int userID){
		categoryIds.stream().forEach(categoryId -> userCategoryRepository.addUserCategory(categoryId, userID));
	}
}
