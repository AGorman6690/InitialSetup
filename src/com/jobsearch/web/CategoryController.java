package com.jobsearch.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.jobsearch.category.service.CategoryServiceImpl;
import com.jobsearch.json.JSON;
import com.jobsearch.model.App;
import com.jobsearch.model.Item;

@Controller
@SessionAttributes({ "user", "app" })
public class CategoryController {

	@Autowired 
	CategoryServiceImpl categoryService;
	
	@RequestMapping(value = "/addCategoryToJob", method = RequestMethod.GET)
	@ResponseBody
	public String addCategoryToJob(@RequestParam int jobId,	@RequestParam int categoryId) {

		categoryService.addCategoryToJob(jobId, categoryId);

		return JSON.stringify(categoryService.getCategoriesByJobId(jobId));

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
	
//	@RequestMapping(value = "/getCatsBySuperCat", method = RequestMethod.GET)
//	@ResponseBody
//	public String superCatId(@RequestParam int superCatId){		
//		Item item = new Item();
//		item.setCategories(categoryService.getCatsBySuperCat(superCatId));
//		return JSON.stringify(item);
//	}
	
	@RequestMapping(value = "/getAppCategories", method = RequestMethod.GET)
	@ResponseBody
	public String getAppCategories(){		
		return JSON.stringify(categoryService.getAppCategories());
	}
}
