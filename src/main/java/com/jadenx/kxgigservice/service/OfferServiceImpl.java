package com.jadenx.kxgigservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jadenx.kxgigservice.domain.Contract;
import com.jadenx.kxgigservice.domain.Gig;
import com.jadenx.kxgigservice.domain.Offer;
import com.jadenx.kxgigservice.domain.Specialist;
import com.jadenx.kxgigservice.mapper.ContractMapper;
import com.jadenx.kxgigservice.mapper.GigDTOMapper;
import com.jadenx.kxgigservice.model.*;
import com.jadenx.kxgigservice.proxy.FeignRestClientProxy;
import com.jadenx.kxgigservice.repos.ContractRepository;
import com.jadenx.kxgigservice.repos.GigRepository;
import com.jadenx.kxgigservice.repos.OfferRepository;
import com.jadenx.kxgigservice.repos.SpecialistRepository;
import com.jadenx.kxgigservice.util.PaginatedResponseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@Slf4j
@RequiredArgsConstructor
public class OfferServiceImpl implements OfferService {

    private final OfferRepository offerRepository;
    private final GigRepository gigRepository;
    private final SpecialistRepository specialistRepository;
    private final FeignRestClientProxy feignRestClientProxy;
    private final ContractService contractService;
    private final GigDTOMapper gigDTOMapper;
    private final ContractMapper contractMapper;
    private final ContractRepository contractRepository;
    private final SpecialistService specialistService;


    @Override
    public PaginatedResponse<?> findAll(final Pageable pageable) {
        Page<Offer> offerPage = offerRepository.findAll(pageable);
        List<OfferDTO> responseData = offerPage.stream()
            .map(offer -> mapToDTO(offer, new OfferDTO()))
            .collect(Collectors.toList());
        return PaginatedResponseUtil.paginatedResponse(responseData, offerPage);
    }

    @Override
    public PaginatedResponse<?> getOffersByOwnerOrSpecialist(final UUID userId, final Pageable pageable) {
        Page<Offer> offerPage = offerRepository.getAllByGig_DataOwner_UidOrSpecialist_Uid(userId, userId, pageable);
        List<OfferDTO> responseData = offerPage.stream()
            .map(offer -> mapToDTO(offer, new OfferDTO()))
            .collect(Collectors.toList());
        return PaginatedResponseUtil.paginatedResponse(responseData, offerPage);
    }

