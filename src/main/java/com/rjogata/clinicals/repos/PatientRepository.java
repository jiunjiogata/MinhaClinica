package com.rjogata.clinicals.repos;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rjogata.clinicals.model.Patient;

public interface PatientRepository extends JpaRepository<Patient, Integer> {

}
