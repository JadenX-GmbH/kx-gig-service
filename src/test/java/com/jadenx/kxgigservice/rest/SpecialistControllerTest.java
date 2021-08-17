package com.jadenx.kxgigservice.rest;

import com.jadenx.kxgigservice.config.BaseIT;
import com.jadenx.kxgigservice.model.ErrorResponse;
import com.jadenx.kxgigservice.model.SpecialistDTO;
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


public class SpecialistControllerTest extends BaseIT {

    @Test
    @Sql("/data/specialistData.sql")
    public void getAllSpecialists_success() {
        final HttpEntity<String> request = new HttpEntity<>(null, headers());
        final ResponseEntity<List<SpecialistDTO>> response = restTemplate.exchange(
            "/api/specialists", HttpMethod.GET, request,
            new ParameterizedTypeReference<List<SpecialistDTO>>() {
            });

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1600L, response.getBody().get(0).getId());
    }

    @Test
    @Sql("/data/specialistData.sql")
    public void getSpecialist_success() {
        final HttpEntity<String> request = new HttpEntity<>(null, headers());
        final ResponseEntity<SpecialistDTO> response = restTemplate.exchange(
            "/api/specialists/1600", HttpMethod.GET, request, SpecialistDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(UUID.fromString("bf14fa45-aa3c-4fa6-8f30-80a71fa6af5c"), response.getBody().getUid());
    }

    @Test
    public void getSpecialist_notFound() {
        final HttpEntity<String> request = new HttpEntity<>(null, headers());
        final ResponseEntity<ErrorResponse> response = restTemplate.exchange(
            "/api/specialists/2266", HttpMethod.GET, request, ErrorResponse.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("ResponseStatusException", response.getBody().getException());
    }

    @Test
    public void createSpecialist_success() {
        final HttpEntity<String> request = new HttpEntity<>(
            readResource("/requests/specialistDTORequest.json"), headers());
        final ResponseEntity<Long> response = restTemplate.exchange(
            "/api/specialists", HttpMethod.POST, request, Long.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(1, specialistRepository.count());
    }

    @Test
    public void createSpecialist_missingField() {
        final HttpEntity<String> request = new HttpEntity<>(
            readResource("/requests/specialistDTORequest_missingField.json"), headers());
        final ResponseEntity<ErrorResponse> response = restTemplate.exchange(
            "/api/specialists", HttpMethod.POST, request, ErrorResponse.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("MethodArgumentNotValidException", response.getBody().getException());
        assertEquals("uid", response.getBody().getFieldErrors().get(0).getField());
    }

    @Test
    @Sql("/data/specialistData.sql")
    public void updateSpecialist_success() {
        final HttpEntity<String> request = new HttpEntity<>(
            readResource("/requests/specialistDTORequest.json"), headers());
        final ResponseEntity<Void> response = restTemplate.exchange(
            "/api/specialists/1600", HttpMethod.PUT, request, Void.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(UUID.fromString("bf14fa45-aa3c-4fa6-8f30-80a71fa6af5c"),
            specialistRepository.findById(1600L).get().getUid());

    }

    @Test
    @Sql("/data/specialistData.sql")
    public void deleteSpecialist_success() {
        final HttpEntity<String> request = new HttpEntity<>(null, headers());
        final ResponseEntity<Void> response = restTemplate.exchange(
            "/api/specialists/1600", HttpMethod.DELETE, request, Void.class);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertEquals(0, specialistRepository.count());
    }

}
