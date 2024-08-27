package com.dr.models.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.Date;

@Data
public class EmployeeDto {

    private String name;
    private String gender;
    private LocalDate dob;
    private String maritalStatus;
    private String fatherName;
    private String motherName;
    private Long mobileNo;
    private String email;
    private String permanentAddress;
    private String currentAddress;

    private String jobTitle;
    private String manager;
    private LocalDate doj;
    private String empType;
    private String workLocation;
    private Double salary;
    private String password;

    private String bankName;
    private Long accNo;
    private String accHolderName;
    private String branchName;
    private String ifscCode;
}
