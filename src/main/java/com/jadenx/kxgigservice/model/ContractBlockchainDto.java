package com.jadenx.kxgigservice.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Setter
@Getter
public class ContractBlockchainDto {

    @NotNull
    private String contractHash;
}
