package com.jadenx.kxgigservice.rest;

import com.jadenx.kxgigservice.model.CandidateSpecialistDTO;
import com.jadenx.kxgigservice.service.CandidateSpecialistService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping(value = "/api/candidateSpecialists", produces = MediaType.APPLICATION_JSON_VALUE)
public class CandidateSpecialistController {

    private final CandidateSpecialistService candidateSpecialistService;

    public CandidateSpecialistController(
        final CandidateSpecialistService candidateSpecialistService) {
        this.candidateSpecialistService = candidateSpecialistService;
    }

    @GetMapping
    public ResponseEntity<List<CandidateSpecialistDTO>> getAllCandidateSpecialists() {
        return ResponseEntity.ok(candidateSpecialistService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CandidateSpecialistDTO> getCandidateSpecialist(
        @PathVariable final Long id) {
        return ResponseEntity.ok(candidateSpecialistService.get(id));
    }

    @PostMapping
    public ResponseEntity<Long> createCandidateSpecialist(
        @RequestBody @Valid final CandidateSpecialistDTO candidateSpecialistDTO) {
        return new ResponseEntity<>(candidateSpecialistService.create(candidateSpecialistDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateCandidateSpecialist(
        @PathVariable final Long id,
        @RequestBody @Valid final CandidateSpecialistDTO candidateSpecialistDTO) {
        candidateSpecialistService.update(id, candidateSpecialistDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCandidateSpecialist(@PathVariable final Long id) {
        candidateSpecialistService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
