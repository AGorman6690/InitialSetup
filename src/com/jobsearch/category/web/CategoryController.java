package com.jobsearch.category.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jobsearch.category.service.Category;
import com.jobsearch.category.service.CategoryServiceImpl;
import com.jobsearch.category.service.SubCategoryRequestDTO;
import com.jobsearch.job.service.JobServiceImpl;
import com.jobsearch.json.JSON;

@Controller
//@SessionAttributes({ "user" })
public class CategoryController {

	@Autowired
	CategoryServiceImpl categoryService;

	@Autowired
	JobServiceImpl jobService;

	@RequestMapping(value = "category/{superCategory}/subCategories", method = RequestMethod.GET)
	@ResponseBody
	public String getSubCategories_OLD(@PathVariable(value = "superCategory") int superCategory) {

		List<Category> categories = categoryService.getSubCategories_CALL_THIS_SOMETHING_DIFFERENT(superCategory);

		return JSON.stringify(categories);

	}
	
	@RequestMapping(value = "/categories/subCategories", method = RequestMethod.GET)
	@ResponseBody
	public String getSubCategories(@RequestParam(name = "categoryId", value = "categoryId") List<Integer> categoryIds) {
		
		List<SubCategoryRequestDTO> subCategoryRequestDtos = categoryService.getSubCategoryDTOs(categoryIds);

		return JSON.stringify(subCategoryRequestDtos);

	}
	
	

}
