package com.jadenx.kxgigservice.service;

import com.jadenx.kxgigservice.domain.Gig;
import com.jadenx.kxgigservice.domain.Skillset;
import com.jadenx.kxgigservice.model.SkillsetDTO;
import com.jadenx.kxgigservice.repos.GigRepository;
import com.jadenx.kxgigservice.repos.SkillsetRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class SkillsetServiceImpl implements SkillsetService {

    private final SkillsetRepository skillsetRepository;
    private final GigRepository gigRepository;

    public SkillsetServiceImpl(final SkillsetRepository skillsetRepository,
                               final GigRepository gigRepository) {
        this.skillsetRepository = skillsetRepository;
        this.gigRepository = gigRepository;
    }

    @Override
    public List<SkillsetDTO> findAll() {
        return skillsetRepository.findAll()
            .stream()
            .map(skillset -> mapToDTO(skillset, new SkillsetDTO()))
            .collect(Collectors.toList());
    }

    @Override
    public SkillsetDTO get(final Long id) {
        return skillsetRepository.findById(id)
            .map(skillset -> mapToDTO(skillset, new SkillsetDTO()))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Override
    public Long create(final SkillsetDTO skillsetDTO) {
        final Skillset skillset = new Skillset();
        mapToEntity(skillsetDTO, skillset);
        return skillsetRepository.save(skillset).getId();
    }

    @Override
    public void update(final Long id, final SkillsetDTO skillsetDTO) {
        final Skillset skillset = skillsetRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        mapToEntity(skillsetDTO, skillset);
        skillsetRepository.save(skillset);
    }

    @Override
    public void delete(final Long id) {
        skillsetRepository.deleteById(id);
    }

    private SkillsetDTO mapToDTO(final Skillset skillset, final SkillsetDTO skillsetDTO) {
        skillsetDTO.setId(skillset.getId());
        skillsetDTO.setSklillsetId(skillset.getSklillsetId());
        skillsetDTO.setGig(skillset.getGig() == null ? null : skillset.getGig().getId());
        return skillsetDTO;
    }

    private Skillset mapToEntity(final SkillsetDTO skillsetDTO, final Skillset skillset) {
        skillset.setSklillsetId(skillsetDTO.getSklillsetId());
        if (skillsetDTO.getGig() != null
            && (skillset.getGig() == null
            || !skillset.getGig().getId().equals(skillsetDTO.getGig()))) {
            final Gig gig = gigRepository.findById(skillsetDTO.getGig())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "gig not found"));
            skillset.setGig(gig);
        }
        return skillset;
    }

}
