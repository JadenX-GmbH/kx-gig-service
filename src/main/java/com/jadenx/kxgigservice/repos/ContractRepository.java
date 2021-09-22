package com.jadenx.kxgigservice.repos;

import com.jadenx.kxgigservice.domain.Contract;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface ContractRepository extends JpaRepository<Contract, Long> {

    List<Contract> findContractsByOffer_AcceptedTrueAndOffer_Gig_Id(Long gigId);

    Optional<Contract> findContractByOffer_AcceptedTrueAndOffer_Id(Long offerId);
}
