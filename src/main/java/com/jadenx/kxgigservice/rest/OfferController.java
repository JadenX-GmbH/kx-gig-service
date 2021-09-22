package com.jadenx.kxgigservice.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jadenx.kxgigservice.model.ContractDTO;
import com.jadenx.kxgigservice.model.OfferDTO;
import com.jadenx.kxgigservice.model.PaginatedResponse;
import com.jadenx.kxgigservice.service.OfferService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.UUID;


@RestController
@RequestMapping(value = "/api/offers", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class OfferController {

    private final OfferService offerService;

    @GetMapping
    public ResponseEntity<PaginatedResponse<?>> getAllOffers(final Pageable pageable) {
        return ResponseEntity.ok(offerService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OfferDTO> getOffer(@PathVariable final Long id) {
        return ResponseEntity.ok(offerService.get(id));
    }

    @PostMapping
    public ResponseEntity<Long> createOffer(@RequestBody @Valid final OfferDTO offerDTO, final Principal user) {
        return new ResponseEntity<>(offerService.create(offerDTO, UUID.fromString(user.getName())), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateOffer(@PathVariable final Long id,
                                            @RequestBody @Valid final OfferDTO offerDTO) {
        offerService.update(id, offerDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOffer(@PathVariable final Long id) {
        offerService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> acceptOffer(@PathVariable final long id,
                                            @RequestParam final Boolean accepted,
                                            @RequestHeader("Authorization") final String user)
        throws JsonProcessingException {
        offerService.acceptOffer(id, accepted, user);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/contract")
    public ResponseEntity<ContractDTO> getContractByOfferId(@PathVariable final Long id) {
        return ResponseEntity.ok(offerService.getContractByOfferId(id));
    }
}
