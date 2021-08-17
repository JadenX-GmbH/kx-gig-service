package com.jadenx.kxgigservice.service;

import com.jadenx.kxgigservice.model.GigDTO;

import java.util.List;
import java.util.UUID;


public interface GigService {

    List<GigDTO> findAll();

    List<GigDTO> getGigsByDataOwner(UUID uid);

    List<GigDTO> getGigsBySpecialist(UUID uid);

    GigDTO get(final Long id);

    Long create(final GigDTO gigDTO);

    void update(final Long id, final GigDTO gigDTO);

    void delete(final Long id);

}
