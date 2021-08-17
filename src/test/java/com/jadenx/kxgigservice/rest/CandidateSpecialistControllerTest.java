package com.jadenx.kxgigservice.rest;

import com.jadenx.kxgigservice.config.BaseIT;
import com.jadenx.kxgigservice.model.CandidateSpecialistDTO;
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


public class CandidateSpecialistControllerTest extends BaseIT {

    @Test
    @Sql({"/data/dataOwnerData.sql","/data/gigData.sql","/data/candidateSpecialistData.sql"})
    public void getAllCandidateSpecialists_success() {
        final HttpEntity<String> request = new HttpEntity<>(null, headers());
        final ResponseEntity<List<CandidateSpecialistDTO>> response = restTemplate.exchange(
            "/api/candidateSpecialists", HttpMethod.GET, request,
            new ParameterizedTypeReference<List<CandidateSpecialistDTO>>() {
            });

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1700L, response.getBody().get(0).getId());
    }

    @Test
    @Sql({"/data/dataOwnerData.sql","/data/gigData.sql","/data/candidateSpecialistData.sql"})
    public void getCandidateSpecialist_success() {
        final HttpEntity<String> request = new HttpEntity<>(null, headers());
        final ResponseEntity<CandidateSpecialistDTO> response = restTemplate.exchange(
            "/api/candidateSpecialists/1700", HttpMethod.GET, request, CandidateSpecialistDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(UUID.fromString("bf14fa45-aa3c-4fa6-8f30-80a71fa6af5c"), response.getBody().getUid());
    }

    @Test
    public void getCandidateSpecialist_notFound() {
        final HttpEntity<String> request = new HttpEntity<>(null, headers());
        final ResponseEntity<ErrorResponse> response = restTemplate.exchange(
            "/api/candidateSpecialists/2366", HttpMethod.GET, request, ErrorResponse.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("ResponseStatusException", response.getBody().getException());
    }

    @Test
    public void createCandidateSpecialist_success() {
        final HttpEntity<String> request = new HttpEntity<>(
            readResource("/requests/candidateSpecialistDTORequest.json"), headers());
        final ResponseEntity<Long> response = restTemplate.exchange(
            "/api/candidateSpecialists", HttpMethod.POST, request, Long.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(1, candidateSpecialistRepository.count());
    }

    @Test
    public void createCandidateSpecialist_missingField() {
        final HttpEntity<String> request = new HttpEntity<>(
            readResource("/requests/candidateSpecialistDTORequest_missingField.json"), headers());
        final ResponseEntity<ErrorResponse> response = restTemplate.exchange(
            "/api/candidateSpecialists", HttpMethod.POST, request, ErrorResponse.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("MethodArgumentNotValidException", response.getBody().getException());
        assertEquals("uid", response.getBody().getFieldErrors().get(0).getField());
    }

    @Test
    @Sql({"/data/dataOwnerData.sql","/data/gigData.sql","/data/candidateSpecialistData.sql"})
    public void updateCandidateSpecialist_success() {
        final HttpEntity<String> request = new HttpEntity<>(
            readResource("/requests/candidateSpecialistDTORequest.json"), headers());
        final ResponseEntity<Void> response = restTemplate.exchange(
            "/api/candidateSpecialists/1700", HttpMethod.PUT, request, Void.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(UUID.fromString("bf14fa45-aa3c-4fa6-8f30-80a71fa6af5c"),
            candidateSpecialistRepository.findById(1700L).get().getUid());

    }

    @Test
    @Sql({"/data/dataOwnerData.sql","/data/gigData.sql","/data/candidateSpecialistData.sql"})
    public void deleteCandidateSpecialist_success() {
        final HttpEntity<String> request = new HttpEntity<>(null, headers());
        final ResponseEntity<Void> response = restTemplate.exchange(
            "/api/candidateSpecialists/1700", HttpMethod.DELETE, request, Void.class);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertEquals(0, candidateSpecialistRepository.count());
    }

}
