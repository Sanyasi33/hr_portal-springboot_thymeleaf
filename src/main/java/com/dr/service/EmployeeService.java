package com.dr.service;

import com.dr.models.db.Employee;
import com.dr.models.dto.EmployeeDto;
import com.dr.repository.EmployeeRepository;
import jakarta.mail.MessagingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class EmployeeService {

    @Autowired
    private EmployeeRepository empRepo;

    @Autowired
    private MailService mailService;

    //Admin Action
    public Employee addEmployee(EmployeeDto empDto){
        Employee emp=new Employee();
        BeanUtils.copyProperties(empDto, emp);
        Employee employee=empRepo.save(emp);
        log.info("Employee Added successfully");
        /*mailService.sendEmpRegistrationMail(employee.getEmail(), "New Employee Registration",
                     "Congratulations "+employee.getName()+" Your emp id is: " + employee.getEmpId()+
                                                                      " & Password is: "+employee.getPassword());*/
        try {
            mailService.sendEmpRegistrationMailWithHtmlLink(employee.getEmail(), "New Employee Registration",
                    "Congratulations "+employee.getName()+" Your emp id is: " + employee.getEmpId()+
                            " & Password is: "+employee.getPassword(), "http://localhost:8888/tempLogin");
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        log.info("Mail sent successfully to the employee's registered mail id");
        return employee;
    }

    //Admin Action
    public Employee updateEmployee(int id, EmployeeDto empDto){
        Optional<Employee> opt=empRepo.findById(id);
        if(opt.isPresent()){
            Employee emp=opt.get();
            BeanUtils.copyProperties(empDto, emp);
            Employee e=empRepo.save(emp);
            log.info("Employee updated successfully");
            return e;
        }
        else {
            log.error("Employee details not updated...");
            return null;
        }
    }

    //Admin Action
    public List<Employee> getAllEmployees(){
        return empRepo.findAll();
    }

    //Admin Action
    public Employee getEmployee(int id){
        Optional<Employee> opt=empRepo.findById(id);
        return opt.orElse(null);
    }

    //Employee Action
    public Employee viewSelfDetails(int id){
        Optional<Employee> opt=empRepo.findById(id);
        return opt.orElse(null);
    }

}
