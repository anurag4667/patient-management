package org.pm.patientservice.repository;

import org.pm.patientservice.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PatientRepository extends JpaRepository<Patient, UUID> {
    boolean existsByEmail(String email);
}
