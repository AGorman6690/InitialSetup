package com.jobsearch.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.print.attribute.standard.JobOriginatingUserName;

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

	//public List<JobSearchUser> JobSearchUserRowMapper(){
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
	
	public List<Profile> ProfilesRowMapper(String sql){  
		
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
		 
		 return list;
	}
	
	public List<JobSearchUser> UserCategoryRowMapper(String sql, String email){
		 
		return jdbcTemplate.query(sql, new Object[]{email}, new RowMapper<JobSearchUser>(){  
		    @Override  
		    public JobSearchUser mapRow(ResultSet rs, int rownumber) throws SQLException {  
		    	JobSearchUser e = new JobSearchUser();  
		        e.setUserId(rs.getInt(1)); 
		        e.setFirstName(rs.getString(2));  
		        e.setLastName(rs.getString(3));  
		        return e;  
		    }  
		    });             
	}
	
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

	public List<Category> getCats() {

		//String sql = "select * from categories";
		List<Category> list = this.CategoryRowMapper("select * from categories");													
		
		return list;		
	}


	public void setUserCats(JobSearchUser user, String[] cats, App app) {
		
		List<Category> userCats;
		userCats = user.getCategories();
		
		
		for(String strCat : cats){
			user.addCategory(app.getCategoryByName(strCat));			
		}
		
		for(Category cat : user.getCategories()){
			System.out.println(cat.getName());
		}
		
		
	}


	
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



	@SuppressWarnings("null")
	public List<Category> getCategories(JobSearchUser user, App app) {
		
		String sql;
		sql =  "select * from usercategories where UserId = ?";
		
		//An array list holding sql statement values
		ArrayList<Object> args = new ArrayList<Object>();
		args.add(user.getUserId());
		
		//Get the category IDs associated with user
		List<Category> dbCatIds;
		dbCatIds = this.UserCategoriesRowMapper(sql, new Object[]{user.getUserId()});
		
		//From the user's category IDs, get category objects from the application
		List<Category> userCats = new ArrayList<Category>();
		for(Category cat : dbCatIds){
			userCats.add(app.getCategoryById(cat.getId()));
		}
		
		return userCats;
	}



	public void setUserByEmail(JobSearchUser user) {
		String sql;
		sql = "select * from user where Email = ?";
			
		List<JobSearchUser> list = JobSearchUserRowMapper(sql, user.getEmailAddress());
		
		user = list.get(0);	

	}

	

	public void deleteCat(DataBaseItem item) {
		
		String sql;
		sql = "delete from usercategories where userId = ? and categoryId = ?";
		jdbcTemplate.update(sql, new Object[]{item.getUserId(), item.getCategoryId()});
		
	}

	
	public List<Category> getUserCats(DataBaseItem item){
		
		String sql;
		sql = "select * from usercategories where UserID = ? and CategoryID = ?";
		
		return this.UserCategoriesRowMapper(sql, new Object[]{item.getUserId(), item.getCategoryId()});
		
		
	}
	
	
	public void addCat(DataBaseItem item) {
		
		String sql;
		sql = "insert into usercategories (UserID, CategoryID) values (?, ?)";
		jdbcTemplate.update(sql, new Object[]{item.getUserId(), item.getCategoryId()});
		
	}



	public ArrayList<Profile> getProfiles() {
		
		String sql;
		sql = "select * from profiles";
		
		return (ArrayList<Profile>) this.ProfilesRowMapper(sql);
		
	}

}
