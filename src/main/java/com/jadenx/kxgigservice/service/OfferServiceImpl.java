package com.jadenx.kxgigservice.service;

import com.jadenx.kxgigservice.domain.Gig;
import com.jadenx.kxgigservice.domain.Offer;
import com.jadenx.kxgigservice.domain.Specialist;
import com.jadenx.kxgigservice.model.OfferDTO;
import com.jadenx.kxgigservice.repos.GigRepository;
import com.jadenx.kxgigservice.repos.OfferRepository;
import com.jadenx.kxgigservice.repos.SpecialistRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class OfferServiceImpl implements OfferService {

    private final OfferRepository offerRepository;
    private final GigRepository gigRepository;
    private final SpecialistRepository specialistRepository;

    public OfferServiceImpl(final OfferRepository offerRepository,
                            final GigRepository gigRepository, final SpecialistRepository specialistRepository) {
        this.offerRepository = offerRepository;
        this.gigRepository = gigRepository;
        this.specialistRepository = specialistRepository;
    }

    @Override
    public List<OfferDTO> findAll() {
        return offerRepository.findAll()
            .stream()
            .map(offer -> mapToDTO(offer, new OfferDTO()))
            .collect(Collectors.toList());
    }

    @Override
    public OfferDTO get(final Long id) {
        return offerRepository.findById(id)
            .map(offer -> mapToDTO(offer, new OfferDTO()))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Override
    public Long create(final OfferDTO offerDTO) {
        final Offer offer = new Offer();
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

    private OfferDTO mapToDTO(final Offer offer, final OfferDTO offerDTO) {
        offerDTO.setId(offer.getId());
        offerDTO.setAccepted(offer.getAccepted());
        offerDTO.setPrice(offer.getPrice());
        offerDTO.setPriceToken(offer.getPriceToken());
        offerDTO.setDescription(offer.getDescription());
        offerDTO.setIsActive(offer.getIsActive());
        offerDTO.setGig(offer.getGig() == null ? null : offer.getGig().getId());
        offerDTO.setSpecialist(offer.getSpecialist() == null ? null : offer.getSpecialist().getId());
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
            || !offer.getSpecialist().getId().equals(offerDTO.getSpecialist()))) {
            final Specialist specialist = specialistRepository.findById(offerDTO.getSpecialist())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "specialist not found"));
            offer.setSpecialist(specialist);
        }
        return offer;
    }

}
