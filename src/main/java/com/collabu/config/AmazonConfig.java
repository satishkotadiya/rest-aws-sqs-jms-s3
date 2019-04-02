package com.collabu.config;

import com.amazonaws.auth.AWSCredentialsProviderChain;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Slf4j
@Configuration
public class AmazonConfig {

  @Value("${aws.sqs.queue.url}")
  private String queueUrl;

  @Value("${aws.region}")
  private String region;

  @Value("${aws.accessKeyId}")
  private String accessKeyId;

  @Value("${aws.accessKeySecret}")
  private String accessKeySecret;


  @Bean
  public AWSCredentialsProviderChain awsCredentialsProviderChain() {
    return new DefaultAWSCredentialsProviderChain();
  }

  @Bean
  public AmazonSQS amazonSqs(AWSCredentialsProviderChain credentials) {
    AmazonSQSClientBuilder builder = AmazonSQSClientBuilder.standard();
    builder.setCredentials(credentials);
    builder.setRegion(region);
    return builder.build();
  }

  @Bean
  public AmazonS3 amazonS3(AWSCredentialsProviderChain credentials) {
    AmazonS3ClientBuilder builder = AmazonS3ClientBuilder.standard();
    builder.setCredentials(credentials);
    builder.setRegion(region);
    return builder.build();
  }

}
