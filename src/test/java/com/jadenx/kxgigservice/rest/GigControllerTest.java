package com.jadenx.kxgigservice.rest;

import com.jadenx.kxgigservice.config.BaseIT;
import com.jadenx.kxgigservice.model.ErrorResponse;
import com.jadenx.kxgigservice.model.GigDTO;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class GigControllerTest extends BaseIT {

    // CHECKSTYLE IGNORE check FOR NEXT 2 LINES
    private static final String DATA_OWNER_TOKEN= "eyJraWQiOiJvUVA3Zlp4NXN2SHRZVVptS0ZPeUw2WXVIUkFWdVZCclZ0XC80QkdaVHVhTT0iLCJhbGciOiJSUzI1NiJ9.eyJhdF9oYXNoIjoiWGMwWnlWUWpWR0doTWI4TDcxRnpEQSIsInN1YiI6ImRjMjY3ZmFiLTYxMmMtNDJiMS1iZmU2LTI2MTQyMGVmYzQ1NyIsImNvZ25pdG86Z3JvdXBzIjpbImtub3dsZWRnZXgtZGF0YW93bmVyIl0sImVtYWlsX3ZlcmlmaWVkIjp0cnVlLCJjb2duaXRvOnByZWZlcnJlZF9yb2xlIjoiYXJuOmF3czppYW06OjA0ODk0Mjk0MTI3Mzpyb2xlXC9rbm93bGVkZ2V4LWRhdGFvd25lciIsImlzcyI6Imh0dHBzOlwvXC9jb2duaXRvLWlkcC5ldS1jZW50cmFsLTEuYW1hem9uYXdzLmNvbVwvZXUtY2VudHJhbC0xX2ZkNUQ0b2RuQSIsImNvZ25pdG86dXNlcm5hbWUiOiJkYzI2N2ZhYi02MTJjLTQyYjEtYmZlNi0yNjE0MjBlZmM0NTciLCJvcmlnaW5fanRpIjoiNmVlNTExYjgtMTNkZS00YjIzLThkZDAtMWJhMjc3N2U5M2NhIiwiY29nbml0bzpyb2xlcyI6WyJhcm46YXdzOmlhbTo6MDQ4OTQyOTQxMjczOnJvbGVcL2tub3dsZWRnZXgtZGF0YW93bmVyIl0sImF1ZCI6IjY0bDFrMzJrajc5OXIyczJ0NGM5c21hdmE1IiwiZXZlbnRfaWQiOiI4ZGQ4MzkzMi0zY2NmLTRiNWYtYTc0MS0xMTdmOTMyOGU5YjYiLCJ0b2tlbl91c2UiOiJpZCIsImF1dGhfdGltZSI6MTYyODU5MDg2MywiZXhwIjoxNjI4Njc3MjYzLCJpYXQiOjE2Mjg1OTA4NjMsImp0aSI6IjRmOTE0ZjdmLTZkYmYtNDE3Mi05OGQzLWQ5YWQ3NDM5MGVmOSIsImVtYWlsIjoiY2xhdWRpby5jb250aUBrbm93bGVkZ2V4LmV1In0.nwS0EG2Z2TnsQzrOG1T_5TT21gLkOISqwUNmkGePVZGjTMVvclNa9e9PjNPpCVoIs_8pBbu2e7LvMs2gj3XY1KCvkqxDPtOHAcwilG2i8wm09BKHmtwPJX2XrogdtSM9TTXwdXxKjqAWioIlfhiCIEpw73uoMlSdHGgn0VXQ6wEWnS8-s7HgKVCld8J1fbVwfDzFDC6vYEY5kPwkmRTNf36cZpZeNFXV6yeZzxQUq70hsv9AdxCYzQ9B9HACi_ZWHDFy6CpQ9lO207dyt0Ds35wDcC6njlUUyI612_C5BT316VauBZhgz4WATRlBSUR6WmHgbVLpKoY81vi2FOpwFw";
    private static final String SPECIALIST_TOKEN="eyJraWQiOiJvUVA3Zlp4NXN2SHRZVVptS0ZPeUw2WXVIUkFWdVZCclZ0XC80QkdaVHVhTT0iLCJhbGciOiJSUzI1NiJ9.eyJhdF9oYXNoIjoiNWNGNWM2WjYxNTRIcWVzVDF5a080QSIsInN1YiI6ImJmMTRmYTQ1LWFhM2MtNGZhNi04ZjMwLTgwYTcxZmE2YWY1YyIsImNvZ25pdG86Z3JvdXBzIjpbImtub3dsZWRnZXgtZGF0YXNjaWVudGlzdCJdLCJlbWFpbF92ZXJpZmllZCI6dHJ1ZSwiY29nbml0bzpwcmVmZXJyZWRfcm9sZSI6ImFybjphd3M6aWFtOjowNDg5NDI5NDEyNzM6cm9sZVwva25vd2xlZGdleC1kYXRhc2NpZW50aXN0IiwiaXNzIjoiaHR0cHM6XC9cL2NvZ25pdG8taWRwLmV1LWNlbnRyYWwtMS5hbWF6b25hd3MuY29tXC9ldS1jZW50cmFsLTFfZmQ1RDRvZG5BIiwiY29nbml0bzp1c2VybmFtZSI6ImJmMTRmYTQ1LWFhM2MtNGZhNi04ZjMwLTgwYTcxZmE2YWY1YyIsIm9yaWdpbl9qdGkiOiIzN2I2YTU0NC0xNWE4LTQ3ZDMtOGFjOC01NmUwNjBkOTQ2ZjMiLCJjb2duaXRvOnJvbGVzIjpbImFybjphd3M6aWFtOjowNDg5NDI5NDEyNzM6cm9sZVwva25vd2xlZGdleC1kYXRhc2NpZW50aXN0Il0sImF1ZCI6IjY0bDFrMzJrajc5OXIyczJ0NGM5c21hdmE1IiwiZXZlbnRfaWQiOiJkMzdmZjIxMS00ZTdmLTQ5ZmYtODY0ZS01MGEzMTkwMzE5MTAiLCJ0b2tlbl91c2UiOiJpZCIsImF1dGhfdGltZSI6MTYyODUwMzY1NywiZXhwIjoxNjI4NTkwMDU3LCJpYXQiOjE2Mjg1MDM2NTcsImp0aSI6Ijk0ZjMwN2I1LTM4YmEtNDA4Mi1iYTY4LTkyOGE1Njg2YTdjOSIsImVtYWlsIjoidmFuZXNzYS5tYXJ0aW5lekBrbm93bGVkZ2V4LmV1In0.GkCdTdKw7J7U-2rQrzHruJTd2PY43zB_zwzs7EOuvYZWD1UFm7_oUQ4hINVjSfQLNOiL6Be4ZT1YW0wdjYx4_Uu5ctgGHE-6xMbfcgVfXABrEoL3jLE-V2aXuCWjL10LfZXsxammX9NRWwMhZoAbOl954s2NR32JBn-lRIdxme-albE9WQyho1PEMXyvvtYymLa4whOvM3Pm0ZZbw5NVoSIPVBI_aRjI9VGP4D2K5mYsVyKKeNGzaUo808GKSAuPIPAd3HFY5GPCSgeWZz1z9Wngf5NijjomN_9_PZbic9LBOLRooIFRGs0fJPErbb03dMRaVNnDW-ohCNgI-3qotw";

    @Test
    @Sql({"/data/dataOwnerData.sql","/data/gigData.sql"})
    public void getAllGigs_success() {
        final HttpEntity<String> request = new HttpEntity<>(null, headers());
        final ResponseEntity<List<GigDTO>> response = restTemplate.exchange(
            "/api/gigs", HttpMethod.GET, request, new ParameterizedTypeReference<List<GigDTO>>() {
            });

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1000, response.getBody().get(0).getId());
    }

    @Test
    @Sql({"/data/dataOwnerData.sql","/data/gigData.sql","/data/candidateSpecialistData.sql"})
    public void getGigsDataOwner_success() {
        var header = headers();

        header.setBearerAuth(DATA_OWNER_TOKEN);

        final HttpEntity<String> request = new HttpEntity<>(null, header);
        final ResponseEntity<List<GigDTO>> response = restTemplate.exchange(
            "/api/gigs/dataowner/gigs", HttpMethod.GET, request, new ParameterizedTypeReference<List<GigDTO>>() {
            });

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1001, response.getBody().get(0).getId());
    }

    @Test
    @Sql({"/data/dataOwnerData.sql","/data/gigData.sql","/data/candidateSpecialistData.sql"})
    public void getGigsSpecialist_success() {
        var header = headers();
        // CHECKSTYLE IGNORE check FOR NEXT 1 LINES
        header.setBearerAuth(SPECIALIST_TOKEN);
        final HttpEntity<String> request = new HttpEntity<>(null, header);
        final ResponseEntity<List<GigDTO>> response = restTemplate.exchange(
            "/api/gigs/specialist/gigs", HttpMethod.GET, request, new ParameterizedTypeReference<List<GigDTO>>() {
            });

        assertEquals(HttpStatus.OK, response.getStatusCode());
        //assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals(1000, response.getBody().get(0).getId());
    }

    @Test
    @Sql({"/data/dataOwnerData.sql","/data/gigData.sql"})
    public void getGig_success() {
        final HttpEntity<String> request = new HttpEntity<>(null, headers());
        final ResponseEntity<GigDTO> response = restTemplate.exchange(
            "/api/gigs/1000", HttpMethod.GET, request, GigDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Donec ac nibh...", response.getBody().getGigId());
    }

    @Test
    public void getGig_notFound() {
        final HttpEntity<String> request = new HttpEntity<>(null, headers());
        final ResponseEntity<ErrorResponse> response = restTemplate.exchange(
            "/api/gigs/1666", HttpMethod.GET, request, ErrorResponse.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("ResponseStatusException", response.getBody().getException());
    }

    @Test
    public void createGig_success() {
        final HttpEntity<String> request = new HttpEntity<>(
            readResource("/requests/gigDTORequest.json"), headers());
        final ResponseEntity<Long> response = restTemplate.exchange(
            "/api/gigs", HttpMethod.POST, request, Long.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(1, gigRepository.count());
    }

    @Test
    public void createGig_missingField() {
        final HttpEntity<String> request = new HttpEntity<>(
            readResource("/requests/gigDTORequest_missingField.json"), headers());
        final ResponseEntity<ErrorResponse> response = restTemplate.exchange(
            "/api/gigs", HttpMethod.POST, request, ErrorResponse.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("MethodArgumentNotValidException", response.getBody().getException());
        assertEquals("gigId", response.getBody().getFieldErrors().get(0).getField());
    }

    @Test
    @Sql({"/data/dataOwnerData.sql","/data/gigData.sql"})
    public void updateGig_success() {
        final HttpEntity<String> request = new HttpEntity<>(
            readResource("/requests/gigDTORequest.json"), headers());
        final ResponseEntity<Void> response = restTemplate.exchange(
            "/api/gigs/1000", HttpMethod.PUT, request, Void.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Aenean pulvinar...", gigRepository.findById(1000L).get().getGigId());
    }

    @Test
    @Sql({"/data/dataOwnerData.sql","/data/gigData.sql",
        "/data/candidateSpecialistData.sql",
        "/data/specialistData.sql",
        "/data/skillsetData.sql",
        "/data/slaStatementData.sql", "/data/offerData.sql"})
    public void deleteGig_success() {
        final HttpEntity<String> request = new HttpEntity<>(null, headers());
        final ResponseEntity<Void> response = restTemplate.exchange(
            "/api/gigs/1000", HttpMethod.DELETE, request, Void.class);
        assertAll("Evaluate state of db after delete",
            () -> assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode()),
            () -> assertEquals(1, gigRepository.count()),
            () -> assertEquals(1, candidateSpecialistRepository.count()),
            () -> assertEquals(1, dataOwnerRepository.count()),
            () -> assertEquals(0, skillsetRepository.count()),
            () -> assertEquals(0, slaStatementRepository.count()),
            () -> assertEquals(0, offerRepository.count()),
            () -> assertEquals(1, specialistRepository.count())


        );


    }

}
