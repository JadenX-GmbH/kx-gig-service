package com.jadenx.kxgigservice.rest;

import com.jadenx.kxgigservice.config.BaseIT;
import com.jadenx.kxgigservice.model.ErrorResponse;
import com.jadenx.kxgigservice.model.OfferDTO;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class OfferControllerTest extends BaseIT {

    @Test
    @Sql({"/data/specialistData.sql","/data/dataOwnerData.sql", "/data/gigData.sql", "/data/offerData.sql"})
    public void getAllOffers_success() {
        final HttpEntity<String> request = new HttpEntity<>(null, headers());
        final ResponseEntity<List<OfferDTO>> response = restTemplate.exchange(
            "/api/offers", HttpMethod.GET, request, new ParameterizedTypeReference<List<OfferDTO>>() {
            });

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1100, response.getBody().get(0).getId());
    }

    @Test
    @Sql({"/data/specialistData.sql","/data/dataOwnerData.sql",
        "/data/gigData.sql", "/data/offerData.sql"})
    public void getOffer_success() {
        final HttpEntity<String> request = new HttpEntity<>(null, headers());
        final ResponseEntity<OfferDTO> response = restTemplate.exchange(
            "/api/offers/1100", HttpMethod.GET, request, OfferDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(false, response.getBody().getAccepted());
    }

    @Test
    public void getOffer_notFound() {
        final HttpEntity<String> request = new HttpEntity<>(null, headers());
        final ResponseEntity<ErrorResponse> response = restTemplate.exchange(
            "/api/offers/1766", HttpMethod.GET, request, ErrorResponse.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("ResponseStatusException", response.getBody().getException());
    }

    @Test
    @Sql({"/data/specialistData.sql","/data/dataOwnerData.sql", "/data/gigData.sql"})
    public void createOffer_success() {
        final HttpEntity<String> request = new HttpEntity<>(readResource("/requests/offerDTORequest.json"), headers());
        final ResponseEntity<Long> response = restTemplate.exchange(
            "/api/offers", HttpMethod.POST, request, Long.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(1, offerRepository.count());
    }

    @Test
    public void createOffer_missingField() {
        final HttpEntity<String> request = new HttpEntity<>(
            readResource("/requests/offerDTORequest_missingField.json"), headers());
        final ResponseEntity<ErrorResponse> response = restTemplate.exchange(
            "/api/offers", HttpMethod.POST, request, ErrorResponse.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("MethodArgumentNotValidException", response.getBody().getException());
        assertEquals("price", response.getBody().getFieldErrors().get(0).getField());
    }

    @Test
    @Sql({"/data/specialistData.sql","/data/dataOwnerData.sql", "/data/gigData.sql", "/data/offerData.sql"})
    public void updateOffer_success() {
        final HttpEntity<String> request = new HttpEntity<>(
            readResource("/requests/offerDTORequest.json"), headers());
        final ResponseEntity<Void> response = restTemplate.exchange(
            "/api/offers/1100", HttpMethod.PUT, request, Void.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, offerRepository.findById(1100L).get().getAccepted());
    }

    @Test
    @Sql({"/data/specialistData.sql","/data/dataOwnerData.sql",
        "/data/gigData.sql", "/data/offerData.sql",
        "/data/contractData.sql"})
    public void deleteOffer_success() {
        final HttpEntity<String> request = new HttpEntity<>(null, headers());
        final ResponseEntity<Void> response = restTemplate.exchange(
            "/api/offers/1100", HttpMethod.DELETE, request, Void.class);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertEquals(0, offerRepository.count());
        assertEquals(0, contractRepository.count());
        assertEquals(1, specialistRepository.count());
        assertEquals(2, gigRepository.count());
    }

}
