package com.jadenx.kxgigservice.rest;

import com.jadenx.kxgigservice.model.DataOwnerDTO;
import com.jadenx.kxgigservice.service.DataOwnerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping(value = "/api/dataOwners", produces = MediaType.APPLICATION_JSON_VALUE)
public class DataOwnerController {

    private final DataOwnerService dataOwnerService;

    public DataOwnerController(final DataOwnerService dataOwnerService) {
        this.dataOwnerService = dataOwnerService;
    }

    @GetMapping
    public ResponseEntity<List<DataOwnerDTO>> getAllDataOwners() {
        return ResponseEntity.ok(dataOwnerService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DataOwnerDTO> getDataOwner(@PathVariable final Long id) {
        return ResponseEntity.ok(dataOwnerService.get(id));
    }

    @PostMapping
    public ResponseEntity<Long> createDataOwner(
        @RequestBody @Valid final DataOwnerDTO dataOwnerDTO) {
        return new ResponseEntity<>(dataOwnerService.create(dataOwnerDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateDataOwner(@PathVariable final Long id,
                                                @RequestBody @Valid final DataOwnerDTO dataOwnerDTO) {
        dataOwnerService.update(id, dataOwnerDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDataOwner(@PathVariable final Long id) {
        dataOwnerService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
