package com.openAi.security.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@Accessors(chain = true)
public class CustomerCredentials {
    @Id
    @Column(columnDefinition = "INT(11) UNSIGNED")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String userName;
    private String userEmail;
    private String password;
    private Boolean isReset;
    private Date pwdGeneratedDate;
    private Integer loginCount;
    private Boolean tourClosed;
}
