package com.jobsearch.user.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.print.attribute.standard.JobOriginatingUserName;

import org.apache.tools.ant.taskdefs.optional.ejb.JbossDeploymentTool;
import org.objenesis.instantiator.android.AndroidSerializationInstantiator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ArgumentPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.jobsearch.model.Item;
import com.jobsearch.model.Profile;
import com.jobsearch.user.service.JobSearchUser;
import com.jobsearch.category.service.Category;
import com.jobsearch.job.service.Job;
import com.jobsearch.model.App;

@Repository
public class UserRepository {

	@Autowired
	JdbcTemplate jdbcTemplate;

	public JobSearchUser getUser(int userId) {

		/*
		 * String sql2 =
		 * "insert into user (UserId, FirstName, LastName, Email)  values ( 4 ,' Alice', 'Anderson', 'ee')"
		 * ;
		 * 
		 * jdbcTemplate.update(sql2);
		 */

		String sql = "Select * from User wherae userId = " + userId;

		return jdbcTemplate.query(sql, new ResultSetExtractor<JobSearchUser>() {

			@Override
			public JobSearchUser extractData(ResultSet rs) throws SQLException, DataAccessException {
				if (rs.next()) {
					JobSearchUser user = new JobSearchUser();
					user.setUserId(rs.getInt("UserId"));
					user.setFirstName(rs.getString("FirstName"));
					user.setEmailAddress(rs.getString("Email"));
					user.setLastName(rs.getString("LastName"));
					return user;
				}

				return null;
			}

		});

	}

	public void createUser(JobSearchUser user) {

		String sql = "insert into user (FirstName, LastName, Email, ProfileId)  values (?, ?, ?, ?)";

		jdbcTemplate.update(sql,
				new Object[] { user.getFirstName(), user.getLastName(), user.getEmailAddress(), user.getProfileId() });

		/*
		 * @Override public JobSearchUser extractData(ResultSet rs) throws
		 * SQLException, DataAccessException { if (rs.next()) { JobSearchUser
		 * user = new JobSearchUser(); user.setUserId(rs.getInt("UserId"));
		 * user.setFirstName(rs.getString("FirstName"));
		 * user.setEmailAddress(rs.getString("Email"));
		 * user.setLastName(rs.getString("LastName")); return user; }
		 * 
		 * return null; }
		 */

	}

	// This should be combined with the sql, args method...
	// ******************************************************************************************************
	public List<JobSearchUser> JobSearchUserRowMapper(String sql, String email) {
		return jdbcTemplate.query(sql, new Object[] { email }, new RowMapper<JobSearchUser>() {
			@Override
			public JobSearchUser mapRow(ResultSet rs, int rownumber) throws SQLException {
				JobSearchUser e = new JobSearchUser();
				e.setUserId(rs.getInt(1));
				e.setFirstName(rs.getString(2));
				e.setLastName(rs.getString(3));
				e.setEmailAddress(rs.getString(4));
				e.setProfileId(rs.getInt(5));
				return e;
			}
		});
	}
	// ******************************************************************************************************

	public List<JobSearchUser> JobSearchUserRowMapper(String sql, Object[] args) {
		return jdbcTemplate.query(sql, args, new RowMapper<JobSearchUser>() {
			@Override
			public JobSearchUser mapRow(ResultSet rs, int rownumber) throws SQLException {
				JobSearchUser e = new JobSearchUser();
				e.setUserId(rs.getInt(1));
				e.setFirstName(rs.getString(2));
				e.setLastName(rs.getString(3));
				e.setEmailAddress(rs.getString(4));
				e.setProfileId(rs.getInt(5));
				return e;
			}
		});
	}

	public List<Profile> ProfilesRowMapper(String sql, Object[] args) {

		List<Profile> list;

		list = jdbcTemplate.query(sql, args, new RowMapper<Profile>() {

			@Override
			public Profile mapRow(ResultSet rs, int rownumber) throws SQLException {
				Profile e = new Profile();
				e.setId(rs.getInt(1));
				e.setName(rs.getString(2));
				e.setName2(rs.getString(3));
				return e;
			}
		});

		return list;
	}

	public ArrayList<Profile> ProfilesRowMapper(String sql) {

		List<Profile> list;

		list = jdbcTemplate.query(sql, new RowMapper<Profile>() {

			@Override
			public Profile mapRow(ResultSet rs, int rownumber) throws SQLException {
				Profile e = new Profile();
				e.setId(rs.getInt(1));
				e.setName(rs.getString(2));
				e.setName2(rs.getString(3));
				return e;
			}
		});

		return (ArrayList<Profile>) list;
	}

