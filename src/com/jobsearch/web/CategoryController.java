package com.jobsearch.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.jobsearch.category.service.CategoryServiceImpl;

@Controller		
public class CategoryController {

	@Autowired 
	CategoryServiceImpl categoryService;
}
