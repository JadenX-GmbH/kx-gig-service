package com.jadenx.kxgigservice.rest;

import com.jadenx.kxgigservice.model.SpecialistDTO;
import com.jadenx.kxgigservice.service.SpecialistService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping(value = "/api/specialists", produces = MediaType.APPLICATION_JSON_VALUE)
public class SpecialistController {

    private final SpecialistService specialistService;

    public SpecialistController(final SpecialistService specialistService) {
        this.specialistService = specialistService;
    }

    @GetMapping
    public ResponseEntity<List<SpecialistDTO>> getAllSpecialists() {
        return ResponseEntity.ok(specialistService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SpecialistDTO> getSpecialist(@PathVariable final Long id) {
        return ResponseEntity.ok(specialistService.get(id));
    }

    @PostMapping
    public ResponseEntity<Long> createSpecialist(
        @RequestBody @Valid final SpecialistDTO specialistDTO) {
        return new ResponseEntity<>(specialistService.create(specialistDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateSpecialist(@PathVariable final Long id,
                                                 @RequestBody @Valid final SpecialistDTO specialistDTO) {
        specialistService.update(id, specialistDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSpecialist(@PathVariable final Long id) {
        specialistService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
