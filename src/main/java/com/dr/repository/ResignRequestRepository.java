package com.dr.repository;

import com.dr.models.db.ResignRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ResignRequestRepository extends JpaRepository<ResignRequest, Integer> {

    @Query("FROM ResignRequest rr WHERE rr.resign.empId=:id")
    List<ResignRequest> findResignRequestsByEmployeeId(int id);
}
