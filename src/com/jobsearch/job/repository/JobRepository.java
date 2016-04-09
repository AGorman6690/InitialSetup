package com.jobsearch.job.repository;

import java.sql.CallableStatement;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.jobsearch.application.service.ApplicationServiceImpl;
import com.jobsearch.category.service.Category;
import com.jobsearch.category.service.CategoryServiceImpl;
import com.jobsearch.job.service.CreateJobDTO;
import com.jobsearch.job.service.FilterDTO;
import com.jobsearch.job.service.Job;
import com.jobsearch.job.service.JobServiceImpl;
import com.jobsearch.model.Endorsement;
import com.jobsearch.model.Question;
import com.jobsearch.user.service.UserServiceImpl;

@Repository
public class JobRepository {

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	CategoryServiceImpl categoryService;

	@Autowired
	UserServiceImpl userService;

	@Autowired
	JobServiceImpl jobService;

	@Autowired
	ApplicationServiceImpl applicationService;

	public List<Job> JobRowMapper(String sql, Object[] args) {

		try{

			return jdbcTemplate.query(sql, args, new RowMapper<Job>() {
	
				@Override
				public Job mapRow(ResultSet rs, int rownumber) throws SQLException {
					Job e = new Job();
					e.setId(rs.getInt("JobId"));
					e.setJobName(rs.getString("JobName"));
					e.setUserId(rs.getInt("UserId"));
					e.setIsActive(rs.getInt("IsActive"));
					e.setDescription(rs.getString("Description"));
					e.setStreetAddress(rs.getString("StreetAddress"));
					e.setCity(rs.getString("City"));
					e.setState(rs.getString("State"));
					e.setZipCode(rs.getString("ZipCode"));
					e.setLat(rs.getFloat("Lat"));
					e.setLng(rs.getFloat("Lng"));
					e.setStartDate(rs.getDate("StartDate"));
					e.setEndDate(rs.getDate("EndDate"));
					e.setStartTime(rs.getTime("StartTime"));
					e.setEndTime(rs.getTime("EndTime"));
					return e;
				}
			});
			
		}catch(Exception e){
			return null;
		}
				
	}

	public List<Question> QuestionRowMapper(String sql, Object[] args) {

		try{

			return jdbcTemplate.query(sql, args, new RowMapper<Question>() {
	
				@Override
				public Question mapRow(ResultSet rs, int rownumber) throws SQLException {
					Question e = new Question();
					e.setQuestionId(rs.getInt("QuestionId"));
					e.setJobId(rs.getInt("JobId"));
					e.setFormatId(rs.getInt("FormatId"));
					e.setQuestion(rs.getString("Question"));

					return e;
				}
			});
			
		}catch(Exception e){
			return null;
		}
				
	}
	
