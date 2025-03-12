package com.openAi.Attendance;

import lombok.*;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "attendance")
public class Attendance {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String employeeCode;

    private String employeeName;

    private String firstName;

    private String lastName;

    private String projectName;

    private String employeeGrade;

    private String reportingManager;

    private String emailId;

    private Integer presentDays;

    private Integer year;

    private Integer month;

    private Date assignmentStartDate;

    private Date assignmentEndDate;

    private Date dateOfJoining;

    private String employeeFunction;

    private Integer billingPercentage;

    private String businessUnit;

    private Double totalExperience;

    public Attendance(Attendance att) {
        this.id = att.id;
        this.employeeCode = att.employeeCode;
        this.employeeName = att.employeeName;
        this.firstName = att.firstName;
        this.lastName = att.lastName;
        this.projectName = att.projectName;
        this.employeeGrade = att.employeeGrade;
        this.reportingManager = att.reportingManager;
        this.emailId = att.emailId;
        this.presentDays = att.presentDays;
        this.year = att.year;
        this.month = att.month;
        this.assignmentStartDate = att.assignmentStartDate;
        this.assignmentEndDate = att.assignmentEndDate;
        this.employeeFunction = att.employeeFunction;
        this.billingPercentage = att.billingPercentage;
        this.businessUnit = att.businessUnit;
        this.dateOfJoining = att.dateOfJoining;
        this.totalExperience = att.totalExperience;

    }

}
