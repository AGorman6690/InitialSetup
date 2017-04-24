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
import com.jobsearch.job.service.JobServiceImpl;
import com.jobsearch.model.EmployeeSearch;
import com.jobsearch.model.Endorsement;
import com.jobsearch.model.JobSearchUser;
import com.jobsearch.model.Profile;
import com.jobsearch.model.RateCriterion;
import com.jobsearch.model.WorkDay;
import com.jobsearch.user.service.UserServiceImpl;
import com.jobsearch.user.web.AvailabilityDTO;
import com.jobsearch.utilities.VerificationServiceImpl;

@Repository
public class UserRepository {

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	UserServiceImpl userService;

	@Autowired
	JobServiceImpl jobService;
	
	@Autowired
	VerificationServiceImpl verificationService;
	
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
		// *******************************************************
		// *******************************************************
		// Why are there two row mappers for job search user????
		// Address this later.
		// *******************************************************
		// *******************************************************
		
		
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
				e.setAbout(rs.getString("About"));
				return e;
			}
		});
	}

	public List<JobSearchUser> JobSearchUserProfileRowMapper(String sql, Object[] args) {
		//*******************************************************
		// *******************************************************
		// Why are there two row mappers for job search user????
		// Address this later.
		// *******************************************************
		// *******************************************************
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
					e.setAbout(rs.getString("About"));
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
				
				e.setRateCriterionId(rs.getInt("RateCriterionId"));
				e.setName(rs.getString("Name"));
				e.setIsUsedToRateEmployee(rs.getBoolean("IsUsedToRateEmployee"));
				e.setShortName(rs.getString("ShortName"));
				
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

		String sql = "SELECT * FROM user u"
					+ " INNER JOIN employment e ON e.UserId = u.UserId"
					+ " AND e.JobId = ?"
					+ " AND e.WasTerminated = 0";

		return (ArrayList<JobSearchUser>) this.JobSearchUserRowMapper(sql, new Object[] { jobId });
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
		String sql = "UPDATE rating SET Value = ?"
					+ " WHERE RateCriterionId = ?"
					+ " AND UserId = ?"
					+ " AND RatedByUserId = ?"
					+ " AND JobId = ?";

		jdbcTemplate.update(sql,
				new Object[] { rc.getValue(),
						rc.getRateCriterionId(),
						rc.getUserId_ratee(),
						rc.getUserId_rater(),
						rc.getJobId() });

	}

	public List<RateCriterion> getRatingCriteia_toRateEmployee() {
		String sql = "SELECT * FROM ratecriterion WHERE IsUsedToRateEmployee = 1 ORDER BY DisplayOrder ASC";
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

	public void addComment(int userId, int jobId, String comment, int userId_commenter) {
		String sql = "INSERT INTO comment (JobId, UserId, Comment, UserId_Commenter) VALUES (?, ?, ?, ?)";

		jdbcTemplate.update(sql, new Object[] { jobId, userId, comment, userId_commenter });

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
		
		String sql = "SELECT AVG(Value) FROM rating"
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


	public List<String> getAvailableDays_byWorkDays(int userId, List<WorkDay> workDays) {
		
		// ***********************************************
		// ***********************************************
		// "availability" table needs to be changed to "unavailability"
		// ***********************************************
		// ***********************************************
		
		String sql = "SELECT Date FROM  date d"
						+ " INNER JOIN availability a ON a.DateId = d.Id"
//						+ " INNER JOIN employment e ON e.DateId = a.DateId"
						+ " WHERE a.UserId = ?"
//						+ " WHERE e.UserId = ?"
						+ " AND a.DateId NOT IN ("
						+ "	SELECT wd.DateId FROM work_day wd"
						+ " INNER JOIN employment e ON e.JobId = wd.JobId"
						+ " WHERE e.UserId = ?"
						+ " )"
						+ " AND (";
		
		List<Object> args = new ArrayList<Object>();
		args.add(userId);
		args.add(userId);
		
		boolean isFirst = true;
		for(WorkDay workDay : workDays){
			
			if(!isFirst) sql += " OR";
			sql += " d.Date = ?";
			
			args.add(workDay.getStringDate());
			
			isFirst = false;
		}
		sql += ")";
		
		
		return jdbcTemplate.queryForList(sql, args.toArray(), String.class);
	}


	public List<JobSearchUser> getUsers_ByFindEmployeesSearch(EmployeeSearch employeeSearch) {

		
		// *******************************************************************
		// *******************************************************************
		// NOTE: If the employee search is no longer considering whether a job allows partial availability,
		// and only considers the selected days, then this query can be simplified.
		// Review this.
		// *******************************************************************
		// *******************************************************************
		
		
		
		// **********************************
		// ADD NOTE ABOUT EXCLUDING AND INCLUDIND APPLICANTS FOR A PARTICULAR JOB	
		// Summary:
		// 1) The sub query for the distance between each employee's home location
		// 		and the requested filter location is required.
		// 2) The sub query for availability is optional.
		//		2a) If availability is specified, then it is ensured that 
		//			each potential employee's employment does not conflict
		//			with the specified days in the search
		//		2b) Note the query differences for whether partial availability is allowed  
		// 3) The sub query for categories is optional.
		// **********************************

		// ***************************************************
		// Main query
		// ***************************************************
		String sql = "SELECT * FROM user u";
		List<Object> argsList = new ArrayList<Object>();
				


		if(employeeSearch.getJobId_onlyIncludeApplicantsOfThisJob_butExcludeApplicantsOnTheseWorkDays() != null){
			sql += " JOIN application ap on u.UserId = ap.UserId AND ap.JobId = ?";
			argsList.add(employeeSearch.getJobId_onlyIncludeApplicantsOfThisJob_butExcludeApplicantsOnTheseWorkDays());
		}	
		
		// ********************************************
		// Distance sub query
		// ********************************************	
		// This returns all user ids having a distance between the user's
		// home location and job location that is less than or equal to the
		// user's max-distance-willing-to-travel.
		// This sub query is always required.		
		sql += " WHERE ( 3959 * acos( cos( radians(?) ) * cos( radians( u.HomeLat ) ) * cos( radians( u.HomeLng ) - radians(?) ) "
										+ "+ sin( radians(?) ) * sin( radians( u.HomeLat ) ) ) ) <= u.MaxWorkRadius ";

		argsList.add(employeeSearch.getJobDto().getJob().getLat());
		argsList.add(employeeSearch.getJobDto().getJob().getLng());
		argsList.add(employeeSearch.getJobDto().getJob().getLat());

		
		// ********************************************
		// Availability sub query
		// ********************************************	
		String subQuery_Dates = null;
		if (verificationService.isListPopulated(employeeSearch.getJobDto().getWorkDays())) {
			
			// Start the availability sub query
			subQuery_Dates = " AND u.UserId NOT IN (";

			// *************************************
			// UPDATE THIS NOTE TO REFLECT UNAVAILABILITY, NOT AVAILABILITY
			// If Partial availability is allowed.
			// A user id is selected if the user has availability
			// on **AT LEAST 1** of the requested days.
			// *************************************				
			subQuery_Dates += " SELECT DISTINCT(u3.UserId) FROM user u3";		
			
			int i = 0;
			for(WorkDay workDay : employeeSearch.getJobDto().getWorkDays()){
									
					subQuery_Dates += " JOIN availability a" + i
									+ " ON u3.UserId = a" + i + ".UserId"
									+ " AND a" + i + ".DateId = ?";						
//					}

				i += 1;							
				argsList.add(jobService.getDateId(workDay.getStringDate()));
			}

			subQuery_Dates += " UNION"; 

			// **************************************************
			// Sub query to verify employment does not conflict with requested days.
			// Only exclude the user ids that have employment on **ALL** requested days.
			// **************************************************
			subQuery_Dates += " SELECT DISTINCT(u4.UserId) FROM user u4";
			subQuery_Dates += " JOIN employment e ON u4.UserId = e.UserId";
			
			i = 0;
			for (WorkDay workDay : employeeSearch.getJobDto().getWorkDays()) {
				
				subQuery_Dates += " JOIN work_day wd" + i
						 			+ " ON e.JobId = wd" + i + ".JobId"
						 			+ " AND wd" + i + ".DateId = ?";					
				
				argsList.add(jobService.getDateId(workDay.getStringDate()));
				i += 1;
			}	
			
			// Exclude **ALL** applicants of this job
			if(employeeSearch.getJobDto().getJob().getId() != null){
				subQuery_Dates += " UNION";
				subQuery_Dates += " SELECT ap.UserId FROM application ap WHERE ap.JobId = ?";
				argsList.add(employeeSearch.getJobDto().getJob().getId());
			}
			
			
			// Exclude applicants who applied for a particular day.
			// *****************************
			// Note: currently this is only built for one work day.
			// *****************************
			if(employeeSearch.getJobId_onlyIncludeApplicantsOfThisJob_butExcludeApplicantsOnTheseWorkDays() != null &&
					employeeSearch.getJobDto().getWorkDays().size() > 0){
				subQuery_Dates += " UNION";
				subQuery_Dates += " SELECT DISTINCT(u5.userId) FROM user u5";
				subQuery_Dates += " JOIN application ap ON u5.userId = ap.userId";
				subQuery_Dates += " JOIN wage_proposal wp ON ap.ApplicationId = wp.ApplicationId";
				subQuery_Dates += " JOIN employment_proposal_work_day ep ON wp.WageProposalId = ep.EmploymentProposalId";
				subQuery_Dates += " JOIN work_day wd ON ep.WorkDayId = wd.WorkDayId";
				subQuery_Dates += " WHERE wp.IsCurrentProposal = 1";
				subQuery_Dates += " AND wd.DateId = ?";
				subQuery_Dates += " AND ap.JobId = ?";
				
				argsList.add(jobService.getDateId(employeeSearch.getJobDto().getWorkDays().get(0).getStringDate()));
				argsList.add(employeeSearch.getJobId_onlyIncludeApplicantsOfThisJob_butExcludeApplicantsOnTheseWorkDays());
				
			}
			
			subQuery_Dates += " ) "; // Close the sub query
							
		}

		// Build the query string		
//		sql_mainQuery += whereCondition_distance;
		if(subQuery_Dates != null) sql += subQuery_Dates;
		
		// close the sub queries
//		for (int i = 0; i < subQueryCount; i++) {
//			sql += ")";
//		}

		sql += " LIMIT 25";
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

	public List<RateCriterion> getRateCriteria_toRateEmployer() {
		String sql = "SELECT * FROM ratecriterion WHERE IsUsedToRateEmployee = 0 ORDER BY DisplayOrder ASC";
		return this.RateCriterionRowMapper(sql, new Object[]{});
	}

	public void insertRating(Integer rateCriterionId, int userIdToRate, Integer jobId, Integer ratedByUserId) {
		
		String sql = "INSERT INTO rating (RateCriterionId, UserId, JobId, Value, RatedByUserId)"
					+ " VALUES (?, ?, ?, ?, ?)";
		
		jdbcTemplate.update(sql, new Object[]{ rateCriterionId, userIdToRate, jobId,
												RateCriterion.VALUE_NOT_YET_RATED, ratedByUserId });
		
	}

	public void insertEmployment(int userId, int jobId) {
		
		String sql = "INSERT INTO employment ( UserId, JobId, WasTerminated ) VALUES( ?, ?, 0)";
		jdbcTemplate.update(sql, new Object[]{ userId, jobId } );
		
	}

	public Double getRating_byJobAndUser(Integer jobId, int userId) {
		String sql = "SELECT AVG(Value) FROM rating WHERE UserId = ? AND JobId = ? AND Value > -1";

		Double rating = jdbcTemplate.queryForObject(sql, new Object[] { userId, jobId }, Double.class);

		if (rating == null) return -1.0;
		else return rating;

	}

	public Double getRatingValue_byCriteriaAndUser(Integer rateCriterionId, int userId) {
		
		String sql = "SELECT AVG(Value) FROM rating WHERE UserId = ? AND RateCriterionId = ? AND Value > -1";

		Double rating = jdbcTemplate.queryForObject(sql, new Object[] { userId, rateCriterionId }, Double.class);

		if (rating == null) return null;
		else return rating;
	}

	public void updateAbout(int userId, String about) {
		String sql = "UPDATE user SET About = ? WHERE UserId = ?";
		jdbcTemplate.update(sql, new Object[]{ about, userId });
		
	}

}
