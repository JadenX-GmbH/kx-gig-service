package com.jadenx.kxgigservice.service;

import com.jadenx.kxgigservice.model.ContractDTO;
import com.jadenx.kxgigservice.model.GigDTO;
import com.jadenx.kxgigservice.model.PaginatedResponse;
import org.springframework.data.domain.Pageable;

import java.security.Principal;
import java.util.List;
import java.util.UUID;


public interface GigService {

    PaginatedResponse<?> findAll(final Pageable pageable);

    PaginatedResponse<?> getGigsByDataOwner(UUID uid, Pageable pageable);

    PaginatedResponse<?> getGigsBySpecialist(UUID uid, Pageable pageable);

    GigDTO get(final Long id);

    Long create(final GigDTO gigDTO, final Principal user, final String token);

    void update(final Long id, final GigDTO gigDTO);

    void delete(final Long id);

    List<ContractDTO> getContractsByGigId(final Long id);
}
