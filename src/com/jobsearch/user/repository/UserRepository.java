package com.jobsearch.user.repository;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.jobsearch.application.service.Application;
import com.jobsearch.category.service.Category;
import com.jobsearch.google.Coordinate;
import com.jobsearch.job.service.JobDTO;
import com.jobsearch.job.service.JobServiceImpl;
import com.jobsearch.model.Endorsement;
import com.jobsearch.model.JobSearchUser;
import com.jobsearch.model.Profile;
import com.jobsearch.model.RateCriterion;
import com.jobsearch.model.WorkDay;
import com.jobsearch.user.service.UserServiceImpl;
import com.jobsearch.user.web.AvailabilityDTO;
import com.jobsearch.utilities.VerificationUtility;

@Repository
public class UserRepository {

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	UserServiceImpl userService;

	@Autowired
	JobServiceImpl jobService;
	
	public JobSearchUser getUser(int userId) {

		String sql = "select u.*, p.* from user u inner join profile p on p.profileId = u.profileId where userid = ?";

		return JobSearchUserProfileRowMapper(sql, new Object[] { userId }).get(0);

	}

	public JobSearchUser validateUser(int userId) {

		String sql = "Update user Set enabled = 1 where userId = ?";

		jdbcTemplate.update(sql, new Object[] { userId });

		return getUser(userId);
	}

