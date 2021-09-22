package com.jadenx.kxgigservice.rest;

import com.jadenx.kxgigservice.model.ContractDTO;
import com.jadenx.kxgigservice.model.GigDTO;
import com.jadenx.kxgigservice.model.OfferDTO;
import com.jadenx.kxgigservice.model.PaginatedResponse;
import com.jadenx.kxgigservice.service.GigService;
import com.jadenx.kxgigservice.service.OfferService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping(value = "/api/gigs", produces = MediaType.APPLICATION_JSON_VALUE)
public class GigController {

    private final GigService gigService;

    private final OfferService offerService;


    public GigController(final GigService gigService, final OfferService offerService) {
        this.gigService = gigService;
        this.offerService = offerService;
    }

    @GetMapping
    public ResponseEntity<PaginatedResponse<?>> getAllGigs(final Pageable pageable) {
        return ResponseEntity.ok(gigService.findAll(pageable));
    }

    @GetMapping("/dataowner/gigs")
    public ResponseEntity<PaginatedResponse<?>> getGigsDataOwner(final Principal user,
                                                                 final Pageable pageable) {
        return ResponseEntity.ok(gigService.getGigsByDataOwner(UUID.fromString(user.getName()), pageable));
    }

    @GetMapping("/specialist/gigs")
    public ResponseEntity<PaginatedResponse<?>> getGigsSpecialist(final Principal user,
                                                                  final Pageable pageable) {
        log.info(user.getName());
        return ResponseEntity.ok(gigService.getGigsBySpecialist(UUID.fromString(user.getName()), pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GigDTO> getGig(@PathVariable final Long id) {
        return ResponseEntity.ok(gigService.get(id));
    }

    @PostMapping
    public ResponseEntity<Long> createGig(final Principal user, @RequestBody @Valid final GigDTO gigDTO,
                                          @RequestHeader("Authorization") final String token) {
        return new ResponseEntity<>(gigService.create(gigDTO, user, token), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateGig(@PathVariable final Long id,
                                          @RequestBody @Valid final GigDTO gigDTO) {
        gigService.update(id, gigDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGig(@PathVariable final Long id) {
        gigService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{gigId}/offers")
    public ResponseEntity<PaginatedResponse<?>> getOffersByGigId(
        @PathVariable final Long gigId, final Pageable pageable) {
        return ResponseEntity.ok(offerService.getOffersByGigId(gigId, pageable));
    }

    @GetMapping("/{gigId}/specialist/offers")
    public ResponseEntity<List<OfferDTO>> getOfferByGigAndSpecialist(@PathVariable final Long gigId,
                                                                     final Principal user) {
        return ResponseEntity.ok((offerService.getOfferByGigAndSpecialist(gigId,UUID.fromString(user.getName()))));
    }

    @GetMapping("/{id}/contracts")
    public ResponseEntity<List<ContractDTO>> getContractsByGigId(@PathVariable final Long id) {
        return ResponseEntity.ok(gigService.getContractsByGigId(id));
    }
}
