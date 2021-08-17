package com.jadenx.kxgigservice.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Getter
@Setter
public class SlaStatementDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    private String subject;

    @NotNull
    private String restriction;

    private Boolean isActive = Boolean.TRUE;

    @NotNull
    private Long gig;

}
