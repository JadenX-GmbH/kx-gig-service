package com.jadenx.kxgigservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jadenx.kxgigservice.model.ContractDTO;
import com.jadenx.kxgigservice.model.OfferDTO;
import com.jadenx.kxgigservice.model.PaginatedResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;


public interface OfferService {

    PaginatedResponse<?> findAll(final Pageable pageable);

    PaginatedResponse<?> getOffersByOwnerOrSpecialist(UUID userId, Pageable pageable);

    OfferDTO get(final Long id);

    List<OfferDTO> getOfferByGigAndSpecialist(final Long id , final UUID uid);

    Long create(final OfferDTO offerDTO, final UUID uid);

    void update(final Long id, final OfferDTO offerDTO);

    void delete(final Long id);

    PaginatedResponse<?> getOffersByGigId(final Long gigId, Pageable pageable);

    void acceptOffer(final Long id, final Boolean accepted, final String user) throws JsonProcessingException;

    ContractDTO getContractByOfferId(final Long id);
}
