package com.jadenx.kxgigservice.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Getter
@Setter
public class SkillsetDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String skillsetId;

    private Long gig;

}
