package com.jadenx.kxgigservice.rest;

import com.jadenx.kxgigservice.model.SkillsetDTO;
import com.jadenx.kxgigservice.service.SkillsetService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping(value = "/api/skillsets", produces = MediaType.APPLICATION_JSON_VALUE)
public class SkillsetController {

    private final SkillsetService skillsetService;

    public SkillsetController(final SkillsetService skillsetService) {
        this.skillsetService = skillsetService;
    }

    @GetMapping
    public ResponseEntity<List<SkillsetDTO>> getAllSkillsets() {
        return ResponseEntity.ok(skillsetService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SkillsetDTO> getSkillset(@PathVariable final Long id) {
        return ResponseEntity.ok(skillsetService.get(id));
    }

    @PostMapping
    public ResponseEntity<Long> createSkillset(@RequestBody @Valid final SkillsetDTO skillsetDTO) {
        return new ResponseEntity<>(skillsetService.create(skillsetDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateSkillset(@PathVariable final Long id,
                                               @RequestBody @Valid final SkillsetDTO skillsetDTO) {
        skillsetService.update(id, skillsetDTO);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSkillset(@PathVariable final Long id) {
        skillsetService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
