package com.jobsearch.queue

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
class AmazonQueueResponse {
	@JsonProperty("Subject")
	String subject;
	@JsonProperty("Message")
	String message;
}
