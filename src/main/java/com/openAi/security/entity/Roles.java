package com.openAi.security.entity;

import com.openAi.security.enums.UserRole;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import jakarta.persistence.*;

@Entity
@Getter
@Setter
@Accessors(chain = true)
public class Roles {
    @Id
    @Column(columnDefinition = "INT(11) UNSIGNED")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Enumerated(EnumType.STRING)
    private UserRole role = UserRole.LEAD;

    private float priority;
}
