package com.collabu.utils;

import ch.qos.logback.classic.BasicConfigurator;
import com.amazon.sqs.javamessaging.AmazonSQSMessagingClientWrapper;
import com.amazon.sqs.javamessaging.SQSConnection;
import com.amazonaws.util.Base64;
import lombok.extern.slf4j.Slf4j;

import javax.jms.*;

@Slf4j
public class AwsUtil {

    /**
     * A utility function to check the queue exists and create it if needed. For most
     * use cases this is usually done by an administrator before the application is run.
     */
    public static void ensureQueueExists(SQSConnection connection, String queueName) throws JMSException {
        AmazonSQSMessagingClientWrapper client = connection.getWrappedAmazonSQSClient();

        /**
         * In most cases, you can do this with just a createQueue call, but GetQueueUrl
         * (called by queueExists) is a faster operation for the common case where the queue
         * already exists. Also many users and roles have permission to call GetQueueUrl
         * but don't have permission to call CreateQueue.
         */
        if (!client.queueExists(queueName)) {
            client.createQueue(queueName);
        }
    }

    public static void setupLogging() {
        // Setup logging
//        BasicConfigurator.configure();
//        Logger.getRootLogger().setLevel(Level.WARN);
    }

    public static void handleMessage(Message message) throws JMSException {
        System.out.println("Got message " + message.getJMSMessageID());
        System.out.println("Content: ");
        if (message instanceof TextMessage) {
            TextMessage txtMessage = (TextMessage) message;
            System.out.println("\t" + txtMessage.getText());
        } else if (message instanceof BytesMessage) {
            BytesMessage byteMessage = (BytesMessage) message;
            // Assume the length fits in an int - SQS only supports sizes up to 256k so that
            // should be true
            byte[] bytes = new byte[(int) byteMessage.getBodyLength()];
            byteMessage.readBytes(bytes);
            System.out.println("\t" + Base64.encodeAsString(bytes));
        } else if (message instanceof ObjectMessage) {
            ObjectMessage objMessage = (ObjectMessage) message;
            System.out.println("\t" + objMessage.getObject());
        }
    }
}