	public JobSearchUser createUser(JobSearchUser user) {

		try {
			CallableStatement cStmt = jdbcTemplate.getDataSource().getConnection()
					.prepareCall("{call insertUser(?, ?, ?, ?, ?, ?)}");

			cStmt.setString(1, user.getFirstName());
			cStmt.setString(2, user.getLastName());
			cStmt.setString(3, user.getEmailAddress());
			cStmt.setInt(4, new Integer(1));
			cStmt.setString(5, user.getPassword());
			cStmt.setInt(6, user.getProfileId());

			ResultSet result = cStmt.executeQuery();

			JobSearchUser newUser = new JobSearchUser();
			Profile profile = new Profile();
			while (result.next()) {
				newUser = new JobSearchUser();
				newUser.setUserId(result.getInt("userId"));
				newUser.setFirstName(result.getString("firstName"));
				newUser.setLastName(result.getString("lastname"));
				newUser.setEmailAddress(result.getString("email"));

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

		jdbcTemplate.update(sql, new Object[] { user.getFirstName(), user.getLastName(), user.getEmailAddress(),
				new Integer(2), user.getPassword() });
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
				e.setHomeLat(rs.getFloat("HomeLat"));
				e.setHomeLng(rs.getFloat("HomeLng"));
				return e;
			}
		});
	}

	public List<JobSearchUser> JobSearchUserProfileRowMapper(String sql, Object[] args) {

		return jdbcTemplate.query(sql, args, new RowMapper<JobSearchUser>() {
			@Override
			public JobSearchUser mapRow(ResultSet rs, int rownumber) throws SQLException {
				
				try {
					JobSearchUser e = new JobSearchUser();
					e.setUserId(rs.getInt("UserId"));
					e.setFirstName(rs.getString("FirstName"));
					e.setLastName(rs.getString("LastName"));
					e.setEmailAddress(rs.getString("Email"));
					e.setProfileId(rs.getInt("ProfileId"));
					e.setHomeLat(rs.getFloat("HomeLat"));
					e.setHomeLng(rs.getFloat("HomeLng"));
					e.setHomeCity(rs.getString("HomeCity"));
					e.setHomeState(rs.getString("HomeState"));
					e.setHomeZipCode(rs.getString("HomeZipCode"));
					e.setMaxWorkRadius(rs.getInt("MaxWorkRadius"));
					e.setCreateNewPassword(rs.getInt("CreateNewPassword"));
					e.setMinimumDesiredPay(rs.getDouble("MinimumPay"));
					e.setStringMinimumDesiredPay(String.format("%.2f", rs.getDouble("MinimumPay")));

//					Profile profile = new Profile();
//					profile.setName(rs.getString("p.ProfileType"));

//					e.setProfile(profile);
					return e;
				} catch (Exception e) {
					return null;
				}

			}
		});
	}


	public ArrayList<Profile> ProfilesRowMapper(String sql) {

		List<Profile> list;

		list = jdbcTemplate.query(sql, new RowMapper<Profile>() {

			@Override
			public Profile mapRow(ResultSet rs, int rownumber) throws SQLException {
				Profile e = new Profile();
				e.setId(rs.getInt("ProfileId"));
				e.setName(rs.getString("ProfileType"));
				e.setAltName1(rs.getString("AltName1"));
				return e;
			}
		});

		return (ArrayList<Profile>) list;
	}

	public List<Category> UserCategoriesRowMapper(String sql, Object[] args) {

		List<Category> list;
		list = jdbcTemplate.query(sql, args, new RowMapper<Category>() {

			@Override
			public Category mapRow(ResultSet rs, int rownumber) throws SQLException {
				Category e = new Category();
				e.setId(rs.getInt(2));
				return e;
			}
		});

		// System.out.println(list.get(0));

		return list;

	}

	public List<RateCriterion> RateCriterionRowMapper(String sql, Object[] args) {

		List<RateCriterion> list;
		list = jdbcTemplate.query(sql, args, new RowMapper<RateCriterion>() {

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
		sql = "select u.*, p.* from user u inner join profile p on p.profileId = u.profileId where Email = ?";

		List<JobSearchUser> list = JobSearchUserProfileRowMapper(sql, new Object[] { email });

		if(list.size() >0 ){
			return list.get(0);
		}
		else{
			return null;
		}


	}

	public void setUsersId(JobSearchUser user) {

		// From the user table, get the ID associated with the user's email
		user.setUserId(getUserByEmail(user.getEmailAddress()).getUserId());

	}

	public boolean hasCategory(int userId, int categoryId) {

		String sql;
		sql = "select * from user_category where UserId = ? and CategoryId = ?";

		List<Category> categories = this.UserCategoriesRowMapper(sql, new Object[] { userId, categoryId });

		if (categories.size() > 0) {
			return true;
		} else {
			return false;
		}

	}

	public ArrayList<JobSearchUser> getApplicantsByJob_SubmittedOrConsidered(int jobId) {

		String sql = "SELECT *" + " FROM user" + " INNER JOIN application" + " ON user.UserId = application.UserId"
				+ " INNER JOIN job" + " ON application.JobId = job.JobId" + " AND application.JobId = ?"
				+ " WHERE application.Status = ? or application.Status = ?";

		return (ArrayList<JobSearchUser>) this.JobSearchUserRowMapper(sql, new Object[] { jobId, 
												Application.STATUS_SUBMITTED, Application.STATUS_CONSIDERED });
	}

	public ArrayList<JobSearchUser> getEmpolyeesByJob(int jobId) {

		String sql = "SELECT *" + " FROM user" + " INNER JOIN employment" + " ON user.UserId = employment.UserId"
				+ " AND employment.JobId = ?";

		return (ArrayList<JobSearchUser>) this.JobSearchUserRowMapper(sql, new Object[] { jobId });
	}

	public void hireApplicant(int userId, int jobId) {

		try {
			CallableStatement cStmt = jdbcTemplate.getDataSource().getConnection()
					.prepareCall("{call hire_applicant(?, ?)}");

			cStmt.setInt(1, userId);
			cStmt.setInt(2, jobId);

			cStmt.executeQuery();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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

	public void updateRating(RateCriterion rc) {
		String sql = "UPDATE rating SET Value = ? WHERE RateCriterionId = ? AND UserId = ? AND JobId = ?";

		jdbcTemplate.update(sql,
				new Object[] { rc.getValue(), rc.getRateCriterionId(), rc.getEmployeeId(), rc.getJobId() });

	}

	public List<RateCriterion> getRatingCriteria() {
		String sql = "SELECT * FROM ratecriterion";
		return this.RateCriterionRowMapper(sql, new Object[] {});
	}

	public void deleteEndorsements(int employeeId, int jobId) {
		String sql = "DELETE FROM endorsement WHERE UserId = ? AND JobId = ?";
		jdbcTemplate.update(sql, new Object[] { employeeId, jobId });

	}

	public void addEndorsement(Endorsement endorsement) {
		String sql = "INSERT INTO endorsement (JobId, UserId, CategoryId)" + " VALUES (?, ?, ?)";

		jdbcTemplate.update(sql,
				new Object[] { endorsement.getJobId(), endorsement.getUserId(), endorsement.getCategoryId() });

	}

	public void addComment(int userId, int jobId, String comment) {
		String sql = "INSERT INTO comment (JobId, UserId, Comment) VALUES (?, ?, ?)";

		jdbcTemplate.update(sql, new Object[] { jobId, userId, comment });

	}

	public void deleteComment(int jobId, int employeeId) {
		String sql = "DELETE FROM comment WHERE JobId = ? AND UserId = ?";

		jdbcTemplate.update(sql, new Object[] { jobId, employeeId });

	}

	public String getComment(int jobId, int userId) {
		String sql = "SELECT Comment FROM comment WHERE JobId = ? AND UserId = ?";

		String comment = "";
		try {
			comment = jdbcTemplate.queryForObject(sql, new Object[] { jobId, userId }, String.class);

		} catch (Exception e) {
			// TODO: handle exception
		}

		return comment;

	}

	public Double getRating(int userId) {
		String sql = "SELECT AVG(Value) FROM rating WHERE UserId = ? and Value > -1";

		Double rating = jdbcTemplate.queryForObject(sql, new Object[] { userId }, Double.class);

		if (rating == null) {
			return -1.0;
		} else {
			return rating;
		}
	}

	public List<Double> getRatingForJob(int userId, int jobId) {
		String sql = "SELECT Value FROM rating"
				+ " WHERE UserId = ?"
				+ " AND JobId = ?";
		
		return jdbcTemplate.queryForList(sql, new Object[] { userId, jobId }, Double.class);
	}
	

	public double getRatingValue_ByUserAndJob(int rateCriterionId, int userId, int jobId) {
		
		String sql = "SELECT Value FROM rating"
				+ " WHERE UserId = ?"
				+ " AND JobId = ?"
				+ " AND RateCriterionId = ?";
		
		Double d = jdbcTemplate.queryForObject(sql, new Object[] { userId, jobId, rateCriterionId }, Double.class);
		return jdbcTemplate.queryForObject(sql, new Object[] { userId, jobId, rateCriterionId }, Double.class);
	}

	public List<Integer> getEndorsementCategoryIds(int userId) {
		String sql = "SELECT CategoryId FROM endorsement"
				+ " WHERE UserId = ?"
				+ " GROUP BY CategoryId";
		
		return jdbcTemplate.queryForList(sql, new Object[] { userId }, Integer.class);
	}

	public int getEndorsementCountByCategory(int userId, int categoryId) {
		String sql = "SELECT COUNT(*) FROM endorsement WHERE UserId = ? AND CategoryId = ?";
		return jdbcTemplate.queryForObject(sql, new Object[] { userId, categoryId }, Integer.class);
	}

	public List<Integer> getEndorsementCategoryIdsByJob(int userId, int jobId) {
		String sql = "SELECT CategoryId FROM endorsement WHERE UserId = ? AND JobId = ? GROUP BY CategoryId";
		return jdbcTemplate.queryForList(sql, new Object[] { userId, jobId }, Integer.class);
	}

	public int getEndorsementCountByCategoryAndJob(int userId, int categoryId, int jobId) {
		String sql = "SELECT COUNT(*) FROM endorsement WHERE UserId = ? AND CategoryId = ? AND JobId = ?";
		return jdbcTemplate.queryForObject(sql, new Object[] { userId, categoryId, jobId }, Integer.class);
	}

	public void deleteAvailability(int userId) {
		String sql = "DELETE FROM availability WHERE UserId = ?";
		jdbcTemplate.update(sql, new Object[] { userId });

	}

	public void addAvailability(AvailabilityDTO availabilityDto){ //Date sqlDate) {
		
		// *************************************
		// See note in userService.updateAvailability
		// *************************************
		
		String sql = "INSERT INTO availability (UserId, DateId) VALUES";
		ArrayList<Object> args = new ArrayList<Object>();
		
		boolean isFirst = true;
		for(String date : availabilityDto.getStringDays()){
			
			if(!isFirst) sql += ",";			
			
			sql += " (?, ?)";

			
			isFirst = false;
			args.add(availabilityDto.getUserId());
			args.add(jobService.getDateId(date));
		}
		
		sql += " ON DUPLICATE KEY UPDATE UserId = UserId";
		
		jdbcTemplate.update(sql, args.toArray());

	}

	public List<String> getAvailableDays(int userId) {
		String sql = "SELECT Date FROM  date d"
						+ " INNER JOIN availability a ON a.DateId = d.Id"
						+ " WHERE UserId = ?";
		return jdbcTemplate.queryForList(sql, new Object[] { userId }, String.class);
	}


	public List<JobSearchUser> getUsers_ByFindEmployeesSearch(JobDTO jobDto) {

		// **********************************
		// Summary:
		// 1) The sub query for the distance between each employee's home location
		// 		and the filter location is required.
		// 2) The sub query for availability is optional.
		//		2a) If availability is specified, then it is ensured that 
		//			each potential employee's employment does not conflict
		//			with the specified days in the search
		//		2b) Note the query differences for whether partial availability is allowed  
		// 3) The sub query for categories is optional.
		// **********************************

		String sql = "SELECT * FROM user u WHERE u.UserId IN";
		List<Object> argsList = new ArrayList<Object>();


		int subQueryCount = 1; // 1 because the distance sub query is required

		// ********************************************
		// Availability sub query
		// ********************************************		
		String subQueryDates = null;
		if (VerificationUtility.isListPopulated(jobDto.getWorkDays())) {

			// *************************************
			// If Partial availability is allowed
			// *************************************
			if(jobDto.getJob().getIsPartialAvailabilityAllowed()){
				
				subQueryDates = " ( SELECT DISTINCT(a.UserId) FROM availability a WHERE (";
				Boolean isFirst = true;
				
				for (WorkDay workDay : jobDto.getWorkDays()) {
					
					if(!isFirst) subQueryDates += " OR";
					
					subQueryDates += " a.DateId = ?";
					
					argsList.add(jobService.getDateId(workDay.getStringDate()));
					
					isFirst = false;
				}	
				
				// close the WHERE clause
				subQueryDates += ")";
				
				// Start the sub query to exclude the user ids with conflicting employment
				subQueryDates += " AND a.UserId NOT IN ("; 
				
				// **************************************************
				// Sub query to verify employment does not conflict with requested days.
				// Only exclude the user ids that have employment on **ALL** requested days.
				// **************************************************
				subQueryDates += " SELECT DISTINCT(u3.UserId) FROM user u3";
				subQueryDates += " INNER JOIN employment e ON e.UserId = u3.UserId";
				
				int i = 0;
				for (WorkDay workDay : jobDto.getWorkDays()) {
					
					subQueryDates += " INNER JOIN work_day wd" + i
							 			+ " ON e.JobId = wd" + i + ".JobId"
							 			+ " AND wd" + i + ".DateId = ?";					
					
					argsList.add(jobService.getDateId(workDay.getStringDate()));
					i += 1;
				}	
				
				subQueryDates += " AND u3.UserId IN ";
								
			}
			
			// *************************************
			// Else applicant must have ALL work days available 
			// *************************************
			else{
							
				subQueryDates = " (SELECT DISTINCT(a0.UserId) FROM availability a0";
	
				// See this SO post for sql details:
				// http://stackoverflow.com/questions/1054299/sql-many-to-many-table-and-query
				int i = 1;
				for(WorkDay workDay : jobDto.getWorkDays()){
					
					
					if(i == jobDto.getWorkDays().size()){
						
						// The WHERE clause must FOLLOW the JOINs
						subQueryDates += " WHERE a0.DateId = ?";
					}
					else{
						subQueryDates += " JOIN availability a" + i
										+ " ON a" + (i - 1) + ".UserId = a"	+ i + ".UserId"
										+ " AND a" + i + ".DateId = ?";						
					}

					i += 1;							
					argsList.add(jobService.getDateId(workDay.getStringDate()));
				}
	
				// subQueryDates += " AND a0.UserId IN "; 

				// Start the sub query to exclude the user ids with conflicting employment
				subQueryDates += " AND a0.UserId NOT IN ("; 
				
				// **************************************************
				// Sub query to verify employment does not conflict with requested days.
				// Only exclude the user ids that have employment on **ALL** requested days.
				// **************************************************
				subQueryDates += " SELECT DISTINCT(u3.UserId) FROM user u3";
				subQueryDates += " INNER JOIN employment e ON e.UserId = u3.UserId";	
				subQueryDates += " INNER JOIN work_day wd ON e.JobId = wd.JobId";	
				subQueryDates += " WHERE (";
				Boolean isFirst = true;
				
				for (WorkDay workDay : jobDto.getWorkDays()) {
					
					if(!isFirst) subQueryDates += " OR";
					
					subQueryDates += " wd.DateId = ?";
					
					argsList.add(jobService.getDateId(workDay.getStringDate()));
					
					isFirst = false;
				}	
				
				// close the WHERE clause
				subQueryDates += ")";
				
				subQueryDates += " AND u3.UserId IN ";				
				
				
			}
			
			sql += subQueryDates;
			subQueryCount += 2; // 1 for the availability sub query, 1 for the employment sub query

		}

		// ********************************************
		// Category sub query
		// ********************************************	
		// Build the sub query for categories.
		// This sub query has the same structure as the availability sub query.
		// This returns all user ids that have ALL the requested categories.
		
/*
		String subQueryCategories = null;
		if (findEmployeesDto.getCategoryIds() != null) {

			subQueryCategories = " (SELECT uc0.UserId FROM user_category uc0";

			for (int categoryCount = 1; categoryCount < findEmployeesDto.getCategoryIds().size(); categoryCount++) {
				int categoryCountMinus1 = categoryCount - 1;
				subQueryCategories += " JOIN user_category uc" + categoryCount + " ON uc" + categoryCountMinus1
						+ ".UserId" + " = uc" + categoryCount + ".UserId AND uc" + categoryCount + ".CategoryId = ?";
				argsList.add(findEmployeesDto.getCategoryIds().get(categoryCount));
			}

			subQueryCategories += " WHERE uc0.CategoryId = ? AND uc0.UserId IN ";
			argsList.add(findEmployeesDto.getCategoryIds().get(0));

			sql += subQueryCategories;
			subQueryCount += 1;

		}
*/

		// Distance sub query.
		// This returns all user ids having a distance between the user's
		// home location and job location that is less than or equal to the
		// user's max-distance-willing-to-travel.
		// This sub query is always required.
		String subQueryDistance = " ( SELECT u2.UserId FROM user u2"
				+ " WHERE ( 3959 * acos( cos( radians(?) ) * cos( radians( u2.HomeLat ) ) * cos( radians( u2.HomeLng ) - radians(?) ) "
				+ "+ sin( radians(?) ) * sin( radians( u2.HomeLat ) ) ) ) <= u2.MaxWorkRadius ";

		sql += subQueryDistance;

		argsList.add(jobDto.getJob().getLat());
		argsList.add(jobDto.getJob().getLng());
		argsList.add(jobDto.getJob().getLat());



		// Need to close the sub queries
		for (int i = 0; i < subQueryCount; i++) {
			sql += ")";
		}


		return this.JobSearchUserProfileRowMapper(sql, argsList.toArray());
	}

	public void createUsers_DummyData(List<JobSearchUser> users, int dummyCreationId) {

		try {
			CallableStatement cStmt = jdbcTemplate.getDataSource().getConnection()
					.prepareCall("{call insertUser_DummyData(?,?, ?, ?,?, ?, ?, ?, ?, ?, ?, ?)}");

			for (JobSearchUser user : users) {
				cStmt.setString(1, user.getFirstName());
				cStmt.setString(2, user.getLastName());
				cStmt.setString(3, user.getEmailAddress());
				cStmt.setString(4, user.getPassword());
				cStmt.setInt(5, user.getProfileId());
				cStmt.setFloat(6, user.getHomeLat());
				cStmt.setFloat(7, user.getHomeLng());
				cStmt.setInt(8, user.getMaxWorkRadius());
				cStmt.setInt(9, dummyCreationId);
				cStmt.setDouble(10, user.getRating());
				cStmt.setString(11, user.getHomeCity());
				cStmt.setString(12, user.getHomeState());

				cStmt.addBatch();
			}

			cStmt.executeBatch();
			// cStmt.executeQuery();

			cStmt.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {

			try {
				jdbcTemplate.getDataSource().getConnection().close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public int getLastDummyCreationId(String table) {
		String sql = "SELECT MAX(DummyCreationId) FROM " + table;
		return jdbcTemplate.queryForObject(sql, Integer.class);
	}




	public boolean resetPassword(String username, String newPassword) {
		String sql = "UPDATE user SET password = ?, createNewPassword = 1 WHERE email = ?";

		int rowsAffected = jdbcTemplate.update(sql, new Object[] { newPassword, username });

		if (rowsAffected == 1) {
			return true;
		}

		return false;
	}

	public void updatePassword(String password, String email) {
		String sql = "UPDATE user SET password = ?, createNewPassword = 0 WHERE email = ?";

		jdbcTemplate.update(sql, new Object[] { password, email });
	}

	public Double getRatingValue_ByCategory(int userId, int categoryId) {

		String sql = "SELECT Avg(r.value) FROM rating r"
				+ " INNER JOIN job j on j.JobId = r.JobId"
				+ " INNER JOIN job_category jc on jc.JobId = j.JobId"
				+ " AND r.UserId = ?"
				+ " AND r.Value != -1"
				+ " AND jc.CategoryId = ?";				
		
		return jdbcTemplate.queryForObject(sql, new Object[]{ userId, categoryId }, Double.class);

	}

	public void updateHomeLocation(JobSearchUser user_edited, Coordinate coordinate) {
		String sql = "UPDATE user SET HomeLat = ?, HomeLng = ?, HomeCity = ?, HomeState = ?,"
				+ " HomeZipCode = ? WHERE UserId = ?";

		jdbcTemplate.update(sql,
				new Object[] { coordinate.getLatitude(), coordinate.getLongitude(),
						user_edited.getHomeCity(), user_edited.getHomeState(),
						user_edited.getHomeZipCode(), user_edited.getUserId() });
		
	}

	public void updateMaxDistanceWillingToWork(int userId, Integer maxWorkRadius) {
		String sql = "UPDATE user SET MaxWorkRadius = ? WHERE UserId = ?";

		jdbcTemplate.update(sql,
				new Object[] { maxWorkRadius, userId });
	}

	public void updateMinimumDesiredPay(int userId, Double minimumDesiredPay) {
		String sql = "UPDATE user SET MinimumPay = ? WHERE UserId = ?";

		jdbcTemplate.update(sql,
				new Object[] { minimumDesiredPay, userId });
	}

}
