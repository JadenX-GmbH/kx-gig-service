package com.jadenx.kxgigservice.service;

import com.jadenx.kxgigservice.model.SpecialistDTO;

import java.util.List;


public interface SpecialistService {

    List<SpecialistDTO> findAll();

    SpecialistDTO get(final Long id);

    Long create(final SpecialistDTO specialistDTO);

    void update(final Long id, final SpecialistDTO specialistDTO);

    void delete(final Long id);

}
