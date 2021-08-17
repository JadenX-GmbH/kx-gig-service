package com.jadenx.kxgigservice.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.UUID;


@Getter
@Setter
public class CandidateSpecialistDTO {

    private Long id;

    @NotNull
    private UUID uid;
}
