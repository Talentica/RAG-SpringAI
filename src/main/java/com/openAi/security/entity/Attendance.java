package com.openAi.security.entity;

import lombok.*;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Date;

@Entity
@Data
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

  }

}
