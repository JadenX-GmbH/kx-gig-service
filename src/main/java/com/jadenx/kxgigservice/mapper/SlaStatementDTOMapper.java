package com.jadenx.kxgigservice.mapper;

import com.jadenx.kxgigservice.domain.Gig;
import com.jadenx.kxgigservice.domain.SlaStatement;
import com.jadenx.kxgigservice.model.SlaStatementDTO;
import com.jadenx.kxgigservice.repos.GigRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class SlaStatementDTOMapper {

    private final GigRepository gigRepository;

    public SlaStatementDTOMapper(final GigRepository gigRepository) {
        this.gigRepository = gigRepository;
    }

    public SlaStatementDTO mapToDTO(final SlaStatement slaStatement,
                                                 final SlaStatementDTO slaStatementDTO) {
        slaStatementDTO.setId(slaStatement.getId());
        slaStatementDTO.setSubject(slaStatement.getSubject());
        slaStatementDTO.setRestriction(slaStatement.getRestriction());
        slaStatementDTO.setIsActive(slaStatement.getIsActive());
        slaStatementDTO.setGig(slaStatement.getGig() == null ? null : slaStatement.getGig().getId());
        return slaStatementDTO;
    }

    public SlaStatement mapToEntity(final SlaStatementDTO slaStatementDTO,
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
