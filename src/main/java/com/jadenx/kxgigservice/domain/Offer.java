package com.jadenx.kxgigservice.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;


@Entity
@Getter
@Setter
public class Offer {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Boolean accepted;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    private Double priceToken;

    // CHECKSTYLE IGNORE check FOR NEXT 5 LINES
    @Column(
        nullable = false,
        name = "\"description\"",
        columnDefinition = "longtext"
    )
    private String description;

    @Column
    private Boolean isActive = Boolean.TRUE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gig_id", nullable = false)
    private Gig gig;

    @OneToOne(mappedBy = "offer",
        fetch = FetchType.LAZY,
         orphanRemoval = true,
        cascade = CascadeType.ALL)
    private Contract contract;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "specialist_id", nullable = false)
    private Specialist specialist;

    @Column(nullable = false, updatable = false)
    private OffsetDateTime dateCreated;

    @Column(nullable = false)
    private OffsetDateTime lastUpdated;

    @PrePersist
    public void prePersist() {
        dateCreated = OffsetDateTime.now();
        lastUpdated = dateCreated;
    }

    @PreUpdate
    public void preUpdate() {
        lastUpdated = OffsetDateTime.now();
    }

}
