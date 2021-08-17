package com.jadenx.kxgigservice.rest;

import com.jadenx.kxgigservice.model.SlaStatementDTO;
import com.jadenx.kxgigservice.service.SlaStatementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping(value = "/api/slaStatements", produces = MediaType.APPLICATION_JSON_VALUE)
public class SlaStatementController {

    private final SlaStatementService slaStatementService;

    public SlaStatementController(final SlaStatementService slaStatementService) {
        this.slaStatementService = slaStatementService;
    }

    @GetMapping
    public ResponseEntity<List<SlaStatementDTO>> getAllSlaStatements() {
        return ResponseEntity.ok(slaStatementService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SlaStatementDTO> getSlaStatement(@PathVariable final Long id) {
        return ResponseEntity.ok(slaStatementService.get(id));
    }

    @PostMapping
    public ResponseEntity<Long> createSlaStatement(
        @RequestBody @Valid final SlaStatementDTO slaStatementDTO) {
        return new ResponseEntity<>(slaStatementService.create(slaStatementDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateSlaStatement(@PathVariable final Long id,
                                                   @RequestBody @Valid final SlaStatementDTO slaStatementDTO) {
        slaStatementService.update(id, slaStatementDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSlaStatement(@PathVariable final Long id) {
        slaStatementService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
