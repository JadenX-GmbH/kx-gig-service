package com.jadenx.kxgigservice.repos;

import com.jadenx.kxgigservice.domain.Specialist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface SpecialistRepository extends JpaRepository<Specialist, Long> {
    Optional<Specialist> findByUid(UUID uid);
    List<Specialist> findAll();
    boolean existsSpecialistByUid(UUID uid);

}
