package com.jobsearch.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
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
	
	@RequestMapping(value = "category/{superCategory}/subCategories", method = RequestMethod.GET)
	@ResponseBody
	public String getSubCategories(@PathVariable int superCategory) {
	
		List<Category> categories = categoryService.getSubCategories(superCategory);
		
		return JSON.stringify(categories);
	
	}

}
