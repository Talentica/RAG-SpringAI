package com.openAi.security.entity;

import lombok.*;
import lombok.experimental.Accessors;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@Builder
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_log")
public class UserLog {
    @Id
    @Column(columnDefinition = "INT(11) UNSIGNED")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private Integer userId;

    private Date logInDate;
}