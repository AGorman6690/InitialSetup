package com.jobsearch.category.service;

public class SubCategoryRequestDTO {
	
	public int categoryId;
	public int subCategoryId;
	public String subCategoryName;
	
	//When A is the categoryId and B is the subCategoryId:
	//Example: If A has sub category B, and B has sub categories C and D,
	//this this equals 2 (C and D)
	public int subSubCategoryCount;
	
	public int getSubSubCategoryCount() {
		return subSubCategoryCount;
	}
	public void setSubSubCategoryCount(int subSubCategoryCount) {
		this.subSubCategoryCount = subSubCategoryCount;
	}
	public int getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}
	public int getSubCategoryId() {
		return subCategoryId;
	}
	public void setSubCategoryId(int subCategoryId) {
		this.subCategoryId = subCategoryId;
	}
	public String getSubCategoryName() {
		return subCategoryName;
	}
	public void setSubCategoryName(String subCategoryName) {
		this.subCategoryName = subCategoryName;
	}

}
