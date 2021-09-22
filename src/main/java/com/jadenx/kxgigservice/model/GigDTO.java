package com.jadenx.kxgigservice.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;


@Getter
@Setter
public class GigDTO {

    private Long id;

    @Size(max = 255)
    private String gigId;

    @Digits(integer = 10, fraction = 2)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Schema(type = "string", example = "81.08")
    private BigDecimal maxPrice;

    @Digits(integer = 10, fraction = 2)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Schema(type = "string", example = "71.08")
    private BigDecimal price;

    private Double priceToken;

    @NotNull
    @Size(max = 255)
    private String title;

    @NotNull
    private String description;

    @NotNull
    private ExecutionType executionType;

    @NotNull
    private Status status;

    @Size(max = 255)
    private String executionpool;

    private Boolean isActive = Boolean.TRUE;

    private List<UUID> gigChosenSpecialists;

    private UUID acceptedSpecialist;

    private UUID dataOwner;

    private List<SlaStatementDTO> slaStatementDTOList;

    private List<SkillsetDTO> skillsetDTOList;

    private Boolean offerAccepted = Boolean.FALSE;

}
