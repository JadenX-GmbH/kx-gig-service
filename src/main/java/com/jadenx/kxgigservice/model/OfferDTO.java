package com.jadenx.kxgigservice.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;


@Getter
@Setter
public class OfferDTO {

    private Long id;

    private Boolean accepted;

    @NotNull
    @Digits(integer = 10, fraction = 2)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Schema(type = "string", example = "71.08")
    private BigDecimal price;

    @NotNull
    private Double priceToken;

    @NotNull
    private String description;

    private Boolean isActive = Boolean.TRUE;

    @NotNull
    private Long gig;

    @NotNull
    private Long specialist;

}
