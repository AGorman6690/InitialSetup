package com.jobsearch.category.service;

public class CategoryNode {
	private int categoryId;
	private String categoryName;
	
	//List<Category>
	private CategoryNode leftNode;
	private CategoryNode centerNode;
	private CategoryNode rightNode;

	public CategoryNode(int categoryId, String categoryName) {
		this.categoryId = categoryId;
		this.categoryName = categoryName;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}

	public CategoryNode getLeftNode() {
		return leftNode;
	}

	public void setLeftNode(CategoryNode leftNode) {
		this.leftNode = leftNode;
	}

	public CategoryNode getCenterNode() {
		return centerNode;
	}

	public void setCenterNode(CategoryNode centerNode) {
		this.centerNode = centerNode;
	}

	public CategoryNode getRightNode() {
		return rightNode;
	}

	public void setRightNode(CategoryNode rightNode) {
		this.rightNode = rightNode;
	}
}
