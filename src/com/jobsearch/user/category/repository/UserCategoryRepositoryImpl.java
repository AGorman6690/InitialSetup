package com.jobsearch.user.category.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class UserCategoryRepositoryImpl {
	@Autowired
	JdbcTemplate jdbcTemplate;

	public void addUserCategory(int categoryID, int userID) {
		String sql = "INSERT INTO user_category (categoryID, userID) values (%d, %d)";
		jdbcTemplate.execute(
				String.format(sql, categoryID, userID));
	}
}
