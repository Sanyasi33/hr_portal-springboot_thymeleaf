package com.dr.models.dto;

import com.dr.enums.LeaveType;
import lombok.Data;

import java.time.LocalDate;

@Data
public class LeaveRequestDto {

    private String leaveType;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer noOfDays;
    private String reason;
}
