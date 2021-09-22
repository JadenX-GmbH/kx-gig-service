package com.jadenx.kxgigservice.repos;

import com.jadenx.kxgigservice.domain.CandidateSpecialist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;


public interface CandidateSpecialistRepository extends JpaRepository<CandidateSpecialist, Long> {
    List<CandidateSpecialist> findAllByUidIn(List<UUID> uids);
    CandidateSpecialist findByUid(UUID uid);
}
