package com.dr.controller;

import com.dr.models.db.Attendance;
import com.dr.models.db.Employee;
import com.dr.repository.EmployeeRepository;
import com.dr.service.AttendanceService;
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
import java.time.LocalTime;

@Controller
@Tag(name = "Attendance Controller", description = "In this controller all attendance related APIs developed")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private EmployeeRepository empRepo;

    // Employee Action
    // Attendance Link Controlling
    @GetMapping("/markAttendance/{id}")
    public String markAttendance(@PathVariable int id, Model model){
        model.addAttribute("empId", id);
        return "markAttendance";
    }

    @Operation(summary = "Add Check-In Form", description = "Enter EmpId it will Show the Name with Date & Time")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Check-In Form displayed successfully"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/addCheckIn/{id}")
    public String addCheckIn(@PathVariable int id, Model model){
        Employee emp=empRepo.findById(id).get();
        model.addAttribute("empId", id);
        model.addAttribute("name", emp.getName());
        model.addAttribute("checkInDate", LocalDate.now());
        model.addAttribute("checkInTime", LocalTime.now());
        return "addCheckIn";
    }

    //Employee Action
    @Operation(summary = "Add check-in", description = "Enter empId to add check-in")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Check-in added successfully"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PostMapping("/addCheckIn/{id}")
    public String addCheckIn(@PathVariable int id){
        Attendance attendance=attendanceService.addCheckIn(id);
        String msg = "";
        if (attendance!=null){
            msg= URLEncoder.encode("CheckIn added Successfully", StandardCharsets.UTF_8);
            return "redirect:/addCheckIn/" + id + "?status=success&msg=" + msg;
        }
        else{
            msg= URLEncoder.encode("Today's Check-In already raised", StandardCharsets.UTF_8);
            return "redirect:/addCheckIn/" + id + "?status=fail&msg=" + msg;
        }
    }

    @Operation(summary = "Add Check-Out Form", description = "Enter EmpId it will Show the Name with Date & Time")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Check-Out Form displayed successfully"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/addCheckOut/{id}")
    public String addCheckOut(@PathVariable int id, Model model){
        Employee emp=empRepo.findById(id).get();
        model.addAttribute("empId", id);
        model.addAttribute("name", emp.getName());
        model.addAttribute("checkInDate", LocalDate.now());
        model.addAttribute("checkInTime", LocalTime.now());
        return "addCheckOut";
    }

    //Employee Action
    @Operation(summary = "Add check-out", description = "Enter empId to add check-out")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Check-out added successfully"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PostMapping("/addCheckOut/{id}")
    public String addCheckOut(@PathVariable int id){
        String result=attendanceService.addCheckOut(id);
        String msg = "";
        if (result.equals("valid")){
            msg= URLEncoder.encode("Check-Out added Successfully", StandardCharsets.UTF_8);
            return "redirect:/addCheckOut/" + id + "?status=success&msg=" + msg;
        }
        else{
            msg= URLEncoder.encode("Today's CheckIn has not been Raised", StandardCharsets.UTF_8);
            return "redirect:/addCheckOut/" + id + "?status=fail&msg=" + msg;
        }
    }

}
