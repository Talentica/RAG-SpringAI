package com.openAi.customers;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.openAi.security.enums.EntityStatus;
import com.openAi.team.Team;
import lombok.*;
import lombok.experimental.Accessors;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Table(name = "customers")
public class Customer {

    @Id
    @Column(columnDefinition = "INT(11) UNSIGNED")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String customerName;
    private String projectName;

    @Enumerated(EnumType.STRING)
    private EntityStatus status = EntityStatus.ACTIVE;

    @Enumerated(EnumType.STRING)
    private CustomerSegment customerSegment = CustomerSegment.PROJECT;

    private String currentStage;

    private String engagementType;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JsonIgnoreProperties("teamLeads")
    private List<Team> teams;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties("customer")
    private List<CustomerUserRoles> customerUserRoles;

    @Column(columnDefinition = "DATE")
    private LocalDate startDate;

    @Column(columnDefinition = "DATE")
    private LocalDate endDate;

    private Boolean isCicero;

    private Boolean isTeg;

    private String link;

}
