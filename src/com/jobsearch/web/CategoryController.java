package com.jobsearch.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.jobsearch.category.service.CategoryServiceImpl;

@Controller
@SessionAttributes({ "user", "app" })
public class CategoryController {

	@Autowired
	CategoryServiceImpl categoryService;
}
