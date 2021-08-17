package com.jadenx.kxgigservice.service;

import com.jadenx.kxgigservice.domain.Gig;
import com.jadenx.kxgigservice.domain.SlaStatement;
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

    public SlaStatementServiceImpl(final SlaStatementRepository slaStatementRepository,
                                   final GigRepository gigRepository) {
        this.slaStatementRepository = slaStatementRepository;
        this.gigRepository = gigRepository;
    }

    @Override
    public List<SlaStatementDTO> findAll() {
        return slaStatementRepository.findAll()
            .stream()
            .map(slaStatement -> mapToDTO(slaStatement, new SlaStatementDTO()))
            .collect(Collectors.toList());
    }

    @Override
    public SlaStatementDTO get(final Long id) {
        return slaStatementRepository.findById(id)
            .map(slaStatement -> mapToDTO(slaStatement, new SlaStatementDTO()))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Override
    public Long create(final SlaStatementDTO slaStatementDTO) {
        final SlaStatement slaStatement = new SlaStatement();
        mapToEntity(slaStatementDTO, slaStatement);
        return slaStatementRepository.save(slaStatement).getId();
    }

    @Override
    public void update(final Long id, final SlaStatementDTO slaStatementDTO) {
        final SlaStatement slaStatement = slaStatementRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        mapToEntity(slaStatementDTO, slaStatement);
        slaStatementRepository.save(slaStatement);
    }

    @Override
    public void delete(final Long id) {
        slaStatementRepository.deleteById(id);
    }

    private SlaStatementDTO mapToDTO(final SlaStatement slaStatement,
                                     final SlaStatementDTO slaStatementDTO) {
        slaStatementDTO.setId(slaStatement.getId());
        slaStatementDTO.setSubject(slaStatement.getSubject());
        slaStatementDTO.setRestriction(slaStatement.getRestriction());
        slaStatementDTO.setIsActive(slaStatement.getIsActive());
        slaStatementDTO.setGig(slaStatement.getGig() == null ? null : slaStatement.getGig().getId());
        return slaStatementDTO;
    }

    private SlaStatement mapToEntity(final SlaStatementDTO slaStatementDTO,
                                     final SlaStatement slaStatement) {
        slaStatement.setSubject(slaStatementDTO.getSubject());
        slaStatement.setRestriction(slaStatementDTO.getRestriction());
        slaStatement.setIsActive(slaStatementDTO.getIsActive());
        if (slaStatementDTO.getGig() != null
            && (slaStatement.getGig() == null
            || !slaStatement.getGig().getId().equals(slaStatementDTO.getGig()))) {
            final Gig gig = gigRepository.findById(slaStatementDTO.getGig())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "gig not found"));
            slaStatement.setGig(gig);
        }
        return slaStatement;
    }

}
