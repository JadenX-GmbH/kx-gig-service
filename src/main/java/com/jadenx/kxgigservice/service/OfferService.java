package com.jadenx.kxgigservice.service;

import com.jadenx.kxgigservice.model.OfferDTO;

import java.util.List;


public interface OfferService {

    List<OfferDTO> findAll();

    OfferDTO get(final Long id);

    Long create(final OfferDTO offerDTO);

    void update(final Long id, final OfferDTO offerDTO);

    void delete(final Long id);

}
