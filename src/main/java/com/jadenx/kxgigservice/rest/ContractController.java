package com.jadenx.kxgigservice.rest;

import com.jadenx.kxgigservice.model.ContractDTO;
import com.jadenx.kxgigservice.service.ContractService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping(value = "/api/contracts", produces = MediaType.APPLICATION_JSON_VALUE)
public class ContractController {

    private final ContractService contractService;

    public ContractController(final ContractService contractService) {
        this.contractService = contractService;
    }

    @GetMapping
    public ResponseEntity<List<ContractDTO>> getAllContracts() {
        return ResponseEntity.ok(contractService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContractDTO> getContract(@PathVariable final Long id) {
        return ResponseEntity.ok(contractService.get(id));
    }

    @PostMapping
    public ResponseEntity<Long> createContract(@RequestBody @Valid final ContractDTO contractDTO) {
        return new ResponseEntity<>(contractService.create(contractDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateContract(@PathVariable final Long id,
                                               @RequestBody @Valid final ContractDTO contractDTO) {
        contractService.update(id, contractDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContract(@PathVariable final Long id) {
        contractService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
