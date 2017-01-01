package com.jobsearch.queue;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sqs.AmazonSQSClient;

public class SQSConfig {

    @Value("${amazon.sqs.url}")
    private String endpoint;

    @Value("${queue.name}")
    private String queueName;

    @Bean
    public AmazonSQSClient createSQSClient() {

        AmazonSQSClient amazonSQSClient = new AmazonSQSClient(new BasicAWSCredentials("",""));
        amazonSQSClient.setEndpoint(endpoint);
        amazonSQSClient.createQueue(queueName);

        return amazonSQSClient;
    }

}
