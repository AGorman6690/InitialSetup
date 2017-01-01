package com.jobsearch.user.category.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.jobsearch.user.category.service.UserCategoryServiceImpl;

@Controller
@RequestMapping("/user/category")
public class UserCategoryController {

	@Autowired
	UserCategoryServiceImpl userCategoryService;

	@RequestMapping(value = "/add",  method = RequestMethod.PUT)
	public void addUserCategories(@RequestBody UserCategoriesDTO userCategoriesDTO){
		//userCategoryService.addUserCategories(userCategoriesDTO.getCategories(), userCategoriesDTO.getUserID());
	}
}
