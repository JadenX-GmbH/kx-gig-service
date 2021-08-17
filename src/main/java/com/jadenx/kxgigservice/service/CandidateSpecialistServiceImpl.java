package com.jadenx.kxgigservice.service;

import com.jadenx.kxgigservice.domain.CandidateSpecialist;
import com.jadenx.kxgigservice.model.CandidateSpecialistDTO;
import com.jadenx.kxgigservice.repos.CandidateSpecialistRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class CandidateSpecialistServiceImpl implements CandidateSpecialistService {

    private final CandidateSpecialistRepository candidateSpecialistRepository;

    public CandidateSpecialistServiceImpl(
        final CandidateSpecialistRepository candidateSpecialistRepository) {
        this.candidateSpecialistRepository = candidateSpecialistRepository;
    }

    @Override
    public List<CandidateSpecialistDTO> findAll() {
        return candidateSpecialistRepository.findAll()
            .stream()
            .map(candidateSpecialist -> mapToDTO(candidateSpecialist, new CandidateSpecialistDTO()))
            .collect(Collectors.toList());
    }

    @Override
    public CandidateSpecialistDTO get(final Long id) {
        return candidateSpecialistRepository.findById(id)
            .map(candidateSpecialist -> mapToDTO(candidateSpecialist, new CandidateSpecialistDTO()))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Override
    public Long create(final CandidateSpecialistDTO candidateSpecialistDTO) {
        final CandidateSpecialist candidateSpecialist = new CandidateSpecialist();
        mapToEntity(candidateSpecialistDTO, candidateSpecialist);
        return candidateSpecialistRepository.save(candidateSpecialist).getId();
    }

    @Override
    public void update(final Long id, final CandidateSpecialistDTO candidateSpecialistDTO) {
        final CandidateSpecialist candidateSpecialist = candidateSpecialistRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        mapToEntity(candidateSpecialistDTO, candidateSpecialist);
        candidateSpecialistRepository.save(candidateSpecialist);
    }

    @Override
    public void delete(final Long id) {
        candidateSpecialistRepository.deleteById(id);
    }

    private CandidateSpecialistDTO mapToDTO(final CandidateSpecialist candidateSpecialist,
                                            final CandidateSpecialistDTO candidateSpecialistDTO) {
        candidateSpecialistDTO.setId(candidateSpecialist.getId());
        candidateSpecialistDTO.setUid(candidateSpecialist.getUid());
        return candidateSpecialistDTO;
    }

    private CandidateSpecialist mapToEntity(final CandidateSpecialistDTO candidateSpecialistDTO,
                                            final CandidateSpecialist candidateSpecialist) {
        candidateSpecialist.setUid(candidateSpecialistDTO.getUid());
        return candidateSpecialist;
    }

}
