package com.jadenx.kxgigservice.service;

import com.jadenx.kxgigservice.model.ContractDTO;

import java.util.List;


public interface ContractService {

    List<ContractDTO> findAll();

    ContractDTO get(final Long id);

    Long create(final ContractDTO contractDTO);

    void update(final Long id, final ContractDTO contractDTO);

    void delete(final Long id);

}
