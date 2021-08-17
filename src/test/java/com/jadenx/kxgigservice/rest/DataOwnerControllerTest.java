package com.jadenx.kxgigservice.rest;

import com.jadenx.kxgigservice.config.BaseIT;
import com.jadenx.kxgigservice.model.DataOwnerDTO;
import com.jadenx.kxgigservice.model.ErrorResponse;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class DataOwnerControllerTest extends BaseIT {

    @Test
    @Sql("/data/dataOwnerData.sql")
    public void getAllDataOwners_success() {
        final HttpEntity<String> request = new HttpEntity<>(null, headers());
        final ResponseEntity<List<DataOwnerDTO>> response = restTemplate.exchange(
            "/api/dataOwners", HttpMethod.GET, request, new ParameterizedTypeReference<List<DataOwnerDTO>>() {
            });

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1500, response.getBody().get(0).getId());
    }

    @Test
    @Sql("/data/dataOwnerData.sql")
    public void getDataOwner_success() {
        final HttpEntity<String> request = new HttpEntity<>(null, headers());
        final ResponseEntity<DataOwnerDTO> response = restTemplate.exchange(
            "/api/dataOwners/1500", HttpMethod.GET, request, DataOwnerDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(UUID.fromString("dc267fab-612c-42b1-bfe6-261420efc457"), response.getBody().getUid());
    }

    @Test
    public void getDataOwner_notFound() {
        final HttpEntity<String> request = new HttpEntity<>(null, headers());
        final ResponseEntity<ErrorResponse> response = restTemplate.exchange(
            "/api/dataOwners/2166", HttpMethod.GET, request, ErrorResponse.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("ResponseStatusException", response.getBody().getException());
    }

    @Test
    public void createDataOwner_success() {
        final HttpEntity<String> request = new HttpEntity<>(
            readResource("/requests/dataOwnerDTORequest.json"), headers());
        final ResponseEntity<Long> response = restTemplate.exchange(
            "/api/dataOwners", HttpMethod.POST, request, Long.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(1, dataOwnerRepository.count());
    }

    @Test
    public void createDataOwner_missingField() {
        final HttpEntity<String> request = new HttpEntity<>(
            readResource("/requests/dataOwnerDTORequest_missingField.json"), headers());
        final ResponseEntity<ErrorResponse> response = restTemplate.exchange(
            "/api/dataOwners", HttpMethod.POST, request, ErrorResponse.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("MethodArgumentNotValidException", response.getBody().getException());
        assertEquals("uid", response.getBody().getFieldErrors().get(0).getField());
    }

    @Test
    @Sql("/data/dataOwnerData.sql")
    public void updateDataOwner_success() {
        final HttpEntity<String> request = new HttpEntity<>(
            readResource("/requests/dataOwnerDTORequest.json"), headers());
        final ResponseEntity<Void> response = restTemplate.exchange(
            "/api/dataOwners/1500", HttpMethod.PUT, request, Void.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(UUID.fromString("fab26046-bf5a-38b1-a17c-cd7c42fefb62"), dataOwnerRepository
            .findById(1500L).get().getUid());
    }

    @Test
    @Sql("/data/dataOwnerData.sql")
    public void deleteDataOwner_success() {
        final HttpEntity<String> request = new HttpEntity<>(null, headers());
        final ResponseEntity<Void> response = restTemplate.exchange(
            "/api/dataOwners/1500", HttpMethod.DELETE, request, Void.class);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertEquals(0, dataOwnerRepository.count());
    }

}