    @Override
    public OfferDTO get(final Long id) {
        return offerRepository.findById(id)
            .map(offer -> mapToDTO(offer, new OfferDTO()))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Override
    public List<OfferDTO> getOfferByGigAndSpecialist(final Long id, final UUID uid) {
        return offerRepository.getAllByGigId_AndSpecialist_Uid(id, uid)
            .stream()
            .map(offer -> mapToDTO(offer, new OfferDTO()))
            .collect(Collectors.toList());
    }


    @Override
    public Long create(final OfferDTO offerDTO, final UUID uid) {
        final Offer offer = new Offer();
        final SpecialistDTO specialistDTO = new SpecialistDTO();

        if (specialistRepository.existsSpecialistByUid(uid)) {
            offerDTO.setSpecialist(uid);
        } else {
            specialistDTO.setUid(uid);
            specialistService.create(specialistDTO);
            offerDTO.setSpecialist(uid);

        }
        mapToEntity(offerDTO, offer);
        return offerRepository.save(offer).getId();
    }

    @Override
    public void update(final Long id, final OfferDTO offerDTO) {
        final Offer offer = offerRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        mapToEntity(offerDTO, offer);
        offerRepository.save(offer);
    }

    @Override
    public void delete(final Long id) {
        offerRepository.deleteById(id);
    }

    @Override
    public PaginatedResponse<?> getOffersByGigId(final Long gigId, final Pageable pageable) {
        Page<Offer> offerPage = offerRepository.findAllByGig_IdOrderByAcceptedDesc(gigId, pageable);
        List<OfferDTO> responseData = offerPage.stream()
            .map(offer -> mapToDTO(offer, new OfferDTO()))
            .collect(Collectors.toList());
        return PaginatedResponseUtil.paginatedResponse(responseData, offerPage);
    }

    @Override
    @Transactional
    public void acceptOffer(final Long id, final Boolean accepted, final String user) throws JsonProcessingException {
        Offer offer = offerRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        offer.setAccepted(accepted);
        if (accepted) {
            ContractAggregatedDTO contractAggregatedDTO = new ContractAggregatedDTO();
            contractAggregatedDTO.setOfferDTO(mapToDTO(offer, new OfferDTO()));
            contractAggregatedDTO.setGigDTO(gigDTOMapper.mapToDTO(offer.getGig(), new GigDTO()));
            ObjectMapper mapper = new ObjectMapper();
            String aggregatedJson = mapper.writeValueAsString(contractAggregatedDTO);
            ContractDTO contractDTO = new ContractDTO();
            contractDTO.setOffer(id);
            contractDTO.setAggregatedJson(aggregatedJson);
            contractDTO.setSignatureDs(Boolean.TRUE);
            contractDTO.setSignatureRdo(Boolean.TRUE);
            contractService.create(contractDTO);
            try {
                ExecutionGigDTO executionGigDTO = new ExecutionGigDTO();
                executionGigDTO.setId(offer.getGig().getId());
                executionGigDTO.setSpecialist(offer.getSpecialist().getUid());
                executionGigDTO.setDataOwner(offer.getGig().getDataOwner().getUid());
                feignRestClientProxy.updateByGigId(offer.getGig().getId(), executionGigDTO, user);
                log.info("Successfully update gig");
            } catch (Exception e) {
                log.error("Unable to update gig. {}", e.getMessage());
            }
            offerRepository.save(offer);
        }
    }

    @Override
    public ContractDTO getContractByOfferId(final Long id) {
        Contract contract = contractRepository.findContractByOffer_AcceptedTrueAndOffer_Id(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return contractMapper.mapToDTO(contract, new ContractDTO());
    }

    private OfferDTO mapToDTO(final Offer offer, final OfferDTO offerDTO) {
        offerDTO.setId(offer.getId());
        offerDTO.setAccepted(offer.getAccepted());
        offerDTO.setPrice(offer.getPrice());
        offerDTO.setPriceToken(offer.getPriceToken());
        offerDTO.setDescription(offer.getDescription());
        offerDTO.setIsActive(offer.getIsActive());
        offerDTO.setGig(offer.getGig() == null ? null : offer.getGig().getId());
        offerDTO.setSpecialist(offer.getSpecialist() == null ? null :
            offer.getSpecialist()
                .getUid());
        offerDTO.setDateCreated(offer.getDateCreated());
        offerDTO.setLastUpdated(offer.getLastUpdated());
        return offerDTO;
    }

    private Offer mapToEntity(final OfferDTO offerDTO, final Offer offer) {
        offer.setAccepted(offerDTO.getAccepted());
        offer.setPrice(offerDTO.getPrice());
        offer.setPriceToken(offerDTO.getPriceToken());
        offer.setDescription(offerDTO.getDescription());
        offer.setIsActive(offerDTO.getIsActive());
        if (offerDTO.getGig() != null
            && (offer.getGig() == null
            || !offer.getGig().getId().equals(offerDTO.getGig()))) {
            final Gig gig = gigRepository.findById(offerDTO.getGig())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "gig not found"));
            offer.setGig(gig);
        }
        if (offerDTO.getSpecialist() != null
            && (offer.getSpecialist() == null
            || !offer.getSpecialist().getUid().equals(offerDTO.getSpecialist()))) {
            final Specialist specialist = specialistRepository.findByUid(offerDTO.getSpecialist())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "specialist not found"));
            offer.setSpecialist(specialist);
        }
        return offer;
    }
}
