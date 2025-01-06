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
public class AllowedDomain {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "INT(11) UNSIGNED")
    private Integer id;

    private String domainName;

    private Integer customerId;

    private String teamIds;

    private Boolean isOnBoarded;

    private LocalDate onBoardedDate;

    private LocalDate dataFrom;
}
