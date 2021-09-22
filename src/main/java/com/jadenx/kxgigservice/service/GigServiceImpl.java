package com.jadenx.kxgigservice.service;

import com.jadenx.kxgigservice.domain.*;
import com.jadenx.kxgigservice.mapper.ContractMapper;
import com.jadenx.kxgigservice.mapper.GigDTOMapper;
import com.jadenx.kxgigservice.model.*;
import com.jadenx.kxgigservice.proxy.FeignRestClientProxy;
import com.jadenx.kxgigservice.repos.*;
import com.jadenx.kxgigservice.util.PaginatedResponseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.security.Principal;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;


@Transactional
@Service
@Slf4j
@RequiredArgsConstructor
public class GigServiceImpl implements GigService {

    private final GigRepository gigRepository;
    private final DataOwnerRepository dataOwnerRepository;
    private final CandidateSpecialistRepository candidateSpecialistRepository;
    private final FeignRestClientProxy feignRestClientProxy;
    private final DataOwnerService dataOwnerService;
    private final SpecialistRepository specialistRepository;
    private final GigDTOMapper gigDTOMapper;
    private final ContractMapper contractMapper;
    private final ContractRepository contractRepository;

    @Override
    public PaginatedResponse<?> findAll(final Pageable pageable) {
        Page<Gig> gigPage = gigRepository.findAll(pageable);
        List<GigDTO> responseData = gigPage.stream()
            .map(gig -> gigDTOMapper.mapToDTO(gig, new GigDTO()))
            .collect(Collectors.toList());
        return PaginatedResponseUtil.paginatedResponse(responseData, gigPage);
    }

    @Override
    public PaginatedResponse<?> getGigsByDataOwner(final UUID uid, final Pageable pageable) {
        Page<Gig> gigPage = gigRepository.findAllByDataOwner_Uid(uid, pageable);
        List<GigDTO> responseData = gigPage.stream()
            .map(gig -> gigDTOMapper.mapToDTO(gig, new GigDTO()))
            .collect(Collectors.toList());
        return PaginatedResponseUtil.paginatedResponse(responseData, gigPage);
    }

    @Override
    public PaginatedResponse<?> getGigsBySpecialist(final UUID uid, final Pageable pageable) {
        Page<Gig> gigPage = gigRepository.findAllByGigChosenSpecialistCandidateSpecialists_Uid(uid, pageable);
        List<GigDTO> responseData = gigPage.stream()
            .map(gig -> gigDTOMapper.mapToDTO(gig, new GigDTO()))
            .collect(Collectors.toList());
        return PaginatedResponseUtil.paginatedResponse(responseData, gigPage);
    }

    @Override
    public GigDTO get(final Long id) {
        return gigRepository.findById(id)
            .map(gig -> gigDTOMapper.mapToDTO(gig, new GigDTO()))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Override
    public Long create(final GigDTO gigDTO, final Principal user, final String token) {
        final Gig gig = new Gig();
        gigDTO.setGigId(UUID.randomUUID().toString());
        if (dataOwnerRepository.existsDataOwnerByUid(UUID.fromString(user.getName()))) {
            gigDTO.setDataOwner(UUID.fromString(user.getName()));
        } else {
            DataOwnerDTO dataOwner = new DataOwnerDTO();
            dataOwner.setUid(UUID.fromString(user.getName()));
            dataOwnerService.create(dataOwner);
            gigDTO.setDataOwner(dataOwner.getUid());
        }
        gigDTOMapper.mapToEntity(gigDTO, gig);
        final Gig createdGig = gigRepository.save(gig);
        List<Specialist> specialists = new ArrayList<>();
        mapRandomSpecialistToCandidateSpecialist(specialists, createdGig);
        try {
            ExecutionGigDTO executionGigDTO = new ExecutionGigDTO();
            executionGigDTO.setId(createdGig.getId());
            executionGigDTO.setDataOwner(createdGig.getDataOwner().getUid());
            feignRestClientProxy.createGig(executionGigDTO, token);
            log.info("Gig is successfully created in Execution service");
        } catch (Exception e) {
            log.error("Unable to create gig. {}", e.getMessage());
        }
        return createdGig.getId();
    }

    @Override
    public void update(final Long id, final GigDTO gigDTO) {
        final Gig gig = gigRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        gigDTO.setId(gig.getId());
        gigDTOMapper.mapToEntity(gigDTO, gig);
        gigRepository.save(gig);
    }

    @Override
    public void delete(final Long id) {
        gigRepository.deleteById(id);
    }

    @Override
    public List<ContractDTO> getContractsByGigId(final Long id) {
        return contractRepository.findContractsByOffer_AcceptedTrueAndOffer_Gig_Id(id)
            .stream()
            .map(contract -> contractMapper.mapToDTO(contract, new ContractDTO()))
            .collect(Collectors.toList());
    }

    private void mapRandomSpecialistToCandidateSpecialist(final List<Specialist> specialists, final Gig createdGig) {
        int count = 3;
        Random random = new Random();
        List<Specialist> specialistList = specialistRepository.findAll();
        if (specialistList.size() != 0) {
            if (specialistList.size() < 3) {
                count = specialistList.size();
            }
            for (int i = 0; i < count; i++) {
                int randomIndex = 0;
                if (specialistList.size() != 1) {
                    randomIndex = random.nextInt(specialistList.size() - 1);
                }
                Specialist specialist = specialistList.get(randomIndex);
                specialists.add(specialist);
                specialistList.remove(randomIndex);
            }
            Set<CandidateSpecialist> candidateSpecialistSet =
                mapSpecialistToCandidateSpecialist(specialists, createdGig);
            createdGig.setGigChosenSpecialistCandidateSpecialists(candidateSpecialistSet);
            gigRepository.save(createdGig);
        }
    }

    private Set<CandidateSpecialist> mapSpecialistToCandidateSpecialist(final List<Specialist> specialistList,
                                                                        final Gig gig) {
        Set<Gig> gigSet = new HashSet<>();
        Set<CandidateSpecialist> specialistSet = new HashSet<>();
        gigSet.add(gig);
        List<CandidateSpecialist> specialists = specialistList.stream().map(specialist -> CandidateSpecialist.builder()
            .uid(specialist.getUid())
            .gigChosenSpecialistGigs(gigSet)
            .dateCreated(specialist.getDateCreated())
            .lastUpdated(specialist.getLastUpdated())
            .build())
            .collect(Collectors.toList());

        specialists.forEach(candidateSpecialist -> {
            CandidateSpecialist specialist = candidateSpecialistRepository.findByUid(candidateSpecialist.getUid());
            if (specialist == null) {
                candidateSpecialist.setDateCreated(OffsetDateTime.now());
                candidateSpecialist.setLastUpdated(OffsetDateTime.now());
                CandidateSpecialist candidateSpecialist1 = candidateSpecialistRepository.save(candidateSpecialist);
                specialistSet.add(candidateSpecialist1);
            } else {
                specialist.setGigChosenSpecialistGigs(candidateSpecialist.getGigChosenSpecialistGigs());
                specialist.setLastUpdated(OffsetDateTime.now());
                candidateSpecialistRepository.save(specialist);
                specialistSet.add(specialist);
            }
        });
        return specialistSet;
    }
}
