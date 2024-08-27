package com.dr.repository;

import com.dr.models.db.LeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Integer> {

    @Query("FROM LeaveRequest lr WHERE lr.leave.empId=:empId")
    List<LeaveRequest> findLeaveRequestsByEmployeeId(int empId);
}
