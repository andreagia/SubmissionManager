package org.cirmmp.submissionmanager.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.cirmmp.submissionmanager.model.JobStatus;
import org.cirmmp.submissionmanager.repository.FindRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class KafkaListeners {
    private final Logger LOG = LoggerFactory.getLogger(KafkaListeners.class);
    @Autowired
    FindRepository findRepository;

    @KafkaListener(id = "1", topics = "reflectoring-receivejobs", groupId = "reflectoring-receivejobs-mc", containerFactory = "jsonKafkaListenerContainerFactory")
    void listenerWithMessageConverter(JsonNode json) {
        //LOG.info("MessageConverterUserListener [{}]", json);
        JsonNode findj = searchForEntity(json, "tag");
        JsonNode statusj = searchForEntity(json, "status");
        LOG.info("-------FIND ------------ [{}]", findj);
        LOG.info("-------STATUS ------------ [{}]", statusj);
        List<JobStatus> jobs = new ArrayList<>();
        findRepository.findAll().forEach(jobs::add);
        jobs.forEach(System.out::println);
        if (findj != null) {
            Optional<JobStatus> ftag = findRepository.findByTag(findj.asText());
            if (ftag.isPresent()) {
                JobStatus jobupdate = ftag.get();
                jobupdate.setStatus(statusj.asText());
                findRepository.save(jobupdate);
            }
        }
//        Optional<JobStatus> ftag = findRepository.findByTag(find.asText());

//        if(ftag.isPresent()){
//            JobStatus jobupdate = ftag.get();
//            jobupdate.setStatus(status.asText());
//            findRepository.save(jobupdate);
//        }

    }

    private JsonNode searchForEntity(JsonNode node, String entityName) {
        // A naive depth-first search implementation using recursion. Useful
        // **only** for small object graphs. This will be inefficient
        // (stack overflow) for finding deeply-nested needles or needles
        // toward the end of a forest with deeply-nested branches.
        if (node == null) {
            return null;
        }
        if (node.has(entityName)) {
            return node.get(entityName);
        }
        if (!node.isContainerNode()) {
            return null;
        }
        for (JsonNode child : node) {
            if (child.isContainerNode()) {
                JsonNode childResult = searchForEntity(child, entityName);
                if (childResult != null && !childResult.isMissingNode()) {
                    return childResult;
                }
            }
        }
        // not found fall through
        return null;
    }
}
