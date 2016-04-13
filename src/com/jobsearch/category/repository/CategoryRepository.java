package com.jobsearch.category.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.jobsearch.category.service.Category;
import com.jobsearch.category.service.CategoryServiceImpl;
import com.jobsearch.model.Endorsement;

@Repository
public class CategoryRepository {

	@Autowired
	JdbcTemplate jdbcTemplate;

	public List<Category> CategoryRowMapper(String sql, Object[] args) {
		return jdbcTemplate.query(sql, args, new RowMapper<Category>() {

			@Override
			public Category mapRow(ResultSet rs, int rownumber) throws SQLException {
				Category e = new Category();
				e.setId(rs.getInt(1));
				e.setName(rs.getString(2));
				return e;
			}
		});
	}
	
		
	public void addCategoryToUser(int userId, int categoryId) {

		String sql;
		sql = "INSERT INTO user_category (UserId, CategoryId) VALUES (?, ?)";
		jdbcTemplate.update(sql, new Object[] { userId, categoryId });

	}
	
	public void deleteCategoriesFromUser(int userId) {

		String sql;
		sql = "DELETE FROM user_category" + " WHERE UserId = ?";

		jdbcTemplate.update(sql, new Object[] { userId });
	}

	public List<Category> getCategoriesByJobId(int jobId) {

		String sql = "SELECT *" + " FROM category" + " INNER JOIN job_category"
				+ " ON category.CategoryID = job_category.CategoryId" + " AND job_category.JobId = ?";

		return (List<Category>) this.CategoryRowMapper(sql, new Object[] { jobId });
	}
	
	public Category getCategoryByJobId(int jobId) {

		String sql = "SELECT *" + " FROM category" + " INNER JOIN job_category"
				+ " ON category.CategoryID = job_category.CategoryId" + " AND job_category.JobId = ?";

		List<Category> categories = this.CategoryRowMapper(sql, new Object[] { jobId });
		
		if (categories.size() > 0){
			return categories.get(0);
		}else{
			return null;
		}
		
	}

	public List<Category> getCategoriesByUserId(int userId) {

		String sql = "SELECT *" + " FROM category" + " INNER JOIN user_category"
				+ " ON category.CategoryID = user_category.CategoryID" + " AND user_category.UserID = ?";

		return this.CategoryRowMapper(sql, new Object[] { userId });
	}
	


	public List<Category> getSubCategories(int superCat) {
		
		String sql = "SELECT * FROM category WHERE SuperCategoryId = ?";

		return this.CategoryRowMapper(sql, new Object[]{ superCat });
	}

	public Category getCategory(int categoryId) {
		String sql = "SELECT * FROM category WHERE CategoryId = ?";
		return this.CategoryRowMapper(sql, new Object[] { categoryId }).get(0);
	}


}
