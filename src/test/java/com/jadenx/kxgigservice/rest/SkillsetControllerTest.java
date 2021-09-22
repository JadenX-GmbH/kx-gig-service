package com.jadenx.kxgigservice.rest;

import com.jadenx.kxgigservice.config.BaseIT;
import com.jadenx.kxgigservice.model.ErrorResponse;
import com.jadenx.kxgigservice.model.SkillsetDTO;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class SkillsetControllerTest extends BaseIT {

    @Test
    @Sql({"/data/dataOwnerData.sql", "/data/gigData.sql", "/data/skillsetData.sql"})
    public void getAllSkillsets_success() {
        final HttpEntity<String> request = new HttpEntity<>(null, headers());
        final ResponseEntity<List<SkillsetDTO>> response = restTemplate.exchange(
            "/api/skillsets", HttpMethod.GET, request,
            new ParameterizedTypeReference<List<SkillsetDTO>>() {
            });

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1400, response.getBody().get(0).getId());
    }

    @Test
    @Sql({"/data/dataOwnerData.sql", "/data/gigData.sql", "/data/skillsetData.sql"})
    public void getSkillset_success() {
        final HttpEntity<String> request = new HttpEntity<>(null, headers());
        final ResponseEntity<SkillsetDTO> response = restTemplate.exchange(
            "/api/skillsets/1400", HttpMethod.GET, request, SkillsetDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Donec ac nibh...", response.getBody().getSkillsetId());
    }

    @Test
    public void getSkillset_notFound() {
        final HttpEntity<String> request = new HttpEntity<>(null, headers());
        final ResponseEntity<ErrorResponse> response = restTemplate.exchange(
            "/api/skillsets/2066", HttpMethod.GET, request, ErrorResponse.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("ResponseStatusException", response.getBody().getException());
    }

    @Test
    @Sql({"/data/dataOwnerData.sql", "/data/gigData.sql"})
    public void createSkillset_success() {
        final HttpEntity<String> request = new HttpEntity<>(
            readResource("/requests/skillsetDTORequest.json"), headers());
        final ResponseEntity<Long> response = restTemplate.exchange(
            "/api/skillsets", HttpMethod.POST, request, Long.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(1, skillsetRepository.count());
    }

    @Test
    public void createSkillset_missingField() {
        final HttpEntity<String> request = new HttpEntity<>(
            readResource("/requests/skillsetDTORequest_missingField.json"), headers());
        final ResponseEntity<ErrorResponse> response = restTemplate.exchange(
            "/api/skillsets", HttpMethod.POST, request, ErrorResponse.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("MethodArgumentNotValidException", response.getBody().getException());
        assertEquals("skillsetId", response.getBody().getFieldErrors().get(0).getField());
    }

    @Test
    @Sql({"/data/dataOwnerData.sql", "/data/gigData.sql", "/data/skillsetData.sql"})
    public void updateSkillset_success() {
        final HttpEntity<String> request = new HttpEntity<>(
            readResource("/requests/skillsetDTORequest.json"), headers());
        final ResponseEntity<Void> response = restTemplate.exchange(
            "/api/skillsets/1400", HttpMethod.PUT, request, Void.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Aenean pulvinar...", skillsetRepository.findById(1400L).get().getSkillsetId());
    }

    @Test
    @Sql({"/data/dataOwnerData.sql", "/data/gigData.sql", "/data/skillsetData.sql"})
    public void deleteSkillset_success() {
        final HttpEntity<String> request = new HttpEntity<>(null, headers());
        final ResponseEntity<Void> response = restTemplate.exchange(
            "/api/skillsets/1400", HttpMethod.DELETE, request, Void.class);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertEquals(0, skillsetRepository.count());
        assertEquals(2, gigRepository.count());
    }

}
