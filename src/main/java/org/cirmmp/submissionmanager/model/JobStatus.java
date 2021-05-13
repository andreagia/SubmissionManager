package org.cirmmp.submissionmanager.model;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "jobstatus")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class JobStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(name = "name")
    private String name;
    @Column(name = "tag")
    private String tag;
    @Column(name = "directory")
    private String directory;
    @Column(name = "status")
    private String status;
    @Column(name = "queue")
    private String queue;
    @Column(name = "mail")
    private String mail;


}
