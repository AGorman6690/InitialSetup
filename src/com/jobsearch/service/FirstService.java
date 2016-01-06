package com.jobsearch.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;

import com.jobsearch.model.App;
import com.jobsearch.model.Category;
import com.jobsearch.model.DataBaseItem;
import com.jobsearch.model.JobSearchUser;
import com.jobsearch.model.Profile;
import com.jobsearch.repository.FirstRepository;

@Service
public class FirstService {

	@Autowired
	FirstRepository repository;
	
	public JobSearchUser getUser(int userId){
		return repository.getUser(userId);
	}
	
	public void createUser(JobSearchUser user){
		repository.createUser(user);
	}

	public List<Category> getCats() {
		return repository.getCats();			
	}

	public void setUsersCats(JobSearchUser user, String[] cats, App app) {
		repository.setUserCats(user, cats, app);		
	}

	public void exportUsersCats(JobSearchUser user) {
		repository.exportUsersCats(user);
		
	}

	public void setUsersId(JobSearchUser user) {
		repository.setUsersId(user);
		
	}

	public JobSearchUser getUserByEmail(String emailAddress) {
		return repository.getUserByEmail(emailAddress);
		
	}

	public List<Category> getCategories(JobSearchUser user, App app) {
		return repository.getCategories(user, app);
	}

	public void setUserByEmail(JobSearchUser user) {
		repository.setUserByEmail(user);
		
	}

	public void deleteCat(DataBaseItem item) {
		repository.deleteCat(item);
		
	}

	public void addCat(DataBaseItem item) {
		
		//Verify the usercategory table does not already contain this item
		if(repository.getUserCats(item).size() <= 0){
			repository.addCat(item);	
		}
		
		
	}

	public ArrayList<Profile> getProfiles() {
		
		return repository.getProfiles();
	}

	
}
