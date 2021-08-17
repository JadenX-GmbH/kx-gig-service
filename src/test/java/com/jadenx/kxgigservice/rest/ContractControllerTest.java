package com.jadenx.kxgigservice.rest;

import com.jadenx.kxgigservice.config.BaseIT;
import com.jadenx.kxgigservice.model.ContractDTO;
import com.jadenx.kxgigservice.model.ErrorResponse;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class ContractControllerTest extends BaseIT {

    @Test
    @Sql({"/data/specialistData.sql", "/data/dataOwnerData.sql",
        "/data/gigData.sql",
        "/data/offerData.sql", "/data/contractData.sql"})
    public void getAllContracts_success() {
        final HttpEntity<String> request = new HttpEntity<>(null, headers());
        final ResponseEntity<List<ContractDTO>> response = restTemplate.exchange(
            "/api/contracts", HttpMethod.GET, request, new ParameterizedTypeReference<List<ContractDTO>>() {
            });

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1200L, response.getBody().get(0).getId());
    }

    @Test
    @Sql({"/data/specialistData.sql", "/data/dataOwnerData.sql",
        "/data/gigData.sql", "/data/offerData.sql",
        "/data/contractData.sql"})
    public void getContract_success() {
        final HttpEntity<String> request = new HttpEntity<>(null, headers());
        final ResponseEntity<ContractDTO> response = restTemplate.exchange(
            "/api/contracts/1200", HttpMethod.GET, request, ContractDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(false, response.getBody().getSignatureRdo());
    }

    @Test
    public void getContract_notFound() {
        final HttpEntity<String> request = new HttpEntity<>(null, headers());
        final ResponseEntity<ErrorResponse> response = restTemplate.exchange(
            "/api/contracts/1866", HttpMethod.GET, request, ErrorResponse.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("ResponseStatusException", response.getBody().getException());
    }

    @Test
    @Sql({"/data/specialistData.sql","/data/dataOwnerData.sql",
        "/data/gigData.sql", "/data/offerData.sql"})
    public void createContract_success() {
        final HttpEntity<String> request = new HttpEntity<>(
            readResource("/requests/contractDTORequest.json"), headers());
        final ResponseEntity<Long> response = restTemplate.exchange(
            "/api/contracts", HttpMethod.POST, request, Long.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(1, contractRepository.count());
    }

    @Test
    public void createContract_missingField() {
        final HttpEntity<String> request = new HttpEntity<>(
            readResource("/requests/contractDTORequest_missingField.json"), headers());
        final ResponseEntity<ErrorResponse> response = restTemplate.exchange(
            "/api/contracts", HttpMethod.POST, request, ErrorResponse.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("MethodArgumentNotValidException", response.getBody().getException());
        assertEquals("signatureRdo", response.getBody().getFieldErrors().get(0).getField());
    }

    @Test
    @Sql({"/data/specialistData.sql","/data/dataOwnerData.sql",
        "/data/gigData.sql", "/data/offerData.sql",
        "/data/contractData.sql"})
    public void updateContract_success() {
        final HttpEntity<String> request = new HttpEntity<>(
            readResource("/requests/contractDTORequest.json"), headers());
        final ResponseEntity<Void> response = restTemplate.exchange(
            "/api/contracts/1200", HttpMethod.PUT, request, Void.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, contractRepository.findById(1200L).get().getSignatureRdo());
    }

    @Test
    @Sql({"/data/specialistData.sql","/data/dataOwnerData.sql",
        "/data/gigData.sql", "/data/offerData.sql",
        "/data/contractData.sql"})
    public void deleteContract_success() {
        final HttpEntity<String> request = new HttpEntity<>(null, headers());
        final ResponseEntity<Void> response = restTemplate.exchange(
            "/api/contracts/1200", HttpMethod.DELETE, request, Void.class);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertEquals(0, contractRepository.count());
        assertEquals(1, offerRepository.count());
        assertEquals(2, gigRepository.count());
        assertEquals(1, specialistRepository.count());
    }

}
