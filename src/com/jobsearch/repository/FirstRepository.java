package com.jobsearch.repository;

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

import com.jobsearch.model.Category;
import com.jobsearch.model.DataBaseItem;
import com.jobsearch.model.Job;
import com.jobsearch.model.JobSearchUser;
import com.jobsearch.model.Profile;
import com.jobsearch.model.App;

@Repository
public class FirstRepository {

	@Autowired
	JdbcTemplate jdbcTemplate;	
	
	public JobSearchUser getUser(int userId){
		
		/*String sql2 = "insert into user (UserId, FirstName, LastName, Email)  values ( 4 ,' Alice', 'Anderson', 'ee')";
		
		 jdbcTemplate.update(sql2);*/

		
		String sql = "Select * from User where userId = " + userId;
		
		return jdbcTemplate.query(sql, new ResultSetExtractor<JobSearchUser>() {

            @Override
            public JobSearchUser extractData(ResultSet rs) throws SQLException,
                    DataAccessException {
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
	
	
	
	public void createUser(JobSearchUser user){
		
		String sql = "insert into user (FirstName, LastName, Email, ProfileId)  values (?, ?, ?, ?)";
		
		jdbcTemplate.update(sql, new Object[]{user.getFirstName(), user.getLastName(), user.getEmailAddress(), user.getProfileId()});
		 

/*            @Override
            public JobSearchUser extractData(ResultSet rs) throws SQLException,
                    DataAccessException {
                if (rs.next()) {
                	JobSearchUser user = new JobSearchUser();
                    user.setUserId(rs.getInt("UserId"));
                    user.setFirstName(rs.getString("FirstName"));
                    user.setEmailAddress(rs.getString("Email"));
                    user.setLastName(rs.getString("LastName"));
                    return user;
                }

                return null;
            }*/

        
	}

	//This should be combined with the sql, args method...
	//******************************************************************************************************
	public List<JobSearchUser> JobSearchUserRowMapper(String sql, String email){
		 return jdbcTemplate.query(sql, new Object[]{email}, new RowMapper<JobSearchUser>(){  
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
	//******************************************************************************************************
	
	
	public List<JobSearchUser> JobSearchUserRowMapper(String sql, Object[] args){
		 return jdbcTemplate.query(sql, args, new RowMapper<JobSearchUser>(){  
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
	
	public List<Profile> ProfilesRowMapper(String sql, Object[] args){  
		
		List<Profile> list;
		
		 list = jdbcTemplate.query(sql, args, new RowMapper<Profile>(){  
		    
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
	
	
	
	public ArrayList<Profile> ProfilesRowMapper(String sql){  
		
		List<Profile> list;
		
		 list = jdbcTemplate.query(sql, new RowMapper<Profile>(){  
		    
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
	
//	public List<JobSearchUser> UserCategoryRowMapper(String sql, String email){
//		 
//		return jdbcTemplate.query(sql, new Object[]{email}, new RowMapper<JobSearchUser>(){  
//		    @Override  
//		    public JobSearchUser mapRow(ResultSet rs, int rownumber) throws SQLException {  
//		    	JobSearchUser e = new JobSearchUser();  
//		        e.setUserId(rs.getInt(1)); 
//		        e.setFirstName(rs.getString(2));  
//		        e.setLastName(rs.getString(3));  
//		        return e;  
//		    }  
//		    });             
//	}
	
	public List<Category> CategoryRowMapper(String sql){  
		 return jdbcTemplate.query(sql,new RowMapper<Category>(){  
		    
			 @Override  
		    public Category mapRow(ResultSet rs, int rownumber) throws SQLException {  
		    	Category e = new Category();  
		        e.setId(rs.getInt(1)); 
		        e.setName(rs.getString(2));  
		        return e;  
		    }  
		    });             
	}
	
	
	public List<Category> CategoryRowMapper(String sql, Object[] args){  
		 return jdbcTemplate.query(sql, args, new RowMapper<Category>(){  
		    
			 @Override  
		    public Category mapRow(ResultSet rs, int rownumber) throws SQLException {  
		    	Category e = new Category();  
		        e.setId(rs.getInt(1)); 
		        e.setName(rs.getString(2));  
		        return e;  
		    }  
		    });             
	}
	
	public List<Job> JobRowMapper(String sql, Object[] args){  
		 
		return jdbcTemplate.query(sql, args, new RowMapper<Job>(){  
		    
			@Override  
		    public Job mapRow(ResultSet rs, int rownumber) throws SQLException {  
		    	Job e = new Job();  
		        e.setId(rs.getInt(1)); 
		        e.setJobName(rs.getString(2)); 
		        e.setUserId(rs.getInt(3));
		        e.setIsActive(rs.getInt(4));
		        return e;  
		    }  
		    });    
		 
		 
	}
	
	public List<Category> UserCategoriesRowMapper(String sql, Object[] args){  
		
		List<Category> list;
		 list = jdbcTemplate.query(sql, args, new RowMapper<Category>(){  
		    
			 @Override  
		    public Category mapRow(ResultSet rs, int rownumber) throws SQLException {  
		    	Category e = new Category();  
		        e.setId(rs.getInt(3));  
		        return e;  
		    }  
		    });   
		 
		 
		 //System.out.println(list.get(0));
		 
		 return list;
		 
	}
	
	
	public List<DataBaseItem> JobsCategoriesRowMapper(String sql, Object[] args){  
		
		List<DataBaseItem> list;
		 list = jdbcTemplate.query(sql, args, new RowMapper<DataBaseItem>(){  
		    
			 @Override  
		    public DataBaseItem mapRow(ResultSet rs, int rownumber) throws SQLException {  
				 DataBaseItem e = new DataBaseItem();  
		        e.setJobId(rs.getInt(2));
		        e.setCategoryId(rs.getInt(3));
		        return e;  
		    }  
		    });   
		 
		 
		 //System.out.println(list.get(0));
		 
		 return list;
		 
	}
	
	
//	public List<JobSearchUser> EmployeesCategoriesRowMapper(String sql, Object[] args){  
//		
//		List<JobSearchUser> list;
//		 list = jdbcTemplate.query(sql, args, new RowMapper<JobSearchUser>(){  
//		    
//			 @Override  
//		    public JobSearchUser mapRow(ResultSet rs, int rownumber) throws SQLException {  
//				 JobSearchUser e = new JobSearchUser();
//				e.setFirstName(rs.getString(1));
//		        e.setLastName(rs.getString(2));
//		        e.setEmailAddress(rs.getString(3));
//		        return e;  
//		    }  
//		    });   
//		 
//		 
//		 //System.out.println(list.get(0));
//		 
//		 return list;
//		 
//	}
	

	public List<Category> getCategories() {

		String sql = "SELECT * "
				+ " FROM categories";										
		
		return this.CategoryRowMapper(sql);		
	}


//	public void setUserCats(JobSearchUser user, String[] cats, App app) {
//		
//		List<Category> userCats;
//		userCats = user.getCategories();
//		
//		
//		for(String strCat : cats){
//			user.addCategory(app.getCategoryByName(strCat));			
//		}
//		
//		for(Category cat : user.getCategories()){
//			System.out.println(cat.getName());
//		}
//		
//		
//	}


	
	public void exportUsersCats(JobSearchUser user) {
		
		for(Category cat : user.getCategories()){
			String sql = "insert into usercategories (UserID, CategoryID)  values (?, ?)";
			jdbcTemplate.update(sql, new Object[]{user.getUserId(), cat.getId()});

		}
		
	}
	
	public JobSearchUser getUserByEmail(String email){
		String sql;
		sql = "select * from user where Email = ?";
		
//		Object args[0] = null;
//		args[0] = user.getEmailAddress();
		
		
		List<JobSearchUser> list = JobSearchUserRowMapper(sql, email);
		
		return list.get(0);	
		
	}

	public void setUsersId(JobSearchUser user) {
	
		//From the user table, get the ID associated with the user's email
		user.setUserId(getUserByEmail(user.getEmailAddress()).getUserId());
		
	}


	public void setUserByEmail(JobSearchUser user) {
		String sql;
		sql = "select * from user where Email = ?";
			
		List<JobSearchUser> list = JobSearchUserRowMapper(sql, user.getEmailAddress());
		
		user = list.get(0);	

	}
	

	public void deleteCategory(int userId, int categoryId) {
		
		String sql;
		sql = "DELETE FROM usercategories"
				+ " WHERE userId = ?"
				+ " AND categoryId = ?";
		
		jdbcTemplate.update(sql, new Object[]{userId, categoryId});
	}

	
	public List<Category> getUserCats(int userId, int categoryId){
		
		String sql;
		sql = "select * from usercategories where UserID = ? and CategoryID = ?";
		
		return this.UserCategoriesRowMapper(sql, new Object[]{userId, categoryId});
		
		
	}
	
	
	public void addCategory(int userId, int categoryId) {
		
		String sql;
		sql = "insert into usercategories (UserID, CategoryID) values (?, ?)";
		jdbcTemplate.update(sql, new Object[]{userId, categoryId});
		
	}

	public void addJob(String jobName, int userId) {
		String sql;
		sql = "INSERT INTO jobs (JobName, UserId, IsActive)"
				+ " VALUES (?, ?, 1)";
		
		jdbcTemplate.update(sql, new Object[]{jobName, userId});
		
	}



	public ArrayList<Job> getJobs(JobSearchUser user) {
		String sql;
		sql = "select * from jobs where UserId = ?";
		return (ArrayList<Job>) this.JobRowMapper(sql, new Object[]{user.getUserId()});
		
	}



//	public void exportJobCategory(DataBaseItem item) {
//		String sql;
//		sql = "insert into jobscategories (JobId, CategoryId) values (?, ?)";
//		jdbcTemplate.update(sql, new Object[]{item.getJobId(), item.getCategoryId()});
//
//		
//	}
	
	public void updateJobComplete(int jobId) {
		String sql = "UPDATE jobs"
				+ " SET IsActive = ?"
				+ " WHERE Id = ?";
		
		jdbcTemplate.update(sql, new Object[]{0, jobId});
		
	}



//	@SuppressWarnings("null")
//	public ArrayList<Category> getCategoriesForJob(Job job) {
//		
//		//Need category Ids associated with job Id.
//		//Return a list a db items.
//		String sql = "select * from jobscategories where JobId = ?";
//		Object[] args = {job.getId()};
//		List<DataBaseItem> items = this.JobsCategoriesRowMapper(sql, args);
//		
//		//Use the db items' category id property to get category objects
//		ArrayList<Category> cats = new ArrayList<Category>();		
//		List<Category> returnedCats;
//		sql = "select * from categories where CategoryID = ?";
//		for (DataBaseItem item : items){
//			
//			//Get the category object associated with the db item's category id
//			returnedCats = this.CategoryRowMapper(sql, new Object[] {item.getCategoryId()});
//			cats.add(returnedCats.get(0));
//				
//	
//		}
//	
//		return cats;
//	}

	public ArrayList<JobSearchUser> getUsers(int categoryId, int profileIdNotToInclude) {
		
		//**************************************************
		//Should this select statement dynamically created to capture all table column names?
		//Reason: because this is selecting all columns from the user table, if additional columns 
		//are added to the user table, this select string will need to be updated.
		//**************************************************
		
		//Given a category ID, get all user objects.
		String sql = "SELECT user.UserId, user.FirstName, user.LastName, user.Email, user.ProfileId"
				+ " FROM user"
				+ " INNER JOIN usercategories"
				+ " ON user.UserId = usercategories.UserID"
				+ " AND usercategories.CategoryID = ?"
				+ " AND user.ProfileId <> ?";
				
				
		
		return (ArrayList<JobSearchUser>) this.JobSearchUserRowMapper(sql, new Object[]{categoryId, profileIdNotToInclude});
	}



	public ArrayList<Category> getCategoriesByJobId(int jobId) {
		
		//Given a job ID, get all category objects
		String sql = "SELECT categories.CategoryID, categories.Name"
				+ " FROM categories"
				+ " INNER JOIN jobscategories"
				+ " ON categories.CategoryID = jobscategories.CategoryId"
				+ " AND jobscategories.JobId = ?";
	
		return (ArrayList<Category>) this.CategoryRowMapper(sql, new Object[]{jobId});
	}
	
	public List<Category> getCategoriesByUserId(int userId) {
		
		//Given a user ID, get all category objects
		String sql = "SELECT *"
				+ " FROM categories"
				+ " INNER JOIN usercategories"
				+ " ON categories.CategoryID = usercategories.CategoryID"
				+ " AND usercategories.UserID = ?";
	
		return (ArrayList<Category>) this.CategoryRowMapper(sql, new Object[]{userId});
	}



	public ArrayList<Job> getJobs(JobSearchUser user, boolean isActive) {
		
		//Con
//		int param;
//		if (isActive) param = 1;
//		else param = 0;
		
		String sql = "SELECT *"
				+ " FROM jobs"
				+ " WHERE UserId = ?"
				+ " AND IsActive = ?";
		
		return (ArrayList<Job>) this.JobRowMapper(sql, new Object[]{user.getUserId(), isActive});
		
	}
	
	public ArrayList<Job> getJobs(int userId, boolean isActive) {

		
		String sql = "SELECT *"
				+ " FROM jobs"
				+ " WHERE UserId = ?"
				+ " AND IsActive = ?";
		
		return (ArrayList<Job>) this.JobRowMapper(sql, new Object[]{userId, isActive});
		
	}
	

	public void applyForJob(int jobId, int userId) {
		String sql = "INSERT INTO applications (UserId, JobId)"
				+ " VALUES (?, ?)";
		
		jdbcTemplate.update(sql, new Object[]{userId, jobId});	
		
	}



	public ArrayList<Job> getJobsBySelectedCat(int categoryId) {
		String sql = "SELECT jobs.Id, jobs.JobName, jobs.UserId, jobs.IsActive"
				+ " FROM jobs "
				+ " INNER JOIN jobscategories"
				+ " ON jobs.Id = jobscategories.JobId"
				+ " AND jobscategories.CategoryId = ?"
				+ " AND jobs.IsActive = 1";
					
				
		return (ArrayList<Job>) this.JobRowMapper(sql, new Object[]{categoryId});
	}



	public ArrayList<JobSearchUser> getApplicants(int jobId) {
		
		String sql = "SELECT user.UserId, user.FirstName, user.LastName, user.Email, user.ProfileId"
				+ " FROM user"
				+ " INNER JOIN applications"
				+ " ON user.UserId = applications.UserId"
				+ " INNER JOIN jobs"
				+ " ON applications.JobId = jobs.Id"				
				+ " AND applications.JobId = ?"
				+ " AND jobs.IsActive = 1";
				
		
		return (ArrayList<JobSearchUser>) this.JobSearchUserRowMapper(sql, new Object[]{jobId});
	}



	public Job getJob(int jobId) {
		String sql = "SELECT *"
				+ " FROM jobs"
				+ " WHERE Id = ?";
		
		List<Job> jobs;
		jobs = this.JobRowMapper(sql, new Object[]{jobId});
		
		return jobs.get(0);
	}



	public ArrayList<JobSearchUser> getEmpolyees(int jobId) {
		String sql = "SELECT user.UserId, user.FirstName, user.LastName, user.Email, user.ProfileId"
				+ " FROM user"
				+ " INNER JOIN employment"
				+ " ON user.UserId = employment.UserId"
				+ " AND employment.JobId = ?";
		
		return (ArrayList<JobSearchUser>) this.JobSearchUserRowMapper(sql, new Object[]{jobId});
	}



	public void hireApplicant(int userId, int jobId) {
		String sql = "INSERT INTO employment (UserId, JobId)"
				+ " VALUES (?, ?)";
		
		jdbcTemplate.update(sql, new Object[]{userId, jobId});
				
		
	}

	public ArrayList<Job> getAppliedToJobs(JobSearchUser user, Boolean showOnlyActiveJobs) {
		String sql = "SELECT *"
					+ " FROM jobs"
					+ " INNER JOIN applications"
					+ "	ON jobs.Id = applications.JobId"
					+ "	AND applications.UserId = ?";
					
		if (showOnlyActiveJobs) sql += " AND jobs.IsActive = 1";
		
		return (ArrayList<Job>) this.JobRowMapper(sql, new Object[]{user.getUserId()});
	}

	public ArrayList<Job> getEmployment(JobSearchUser user, boolean showOnlyActiveJobs) {
		String sql = "SELECT *"
				+ " FROM jobs"
				+ " INNER JOIN employment"
				+ "	ON jobs.Id = employment.JobId"
				+ "	AND employment.UserId = ?";
				
	if (showOnlyActiveJobs) sql += " AND jobs.IsActive = 1";
	
	return (ArrayList<Job>) this.JobRowMapper(sql, new Object[]{user.getUserId()});
	}



	public Profile getProfile(int profileId) {
		String sql = "SELECT *"
				+ " FROM profiles"
				+ "	WHERE id = ?";
		
		List<Profile> profiles = this.ProfilesRowMapper(sql, new Object[]{profileId});
		
		return profiles.get(0);			
	}

	public Category getCategory(int categoryId) {
		String sql = "SELECT *"
				+ " FROM categories"
				+ "	WHERE CategoryID = ?";
		
		return this.CategoryRowMapper(sql, new Object[]{categoryId}).get(0);
	}


	public ArrayList<JobSearchUser> getEmployeesByCategory(int categoryId) {
		String sql = "SELECT *"
				+ " FROM user"
				+ "	INNER JOIN usercategories"
				+ "	ON user.UserId = usercategories.userID"
				+ " AND user.ProfileId = 2"
				+ " AND usercategories.CategoryID = ?";
		
		return (ArrayList<JobSearchUser>) this.JobSearchUserRowMapper(sql, new Object[]{categoryId});
	}

	public ArrayList<Job> getJobsByCategory(int categoryId, boolean showOnlyActiveJobs) {
		
		String sql = "SELECT *"
				+ " FROM jobs "
				+ " INNER JOIN jobscategories"
				+ " ON jobs.Id = jobscategories.JobId"
				+ " AND jobscategories.CategoryId = ?"
				+ " AND jobs.IsActive = ?";
				
		return (ArrayList<Job>) this.JobRowMapper(sql, new Object[]{categoryId, showOnlyActiveJobs});
	}



	public ArrayList<Profile> getProfiles() {
		String sql = "SELECT *"
				+ " FROM profiles";
		return this.ProfilesRowMapper(sql);
	}



	public void addJobCategory(int jobId, int categoryId) {
		String sql;
		sql = "INSERT INTO jobscategories (JobId, CategoryId)"
				+" VALUES (?, ?)";
		
		jdbcTemplate.update(sql, new Object[]{jobId, categoryId});

		
		
	}
}
