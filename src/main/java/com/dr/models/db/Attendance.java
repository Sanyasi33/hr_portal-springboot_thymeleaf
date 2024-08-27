package com.dr.models.db;

import com.dr.enums.Status;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Entity
public class Attendance {

    @Id
    @SequenceGenerator(name = "gen3", sequenceName = "ATTENDANCE_SEQ", initialValue = 300, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gen3")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EMP_ID")
    @JsonBackReference("at")
    private Employee attendance;

    private LocalDate date;
    private LocalTime checkIn;
    private LocalTime checkOut;
    @Enumerated(EnumType.STRING)
    private Status status;
}
