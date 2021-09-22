package com.jadenx.kxgigservice.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.OffsetDateTime;


@Entity
@Getter
@Setter
public class Contract {

    @Id
    private Long id;

    @Column(nullable = false)
    private Boolean signatureRdo;

    @Column(nullable = false)
    private Boolean signatureDs;

    @Column(nullable = false, columnDefinition = "longtext")
    private String aggregatedJson;

    @Column
    private String transactionId;

    @Column
    private String blockchainIdentifier;

    @Column
    private Boolean isActive = Boolean.TRUE;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "offer_id", nullable = false)
    @MapsId
    private Offer offer;

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
