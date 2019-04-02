package com.collabu.service;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import com.collabu.config.AmazonConfig;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final AmazonSQS amazonSqs;
    private final AmazonConfig awsConfig;

    public NotificationService(AmazonSQS amazonSqs, AmazonConfig awsConfig) {
        this.amazonSqs = amazonSqs;
        this.awsConfig = awsConfig;
    }

    public SendMessageResult sendMessage(String message) {
        SendMessageRequest request = new SendMessageRequest();
        request.setQueueUrl(awsConfig.getQueueUrl());
        request.setMessageBody(message);
        return amazonSqs.sendMessage(request);
    }
}
