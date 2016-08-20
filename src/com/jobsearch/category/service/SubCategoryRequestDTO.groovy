package com.jobsearch.category.service

class SubCategoryRequestDTO {
	int categoryId
	int subCategoryId
	String subCategoryName

	//When A is the categoryId and B is the subCategoryId:
	//Example: If A has sub category B, and B has sub categories C and D,
	//this this equals 2 (C and D)
	int subSubCategoryCount
}
