package com.jadenx.kxgigservice.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;


@Getter
@Setter
public class ExecutionGigDTO {

    @NotNull
    private Long id;

    @NotNull
    private UUID dataOwner;

    private UUID specialist;

    private List<Long> gigDatasets;

}
