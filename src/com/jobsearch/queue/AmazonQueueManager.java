package com.jobsearch.queue;

import java.util.List;

import com.jobsearch.model.JobSearchUser;

public interface AmazonQueueManager {
	public void sendMessageToSubscribers(String queueName, List<JobSearchUser> subscribers, String subject);
}
