package com.jadenx.kxgigservice.service;

import com.jadenx.kxgigservice.model.SlaStatementDTO;

import java.util.List;


public interface SlaStatementService {

    List<SlaStatementDTO> findAll();

    SlaStatementDTO get(final Long id);

    Long create(final SlaStatementDTO slaStatementDTO);

    void update(final Long id, final SlaStatementDTO slaStatementDTO);

    void delete(final Long id);

}
