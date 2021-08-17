package com.jadenx.kxgigservice.service;

import com.jadenx.kxgigservice.model.SkillsetDTO;

import java.util.List;


public interface SkillsetService {

    List<SkillsetDTO> findAll();

    SkillsetDTO get(final Long id);

    Long create(final SkillsetDTO skillsetDTO);

    void update(final Long id, final SkillsetDTO skillsetDTO);

    void delete(final Long id);

}
