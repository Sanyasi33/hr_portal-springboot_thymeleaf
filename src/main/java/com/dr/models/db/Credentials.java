package com.dr.models.db;

import com.dr.enums.Roles;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Credentials {

    @Id
    @SequenceGenerator(name = "gen", sequenceName = "CREDENTIAL_SEQ", initialValue = 10, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "gen")
    private Integer id;

    private String username;

    private String password;

    private String roles;
}
