package com.jadenx.kxgigservice.rest;

import com.jadenx.kxgigservice.config.BaseIT;
import com.jadenx.kxgigservice.model.ContractDTO;
import com.jadenx.kxgigservice.model.ErrorResponse;
import com.jadenx.kxgigservice.model.OfferDTO;
import com.jadenx.kxgigservice.model.PaginatedResponse;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class OfferControllerTest extends BaseIT {
    // CHECKSTYLE IGNORE check FOR NEXT 1 LINES
    private static final String USER_TOKEN="eyJraWQiOiJvUVA3Zlp4NXN2SHRZVVptS0ZPeUw2WXVIUkFWdVZCclZ0XC80QkdaVHVhTT0iLCJhbGciOiJSUzI1NiJ9.eyJhdF9oYXNoIjoiS09YeVVEWHlxV1Z0QURLYjBvZ1lfUSIsInN1YiI6ImRjMjY3ZmFiLTYxMmMtNDJiMS1iZmU2LTI2MTQyMGVmYzQ1NyIsImNvZ25pdG86Z3JvdXBzIjpbImtub3dsZWRnZXgtZGF0YW93bmVyIl0sImVtYWlsX3ZlcmlmaWVkIjp0cnVlLCJjb2duaXRvOnByZWZlcnJlZF9yb2xlIjoiYXJuOmF3czppYW06OjA0ODk0Mjk0MTI3Mzpyb2xlXC9rbm93bGVkZ2V4LWRhdGFvd25lciIsImlzcyI6Imh0dHBzOlwvXC9jb2duaXRvLWlkcC5ldS1jZW50cmFsLTEuYW1hem9uYXdzLmNvbVwvZXUtY2VudHJhbC0xX2ZkNUQ0b2RuQSIsImNvZ25pdG86dXNlcm5hbWUiOiJkYzI2N2ZhYi02MTJjLTQyYjEtYmZlNi0yNjE0MjBlZmM0NTciLCJvcmlnaW5fanRpIjoiYWEwMzNiYzktMWYwZC00NzFiLTljYTMtZjgyOGNlODI0YjcwIiwiY29nbml0bzpyb2xlcyI6WyJhcm46YXdzOmlhbTo6MDQ4OTQyOTQxMjczOnJvbGVcL2tub3dsZWRnZXgtZGF0YW93bmVyIl0sImF1ZCI6IjY0bDFrMzJrajc5OXIyczJ0NGM5c21hdmE1IiwiZXZlbnRfaWQiOiJmZjY3ZTc0NC03NmJjLTQ1NGQtYWZlNC0wODRiODY4MzU5YzkiLCJ0b2tlbl91c2UiOiJpZCIsImF1dGhfdGltZSI6MTYyODk1MjkwOCwiZXhwIjoxNjI5MDM5MzA4LCJpYXQiOjE2Mjg5NTI5MDgsImp0aSI6IjIyNjkwNDdmLTdkMmItNDkxYS04NzMxLWI1ODA3YTIzYTU1NyIsImVtYWlsIjoiY2xhdWRpby5jb250aUBrbm93bGVkZ2V4LmV1In0.xNzmCfE49el9QIorqvl_98z9DcaNMzFQVoFKlPJLBcQ2ebgpDUr0WbnWqBkf_Or7xHJ7qyMy6yI3EWDKAmRVJeetxcZlJwivzPDRnxPhIxrMF-LIRAJi_4c6Ws3XOkdkISIDkYedl0PLFW4H9fdKqFjGDCqi2oyVWhsR0LQDS2kKYj16mI_JsFDkde4NZej1vLVVWyTmSrB1JQt1JGvXvAtS5kSzMHT1fghEWgFE-oARflV553RGeEv6gfMmk1Kf8WxjT8tCmANacLrX0XM5HcL5caGXyw8Uwst39jP0BFfKwKm6s8Hfc-URdI2sZSCwLFKEwS6Y7MVsszXMgsYrqA";

    @Test
    @Sql({"/data/specialistData.sql","/data/dataOwnerData.sql", "/data/gigData.sql", "/data/offerData.sql"})
    public void getAllOffers_success() {
        final HttpEntity<String> request = new HttpEntity<>(null, headers());
        final ResponseEntity<PaginatedResponse<?>> response = restTemplate.exchange(
            "/api/offers?page=0&size=1", HttpMethod.GET, request,
            new ParameterizedTypeReference<PaginatedResponse<?>>() {
            });

        assertAll(
            ()-> assertEquals(HttpStatus.OK, response.getStatusCode()),
            ()-> assertEquals(1, response.getBody().getData().size()),
            ()-> assertEquals(1, response.getBody().getTotalPages())
        );
    }

    @Test
    @Sql({"/data/specialistData.sql","/data/dataOwnerData.sql",
        "/data/gigData.sql", "/data/offerData.sql"})
    public void getOffer_success() {
        final HttpEntity<String> request = new HttpEntity<>(null, headers());
        final ResponseEntity<OfferDTO> response = restTemplate.exchange(
            "/api/offers/1100", HttpMethod.GET, request, OfferDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, response.getBody().getAccepted());
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
        var header = headers();
        header.setBearerAuth(USER_TOKEN);
        final HttpEntity<String> request =
            new HttpEntity<>(readResource("/requests/offerDTORequest.json"), header);
        final ResponseEntity<Long> response = restTemplate.exchange(
            "/api/offers", HttpMethod.POST, request, Long.class);
        assertAll(
            () -> assertEquals(HttpStatus.CREATED, response.getStatusCode()),
            () -> assertEquals(1, offerRepository.count()),
            () -> assertEquals(2, specialistRepository.count())
        );
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

    @Test
    @Sql({"/data/specialistData.sql","/data/dataOwnerData.sql",
        "/data/gigData.sql", "/data/offerData.sql"})
    public void acceptOrder_success() {
        var header = headers();

        header.setBearerAuth(USER_TOKEN);

        final HttpEntity<String> request = new HttpEntity<>(null, header);
        final ResponseEntity<Void> response = restTemplate.exchange(
            "/api/offers/1100?accepted=true", HttpMethod.PATCH, request, Void.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, contractRepository.count());
    }

    @Test
    @Sql({"/data/specialistData.sql","/data/dataOwnerData.sql",
        "/data/gigData.sql", "/data/offerData.sql", "/data/contractData.sql"})
    public void getContractByOfferId_success() {
        final HttpEntity<String> request = new HttpEntity<>(null, headers());
        final ResponseEntity<ContractDTO> response = restTemplate.exchange(
            "/api/offers/1100/contract", HttpMethod.GET, request, ContractDTO.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1100, response.getBody().getId());
    }
}
