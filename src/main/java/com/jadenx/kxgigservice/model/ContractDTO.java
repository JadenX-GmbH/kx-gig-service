package com.jadenx.kxgigservice.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Getter
@Setter
@ToString
public class ContractDTO {

    private Long id;

    @NotNull
    private Boolean signatureRdo;

    @NotNull
    private Boolean signatureDs;

    @NotNull
    private String aggregatedJson;

    @Size(max = 255)
    private String transactionId;

    @Size(max = 255)
    private String blockchainIdentifier;

    private Boolean isActive = Boolean.TRUE;

    @NotNull
    private Long offer;

}
