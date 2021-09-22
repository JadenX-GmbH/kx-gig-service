package com.jadenx.kxgigservice.service;

import com.jadenx.kxgigservice.domain.Skillset;
import com.jadenx.kxgigservice.mapper.SkillsetDTOMapper;
import com.jadenx.kxgigservice.model.SkillsetDTO;
import com.jadenx.kxgigservice.repos.GigRepository;
import com.jadenx.kxgigservice.repos.SkillsetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class SkillsetServiceImpl implements SkillsetService {

    private final SkillsetRepository skillsetRepository;
    private final GigRepository gigRepository;
    private final SkillsetDTOMapper skillsetDTOMapper;


    @Override
    public List<SkillsetDTO> findAll() {
        return skillsetRepository.findAll()
            .stream()
            .map(skillset -> skillsetDTOMapper.mapToDTO(skillset, new SkillsetDTO()))
            .collect(Collectors.toList());
    }

    @Override
    public SkillsetDTO get(final Long id) {
        return skillsetRepository.findById(id)
            .map(skillset -> skillsetDTOMapper.mapToDTO(skillset, new SkillsetDTO()))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Override
    public Long create(final SkillsetDTO skillsetDTO) {
        final Skillset skillset = new Skillset();
        skillsetDTOMapper.mapToEntity(skillsetDTO, skillset);
        return skillsetRepository.save(skillset).getId();
    }

    @Override
    public void update(final Long id, final SkillsetDTO skillsetDTO) {
        final Skillset skillset = skillsetRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        skillsetDTOMapper.mapToEntity(skillsetDTO, skillset);
        skillsetRepository.save(skillset);
    }

    @Override
    public void delete(final Long id) {
        skillsetRepository.deleteById(id);
    }


}
