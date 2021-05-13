package org.cirmmp.submissionmanager.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.cirmmp.submissionmanager.model.Job;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.RoutingKafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Component
public class KafkaSender {
    private final Logger LOG = LoggerFactory.getLogger(KafkaSender.class);

    private KafkaTemplate<String, String> kafkaTemplate;
    private RoutingKafkaTemplate routingKafkaTemplate;
    private KafkaTemplate<String, Job> jobKafkaTemplate;
    private KafkaTemplate<String, JsonNode> jsonKafkaTemplate;

    @Autowired
    KafkaSender(KafkaTemplate<String, String> kafkaTemplate, RoutingKafkaTemplate routingKafkaTemplate,
                KafkaTemplate<String, Job> jobKafkaTemplate, KafkaTemplate<String,JsonNode> jsonKafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        this.routingKafkaTemplate = routingKafkaTemplate;
        this.jobKafkaTemplate = jobKafkaTemplate;
        this.jsonKafkaTemplate = jsonKafkaTemplate;
    }

    public void sendJobMessageWithCallback(Job job){
        LOG.info("Sending : {}", job);
        LOG.info("--------------------------------");
        ProducerRecord<String, Job> rec = new ProducerRecord<String, Job>("reflectoring-sendjobs",job);
        //jsonKafkaTemplate.send(rec);

        ListenableFuture<SendResult<String, Job>> future = jobKafkaTemplate.send(rec);

        future.addCallback(new ListenableFutureCallback<SendResult<String, Job>>() {
            @Override
            public void onSuccess(SendResult<String, Job> result) {
                LOG.info("Success JSON Callback: [{}] delivered with offset -{}", //message,
                        result.getRecordMetadata().offset());
            }

            @Override
            public void onFailure(Throwable ex) {
                LOG.warn("Failure JSON Callback: Unable to deliver message [{}]. {}", //message,
                        ex.getMessage());
            }
        });
    }


}
