package com.jobsearch.sns;

public interface AmazonSnsManager {
	void publishToTopic(String topicName, String message, String jobName);
}
