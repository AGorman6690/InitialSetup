package com.jobsearch.sns.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.amazonaws.services.sns.AmazonSNSClient;
import com.jobsearch.sns.AmazonSnsManager;

@Component
public class AmazonSnsManagerImpl implements AmazonSnsManager {
	AmazonSNSClient amazonSnsClient = new AmazonSNSClient();

	@Value("${amazon.topic.arn}")
	private String topicArn;

	@Override
	public void publishToTopic(String topicName, String message, String jobName) {
		amazonSnsClient.publish(topicArn + topicName, message, jobName);
	}
}
