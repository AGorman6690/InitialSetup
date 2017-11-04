package com.jobsearch.responses;

import javax.servlet.http.HttpSession

import com.jobsearch.session.SessionContext

public class BaseResponse {
	
	public BaseResponse(HttpSession session){
		this.isEmployee = SessionContext.isEmployee(session);
		this.isLoggedIn = SessionContext.isLoggedIn(session);
	}
	
	Boolean isEmployee;
	Boolean isLoggedIn;
}
