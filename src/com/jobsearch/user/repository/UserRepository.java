package com.jobsearch.user.repository;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.jobsearch.category.service.Category;
import com.jobsearch.model.Profile;
import com.jobsearch.model.RateCriterion;
import com.jobsearch.user.service.JobSearchUser;

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

		String sql = "Select * from User where userId = " + userId;

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


	//Delete????
	//***********************************************************
	public void createUser2(JobSearchUser user) {


		String insertUser = "insert into user (FirstName, LastName, Email, RoleId, password)  values (?, ?, ?, ?, ?)";

		jdbcTemplate.update(insertUser,
				new Object[] { user.getFirstName(), user.getLastName(), user.getEmailAddress(), 1, user.getPassword() });


	}
	//***********************************************************
	
	public JobSearchUser createUser(JobSearchUser user) {

		try {
			CallableStatement cStmt = jdbcTemplate.getDataSource().getConnection().prepareCall("{call insertUser(?, ?, ?, ?, ?, ?)}");

			 cStmt.setString(1, user.getFirstName());
			 cStmt.setString(2, user.getLastName());
			 cStmt.setString(3, user.getEmailAddress());
			 cStmt.setInt(4, new Integer(1));
			 cStmt.setString(5, user.getPassword());
			 cStmt.setInt(6, user.getProfileId());
			 
			 ResultSet result = cStmt.executeQuery();
			 
			 JobSearchUser newUser = new JobSearchUser();
			 Profile profile = new Profile();
			 while(result.next()){
				 newUser = new JobSearchUser();
				 newUser.setFirstName(result.getString("firstName"));
				 newUser.setFirstName(result.getString("lastname"));
				 newUser.setFirstName(result.getString("email"));
				 newUser.setFirstName(result.getString("roleId"));
				 
				 
				 profile.setId(result.getInt("profileId"));
				 profile.setName(result.getString("profiletype"));
				 
				 newUser.setProfile(profile);
				 
			 }
			 return newUser;
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	public void createAdmin(JobSearchUser user) {

		String sql = "insert into user (FirstName, LastName, Email, RoleId, password)  values (?, ?, ?, ?, ?)";

		jdbcTemplate.update(sql,
				new Object[] { user.getFirstName(), user.getLastName(), user.getEmailAddress(), new Integer(2), user.getPassword() });

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


	public List<JobSearchUser> JobSearchUserRowMapper(String sql, Object[] args) {
		return jdbcTemplate.query(sql, args, new RowMapper<JobSearchUser>() {
			@Override
			public JobSearchUser mapRow(ResultSet rs, int rownumber) throws SQLException {
				JobSearchUser e = new JobSearchUser();
				e.setUserId(rs.getInt(1));
				e.setFirstName(rs.getString(2));
				e.setLastName(rs.getString(3));
				e.setEmailAddress(rs.getString(4));
				e.setProfileId(rs.getInt(9));
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
	
	public List<RateCriterion> RateCriterionRowMapper(String sql, Object[] args){  
		
		List<RateCriterion> list;
		 list = jdbcTemplate.query(sql, args, new RowMapper<RateCriterion>(){  
		    
			 @Override  
		    public RateCriterion mapRow(ResultSet rs, int rownumber) throws SQLException {  
				 RateCriterion e = new RateCriterion();  
		        e.setRateCriterionId(rs.getInt(1));
		        e.setName(rs.getString(2));
		        return e;  
		    }  
		    });   		 
		 return (List<RateCriterion>) list;		 
	}


	public JobSearchUser getUserByEmail(String email) {
		String sql;
		sql = "select * from user where Email = ?";

		// Object args[0] = null;
		// args[0] = user.getEmailAddress();

		List<JobSearchUser> list = JobSearchUserRowMapper(sql, new Object[]{ email });

		return list.get(0);

	}

	public void setUsersId(JobSearchUser user) {

		// From the user table, get the ID associated with the user's email
		user.setUserId(getUserByEmail(user.getEmailAddress()).getUserId());

	}


	public List<Category> getUserCatergories(int userId, int categoryId) {

		String sql;
		sql = "select * from user_category where UserId = ? and CategoryId = ?";

		return this.UserCategoriesRowMapper(sql, new Object[] { userId, categoryId });

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
				+ " INNER JOIN user_category "
				+ " ON user.UserId = user_category.UserID"
				+ " AND user_category.CategoryID = ?" + " AND user.ProfileId <> ?";

		return (ArrayList<JobSearchUser>) this.JobSearchUserRowMapper(sql,
				new Object[] { categoryId, profileIdNotToInclude });
	}

	public ArrayList<JobSearchUser> getApplicants(int jobId) {

		String sql = "SELECT *" + " FROM user"
				+ " INNER JOIN application" + " ON user.UserId = application.UserId" + " INNER JOIN job"
				+ " ON application.JobId = job.JobId" + " AND application.JobId = ?" + " AND job.IsActive = 1";

		return (ArrayList<JobSearchUser>) this.JobSearchUserRowMapper(sql, new Object[] { jobId });
	}

	public ArrayList<JobSearchUser> getEmpolyeesByJob(int jobId) {
		

		String sql = "SELECT *" + " FROM user"
				+ " INNER JOIN application" + " ON user.UserId = application.UserId" 
				+ " AND application.JobId = ?" + " AND application.IsHired = 1";

		return (ArrayList<JobSearchUser>) this.JobSearchUserRowMapper(sql, new Object[] { jobId });
	}

	public void hireApplicant(int userId, int jobId) {
		String sql = "UPDATE application SET IsHired = 1 WHERE UserId = ? AND JobId = ?";

		jdbcTemplate.update(sql, new Object[] { userId, jobId });

	}

	public Profile getProfile(int profileId) {
		String sql = "SELECT *" + " FROM profile" + "	WHERE profileId = ?";

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
		String sql = "SELECT *" + " FROM profile";
		return this.ProfilesRowMapper(sql);
	}
	
	public List<RateCriterion> getAppRateCriteria() {
		String sql = "SELECT * FROM ratecriterion";
		return this.RateCriterionRowMapper(sql, new Object[] {});
	}

	public void rateEmployee(int rateCriterionId, int employeeId, int jobId, int value) {
		String sql = "INSERT INTO ratings (RateCriterionId, UserId, JobId, Value)"
				+ " VALUES(?, ?, ?, ?)";
		
		jdbcTemplate.update(sql, new Object[]{rateCriterionId, employeeId, jobId, value});
	}

}
