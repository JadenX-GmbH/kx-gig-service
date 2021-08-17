package com.jadenx.kxgigservice.repos;

import com.jadenx.kxgigservice.domain.Gig;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;


public interface GigRepository extends JpaRepository<Gig, Long> {
    Optional<Gig> getAllByDataOwner_Uid(UUID uid);
    Optional<Gig> getAllByGigChosenSpecialistCandidateSpecialists_Uid(UUID uid);
}
