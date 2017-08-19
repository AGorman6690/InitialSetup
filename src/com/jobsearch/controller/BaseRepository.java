package com.jobsearch.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.jobsearch.utilities.VerificationServiceImpl;

public class BaseRepository {
	
	@Autowired
	protected JdbcTemplate jdbcTemplate;
	@Autowired
	protected VerificationServiceImpl verificationService;
}
