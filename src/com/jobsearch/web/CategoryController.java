package com.jobsearch.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.jobsearch.category.service.CategoryServiceImpl;
import com.jobsearch.json.JSON;

@Controller
@SessionAttributes({ "user" })
public class CategoryController {

	@Autowired 
	CategoryServiceImpl categoryService;
	
	@RequestMapping(value = "/addCategoryToJob", method = RequestMethod.GET)
	@ResponseBody
	public String addCategoryToJob(@RequestParam int jobId,	@RequestParam int categoryId) {

		categoryService.addCategoryToJob(jobId, categoryId);

		return JSON.stringify(categoryService.getCategoriesByJobId(jobId));

	}
	
	@RequestMapping(value = "/addCategoryToUser", method = RequestMethod.GET)
	@ResponseBody
	public String addCategoryToUser(@RequestParam int categoryId, @RequestParam int userId) {

		// Add the category-user to the database
		categoryService.addCategoryToUser(userId, categoryId);

		//Return the categories associated with the user's id
		return JSON.stringify(categoryService.getCategoriesByUserId(userId));

	}
	
	@RequestMapping(value = "/deleteCategoryFromUser", method = RequestMethod.GET)
	@ResponseBody
	public String deleteCategoryFromUser(@RequestParam int categoryId, @RequestParam int userId) {

		// Update database
		categoryService.deleteCategoryFromUser(userId, categoryId);

		return JSON.stringify(categoryService.getCategoriesByUserId(userId));

	}
	
	@RequestMapping(value = "/getCategoryByJob", method = RequestMethod.GET)
	@ResponseBody
	public String getCategoryByJob(@RequestParam int jobId){		
		return JSON.stringify(categoryService.getCategoriesByJobId(jobId));
	}
	
	
	@RequestMapping(value = "/getCategoriesByUser", method = RequestMethod.GET)
	@ResponseBody
	public String getCategoriesByUser(@RequestParam int userId){		
		return JSON.stringify(categoryService.getCategoriesByUserId(userId));
	}
	
	
	@RequestMapping(value = "/getAppCategories", method = RequestMethod.GET)
	@ResponseBody
	public String getAppCategories(){		
		return JSON.stringify(categoryService.getAppCategories());
	}
}
