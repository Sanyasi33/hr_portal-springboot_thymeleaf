package com.dr.models.db;

import com.dr.enums.Status;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Entity
public class Employee {

    @Id
    @SequenceGenerator(name = "gen1", sequenceName = "EMP_SEQ", initialValue = 100, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gen1")
    private Integer empId;
    private String name;
    private String gender;
    private LocalDate dob;
    private String maritalStatus;
    private String fatherName;
    private String motherName;
    private Long mobileNo;
    private String email;
    @Column(name = "P_ADDRESS")
    private String permanentAddress;
    @Column(name = "C_ADDRESS")
    private String currentAddress;

    private String jobTitle;
    private String manager;
    private LocalDate doj;
    private String empType;
    private String workLocation;
    private Double salary;
    @Enumerated(EnumType.STRING)
    private Status status= Status.ACTIVE;
    private String password;

    private String bankName;
    private Long accNo;
    private String accHolderName;
    private String branchName;
    private String ifscCode;

    @OneToMany(mappedBy = "leave", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("lv")
    @JsonIgnore
    private List<LeaveRequest> leaveRequest;

    @OneToMany(mappedBy = "attendance", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("at")
    @JsonIgnore
    private List<Attendance> attendance;

    @OneToOne(mappedBy = "resign", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("rs")
    @JsonIgnore
    private ResignRequest resignRequest;
}
