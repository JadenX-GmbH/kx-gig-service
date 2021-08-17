package com.jadenx.kxgigservice.domain;

import com.jadenx.kxgigservice.model.ExecutionType;
import com.jadenx.kxgigservice.model.Status;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Set;


@Entity
@Getter
@Setter
public class Gig {

    @Id
    @Column(nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String gigId;

    @Column(precision = 10, scale = 2)
    private BigDecimal maxPrice;

    @Column(precision = 10, scale = 2)
    private BigDecimal price;

    @Column
    private Double priceToken;

    @Column(nullable = false)
    private String title;

    // CHECKSTYLE IGNORE check FOR NEXT 5 LINES
    @Column(
        nullable = false,
        name = "\"description\"",
        columnDefinition = "longtext"
    )
    private String description;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ExecutionType executionType;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column
    private String executionpool;

    @Column
    private Boolean isActive = Boolean.TRUE;

    @OneToMany(mappedBy = "gig", cascade = CascadeType.ALL)
    private Set<Offer> gigOffers;

    @OneToMany(mappedBy = "gig", cascade = CascadeType.ALL)
    private Set<SlaStatement> gigSlaStatements;

    @OneToMany(mappedBy = "gig", cascade = CascadeType.ALL)
    private Set<Skillset> gigSkillsets;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "data_owner_id")
    private DataOwner dataOwner;

    // CHECKSTYLE IGNORE check FOR NEXT 6 LINES
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "gig_chosen_specialist",
        joinColumns = @JoinColumn(name = "gig_id"),
        inverseJoinColumns = @JoinColumn(name = "candidate_specialist_id")
    )
    private Set<CandidateSpecialist> gigChosenSpecialistCandidateSpecialists;

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
