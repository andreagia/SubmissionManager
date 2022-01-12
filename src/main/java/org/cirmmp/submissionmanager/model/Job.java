package org.cirmmp.submissionmanager.model;


import com.fasterxml.jackson.databind.JsonNode;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class Job {
    private String name;
    private String tag;
    private String directory;
    private String mail;
    private String command;
    private String exec;
}
