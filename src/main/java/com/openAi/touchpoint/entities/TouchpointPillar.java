package com.openAi.touchpoint.entities;

import com.openAi.security.enums.EntityStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;

@Entity
@Table(name = "touchpoint_pillar")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TouchpointPillar {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "pillar_name")
    private String pillarName;

    @Enumerated(EnumType.STRING)
    @Column(length = 32, columnDefinition = "varchar(32) default 'ACTIVE'")
    private EntityStatus status = EntityStatus.ACTIVE;


}
