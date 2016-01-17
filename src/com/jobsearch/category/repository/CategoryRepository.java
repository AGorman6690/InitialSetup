package com.jobsearch.category.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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

	public List<Category> getCategories() {

		String sql = "SELECT * " + " FROM categories";

		return this.CategoryRowMapper(sql, null);
	}

	public Category getCategory(int categoryId) {
		String sql = "SELECT *" + " FROM categories" + "	WHERE CategoryID = ?";

		return this.CategoryRowMapper(sql, new Object[] { categoryId }).get(0);
	}

	public ArrayList<Category> getCategoriesByJobId(int jobId) {

		// Given a job ID, get all category objects
		String sql = "SELECT categories.CategoryID, categories.Name" + " FROM categories" + " INNER JOIN jobscategories"
				+ " ON categories.CategoryID = jobscategories.CategoryId" + " AND jobscategories.JobId = ?";

		return (ArrayList<Category>) this.CategoryRowMapper(sql, new Object[] { jobId });
	}

	public List<Category> getCategoriesByUserId(int userId) {

		// Given a user ID, get all category objects
		String sql = "SELECT *" + " FROM categories" + " INNER JOIN usercategories"
				+ " ON categories.CategoryID = usercategories.CategoryID" + " AND usercategories.UserID = ?";

		return (ArrayList<Category>) this.CategoryRowMapper(sql, new Object[] { userId });
	}
}
