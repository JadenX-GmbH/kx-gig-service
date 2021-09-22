package com.jadenx.kxgigservice.repos;

import com.jadenx.kxgigservice.domain.Gig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;


public interface GigRepository extends JpaRepository<Gig, Long> {

    Page<Gig>  findAllByDataOwner_Uid(UUID uid, final Pageable pageable);
    Page<Gig>  findAllByGigChosenSpecialistCandidateSpecialists_Uid(UUID uid, final Pageable pageable);
}
