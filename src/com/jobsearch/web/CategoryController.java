package com.jobsearch.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.jobsearch.category.service.Category;
import com.jobsearch.category.service.CategoryServiceImpl;
import com.jobsearch.job.service.JobServiceImpl;
import com.jobsearch.json.JSON;

@Controller
@SessionAttributes({ "user" })
public class CategoryController {

	@Autowired 
	CategoryServiceImpl categoryService;
	
	@Autowired
	JobServiceImpl jobService;
	
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
	
	@RequestMapping(value = "/addCategoriesToUser", method = RequestMethod.GET)
	@ResponseBody
	public String addCategoriesToUser(@RequestParam(value="category") int[] categoryIds, @RequestParam int userId) {
	

		// Add the category-user to the database
		for(int categoryId : categoryIds){
			categoryService.addCategoryToUser(userId, categoryId);
		}
		
		//Return the categories associated with the user's id
		return JSON.stringify(categoryService.getCategoriesByUserId(userId));

	}
	
	@RequestMapping(value = "/removeCategoriesFromUser", method = RequestMethod.GET)
	@ResponseBody
	public String removeCategoriesFromUser(@RequestParam(value="category") int[] categoryIds, @RequestParam int userId) {
	
		for(int categoryId : categoryIds){
			categoryService.removeCategoryFromUser(userId, categoryId);
		}
		
		//Return the categories associated with the user's id
		return JSON.stringify(categoryService.getCategoriesByUserId(userId));

	}
		
//	@RequestMapping(value = "/getCategoryByLevel", method = RequestMethod.GET)
//	@ResponseBody
//	public String getCategoryByLevel(@RequestParam int level) {
//		return JSON.stringify(categoryService.getCategoriesByLevel(level));
//	}
	
	@RequestMapping(value = "/getCategoriesBySuperCat", method = RequestMethod.GET)
	@ResponseBody
	public String getCategoriesBySuperCat(@RequestParam int superCat) {
	
		List<Category> categories = categoryService.getCategoriesBySuperCategory(superCat);
		
		for(Category category : categories){
			//category.setSubCategories(categoryService.getCategoriesBySuperCategory(category.getId()));
			category.setJobCount(jobService.getJobCountByCategory(category.getId()));
			category.setSubJobCount(jobService.getSubJobCount(category.getId(), 0));
		}
		
		return JSON.stringify(categories);
	
	}
	
	@RequestMapping(value = "/removeCategoryFromUser", method = RequestMethod.GET)
	@ResponseBody
	public String removeCategoryFromUser(@RequestParam int categoryId, @RequestParam int userId) {

		// Update database
		categoryService.removeCategoryFromUser(userId, categoryId);

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
	

}
