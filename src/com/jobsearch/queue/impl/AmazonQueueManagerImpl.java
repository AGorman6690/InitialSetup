package com.jobsearch.queue.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.amazonaws.services.sqs.AmazonSQSClient;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.amazonaws.services.sqs.model.ReceiveMessageResult;
import com.jobsearch.email.Mailer;
import com.jobsearch.json.JSON;
import com.jobsearch.model.JobSearchUser;
import com.jobsearch.queue.AmazonQueueManager;
import com.jobsearch.queue.AmazonQueueResponse;

@Component
public class AmazonQueueManagerImpl implements AmazonQueueManager {

	AmazonSQSClient amazonQueueClient = new AmazonSQSClient();

	@Autowired
	Mailer mailer;

	@Value("${amazon.sqs.url}")
	private String queueUrl;

	@Override
	public void sendMessageToSubscribers(String queueName, List<JobSearchUser> subscribers, String subject) {
		ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(queueUrl + queueName);
		receiveMessageRequest.setWaitTimeSeconds(20);

		ReceiveMessageResult receiveMessageResult = amazonQueueClient.receiveMessage(receiveMessageRequest);

		AmazonQueueResponse response = JSON.parse(receiveMessageResult.getMessages().get(0).getBody(),
				AmazonQueueResponse.class);

		subscribers.parallelStream().forEach(user -> {
			mailer.sendMail(user.getEmailAddress(), response.getSubject(), response.getMessage());
		});

		for (Message message : receiveMessageResult.getMessages()) {
			amazonQueueClient.deleteMessage(queueUrl + queueName, message.getReceiptHandle());
		}
	}
}
