package com.openAi.touchpoint.entities;

import com.openAi.customers.Customer;
import lombok.Data;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Data
public class TouchpointParticipantNew {

    @Id
    @Column(name = "id", columnDefinition = "INT(11) UNSIGNED")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "participant_name")
    private String participantName;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private TouchpointParticipantRole role;

    private Date recordDate;
}
