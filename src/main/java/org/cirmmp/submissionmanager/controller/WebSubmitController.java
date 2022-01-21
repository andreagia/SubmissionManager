package org.cirmmp.submissionmanager.controller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.cirmmp.submissionmanager.model.Job;
import org.cirmmp.submissionmanager.model.JobStatus;
import org.cirmmp.submissionmanager.repository.FindRepository;
import org.cirmmp.submissionmanager.service.KafkaSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;


@Slf4j
@RestController
@RequestMapping("/")
public class WebSubmitController {

    @Autowired
    private KafkaSender kafkaSender;

    @Autowired
    FindRepository findRepository;

    @SneakyThrows
    @PostMapping(consumes = "application/json")
    public ResponseEntity<JobStatus> produceEvent(@RequestBody JsonNode event) {
        // Use hashcode to see if identical events are being submitted
        log.info("Received event with hashcode: {}", event.hashCode());
        //Convert Json to Job
        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        //Now any null value in Map object serialized through this mapper is going to be ignored
        Job job = mapper.treeToValue(event , Job.class);
        //log.info("handling event: {}", event);
        //ProducerRecord<String, JsonNode> rec = new ProducerRecord<String, JsonNode>("reflectoring-json",event);
        //kafkaSenderExample.sendJsonMessage(event);
        if(job.getCommand().equals("submit")){
            kafkaSender.sendJobMessageWithCallback(job);
            JobStatus jobStatus = new JobStatus();
            jobStatus.setName(job.getName());
            jobStatus.setTag(job.getTag());
            jobStatus.setMail(job.getMail());
            jobStatus.setDirectory(job.getDirectory());
            findRepository.save(jobStatus);

            return new ResponseEntity<>(null, HttpStatus.OK);
        }
        if(job.getCommand().equals("check")){
            Optional<JobStatus> jobstag = findRepository.findByTag(job.getTag());
            if(jobstag.isPresent()){
                JobStatus jobstagr = jobstag.get();
                log.info("CHECK: {}",jobstagr);
                return new ResponseEntity<>(jobstagr, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }

        }
        //webLogService.handleEvent(event);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }
}
