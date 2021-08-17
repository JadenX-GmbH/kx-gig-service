package com.jadenx.kxgigservice.service;

import com.jadenx.kxgigservice.domain.Specialist;
import com.jadenx.kxgigservice.model.SpecialistDTO;
import com.jadenx.kxgigservice.repos.SpecialistRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class SpecialistServiceImpl implements SpecialistService {

    private final SpecialistRepository specialistRepository;

    public SpecialistServiceImpl(final SpecialistRepository specialistRepository) {
        this.specialistRepository = specialistRepository;
    }

    @Override
    public List<SpecialistDTO> findAll() {
        return specialistRepository.findAll()
            .stream()
            .map(specialist -> mapToDTO(specialist, new SpecialistDTO()))
            .collect(Collectors.toList());
    }

    @Override
    public SpecialistDTO get(final Long id) {
        return specialistRepository.findById(id)
            .map(specialist -> mapToDTO(specialist, new SpecialistDTO()))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Override
    public Long create(final SpecialistDTO specialistDTO) {
        final Specialist specialist = new Specialist();
        mapToEntity(specialistDTO, specialist);
        return specialistRepository.save(specialist).getId();
    }

    @Override
    public void update(final Long id, final SpecialistDTO specialistDTO) {
        final Specialist specialist = specialistRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        mapToEntity(specialistDTO, specialist);
        specialistRepository.save(specialist);
    }

    @Override
    public void delete(final Long id) {
        specialistRepository.deleteById(id);
    }

    private SpecialistDTO mapToDTO(final Specialist specialist, final SpecialistDTO specialistDTO) {
        specialistDTO.setId(specialist.getId());
        specialistDTO.setUid(specialist.getUid());
        return specialistDTO;
    }

    private Specialist mapToEntity(final SpecialistDTO specialistDTO, final Specialist specialist) {
        specialist.setUid(specialistDTO.getUid());
        return specialist;
    }

}
