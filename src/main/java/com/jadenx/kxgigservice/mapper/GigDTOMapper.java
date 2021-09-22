package com.jadenx.kxgigservice.mapper;

import com.jadenx.kxgigservice.domain.*;
import com.jadenx.kxgigservice.model.GigDTO;
import com.jadenx.kxgigservice.model.SkillsetDTO;
import com.jadenx.kxgigservice.model.SlaStatementDTO;
import com.jadenx.kxgigservice.repos.CandidateSpecialistRepository;
import com.jadenx.kxgigservice.repos.DataOwnerRepository;
import com.jadenx.kxgigservice.repos.SkillsetRepository;
import com.jadenx.kxgigservice.repos.SlaStatementRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GigDTOMapper {

    private final DataOwnerRepository dataOwnerRepository;
    private final CandidateSpecialistRepository candidateSpecialistRepository;
    private final SlaStatementDTOMapper slaStatementDTOMapper;
    private final SkillsetDTOMapper skillsetDTOMapper;
    private final SlaStatementRepository slaStatementRepository;
    private final SkillsetRepository skillsetRepository;


    public GigDTO mapToDTO(final Gig gig, final GigDTO gigDTO) {
        gigDTO.setId(gig.getId());
        gigDTO.setMaxPrice(gig.getMaxPrice());
        gigDTO.setPrice(gig.getPrice());
        gigDTO.setPriceToken(gig.getPriceToken());
        gigDTO.setTitle(gig.getTitle());
        gigDTO.setDescription(gig.getDescription());
        gigDTO.setExecutionType(gig.getExecutionType());
        gigDTO.setStatus(gig.getStatus());
        gigDTO.setExecutionpool(gig.getExecutionpool());
        gigDTO.setIsActive(gig.getIsActive());
        gigDTO.setDataOwner(gig.getDataOwner() == null ? null : gig.getDataOwner().getUid());
        gigDTO.setGigChosenSpecialists(gig.getGigChosenSpecialistCandidateSpecialists() == null
            ? null : gig.getGigChosenSpecialistCandidateSpecialists().stream()
            .map(CandidateSpecialist::getUid)
            .collect(Collectors.toList()));
        gigDTO.setSlaStatementDTOList(gig.getGigSlaStatements() == null
            ? null : gig.getGigSlaStatements().stream()
            .map(slaStatement -> slaStatementDTOMapper.mapToDTO(slaStatement, new SlaStatementDTO()))
            .collect(Collectors.toList()));
        gigDTO.setSkillsetDTOList(gig.getGigSkillsets() == null
            ? null : gig.getGigSkillsets().stream()
            .map(skillset -> skillsetDTOMapper.mapToDTO(skillset, new SkillsetDTO()))
            .collect(Collectors.toList()));
        gig.getGigOffers().stream()
            .filter(offer -> offer.getAccepted() != null && offer.getAccepted().equals(Boolean.TRUE))
            .map(Offer::getAccepted)
            .findFirst()
            .ifPresent(gigDTO::setOfferAccepted);
        gig.getGigOffers().stream()
            .filter(offer -> offer.getAccepted() != null && offer.getAccepted().equals(Boolean.TRUE))
            .map(offer -> offer.getSpecialist().getUid())
            .findFirst()
            .ifPresent(gigDTO::setAcceptedSpecialist);
        return gigDTO;
    }

    public Gig mapToEntity(final GigDTO gigDTO, final Gig gig) {
        gig.setMaxPrice(gigDTO.getMaxPrice());
        gig.setPrice(gigDTO.getPrice());
        gig.setPriceToken(gigDTO.getPriceToken());
        gig.setTitle(gigDTO.getTitle());
        gig.setDescription(gigDTO.getDescription());
        gig.setExecutionType(gigDTO.getExecutionType());
        gig.setStatus(gigDTO.getStatus());
        gig.setExecutionpool(gigDTO.getExecutionpool());
        gig.setIsActive(gigDTO.getIsActive());
        if (gigDTO.getDataOwner() != null
            && (gig.getDataOwner() == null
            || !gig.getDataOwner().getUid().equals(gigDTO.getDataOwner()))) {
            final DataOwner dataOwner = dataOwnerRepository.findByUid(gigDTO.getDataOwner())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "dataOwner not found"));
            gig.setDataOwner(dataOwner);
        }
        if (gigDTO.getGigChosenSpecialists() != null) {
            final List<CandidateSpecialist> gigChosenSpecialists = candidateSpecialistRepository
                .findAllByUidIn(gigDTO.getGigChosenSpecialists());
            if (gigChosenSpecialists.size() != gigDTO.getGigChosenSpecialists().size()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "one of gigChosenSpecialists not found");
            }
            gig.setGigChosenSpecialistCandidateSpecialists(new HashSet<>(gigChosenSpecialists));
        }
        if (gigDTO.getSlaStatementDTOList() != null && !gigDTO.getSlaStatementDTOList().isEmpty()) {
            Set<SlaStatement> slaStatementSet = new HashSet<>();
            gigDTO.getSlaStatementDTOList().forEach(slaStatementDTO -> {
                Optional<SlaStatement> existingSla = Optional.empty();
                if (slaStatementDTO.getId() != null) {
                    existingSla = slaStatementRepository.findById(slaStatementDTO.getId());
                }
                SlaStatement slaStatement = new SlaStatement();
                slaStatementDTOMapper.mapToEntity(slaStatementDTO, slaStatement);
                if (existingSla.isPresent()) {
                    slaStatement.setId(slaStatementDTO.getId());
                } else {
                    slaStatement.setGig(gig);
                }
                slaStatementSet.add(slaStatement);
            });
            gig.setGigSlaStatements(slaStatementSet);
        }
        if (gigDTO.getSkillsetDTOList() != null && !gigDTO.getSkillsetDTOList().isEmpty()) {
            Set<Skillset> skillsets = new HashSet<>();
            gigDTO.getSkillsetDTOList().forEach(skillsetDTO -> {
                Optional<Skillset> existingSkillset = Optional.empty();
                if (skillsetDTO.getId() != null) {
                    existingSkillset = skillsetRepository.findById(skillsetDTO.getId());
                }
                Skillset skillset = new Skillset();
                skillsetDTOMapper.mapToEntity(skillsetDTO, skillset);
                if (existingSkillset.isPresent()) {
                    skillset.setId(skillsetDTO.getId());
                } else {
                    skillset.setGig(gig);
                }
                skillsets.add(skillset);
            });
            gig.setGigSkillsets(skillsets);
        }
        return gig;
    }

}
