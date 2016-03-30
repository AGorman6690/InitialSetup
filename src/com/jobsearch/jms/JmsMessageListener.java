package com.jobsearch.jms;

import java.util.concurrent.atomic.AtomicInteger;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JmsMessageListener implements MessageListener {

	private static final Logger logger = LoggerFactory.getLogger(JmsMessageListener.class);

	@Autowired
	private AtomicInteger counter = null;

	public void test() throws JMSException {
		Connection connection = null;

		try {

			// Producer

			ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");

			connection = connectionFactory.createConnection();

			Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

			Queue queue = session.createQueue("customerQueue");

			MessageProducer producer = session.createProducer(queue);

			String payload = "SomeTask";

			Message msg = session.createTextMessage(payload);

			System.out.println("Sending text '" + payload + "'");
			producer.send(msg);
			session.close();

		} finally {

			if (connection != null) {

				connection.close();

			}

		}
	}

	/**
	 * Implementation of <code>MessageListener</code>.
	 */
	public void onMessage(Message message) {
		try {
			int messageCount = message.getIntProperty(JmsMessageProducer.MESSAGE_COUNT);

			if (message instanceof TextMessage) {
				TextMessage tm = (TextMessage) message;
				String msg = tm.getText();

				logger.info("Processed message '{}'.  value={}", msg, messageCount);

				counter.incrementAndGet();
			}
		} catch (JMSException e) {
			logger.error(e.getMessage(), e);
		}
	}

}