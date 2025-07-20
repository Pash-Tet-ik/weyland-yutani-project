package com.weyland.yutani.core.aspect.impl;

import com.weyland.yutani.core.aspect.AuditSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;

public class KafkaAuditSender implements AuditSender {

    private static final Logger log = LoggerFactory.getLogger(KafkaAuditSender.class);
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final String topic;

    public KafkaAuditSender(KafkaTemplate<String, String> kafkaTemplate, String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    @Override
    public void send(String auditData) {
        try {
            kafkaTemplate.send(topic, auditData);
        } catch (Exception e) {
            log.error("Failed to send audit message to Kafka topic '{}'", topic, e);
        }
    }
}
