package com.jadenx.kxgigservice.rest;

import com.jadenx.kxgigservice.model.PaginatedResponse;
import com.jadenx.kxgigservice.service.OfferService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/users", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class UserController {

    private final OfferService offerService;

    @GetMapping("/offers")
    public ResponseEntity<PaginatedResponse<?>> getOffersByUser(final Principal user, final Pageable pageable) {
        return ResponseEntity.ok(offerService.getOffersByOwnerOrSpecialist(UUID.fromString(user.getName()), pageable));
    }

}
