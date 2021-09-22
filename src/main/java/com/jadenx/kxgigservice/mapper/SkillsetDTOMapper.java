package com.jadenx.kxgigservice.mapper;

import com.jadenx.kxgigservice.domain.Gig;
import com.jadenx.kxgigservice.domain.Skillset;
import com.jadenx.kxgigservice.model.SkillsetDTO;
import com.jadenx.kxgigservice.repos.GigRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
@RequiredArgsConstructor
public class SkillsetDTOMapper {
    private final GigRepository gigRepository;

    public SkillsetDTO mapToDTO(final Skillset skillset, final SkillsetDTO skillsetDTO) {
        skillsetDTO.setId(skillset.getId());
        skillsetDTO.setSkillsetId(skillset.getSkillsetId());
        skillsetDTO.setGig(skillset.getGig() == null ? null : skillset.getGig().getId());
        return skillsetDTO;
    }

    public Skillset mapToEntity(final SkillsetDTO skillsetDTO, final Skillset skillset) {
        skillset.setSkillsetId(skillsetDTO.getSkillsetId());
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
