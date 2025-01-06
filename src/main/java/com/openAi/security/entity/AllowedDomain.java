package com.openAi.security.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "allowed_domain")
public class AllowedDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "INT(11) UNSIGNED")
    private Integer id;

    @Column(name = "domain_name")
    private String domainName;

    @Column(name = "customer_id")
    private Integer customerId;

    @Column(name = "team_ids")
    private String teamIds;

    @Column(name = "is_on_boarded")
    private Boolean isOnBoarded;

    @Column(name = "on_boarded_date")
    private LocalDate onBoardedDate;

    @Column(name = "data_from")
    private LocalDate dataFrom;
}
