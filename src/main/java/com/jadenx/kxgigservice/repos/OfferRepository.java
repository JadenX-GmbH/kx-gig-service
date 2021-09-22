package com.jadenx.kxgigservice.repos;

import com.jadenx.kxgigservice.domain.Offer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;


public interface OfferRepository extends JpaRepository<Offer, Long> {

    Page<Offer> getAllByGig_DataOwner_UidOrSpecialist_Uid(UUID doUid, UUID dsUid, Pageable pageable);
    Page<Offer> findAllByGig_IdOrderByAcceptedDesc(Long gigId, Pageable pageable);
    List<Offer> getAllByGigId_AndSpecialist_Uid(Long gigId, UUID dsUid);


}