	// public List<JobSearchUser> UserCategoryRowMapper(String sql, String
	// email){
	//
	// return jdbcTemplate.query(sql, new Object[]{email}, new
	// RowMapper<JobSearchUser>(){
	// @Override
	// public JobSearchUser mapRow(ResultSet rs, int rownumber) throws
	// SQLException {
	// JobSearchUser e = new JobSearchUser();
	// e.setUserId(rs.getInt(1));
	// e.setFirstName(rs.getString(2));
	// e.setLastName(rs.getString(3));
	// return e;
	// }
	// });
	// }
	public List<Category> UserCategoriesRowMapper(String sql, Object[] args) {

		List<Category> list;
		list = jdbcTemplate.query(sql, args, new RowMapper<Category>() {

			@Override
			public Category mapRow(ResultSet rs, int rownumber) throws SQLException {
				Category e = new Category();
				e.setId(rs.getInt(3));
				return e;
			}
		});

		// System.out.println(list.get(0));

		return list;

	}

//	public List<Item> JobsCategoriesRowMapper(String sql, Object[] args) {
//
//		List<Item> list;
//		list = jdbcTemplate.query(sql, args, new RowMapper<Item>() {
//
//			@Override
//			public Item mapRow(ResultSet rs, int rownumber) throws SQLException {
//				Item e = new Item();
//				e.setJobId(rs.getInt(2));
//				e.setCategoryId(rs.getInt(3));
//				return e;
//			}
//		});
//
//		// System.out.println(list.get(0));
//
//		return list;
//
//	}

	// public List<JobSearchUser> EmployeesCategoriesRowMapper(String sql,
	// Object[] args){
	//
	// List<JobSearchUser> list;
	// list = jdbcTemplate.query(sql, args, new RowMapper<JobSearchUser>(){
	//
	// @Override
	// public JobSearchUser mapRow(ResultSet rs, int rownumber) throws
	// SQLException {
	// JobSearchUser e = new JobSearchUser();
	// e.setFirstName(rs.getString(1));
	// e.setLastName(rs.getString(2));
	// e.setEmailAddress(rs.getString(3));
	// return e;
	// }
	// });
	//
	//
	// //System.out.println(list.get(0));
	//
	// return list;
	//
	// }


	// public void setUserCats(JobSearchUser user, String[] cats, App app) {
	//
	// List<Category> userCats;
	// userCats = user.getCategories();
	//
	//
	// for(String strCat : cats){
	// user.addCategory(app.getCategoryByName(strCat));
	// }
	//
	// for(Category cat : user.getCategories()){
	// System.out.println(cat.getName());
	// }
	//
	//
	// }

	public void exportUsersCats(JobSearchUser user) {

		for (Category cat : user.getCategories()) {
			String sql = "insert into usercategories (UserID, CategoryID)  values (?, ?)";
			jdbcTemplate.update(sql, new Object[] { user.getUserId(), cat.getId() });

		}

	}

	public JobSearchUser getUserByEmail(String email) {
		String sql;
		sql = "select * from user where Email = ?";

		// Object args[0] = null;
		// args[0] = user.getEmailAddress();

		List<JobSearchUser> list = JobSearchUserRowMapper(sql, email);

		return list.get(0);

	}

	public void setUsersId(JobSearchUser user) {

		// From the user table, get the ID associated with the user's email
		user.setUserId(getUserByEmail(user.getEmailAddress()).getUserId());

	}

	public void getUserByEmail(JobSearchUser user) {
		String sql;
		sql = "select * from user where Email = ?";

		List<JobSearchUser> list = JobSearchUserRowMapper(sql, user.getEmailAddress());

		user = list.get(0);

	}

	public void deleteUserCategory(int userId, int categoryId) {

		String sql;
		sql = "DELETE FROM usercategories" + " WHERE userId = ?" + " AND categoryId = ?";

		jdbcTemplate.update(sql, new Object[] { userId, categoryId });
	}

	public List<Category> getUserCatergories(int userId, int categoryId) {

		String sql;
		sql = "select * from usercategories where UserID = ? and CategoryID = ?";

		return this.UserCategoriesRowMapper(sql, new Object[] { userId, categoryId });

	}