	public void addJob(List<CreateJobDTO> jobDtos) {
		List<Job> jobsCreatedByUser = new ArrayList<>();

		try {

			ResultSet result = null;
			for (CreateJobDTO job : jobDtos) {
					CallableStatement cStmt = jdbcTemplate.getDataSource().getConnection()
							.prepareCall("{call create_Job(?, ?, ?, ?, ?)}");

					cStmt.setString(1, job.getJobName());
					cStmt.setInt(2, job.getUserId());
					cStmt.setString(3, job.getDescription());
//					cStmt.setString(4, job.getLocation());
					cStmt.setInt(5, job.getOpenings());

					result = cStmt.executeQuery();

					Job createdJob = new Job();
					while (result.next()) {
						createdJob.setId(result.getInt("JobId"));
						createdJob.setJobName(result.getString("JobName"));
						createdJob.setIsActive(result.getInt("isActive"));
						createdJob.setUserId(result.getInt("UserId"));
						createdJob.setDescription(result.getString("Description"));
//						createdJob.setLocation(result.getString("Location"));
						createdJob.setOpenings(result.getInt("Openings"));
					}
					for(Integer categoryId: job.getCategoryIds()){
						 cStmt = jdbcTemplate.getDataSource().getConnection()
								.prepareCall("{call insertJobCategories(?, ?)}");

						cStmt.setInt(1, createdJob.getId());
						cStmt.setInt(2, categoryId);

						cStmt.executeQuery();
					}
			}


		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void addJob(CreateJobDTO jobDto) {
//		List<Job> jobsCreatedByUser = new ArrayList<>();

		try {
			CallableStatement cStmt = jdbcTemplate.getDataSource().getConnection().prepareCall(
					"{call create_Job(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}");

			 cStmt.setString(1, jobDto.getJobName());
			 cStmt.setInt(2, jobDto.getUserId());
			 cStmt.setString(3, jobDto.getDescription());
			 //cStmt.setInt(6, jobDto.getOpenings());
			 cStmt.setString(4, jobDto.getStreetAddress());
			 cStmt.setString(5, jobDto.getCity());
			 cStmt.setString(6, jobDto.getState());
			 cStmt.setString(7, jobDto.getZipCode());
			 cStmt.setFloat(8,  jobDto.getLat());
			 cStmt.setFloat(9,  jobDto.getLng());
			 cStmt.setDate(10, (Date) jobDto.getStartDate());
			 cStmt.setDate(11, (Date) jobDto.getEndDate());
			 cStmt.setTime(12,  jobDto.getStartTime());
			 cStmt.setTime(13,  jobDto.getEndTime());
			 

			 
			 ResultSet result = cStmt.executeQuery();
			 
			 Job createdJob = new Job();
			 result.next();
//			 while(result.next()){
				 createdJob.setId(result.getInt("JobId"));
//			 }
		 
			for(Integer categoryId: jobDto.getCategoryIds()){
				 cStmt = jdbcTemplate.getDataSource().getConnection()
						.prepareCall("{call insertJobCategories(?, ?)}");
			
				cStmt.setInt(1, createdJob.getId());
				cStmt.setInt(2, categoryId);
			
				cStmt.executeQuery();
			}
			
			for(Question question : jobDto.getQuestions()){
		
				question.setJobId(createdJob.getId());
				this.addQuestion(question);
			}
			 
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void addQuestion(Question question) {
		
		
		CallableStatement cStmt;
		try {
			cStmt = jdbcTemplate.getDataSource().getConnection().
					prepareCall("{call insert_question(?, ?, ?)}");
			
			cStmt.setString(1, question.getQuestion());
			cStmt.setInt(2, question.getFormatId());
			cStmt.setInt(3, question.getJobId());
			
			ResultSet result = cStmt.executeQuery();	
			result.next();
			int createdQuestionId = result.getInt(("QuestionId"));		
			
			
			if(question.getAnswerOptions() != null){
				String sql = "INSERT INTO answer_option (QuestionId, AnswerOption) VALUES (?, ?)";
				for(String answerOption : question.getAnswerOptions()){
					jdbcTemplate.update(sql, new Object[]{ createdQuestionId, answerOption });
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	
		

		
	}


	public List<Job> getJobsByUser(int userId) {
		String sql = "SELECT * FROM job WHERE UserId = ?";
		return this.JobRowMapper(sql, new Object[] { userId });

	}

	public List<Job> getActiveJobsByUser(int userId) {

		// Get active jobs
		String sql = "SELECT * FROM job WHERE IsActive = 1 AND UserId = ?";
		return this.JobRowMapper(sql, new Object[] { userId });


	}
	
	public List<Job> getCompletedJobsByEmployer(int userId) {
		String sql = "SELECT * FROM job WHERE IsActive = 0 AND UserId = ?";
		return this.JobRowMapper(sql, new Object[] { userId });
	}

	public List<Job> getCompletedJobsByEmployee(int userId) {
		String sql = "SELECT * FROM job INNER JOIN employment ON job.JobId = employment.JobId"
						+ " AND job.IsActive = 0 AND employment.UserId = ?";
		return this.JobRowMapper(sql, new Object[] { userId });
	}

	public void markJobComplete(int jobId) {
		String sql = "UPDATE job" + " SET IsActive = 0 WHERE JobId = ?";

		jdbcTemplate.update(sql, new Object[] { jobId });

	}

	public void applyForJob(int jobId, int userId) {
		String sql = "INSERT INTO application (UserId, JobId)" + " VALUES (?, ?)";

		jdbcTemplate.update(sql, new Object[] { userId, jobId });

	}

	public List<Job> getJobsByCategory(int categoryId) {

		String sql = "SELECT *" + " FROM job " + " INNER JOIN job_category" + " ON job.JobId = job_category.JobId"
				+ " AND job_category.CategoryId = ?";

		return this.JobRowMapper(sql, new Object[] { categoryId });
	}

	public int getJobCountByCategory(int categoryId) {

		String sql = "SELECT COUNT(*)" + " FROM job " + " INNER JOIN job_category"
				+ " ON job.JobId = job_category.JobId" + " AND job_category.CategoryId = ? AND job.IsActive = 1";

		int result = jdbcTemplate.queryForObject(sql, new Object[] { categoryId }, int.class);

		return result;
	}

	public List<Job> getApplicationsByUser(int userId) {
		String sql = "SELECT *" + " FROM job" + " INNER JOIN application" + " ON job.JobId = application.JobId"
				+ "	AND application.UserId = ?";

		return this.JobRowMapper(sql, new Object[] { userId });
	}

	public List<Job> getJobOffersByUser(int userId) {
		String sql = "SELECT *" + " FROM job" + " INNER JOIN application" + " ON job.JobId = application.JobId"
				+ "	AND application.UserId = ? AND applicaion.IsOffered = 1";

		return this.JobRowMapper(sql, new Object[] { userId });
	}

	public List<Job> getEmploymentByUser(int userId) {
		String sql = "SELECT *" + " FROM job" + " INNER JOIN employment" + "	ON job.JobId = employment.JobId"
				+ "	AND employment.UserId = ?";

		return this.JobRowMapper(sql, new Object[] { userId });
	}

	public boolean hasAppliedForJob(int jobId, int userId) {
		String sql = "SELECT COUNT(*) FROM application WHERE jobId = ? AND userID = ?";
		int count = jdbcTemplate.queryForObject(sql, new Object[] { jobId, userId }, int.class);

		if (count > 0)
			return true;
		else
			return false;
	}

	public Job getJob(int jobId) {
		String sql = "SELECT * FROM job WHERE JobId=?";

		List<Job> jobs = this.JobRowMapper(sql, new Object[]{ jobId });
		
		if(jobs.size() > 0) return jobs.get(0);
		else return null;
	}


	public List<Job> getFilteredJobs(FilterDTO filter, int sqlArguementCount) {
		// TODO Auto-generated method stub
		
		//Distance formula found here: https://developers.google.com/maps/articles/phpsqlsearch_v3?csw=1#finding-locations-with-mysql
		//This calculates the distance between a database row and a given lat/lng.
		String sql = "SELECT *, ( 3959 * acos( cos( radians(?) ) * cos( radians( lat ) ) * cos( radians( lng ) - radians(?) ) "
					+ "+ sin( radians(?) ) * sin( radians( lat ) ) ) )"
					+ " AS distance FROM job";


		Object[] args = new Object[sqlArguementCount];
		int argCount;
	
		//Arguments for distance filter
		args[0] = filter.getLat();
		args[1] = filter.getLng();
		args[2] = filter.getLat();		
		argCount = 3;
			
		String sql2; 
		//If there are no categories to filter on
		if(filter.getCategoryIds() == null){

			sql2 = " WHERE job.IsActive = 1";
			
		//Else build the where condition for the categories 		
		}else{
	
			sql2 = " INNER JOIN job_category ON job.JobId = job_category.JobId WHERE job.IsActive = 1 AND (";
			for(int i = 0; i < filter.getCategoryIds().length; i++){
				if(i < filter.getCategoryIds().length - 1){
					sql2 += " job_category.CategoryId = ? OR";
				}else{
					sql2 += " job_category.CategoryId = ?)";
				}
			
				args[argCount] = filter.getCategoryIds()[i];
				argCount += 1;
			}			
			
		}
		sql += sql2;
		
		//Add filter on start time
		if(filter.getStartTime() != null){
			sql2 = " AND job.StartTime";
			if(filter.getBeforeStartTime()){
				sql2 += " <= ?";
			}else{
				sql2 += " >= ?";
			}
			sql += sql2;
			
			args[argCount] = filter.getStartTime();
			argCount += 1;
		}
		
		//Add filter on end time
		if(filter.getEndTime() != null){
			sql2 = " AND job.EndTime";
			if(filter.getBeforeEndTime()){
				sql2 += " <= ?";
			}else{
				sql2 += " >= ?";
			}			
			sql += sql2;
			
			args[argCount] = filter.getEndTime();
			argCount += 1;
		}	
		
		//Add filter on start date
		if(filter.getStartDate() != null){
			sql2 = " AND job.StartDate";
			if(filter.getBeforeStartDate()){
				sql2 += " <= ?";
			}else{
				sql2 += " >= ?";
			}			
			sql += sql2;
			
			args[argCount] = filter.getStartDate();
			argCount += 1;
		}
		
		//Add filter on end date
		if(filter.getEndDate() != null){
			sql2 = " AND job.EndDate";
			if(filter.getBeforeEndDate()){
				sql2 += " <= ?";
			}else{
				sql2 += " >= ?";
			}			
			sql += sql2;
			
			args[argCount] = filter.getEndDate();
			argCount += 1;
		}		

		//Complete the distance filter.
		//Add radius for distance filter.
		args[argCount] = filter.getRadius();
		sql += " HAVING distance < ? ORDER BY distance LIMIT 0 , 20";
		
		return this.JobRowMapper(sql, args);
	}


	public List<Question> getQuestions(int id) {
		String sql = "SELECT * FROM question WHERE JobId = ?";
		return this.QuestionRowMapper(sql, new Object[]{ id });
	}

	public List<String> getAnswerOptions(int questionId) {
		
		String sql = "Select AnswerOption FROM answer_option WHERE QuestionId = ?";
		return jdbcTemplate.queryForList(sql, new Object[]{ questionId }, String.class);

	}




}
