package com.jadenx.kxgigservice.service;

import com.jadenx.kxgigservice.domain.CandidateSpecialist;
import com.jadenx.kxgigservice.domain.DataOwner;
import com.jadenx.kxgigservice.domain.Gig;
import com.jadenx.kxgigservice.model.GigDTO;
import com.jadenx.kxgigservice.repos.CandidateSpecialistRepository;
import com.jadenx.kxgigservice.repos.DataOwnerRepository;
import com.jadenx.kxgigservice.repos.GigRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Transactional
@Service
public class GigServiceImpl implements GigService {

    private final GigRepository gigRepository;
    private final DataOwnerRepository dataOwnerRepository;
    private final CandidateSpecialistRepository candidateSpecialistRepository;

    public GigServiceImpl(final GigRepository gigRepository,
                          final DataOwnerRepository dataOwnerRepository,
                          final CandidateSpecialistRepository candidateSpecialistRepository) {
        this.gigRepository = gigRepository;
        this.dataOwnerRepository = dataOwnerRepository;
        this.candidateSpecialistRepository = candidateSpecialistRepository;
    }

    @Override
    public List<GigDTO> findAll() {
        return gigRepository.findAll()
            .stream()
            .map(gig -> mapToDTO(gig, new GigDTO()))
            .collect(Collectors.toList());
    }

    @Override
    public List<GigDTO> getGigsByDataOwner(final UUID uid) {
        return gigRepository.getAllByDataOwner_Uid(uid)
            .stream()
            .map(gig -> mapToDTO(gig, new GigDTO()))
            .collect(Collectors.toList());
    }

    @Override
    public List<GigDTO> getGigsBySpecialist(final UUID uid) {
        return gigRepository.getAllByGigChosenSpecialistCandidateSpecialists_Uid(uid)
            .stream()
            .map(gig -> mapToDTO(gig, new GigDTO()))
            .collect(Collectors.toList());
    }

    @Override
    public GigDTO get(final Long id) {
        return gigRepository.findById(id)
            .map(gig -> mapToDTO(gig, new GigDTO()))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Override
    public Long create(final GigDTO gigDTO) {
        final Gig gig = new Gig();
        mapToEntity(gigDTO, gig);
        return gigRepository.save(gig).getId();
    }

    @Override
    public void update(final Long id, final GigDTO gigDTO) {
        final Gig gig = gigRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        mapToEntity(gigDTO, gig);
        gigRepository.save(gig);
    }

    @Override
    public void delete(final Long id) {
        gigRepository.deleteById(id);
    }

    private GigDTO mapToDTO(final Gig gig, final GigDTO gigDTO) {
        gigDTO.setId(gig.getId());
        gigDTO.setGigId(gig.getGigId());
        gigDTO.setMaxPrice(gig.getMaxPrice());
        gigDTO.setPrice(gig.getPrice());
        gigDTO.setPriceToken(gig.getPriceToken());
        gigDTO.setTitle(gig.getTitle());
        gigDTO.setDescription(gig.getDescription());
        gigDTO.setExecutionType(gig.getExecutionType());
        gigDTO.setStatus(gig.getStatus());
        gigDTO.setExecutionpool(gig.getExecutionpool());
        gigDTO.setIsActive(gig.getIsActive());
        gigDTO.setDataOwner(gig.getDataOwner() == null ? null : gig.getDataOwner().getId());
        gigDTO.setGigChosenSpecialists(gig.getGigChosenSpecialistCandidateSpecialists() == null
            ? null : gig.getGigChosenSpecialistCandidateSpecialists().stream()
            .map(candidateSpecialist -> candidateSpecialist.getId())
            .collect(Collectors.toList()));
        return gigDTO;
    }

    private Gig mapToEntity(final GigDTO gigDTO, final Gig gig) {
        gig.setGigId(gigDTO.getGigId());
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
            || !gig.getDataOwner().getId().equals(gigDTO.getDataOwner()))) {
            final DataOwner dataOwner = dataOwnerRepository.findById(gigDTO.getDataOwner())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "dataOwner not found"));
            gig.setDataOwner(dataOwner);
        }
        if (gigDTO.getGigChosenSpecialists() != null) {
            final List<CandidateSpecialist> gigChosenSpecialists = candidateSpecialistRepository
                .findAllById(gigDTO.getGigChosenSpecialists());
            if (gigChosenSpecialists.size() != gigDTO.getGigChosenSpecialists().size()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "one of gigChosenSpecialists not found");
            }
            gig.setGigChosenSpecialistCandidateSpecialists(gigChosenSpecialists.stream().collect(Collectors.toSet()));
        }
        return gig;
    }

}
