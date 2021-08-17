package com.jadenx.kxgigservice.service;

import com.jadenx.kxgigservice.model.CandidateSpecialistDTO;

import java.util.List;


public interface CandidateSpecialistService {

    List<CandidateSpecialistDTO> findAll();

    CandidateSpecialistDTO get(final Long id);

    Long create(final CandidateSpecialistDTO candidateSpecialistDTO);

    void update(final Long id, final CandidateSpecialistDTO candidateSpecialistDTO);

    void delete(final Long id);

}
