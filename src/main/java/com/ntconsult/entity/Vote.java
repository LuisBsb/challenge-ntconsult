package com.ntconsult.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String associateId;
    private String vote;
    @ManyToOne
    @JoinColumn(name = "topic_id")
    private Topic topic;
}

