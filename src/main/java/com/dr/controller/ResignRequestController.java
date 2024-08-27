package com.dr.controller;

import com.dr.models.db.Employee;
import com.dr.models.db.ResignRequest;
import com.dr.repository.EmployeeRepository;
import com.dr.service.ResignRequestService;
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
@Tag(name = "Resign Request Controller", description = "In this controller all the resign related APIs developed")
public class ResignRequestController {

    @Autowired
    private ResignRequestService resignRequestService;

    @Autowired
    private EmployeeRepository empRepo;

    // Employee Action
    @GetMapping("/applyResign/{id}")
    public String applyResign(@PathVariable int id, Model model){
        Employee employee=empRepo.findById(id).get();
        model.addAttribute("empId", id);
        model.addAttribute("empName", employee.getName());
        model.addAttribute("resignDate", LocalDate.now());
        return "applyResign";
    }

    //Employee Action
    @Operation(summary = "Put Resignation letter", description = "Employee will enter empId to put resignation letter")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Resignation letter submitted successfully"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PostMapping("/applyResign/{id}")
    public String resignRequest(@PathVariable int id, @RequestParam String reason){
        String msg="";
        try {
            ResignRequest resignRequest = resignRequestService.resignRequest(id, reason);
            msg= URLEncoder.encode("Resignation request submitted successfully", StandardCharsets.UTF_8);
            return "redirect:/applyResign/" + id + "?status=success&msg=" + msg;
        }
        catch (Exception e){
            msg= URLEncoder.encode("Something Went Wrong Please Try Again Later", StandardCharsets.UTF_8);
            return "redirect:/applyResign/" + id + "?status=fail&msg=" + msg;
        }
    }

    //HR Action
    @Operation(summary = "View all resignation requests", description = "Admin can view all resignation requests")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Resignation requests fetched successfully"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/getResignRequests")
    public String getResignRequests(Model model){
        model.addAttribute("resignRequests", resignRequestService.getResignRequests());
        return "getResignRequests";
    }

    //HR Action
    @Operation(summary = "Approve resignation request", description = "HR can approve a resignation request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Resignation request approved successfully"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PostMapping("/approveResign/{id}")
    public String approveResign(@PathVariable int id){
        ResignRequest resignRequest = resignRequestService.approveResign(id);
        String msg="";
        if (resignRequest!=null){
            msg=URLEncoder.encode("Resign Request Approved Successfully!!", StandardCharsets.UTF_8);
            return "redirect:/getResignRequests?status=success&msg=" + msg;
        }
        else{
            msg=URLEncoder.encode("Failed to Approve Resign Request", StandardCharsets.UTF_8);
            return "redirect:/getResignRequests?status=fail&msg=" + msg;
        }
    }

    //HR Action
    @Operation(summary = "Reject resignation request", description = "HR can reject resignation request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Resignation request rejected successfully"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PostMapping("/rejectResign/{id}")
    public String  rejectResign(@PathVariable int id){
        ResignRequest resignRequest=resignRequestService.rejectResign(id);
        String msg="";
        if (resignRequest!=null){
            msg=URLEncoder.encode("Resign Request Rejected Successfully!!", StandardCharsets.UTF_8);
            return "redirect:/getResignRequests?status=success&msg=" + msg;
        }
        else {
            msg=URLEncoder.encode("Failed to Reject Resign Request", StandardCharsets.UTF_8);
            return "redirect:/getResignRequests?status=fail&msg=" + msg;
        }
    }

    // ************************** View Leave requests status *************************
    @GetMapping("/resignStatus/{id}")
    public String leaveStatus(@PathVariable int id, Model model){
        model.addAttribute("resignStatus", resignRequestService.resignRequests(id));
        model.addAttribute("empId", id);
        return "resignStatus";
    }
}
