package com.dr.models.db;

import com.dr.enums.Status;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonGetter;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
public class LeaveRequest {

    @Id
    @SequenceGenerator(name = "gen2", sequenceName = "LEAVE_SEQ", initialValue = 200, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gen2")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EMP_ID")
    @JsonBackReference("lv")
    private Employee leave;

    @JsonGetter("empId")
    public Integer getEmpId(){
        return (leave != null) ? leave.getEmpId() : null;
    }

    private String leaveType;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer noOfDays;
    private String reason;
    @Enumerated(EnumType.STRING)
    private Status status=Status.PENDING;
}
