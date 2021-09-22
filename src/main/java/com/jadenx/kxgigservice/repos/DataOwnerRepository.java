package com.jadenx.kxgigservice.repos;

import com.jadenx.kxgigservice.domain.DataOwner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;


public interface DataOwnerRepository extends JpaRepository<DataOwner, Long> {
    Optional<DataOwner> findByUid(UUID uid);
    boolean existsDataOwnerByUid(UUID uid);
}
