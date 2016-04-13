package com.jobsearch.user.repository;

import java.sql.CallableStatement;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.jobsearch.category.service.Category;
import com.jobsearch.job.service.Job;
import com.jobsearch.model.Endorsement;
import com.jobsearch.model.Profile;
import com.jobsearch.model.RateCriterion;
import com.jobsearch.user.rate.RatingDTO;
import com.jobsearch.user.service.EditProfileDTO;
import com.jobsearch.user.service.FindEmployeesDTO;
import com.jobsearch.user.service.JobSearchUser;
import com.jobsearch.user.service.UserServiceImpl;

@Repository
public class UserRepository {

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	UserServiceImpl userService;

	public JobSearchUser getUser(int userId) {

		String sql = "select u.*, up.* from user u inner join user_profile up on u.userid = up.userid where u.userid = ?";

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
				return e;
			}
		});
	}
	
	public List<JobSearchUser> JobSearchUserProfileRowMapper(String sql, Object[] args) {
		return jdbcTemplate.query(sql, args, new RowMapper<JobSearchUser>() {
			@Override
			public JobSearchUser mapRow(ResultSet rs, int rownumber) throws SQLException {
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
		sql = "select u.*, up.* from user u inner join user_profile up on u.userid = up.userid where u.Email = ?";

		// Object args[0] = null;
		// args[0] = user.getEmailAddress();

		List<JobSearchUser> list = JobSearchUserProfileRowMapper(sql, new Object[] { email });

		return list.get(0);

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

	public ArrayList<JobSearchUser> getApplicants(int jobId) {

		String sql = "SELECT *" + " FROM user" + " INNER JOIN application" + " ON user.UserId = application.UserId"
				+ " INNER JOIN job" + " ON application.JobId = job.JobId" + " AND application.JobId = ?"
				+ " AND job.IsActive = 1";

		return (ArrayList<JobSearchUser>) this.JobSearchUserRowMapper(sql, new Object[] { jobId });
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

		jdbcTemplate.update(sql, new Object[] { rc.getValue(), rc.getRateCriterionId(),
				rc.getEmployeeId(), rc.getJobId() });

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
		String sql = "INSERT INTO endorsement (JobId, UserId, CategoryId)"
				+ " VALUES (?, ?, ?)";
		
		jdbcTemplate.update(sql, new Object[] {endorsement.getJobId(),
						endorsement.getUserId(), endorsement.getCategoryId() });
		
	}


	public void addComment(RatingDTO ratingDTO) {
		String sql = "INSERT INTO comment (JobId, UserId,"
					+ " Comment) VALUES (?, ?, ?)";
		
		jdbcTemplate.update(sql, new Object[] {ratingDTO.getJobId(), 
				ratingDTO.getEmployeeId(), ratingDTO.getComment() });
		
	}

	public void deleteComment(int jobId, int employeeId) {
		String sql = "DELETE FROM comment WHERE JobId = ? AND UserId = ?";
		
		jdbcTemplate.update(sql, new Object[] { jobId, employeeId });
		
	}

	public String getComment(int jobId, int userId) {
		String sql = "SELECT Comment FROM comment WHERE JobId = ? AND UserId = ?";
		
		String comment = "";
		try {
			comment = jdbcTemplate.queryForObject(sql, new Object[]{ jobId, userId }, String.class);

		} catch (Exception e) {
			// TODO: handle exception
		}		
				
		return comment;
		
	}

	public List<Double> getRating(int userId) {
		String sql = "SELECT Value FROM rating WHERE UserId = ?";
		return jdbcTemplate.queryForList(sql, new Object[]{ userId }, Double.class);
	}

	public List<Double> getRatingForJob(int userId, int jobId) {
		String sql = "SELECT Value FROM rating WHERE UserId = ? AND JobId = ?";
		return jdbcTemplate.queryForList(sql, new Object[]{ userId, jobId }, Double.class);
	}

	public List<Integer> getEndorsementCategoryIds(int userId) {
		String sql = "SELECT CategoryId FROM endorsement WHERE UserId = ? GROUP BY CategoryId";
		return jdbcTemplate.queryForList(sql, new Object[] { userId }, Integer.class);
	}

	public int getEndorsementCountByCategory(int userId, int categoryId) {
		String sql = "SELECT COUNT(*) FROM endorsement WHERE UserId = ? AND CategoryId = ?";
		return jdbcTemplate.queryForObject(sql, new Object[]{ userId, categoryId }, Integer.class);
	}

	public List<Integer> getEndorsementCategoryIdsByJob(int userId, int jobId) {
		String sql = "SELECT CategoryId FROM endorsement WHERE UserId = ? AND JobId = ? GROUP BY CategoryId";
		return jdbcTemplate.queryForList(sql, new Object[] { userId, jobId }, Integer.class);
	}

	public int getEndorsementCountByCategoryAndJob(int userId, int categoryId, int jobId) {
		String sql = "SELECT COUNT(*) FROM endorsement WHERE UserId = ? AND CategoryId = ? AND JobId = ?";
		return jdbcTemplate.queryForObject(sql, new Object[]{ userId, categoryId, jobId }, Integer.class);
	}


	public void deleteAvailability(int userId) {
		String sql = "DELETE FROM availability WHERE UserId = ?";
		jdbcTemplate.update(sql, new Object[]{ userId });
		
	}


	public void addAvailability(int userId, Date sqlDate) {
		String sql = "INSERT INTO availability (UserId, Day) VALUES(?, ?)";
		jdbcTemplate.update(sql, new Object[]{ userId, sqlDate });
		
	}


	public List<String> getAvailableDates(int userId) {
		String sql = "SELECT Day FROM availability WHERE UserId = ?";
		return jdbcTemplate.queryForList(sql, new Object[]{ userId }, String.class);
	}


	public void setHomeLocation(EditProfileDTO editProfileDTO) {
		String sql = "INSERT INTO user (HomeLat, HomeLng, HomeCity, HomeState, HomeZipCode) "
				+ "VALUES (?, ?, ?, ?, ?) WHERE UserId = ?";
		jdbcTemplate.update(sql, new Object[]{editProfileDTO.getHomeLat(),editProfileDTO.getHomeLng(),
								editProfileDTO.getHomeCity(), editProfileDTO.getHomeState(),
								editProfileDTO.getHomeZipCode(), editProfileDTO.getUserId() });
		
	}
	
	public void updateHomeLocation(EditProfileDTO editProfileDTO) {
		String sql = "UPDATE user SET HomeLat = ?, HomeLng = ?, HomeCity = ?, HomeState = ?,"
				+ " HomeZipCode = ? WHERE UserId = ?";
		
		jdbcTemplate.update(sql, new Object[]{editProfileDTO.getHomeLat(),editProfileDTO.getHomeLng(),
								editProfileDTO.getHomeCity(), editProfileDTO.getHomeState(),
								editProfileDTO.getHomeZipCode(), editProfileDTO.getUserId() });
		
	}


	public void UpdateMaxWorkRadius(int userId, int maxWorkRadius) {
		String sql = "UPDATE user SET MaxWorkRadius = ? WHERE UserId = ?";
		jdbcTemplate.update(sql, new Object[]{ maxWorkRadius, userId });
		
	}


	public List<JobSearchUser> findEmployees(FindEmployeesDTO findEmployeesDto) {
		
		//******************************************************************************************
		//NOTE: For optimal performance, we may want to eventually do some testing to determine
		//the order of these sub queries that executes most efficiently.
		//******************************************************************************************
		
		
		//**********************************
		//Summary:
		//The sub query for the distance each employee is from the specified location is required.
		//The sub query for availability is optional.
		//The sub query for categories is optional.
		//**********************************
			
		String sql = "SELECT * FROM user WHERE user.UserId IN";
		List<Object> argsList = new ArrayList<Object>();
		
		int subQueryCount = 1; //1 because the distance sub query is required
		
		//Build sub query for availability.
		//This filter requires the employee to have availability on ALL requested days.
		//Sub query returns all user ids with availability on ALL requested days.
		//This sub query is optional.
		String subQueryDates = null;
		if (findEmployeesDto.getAvailableDates() != null){
			
			subQueryDates = " (SELECT a0.UserId FROM availability a0";
			
			//Initialize at 1 because the first date is accounted for in the WHERE clause at the end.
			//See this SO post for sql details: http://stackoverflow.com/questions/1054299/sql-many-to-many-table-and-query
			//The sub query takes the form:
//			SELECT UserId
//			FROM availability a0
//			JOIN availability a1 ON a0.UserId = a1.UserId AND a1.Day = X
//			JOIN availability a2 ON a1.UserId = a2.UserId AND a2.Day = Y
//			WHERE Day = Z AND a0.UserId IN ([distance sub query])
			for(int dateCount = 1; dateCount < findEmployeesDto.getAvailableDates().size(); dateCount++){
				int dateCountMinus1 = dateCount - 1;
				subQueryDates += " JOIN availability a" + dateCount + " ON a"  + dateCountMinus1 + ".UserId"
						+ " = a" + dateCount + ".UserId AND a" + dateCount + ".Day = ?"; 
				argsList.add(findEmployeesDto.getAvailableDates().get(dateCount));
			}
			
			subQueryDates += " WHERE a0.Day = ? AND a0.UserId IN ";
			argsList.add(findEmployeesDto.getAvailableDates().get(0));
			
			sql += subQueryDates;
			subQueryCount += 1;
					
		}
		
		//Build the sub query for categories.
		//This sub query has the same structure as the availability sub query.
		//This returns all user ids that have ALL the requested categories.
		String subQueryCategories = null;
		if (findEmployeesDto.getCategoryIds() != null){
			
			subQueryCategories = " (SELECT uc0.UserId FROM user_category uc0";
			
			for(int categoryCount = 1; categoryCount < findEmployeesDto.getCategoryIds().size(); categoryCount++){
				int categoryCountMinus1 = categoryCount - 1;
				subQueryCategories += " JOIN user_category uc" + categoryCount + " ON uc"  + categoryCountMinus1 + ".UserId"
						+ " = uc" + categoryCount + ".UserId AND uc" + categoryCount + ".CategoryId = ?"; 
				argsList.add(findEmployeesDto.getCategoryIds().get(categoryCount));
			}
			
			subQueryCategories += " WHERE uc0.CategoryId = ? AND uc0.UserId IN ";
			argsList.add(findEmployeesDto.getCategoryIds().get(0));
			
			sql += subQueryCategories;
			subQueryCount += 1;
			
		}
		

		
		//Distance sub query.
		//This returns all user ids with a home radius within the requested radius.
		//This sub query is always required.
		String subQueryDistance = " (SELECT UserId FROM user"
				+ " WHERE ( 3959 * acos( cos( radians(?) ) * cos( radians( user.HomeLat ) ) * cos( radians( user.HomeLng ) - radians(?) ) "
				+ "+ sin( radians(?) ) * sin( radians( user.HomeLat ) ) ) ) < ? ";
		
		sql += subQueryDistance;
		
		
		argsList.add(findEmployeesDto.getLat());
		argsList.add(findEmployeesDto.getLng());
		argsList.add(findEmployeesDto.getLat());
		argsList.add(findEmployeesDto.getRadius());
		
		
		//Need to close the sub queries
		for (int i = 0; i < subQueryCount; i++){
			sql += ")";
		}
		

		return this.JobSearchUserRowMapper(sql, argsList.toArray());
	}


}
