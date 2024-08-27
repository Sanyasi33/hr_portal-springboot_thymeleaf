package com.dr.service;

import com.dr.enums.Status;
import com.dr.models.db.Attendance;
import com.dr.models.db.Employee;
import com.dr.repository.AttendanceRepository;
import com.dr.repository.EmployeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class AttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepo;
    @Autowired
    private EmployeeRepository employeeRepo;


    //Employee Action
    public Attendance addCheckIn(int id){
        Optional<Employee> opt=employeeRepo.findById(id);
        if (opt.isPresent()){
            Employee emp= opt.get();

            List<Attendance> attendanceList=emp.getAttendance();
            boolean isCheckInToday=false;           //To check today attendance raised or not
            for (Attendance at:attendanceList){
                if (at.getDate().isEqual(LocalDate.now())){
                    isCheckInToday= true;
                    log.error("Today's attendance already raised at: {}", at.getCheckIn());
                    break;
                }
            }
            if (!isCheckInToday){                  //If attendance not raised then perform checkIn operation
                log.info("Employee {} checked in at: {}", emp.getEmpId(), LocalTime.now());
                Attendance attendance=new Attendance();
                attendance.setAttendance(emp);
                attendance.setDate(LocalDate.now());
                attendance.setCheckIn(LocalTime.now());
                attendance.setStatus(Status.PRESENT);
                return attendanceRepo.save(attendance);
            }
            else
                return null;
        }
        else
            return null;
    }

    //Employee Action
    public String addCheckOut(int id){
        Employee emp=employeeRepo.findById(id).get();

        String at="invalid";      // Declared to assign to check today's checkIn raised or not

        List<Attendance> attendanceList=emp.getAttendance();
        for (Attendance attendance : attendanceList){
            if (attendance.getDate().isEqual(LocalDate.now())) {
                log.info("Employee {} checked out at: {}", emp.getEmpId(), LocalTime.now());
                log.debug("hahaha debug");
                log.trace("hahaha trace");
                attendance.setCheckOut(LocalTime.now());
                attendanceRepo.save(attendance);
                at = "valid";
                break;
            }
        }
        return at;
    }
}
