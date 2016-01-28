package com.jobsearch.category.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.jobsearch.category.service.Category;

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
	
	public void addCategoryToJob(int jobId, int categoryId) {
		String sql;
		sql = "INSERT INTO job_category (JobId, CategoryId)" + " VALUES (?, ?)";

		jdbcTemplate.update(sql, new Object[] { jobId, categoryId });

	}
	
	public void addCategoryToUser(int userId, int categoryId) {

		String sql;
		sql = "insert into user_category (UserID, CategoryID) values (?, ?)";
		jdbcTemplate.update(sql, new Object[] { userId, categoryId });

	}
	
	public void deleteCategoryFromUser(int userId, int categoryId) {

		String sql;
		sql = "DELETE FROM user_category" + " WHERE userId = ?" + " AND categoryId = ?";

		jdbcTemplate.update(sql, new Object[] { userId, categoryId });
	}

	public List<Category> getCategoriesByJobId(int jobId) {

		// Given a job ID, get all category objects
		String sql = "SELECT *" + " FROM category" + " INNER JOIN job_category"
				+ " ON category.CategoryID = job_category.CategoryId" + " AND job_category.JobId = ?";

		return (List<Category>) this.CategoryRowMapper(sql, new Object[] { jobId });
	}

	public List<Category> getCategoriesByUserId(int userId) {

		// Given a user ID, get all category objects
		String sql = "SELECT *" + " FROM category" + " INNER JOIN user_category"
				+ " ON category.CategoryID = user_category.CategoryID" + " AND user_category.UserID = ?";

		return this.CategoryRowMapper(sql, new Object[] { userId });
	}
	

	public List<Category> getAppCategories() {

		String sql = "SELECT * " + " FROM category";

		return this.CategoryRowMapper(sql, null);
	}
}
