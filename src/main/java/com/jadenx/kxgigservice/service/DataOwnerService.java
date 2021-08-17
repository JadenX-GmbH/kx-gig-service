package com.jadenx.kxgigservice.service;

import com.jadenx.kxgigservice.model.DataOwnerDTO;

import java.util.List;


public interface DataOwnerService {

    List<DataOwnerDTO> findAll();

    DataOwnerDTO get(final Long id);

    Long create(final DataOwnerDTO dataOwnerDTO);

    void update(final Long id, final DataOwnerDTO dataOwnerDTO);

    void delete(final Long id);

}
