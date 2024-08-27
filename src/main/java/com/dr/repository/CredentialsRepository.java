package com.dr.repository;

import com.dr.models.db.Credentials;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CredentialsRepository extends JpaRepository<Credentials, Integer> {

    Credentials findByUsername(String username);
}
