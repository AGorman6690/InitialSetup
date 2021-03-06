package com.jobsearch.repository;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.jobsearch.controller.BaseRepository;
import com.jobsearch.google.Coordinate;
import com.jobsearch.model.Job;
import com.jobsearch.model.JobSearchUser;
import com.jobsearch.model.Profile;
import com.jobsearch.request.CreateUserRequest;
import com.jobsearch.request.FindEmployeesRequest;
import com.jobsearch.responses.user.CreateUserResponse;
import com.jobsearch.service.JobServiceImpl;
import com.jobsearch.service.UserServiceImpl;
import com.jobsearch.utilities.DateUtility;
import com.jobsearch.utilities.VerificationServiceImpl;

@Repository
public class UserRepository extends BaseRepository {

	@Autowired
	UserServiceImpl userService;
	@Autowired
	JobServiceImpl jobService;

	public JobSearchUser getUser(int userId) {

		String sql = "select u.*, p.* from user u inner join profile p on p.profileId = u.profileId where userid = ?";

		return JobSearchUserRowMapper(sql, new Object[] { userId }).get(0);

	}

	public CreateUserResponse createUser(CreateUserRequest request) {
		CreateUserResponse response = new CreateUserResponse();
	
		try {
			CallableStatement cStmt = jdbcTemplate.getDataSource().getConnection()
					.prepareCall("{call insertUser(?, ?, ?, ?, ?, ?)}");
			
			cStmt.setString(1, request.getFirstName());
			cStmt.setString(2, request.getLastName());
			cStmt.setString(3, request.getEmailAddress());
			cStmt.setInt(4, new Integer(1));
			cStmt.setString(5, request.getPassword());
			cStmt.setInt(6, request.getProfileId());

			ResultSet result = cStmt.executeQuery();	
			result.next();
			response.setUserId(result.getInt("userId"));
			
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return response;
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
				e.setUserId(rs.getInt("UserId"));
				e.setFirstName(rs.getString("FirstName"));
				e.setLastName(rs.getString("LastName"));
				e.setEmailAddress(rs.getString("Email"));
				e.setProfileId(rs.getInt("ProfileId"));
				e.setHomeLat(rs.getFloat("HomeLat"));
				e.setHomeLng(rs.getFloat("HomeLng"));
				e.setAbout(rs.getString("About"));
				e.setMaxWorkRadius(rs.getInt("MaxWorkRadius"));
				e.setHomeCity(rs.getString("HomeCity"));
				e.setHomeState(rs.getString("HomeState"));
				e.setHomeZipCode(rs.getString("HomeZipCode"));
				return e;
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




	public JobSearchUser getUserByEmail(String email) {
		String sql;
		sql = "select u.*, p.* from user u inner join profile p on p.profileId = u.profileId where Email = ?";

		List<JobSearchUser> list = JobSearchUserRowMapper(sql, new Object[] { email });

		if(list.size() >0 ){
			return list.get(0);
		}
		else{
			return null;
		}


	}

	public ArrayList<JobSearchUser> getEmployeesByJob(int jobId) {

		String sql = "SELECT * FROM user u"
					+ " INNER JOIN employment e ON e.UserId = u.UserId"
					+ " AND e.JobId = ?"
					+ " AND e.WasTerminated = 0";

		return (ArrayList<JobSearchUser>) this.JobSearchUserRowMapper(sql, new Object[] { jobId });
	}
	

	public List<JobSearchUser> getEmployees_byJob_completedWork(int jobId) {
		String sql = "SELECT * FROM user u"
				+ " JOIN employment e ON e.UserId = u.UserId"
				+ " AND u.UserId NOT IN ("
				+ " SELECT e.userId FROM employment e"
				+ " JOIN application a ON e.UserId = a.UserId"
				+ " JOIN wage_proposal wp ON a.ApplicationId = wp.ApplicationId"
				+ " JOIN employment_proposal_work_day ep ON wp.WageProposalId = ep.EmploymentProposalId"
				+ " JOIN work_day wd ON ep.WorkDayId = wd.WorkDayId"
				+ " WHERE e.JobId = ?"
				+ " AND a.JobId = ?"
				+ " AND wp.IsCurrentProposal = 1"
				+ " AND e.WasTerminated = 0"
				+ " AND wd.Timestamp_EndDateTime > ?"
				+ ")"
				+ " AND e.JobId = ?";

		return JobSearchUserRowMapper(sql, new Object[]{ jobId, jobId, DateUtility.getCurrentTimestamp(), jobId });
	}



	public ArrayList<Profile> getProfiles() {
		String sql = "SELECT *" + " FROM profile";
		return this.ProfilesRowMapper(sql);
	}









	public List<JobSearchUser> getUsers_byFindEmployeesRequest(FindEmployeesRequest request) {


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
		List<Object> args = new ArrayList<Object>();


		// ********************************************
		// Distance sub query
		// ********************************************
		sql += " WHERE ( 3959 * acos( cos( radians(?) ) * cos( radians( u.HomeLat ) ) * cos( radians( u.HomeLng ) - radians(?) ) "
										+ "+ sin( radians(?) ) * sin( radians( u.HomeLat ) ) ) ) <= u.MaxWorkRadius ";

		args.add(request.getLat());
		args.add(request.getLng());
		args.add(request.getLat());
		
		
		if(request.getMinimumJobsCompleted() != null){			
			sql += " AND u.UserId IN ("
					+ " SELECT u.UserId FROM user u"
					+ " INNER JOIN employment e ON u.UserId = e.UserId"
					+ " INNER JOIN job j ON e.JobId = j.JobId"
					+ " WHERE j.Status = ?"
					+ " AND e.WasTerminated = 0"
					+ " GROUP BY u.UserId"
					+ " HAVING COUNT(*) >= ?"
					+ ")";
					
			args.add(Job.STATUS_PAST);
			args.add(request.getMinimumJobsCompleted());
			
		}
		
		if(request.getMinimumRating() != null){			
			sql += " AND u.UserId IN ("
					+ " SELECT u.UserId FROM user u"
					+ " INNER JOIN rating r ON u.UserId = r.UserId"
					+ " WHERE r.Value IS NOT NULL"
					+ " GROUP BY(r.UserId)"
					+ " HAVING AVG(r.Value) >= ?"
					+ ")";
			args.add(request.getMinimumRating());
		}

		sql += " LIMIT 25";
		return this.JobSearchUserRowMapper(sql, args.toArray());
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


	public void updateHomeLocation(int userId, String city, String state, String zip,
			Coordinate coordinate) {
		
		String sql = "UPDATE user SET HomeLat = ?, HomeLng = ?, HomeCity = ?, HomeState = ?,"
				+ " HomeZipCode = ? WHERE UserId = ?";

		jdbcTemplate.update(sql,
				new Object[] { coordinate.getLatitude(), coordinate.getLongitude(),
						city, state, zip, userId });

	}

	public void updateMaxWorkRadius(int userId, Integer maxWorkRadius) {
		String sql = "UPDATE user SET MaxWorkRadius = ? WHERE UserId = ?";

		jdbcTemplate.update(sql,
				new Object[] { maxWorkRadius, userId });
	}

	public void insertEmployment(int userId, int jobId) {

		String sql = "INSERT INTO employment ( UserId, JobId, WasTerminated ) VALUES( ?, ?, 0)";
		jdbcTemplate.update(sql, new Object[]{ userId, jobId } );

	}



	public void updateAbout(int userId, String about) {
		String sql = "UPDATE user SET About = ? WHERE UserId = ?";
		jdbcTemplate.update(sql, new Object[]{ about, userId });

	}

	public List<JobSearchUser> getEmployees_byJobAndDate(int jobId, List<String> dateStrings) {

		String sql = "SELECT * FROM user u WHERE u.UserId IN ("
					+ " SELECT DISTINCT u.UserId FROM user u"
					+ " JOIN application a ON u.UserId = a.UserId"
					+ " JOIN employment e ON a.JobId = e.JobId AND a.UserId = e.UserId"
					+ " JOIN wage_proposal wp ON a.ApplicationId = wp.ApplicationId"
					+ " JOIN employment_proposal_work_day ep ON wp.WageProposalId = ep.EmploymentProposalId"
					+ " JOIN work_day wd ON ep.WorkDayId = wd.WorkDayId"
					+ " JOIN date d ON wd.DateId = d.Id"
					+ " WHERE e.JobId = ?"
					+ " AND wp.IsCurrentProposal = 1"
					+ " AND e.WasTerminated = 0"
					+ " AND (" ;

		List<Object> args = new ArrayList<Object>();
		args.add(jobId);

		boolean isFirst = true;
		for(String dateString : dateStrings){
			if(!isFirst) sql += " OR ";
			sql += " d.Id = ?";
			args.add(jobService.getDateId(dateString));
			isFirst = false;
		}
		sql += ") )";

		return JobSearchUserRowMapper(sql,  args.toArray() );
	}
	

	public JobSearchUser getEmployee(Integer jobId, int userId) {
		String sql = "SELECT * FROM user u"
				+ " JOIN employment e ON u.UserId = e.UserId"
				+ " WHERE e.UserId = ?"
				+ " AND e.JobId = ?";
		
		List<JobSearchUser> result = JobSearchUserRowMapper(sql, new Object[]{ userId, jobId });
		if(verificationService.isListPopulated(result)) return result.get(0);
		else return null;
	}

	public List<JobSearchUser> getEmployees_whoLeft(boolean departureHasBeenAcknowledged, int jobId) {
		
		String sql = "SELECT * FROM user u"
				+ " JOIN employment e ON u.UserId = e.UserId"
				+ " WHERE e.JobId = ?"
				+ " AND e.EmployeeLeftJob = 1";
		
		if(departureHasBeenAcknowledged) sql += " AND e.Flag_EmployerAcknowledgedEmployeeLeftJob = 1";
		else sql += " AND e.Flag_EmployerAcknowledgedEmployeeLeftJob = 0";
		
		return JobSearchUserRowMapper(sql, new Object[]{ jobId });
	}





	public List<JobSearchUser> getApplicants_byJob_openApplicantions(int jobId) {
		
		String sql = "SELECT * FROM user u"
				+ " INNER JOIN application a ON u.UserId = a.UserId"
				+ " INNER JOIN job j ON a.JobId = j.JobId"
				+ " WHERE a.isOpen = 1"
				+ " AND j.JobId = ?";
		
		return JobSearchUserRowMapper(sql, new Object[]{ jobId });

	}

	public void saveEquation(String equation) {
		String sql = "INSERT INTO calculator_result (expression) Values (?)";
		jdbcTemplate.update(sql, new Object[]{ equation });
		
	}

	public List<String> getExpressions() {
		String sql = "SELECT expression FROM calculator_result ORDER BY id DESC LIMIT 10";
		return jdbcTemplate.queryForList(sql, String.class);
	}

}
