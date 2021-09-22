package com.jadenx.kxgigservice.service;

import com.jadenx.kxgigservice.domain.SlaStatement;
import com.jadenx.kxgigservice.mapper.SlaStatementDTOMapper;
import com.jadenx.kxgigservice.model.SlaStatementDTO;
import com.jadenx.kxgigservice.repos.GigRepository;
import com.jadenx.kxgigservice.repos.SlaStatementRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class SlaStatementServiceImpl implements SlaStatementService {

    private final SlaStatementRepository slaStatementRepository;
    private final GigRepository gigRepository;
    private final SlaStatementDTOMapper slaStatementDTOMapper;

    public SlaStatementServiceImpl(final SlaStatementRepository slaStatementRepository,
                                   final GigRepository gigRepository,
                                   final SlaStatementDTOMapper slaStatementDTOMapper) {
        this.slaStatementRepository = slaStatementRepository;
        this.gigRepository = gigRepository;
        this.slaStatementDTOMapper = slaStatementDTOMapper;
    }

    @Override
    public List<SlaStatementDTO> findAll() {
        return slaStatementRepository.findAll()
            .stream()
            .map(slaStatement -> slaStatementDTOMapper.mapToDTO(slaStatement, new SlaStatementDTO()))
            .collect(Collectors.toList());
    }

    @Override
    public SlaStatementDTO get(final Long id) {
        return slaStatementRepository.findById(id)
            .map(slaStatement -> slaStatementDTOMapper.mapToDTO(slaStatement, new SlaStatementDTO()))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Override
    public Long create(final SlaStatementDTO slaStatementDTO) {
        final SlaStatement slaStatement = new SlaStatement();
        slaStatementDTOMapper.mapToEntity(slaStatementDTO, slaStatement);
        return slaStatementRepository.save(slaStatement).getId();
    }

    @Override
    public void update(final Long id, final SlaStatementDTO slaStatementDTO) {
        final SlaStatement slaStatement = slaStatementRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        slaStatementDTOMapper.mapToEntity(slaStatementDTO, slaStatement);
        slaStatementRepository.save(slaStatement);
    }

    @Override
    public void delete(final Long id) {
        slaStatementRepository.deleteById(id);
    }

}
