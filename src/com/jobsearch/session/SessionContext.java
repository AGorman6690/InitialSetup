package com.jobsearch.session;

import javax.servlet.http.HttpSession;

import com.jobsearch.model.JobSearchUser;

public class SessionContext {
	public static JobSearchUser getSessionUser(HttpSession session) {
		return (JobSearchUser) session.getAttribute("user");
	}
}
