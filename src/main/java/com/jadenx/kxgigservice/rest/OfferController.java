package com.jadenx.kxgigservice.rest;

import com.jadenx.kxgigservice.model.OfferDTO;
import com.jadenx.kxgigservice.service.OfferService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping(value = "/api/offers", produces = MediaType.APPLICATION_JSON_VALUE)
public class OfferController {

    private final OfferService offerService;

    public OfferController(final OfferService offerService) {
        this.offerService = offerService;
    }

    @GetMapping
    public ResponseEntity<List<OfferDTO>> getAllOffers() {
        return ResponseEntity.ok(offerService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OfferDTO> getOffer(@PathVariable final Long id) {
        return ResponseEntity.ok(offerService.get(id));
    }

    @PostMapping
    public ResponseEntity<Long> createOffer(@RequestBody @Valid final OfferDTO offerDTO) {
        return new ResponseEntity<>(offerService.create(offerDTO), HttpStatus.CREATED);
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

}
