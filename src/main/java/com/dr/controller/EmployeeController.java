package com.dr.controller;

import com.dr.models.db.Employee;
import com.dr.models.dto.EmployeeDto;
import com.dr.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.List;

@Controller
@Tag(name ="Employee Controller", description = "This is Employee APIs for Employee management operations ")
@Slf4j
public class EmployeeController {

    @Autowired
    private EmployeeService empService;

    //HR Action
    @GetMapping("/addEmployee")
    public String addEmployee(){
        return "addEmployee";
    }

    //HR Action
    @Operation(summary = "Add new Employee", description = "Only admin can add new employee")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Employee added successfully"),
            @ApiResponse(responseCode = "400", description = "wrong")
    })
    @PostMapping("/addEmployee")
    public String addEmployee(@ModelAttribute EmployeeDto employeeDto){
        String msg="";
        /*if (employeeDto.getDob().isBefore(LocalDate.now())){

        }*/
        try{
            Employee employee = empService.addEmployee(employeeDto);
            msg= URLEncoder.encode("Employee Details Added Successfully", StandardCharsets.UTF_8);
            return "redirect:addEmployee?status=success&msg="+msg;
        }
        catch (Exception e){
            log.info(e.getMessage());
            msg= URLEncoder.encode("Employee already exist with Mail id: "+ employeeDto.getEmail(), StandardCharsets.UTF_8);
            return "redirect:addEmployee?status=fail&msg="+msg;
        }
    }

    @GetMapping("/getIdForUpdate")
    public String getIdForUpdate(){
        return "getIdForUpdate";
    }

    @GetMapping("/updateEmployee")
    public String updateEmployee(@RequestParam int id, Model model){
        Employee employee = empService.getEmployee(id);
        if (employee!=null){
            model.addAttribute("employee", employee);
            return "updateEmployee";
        }
        else{
            String msg=URLEncoder.encode("Please Enter valid Emp Id", StandardCharsets.UTF_8);
            return "redirect:/getIdForUpdate?status=fail&msg="+msg;
        }
    }

    //HR Action
    @Operation(summary = "Update employee details", description = "Only admin can update the employee details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employee details updated successfully"),
            @ApiResponse(responseCode = "404", description = "Employee not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PostMapping("/updateEmployee")
    public String updateEmployee(@RequestParam int id, @ModelAttribute EmployeeDto employeeDto){
        String msg = "";  // to store the success/failure message in URL encoded format
        try {
            Employee employee = empService.updateEmployee(id, employeeDto);
            msg=URLEncoder.encode("Employee Details Updated Successfully", StandardCharsets.UTF_8);
            return "redirect:/updateEmployee?status=success&msg=" + msg + "&id=" + id;
        }
        catch (Exception e){
            msg=URLEncoder.encode("Something Went Wrong Please Try Again Later", StandardCharsets.UTF_8);
            return "redirect:/updateEmployee?status=fail&msg=" + msg + "&id=" + id;
        }
    }

    //Admin Action
    @Operation(summary = "Get all employee details", description = "Only admin can see the all employee details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employee details fetched successfully"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/getAllEmployees")
    public String getAllEmployees(Model model){
        List<Employee> employees = empService.getAllEmployees();
        model.addAttribute("employees", employees);
        return "getAllEmployees";
    }

    @GetMapping("/getIdForSingleEmp")
    public String getIdForSingleEmp(){
        return "getIdForSingleEmp";
    }

    //Admin Action
    @Operation(summary = "Get single emp record by using empId", description = "Only admin can see the emp details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employee details fetched successfully"),
            @ApiResponse(responseCode = "404", description = "Employee not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/getSingleEmp")
    public String getEmployee(@RequestParam int empId, Model model){
        Employee employee = empService.getEmployee(empId);
        if (employee!=null){
            model.addAttribute("employee", employee);
            return "getSingleEmp";
        }
        else{
            String msg=URLEncoder.encode("Please Enter valid Emp Id", StandardCharsets.UTF_8);
            return "redirect:/getIdForSingleEmp?status=fail&msg="+msg;
        }
    }

    //Employee Action
    @Operation(summary = "View employee details", description = "Only employee can view their details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employee details fetched successfully"),
            @ApiResponse(responseCode = "404", description = "Employee not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @GetMapping("/viewSelfDetails/{id}")
    public String viewSelfDetails(@PathVariable int id, Model model){
        model.addAttribute("employee", empService.viewSelfDetails(id));
        return "viewSelfDetails";
    }



    //********************************** Emp Welcome Page **********************************//

    @GetMapping("/empWelcome")
    public String empWelcomePage(){
        return "empWelcome";
    }

    @GetMapping("/empHome")
    public String empHome(@RequestParam int empId, Model model){
        Employee employee = empService.getEmployee(empId);
        if(employee!=null){
            model.addAttribute("empId", empId);
            return "empHome";
        }
        else {
            String msg = URLEncoder.encode("Employee does not exist with Emp Id: "+ empId, StandardCharsets.UTF_8);
            return "redirect:/empWelcome?status=fail&msg="+msg;
        }
    }
}
