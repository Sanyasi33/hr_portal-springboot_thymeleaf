package com.dr.models.db;

import com.dr.enums.Status;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonGetter;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
public class ResignRequest {

    @Id
    @SequenceGenerator(name = "gen4", sequenceName = "RESIGN_SEQ", initialValue = 400, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gen4")
    private Integer id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "EMP_ID")
    @JsonBackReference("rs")
    private Employee resign;

    @JsonGetter("empId")
    public Integer getEmployeeId() {
        return (resign != null) ? resign.getEmpId() : null;
    }

    private LocalDate date;
    private String reason;
    @Enumerated(EnumType.STRING)
    private Status status=Status.PENDING;
}