	public void addUserCategory(int userId, int categoryId) {

		String sql;
		sql = "insert into usercategories (UserID, CategoryID) values (?, ?)";
		jdbcTemplate.update(sql, new Object[] { userId, categoryId });

	}

	// public void exportJobCategory(DataBaseItem item) {
	// String sql;
	// sql = "insert into jobscategories (JobId, CategoryId) values (?, ?)";
	// jdbcTemplate.update(sql, new Object[]{item.getJobId(),
	// item.getCategoryId()});
	//
	//
	// }

	// @SuppressWarnings("null")
	// public ArrayList<Category> getCategoriesForJob(Job job) {
	//
	// //Need category Ids associated with job Id.
	// //Return a list a db items.
	// String sql = "select * from jobscategories where JobId = ?";
	// Object[] args = {job.getId()};
	// List<DataBaseItem> items = this.JobsCategoriesRowMapper(sql, args);
	//
	// //Use the db items' category id property to get category objects
	// ArrayList<Category> cats = new ArrayList<Category>();
	// List<Category> returnedCats;
	// sql = "select * from categories where CategoryID = ?";
	// for (DataBaseItem item : items){
	//
	// //Get the category object associated with the db item's category id
	// returnedCats = this.CategoryRowMapper(sql, new Object[]
	// {item.getCategoryId()});
	// cats.add(returnedCats.get(0));
	//
	//
	// }
	//
	// return cats;
	// }

	public ArrayList<JobSearchUser> getUsers(int categoryId, int profileIdNotToInclude) {

		// **************************************************
		// Should this select statement dynamically created to capture all table
		// column names?
		// Reason: because this is selecting all columns from the user table, if
		// additional columns
		// are added to the user table, this select string will need to be
		// updated.
		// **************************************************

		// Given a category ID, get all user objects.
		String sql = "SELECT user.UserId, user.FirstName, user.LastName, user.Email, user.ProfileId" + " FROM user"
				+ " INNER JOIN usercategories" + " ON user.UserId = usercategories.UserID"
				+ " AND usercategories.CategoryID = ?" + " AND user.ProfileId <> ?";

		return (ArrayList<JobSearchUser>) this.JobSearchUserRowMapper(sql,
				new Object[] { categoryId, profileIdNotToInclude });
	}

	public ArrayList<JobSearchUser> getApplicants(int jobId) {

		String sql = "SELECT user.UserId, user.FirstName, user.LastName, user.Email, user.ProfileId" + " FROM user"
				+ " INNER JOIN applications" + " ON user.UserId = applications.UserId" + " INNER JOIN jobs"
				+ " ON applications.JobId = jobs.Id" + " AND applications.JobId = ?" + " AND jobs.IsActive = 1";

		return (ArrayList<JobSearchUser>) this.JobSearchUserRowMapper(sql, new Object[] { jobId });
	}

	public ArrayList<JobSearchUser> getEmpolyees(int jobId) {
		String sql = "SELECT user.UserId, user.FirstName, user.LastName, user.Email, user.ProfileId" + " FROM user"
				+ " INNER JOIN employment" + " ON user.UserId = employment.UserId" + " AND employment.JobId = ?";

		return (ArrayList<JobSearchUser>) this.JobSearchUserRowMapper(sql, new Object[] { jobId });
	}

	public void hireApplicant(int userId, int jobId) {
		String sql = "INSERT INTO employment (UserId, JobId)" + " VALUES (?, ?)";

		jdbcTemplate.update(sql, new Object[] { userId, jobId });

	}

	public Profile getProfile(int profileId) {
		String sql = "SELECT *" + " FROM profiles" + "	WHERE id = ?";

		List<Profile> profiles = this.ProfilesRowMapper(sql, new Object[] { profileId });

		return profiles.get(0);
	}


	public ArrayList<JobSearchUser> getEmployeesByCategory(int categoryId) {
		String sql = "SELECT *" + " FROM user" + "	INNER JOIN usercategories"
				+ "	ON user.UserId = usercategories.userID" + " AND user.ProfileId = 2"
				+ " AND usercategories.CategoryID = ?";

		return (ArrayList<JobSearchUser>) this.JobSearchUserRowMapper(sql, new Object[] { categoryId });
	}

	public ArrayList<Profile> getProfiles() {
		String sql = "SELECT *" + " FROM profiles";
		return this.ProfilesRowMapper(sql);
	}

}
