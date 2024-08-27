package com.dr.controller;

import com.dr.models.db.LeaveRequest;
import com.dr.models.dto.LeaveRequestDto;
import com.dr.service.LeaveRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

@Controller
@Tag(name = "Leave Request Controller", description = "In this controller all the leave related APIs developed")
public class LeaveRequestController {

    @Autowired
    private LeaveRequestService leaveRequestService;

    // Employee Action
    @Operation(summary = "Leave Form", description = "It's taking empId as pathVariable to display the leave form")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Leave request form displayed successfully"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/applyLeave/{id}")
    public String applyLeave(@PathVariable int id, Model model){
        model.addAttribute("empId", id);
        return "applyLeave";
    }

    //Employee
    @Operation(summary = "Apply for leave", description = "Employee will enter empId to send leave request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Leave request sent successfully"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PostMapping("/applyLeave/{id}")
    public String applyLeave(@PathVariable int id, @ModelAttribute LeaveRequestDto leaveRequestDto,
                             @RequestParam LocalDate startDate, @RequestParam LocalDate endDate){
        String msg="";
        if (startDate.isBefore(LocalDate.now())){       // Start Date Validation
            msg= URLEncoder.encode("Start date should not be before today's date", StandardCharsets.UTF_8);
            return "redirect:/applyLeave/" + id + "?status=fail&msg=" + msg;
        }
        else if (endDate.isBefore(startDate)){         // End Date Validation
            msg= URLEncoder.encode("End date should be after start date", StandardCharsets.UTF_8);
            return "redirect:/applyLeave/" + id + "?status=fail&msg=" + msg;
        }
        else {
            try {
                leaveRequestService.applyLeave(id, leaveRequestDto);
                msg= URLEncoder.encode("Leave Request Sent Successfully", StandardCharsets.UTF_8);
                return "redirect:/applyLeave/" + id + "?status=success&msg=" + msg;
            }
            catch (Exception e){
                msg= URLEncoder.encode("Something Went Wrong Please try Again Later", StandardCharsets.UTF_8);
                return "redirect:/applyLeave/" + id + "?status=fail&msg=" + msg;
            }
        }
    }

    //HR Action
    @Operation(summary = "View all applied leave requests", description = "Admin can see all the applied leaves")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All leave requests retrieved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/getAppliedLeaves")
    public String viewAppliedLeaves(Model model){
        model.addAttribute("leaveRequests", leaveRequestService.getAppliedLeaves());
        return "getAppliedLeaves";
    }

    //HR Action
    @Operation(summary = "Approve leave request", description = "Admin will enter leave id to approve leave request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Leave request approved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PostMapping("/approveLeave/{id}")
    public String approveLeave(@PathVariable int id){
        LeaveRequest leaveRequest= leaveRequestService.approveLeave(id);
        String msg="";
        if (leaveRequest!=null){
            msg= URLEncoder.encode("Leave Request Approved Successfully", StandardCharsets.UTF_8);
            return "redirect:/getAppliedLeaves?status=success&msg=" + msg;
        }
        else {
            msg= URLEncoder.encode("Failed to approve leave request", StandardCharsets.UTF_8);
            return "redirect:/getAppliedLeaves?status=fail&msg=" + msg;
        }
    }

    //HR Action
    @Operation(summary = "Reject leave request", description = "Admin will enter leave id to reject leave request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Leave request rejected successfully"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PostMapping("/rejectLeave/{id}")
    public String rejectLeave(@PathVariable int id){
        LeaveRequest leaveRequest=leaveRequestService.rejectLeave(id);
        String msg="";
        if (leaveRequest!=null){
            msg= URLEncoder.encode("Leave Request Rejected Successfully", StandardCharsets.UTF_8);
            return "redirect:/getAppliedLeaves?status=success&msg=" + msg;
        }
        else {
            msg= URLEncoder.encode("Failed to reject leave request", StandardCharsets.UTF_8);
            return "redirect:/getAppliedLeaves?status=fail&msg=" + msg;
        }
    }

    // ************************** View Leave requests status *************************
    @GetMapping("/leaveStatus/{id}")
    public String leaveStatus(@PathVariable int id, Model model){
        model.addAttribute("leaveStatus", leaveRequestService.leaveRequests(id));
        model.addAttribute("empId", id);
        return "leaveStatus";
    }
}
