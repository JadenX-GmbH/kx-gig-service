package com.jadenx.kxgigservice.service;

import com.jadenx.kxgigservice.domain.CandidateSpecialist;
import com.jadenx.kxgigservice.domain.DataOwner;
import com.jadenx.kxgigservice.domain.Gig;
import com.jadenx.kxgigservice.domain.Specialist;
import com.jadenx.kxgigservice.mapper.GigDTOMapper;
import com.jadenx.kxgigservice.model.ExecutionType;
import com.jadenx.kxgigservice.model.GigDTO;
import com.jadenx.kxgigservice.model.Status;
import com.jadenx.kxgigservice.proxy.FeignRestClientProxy;
import com.jadenx.kxgigservice.repos.CandidateSpecialistRepository;
import com.jadenx.kxgigservice.repos.DataOwnerRepository;
import com.jadenx.kxgigservice.repos.GigRepository;
import com.jadenx.kxgigservice.repos.SpecialistRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GigServiceImplTest {

    @Mock
    private GigRepository gigRepository;
    @Mock
    private CandidateSpecialistRepository candidateSpecialistRepository;
    @Mock
    private SpecialistRepository specialistRepository;
    @Mock
    private FeignRestClientProxy feignRestClientProxy;
    @Mock
    private GigDTOMapper gigDTOMapper;
    @Mock
    private DataOwnerRepository dataOwnerRepository;
    @Mock
    private DataOwnerService dataOwnerService;
    @Mock
    Principal user;
    @InjectMocks
    private GigServiceImpl service;

    // CHECKSTYLE IGNORE check FOR NEXT 1 LINES
    private static final String DATA_OWNER_TOKEN = "eyJraWQiOiJvUVA3Zlp4NXN2SHRZVVptS0ZPeUw2WXVIUkFWdVZCclZ0XC80QkdaVHVhTT0iLCJhbGciOiJSUzI1NiJ9.eyJhdF9oYXNoIjoiWGMwWnlWUWpWR0doTWI4TDcxRnpEQSIsInN1YiI6ImRjMjY3ZmFiLTYxMmMtNDJiMS1iZmU2LTI2MTQyMGVmYzQ1NyIsImNvZ25pdG86Z3JvdXBzIjpbImtub3dsZWRnZXgtZGF0YW93bmVyIl0sImVtYWlsX3ZlcmlmaWVkIjp0cnVlLCJjb2duaXRvOnByZWZlcnJlZF9yb2xlIjoiYXJuOmF3czppYW06OjA0ODk0Mjk0MTI3Mzpyb2xlXC9rbm93bGVkZ2V4LWRhdGFvd25lciIsImlzcyI6Imh0dHBzOlwvXC9jb2duaXRvLWlkcC5ldS1jZW50cmFsLTEuYW1hem9uYXdzLmNvbVwvZXUtY2VudHJhbC0xX2ZkNUQ0b2RuQSIsImNvZ25pdG86dXNlcm5hbWUiOiJkYzI2N2ZhYi02MTJjLTQyYjEtYmZlNi0yNjE0MjBlZmM0NTciLCJvcmlnaW5fanRpIjoiNmVlNTExYjgtMTNkZS00YjIzLThkZDAtMWJhMjc3N2U5M2NhIiwiY29nbml0bzpyb2xlcyI6WyJhcm46YXdzOmlhbTo6MDQ4OTQyOTQxMjczOnJvbGVcL2tub3dsZWRnZXgtZGF0YW93bmVyIl0sImF1ZCI6IjY0bDFrMzJrajc5OXIyczJ0NGM5c21hdmE1IiwiZXZlbnRfaWQiOiI4ZGQ4MzkzMi0zY2NmLTRiNWYtYTc0MS0xMTdmOTMyOGU5YjYiLCJ0b2tlbl91c2UiOiJpZCIsImF1dGhfdGltZSI6MTYyODU5MDg2MywiZXhwIjoxNjI4Njc3MjYzLCJpYXQiOjE2Mjg1OTA4NjMsImp0aSI6IjRmOTE0ZjdmLTZkYmYtNDE3Mi05OGQzLWQ5YWQ3NDM5MGVmOSIsImVtYWlsIjoiY2xhdWRpby5jb250aUBrbm93bGVkZ2V4LmV1In0.nwS0EG2Z2TnsQzrOG1T_5TT21gLkOISqwUNmkGePVZGjTMVvclNa9e9PjNPpCVoIs_8pBbu2e7LvMs2gj3XY1KCvkqxDPtOHAcwilG2i8wm09BKHmtwPJX2XrogdtSM9TTXwdXxKjqAWioIlfhiCIEpw73uoMlSdHGgn0VXQ6wEWnS8-s7HgKVCld8J1fbVwfDzFDC6vYEY5kPwkmRTNf36cZpZeNFXV6yeZzxQUq70hsv9AdxCYzQ9B9HACi_ZWHDFy6CpQ9lO207dyt0Ds35wDcC6njlUUyI612_C5BT316VauBZhgz4WATRlBSUR6WmHgbVLpKoY81vi2FOpwFw";

    @Test
    public void createGig_success() {
        GigDTO gigDTO = new GigDTO();
        gigDTO.setMaxPrice(BigDecimal.valueOf(100.0));
        gigDTO.setPrice(BigDecimal.valueOf(80.0));
        gigDTO.setPriceToken(18.90);
        gigDTO.setTitle("Aenean pulvinar...");
        gigDTO.setDescription("Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium "
            + "doloremque laudantium, totam rem aperiam.");
        gigDTO.setExecutionType(ExecutionType.TEE);
        gigDTO.setStatus(Status.IN_PROGRESS);
        gigDTO.setExecutionpool("Aenean pulvinar...");
        gigDTO.setIsActive(Boolean.TRUE);

        Gig gig = new Gig();
        gig.setId(100L);
        gig.setMaxPrice(BigDecimal.valueOf(100.0));
        gig.setPrice(BigDecimal.valueOf(80.0));
        gig.setPriceToken(18.90);
        gig.setTitle("Aenean pulvinar...");
        gig.setDescription("Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium "
            + "doloremque laudantium, totam rem aperiam.");
        gig.setExecutionType(ExecutionType.TEE);
        gig.setStatus(Status.IN_PROGRESS);
        gig.setExecutionpool("Aenean pulvinar...");
        gig.setIsActive(Boolean.TRUE);

        final List<Specialist> specialistList = new ArrayList<>();
        Specialist specialist1 = new Specialist();
        specialist1.setId(10000L);
        specialist1.setUid(UUID.fromString("585b3bf4-5221-4c18-b5a6-9454e39c2c3f"));
        Specialist specialist2 = new Specialist();
        specialist2.setId(10001L);
        specialist2.setUid(UUID.fromString("bf14fa45-aa3c-4fa6-8f30-80a71fa6af5c"));
        Specialist specialist3 = new Specialist();
        specialist3.setId(10002L);
        specialist3.setUid(UUID.fromString("cda7de13-c709-450d-9334-49e7adf3b45c"));
        specialistList.add(specialist1);
        specialistList.add(specialist2);
        specialistList.add(specialist3);

        final Set<CandidateSpecialist> candidateSpecialistSet = new HashSet<>();
        CandidateSpecialist candidateSpecialist1 = new CandidateSpecialist();
        candidateSpecialist1.setId(10005L);
        candidateSpecialist1.setUid(UUID.fromString("585b3bf4-5221-4c18-b5a6-9454e39c2c3f"));
        CandidateSpecialist candidateSpecialist2 = new CandidateSpecialist();
        candidateSpecialist2.setId(10006L);
        candidateSpecialist2.setUid(UUID.fromString("bf14fa45-aa3c-4fa6-8f30-80a71fa6af5c"));
        CandidateSpecialist candidateSpecialist3 = new CandidateSpecialist();
        candidateSpecialist3.setId(10007L);
        candidateSpecialist3.setUid(UUID.fromString("cda7de13-c709-450d-9334-49e7adf3b45c"));
        candidateSpecialistSet.add(candidateSpecialist1);
        candidateSpecialistSet.add(candidateSpecialist2);
        candidateSpecialistSet.add(candidateSpecialist3);

        DataOwner dataOwner = new DataOwner();
        dataOwner.setId(10023L);
        dataOwner.setUid(UUID.fromString("cfa7de13-c709-450d-9334-49e7adf3b45b"));
        gig.setDataOwner(dataOwner);

        when(user.getName()).thenReturn("cfa7de13-c709-450d-9334-49e7adf3b45b");
        when(dataOwnerRepository.existsDataOwnerByUid(Mockito.any())).thenReturn(true);
        when(gigDTOMapper.mapToEntity(Mockito.any(), Mockito.any())).thenReturn(gig);
        when(gigRepository.save(Mockito.any())).thenReturn(gig);
        when(specialistRepository.findAll()).thenReturn(specialistList);
        when(candidateSpecialistRepository.findByUid(Mockito.any())).thenReturn(null);
        when(candidateSpecialistRepository.save(Mockito.any())).thenReturn(new CandidateSpecialist());
        gig.setGigChosenSpecialistCandidateSpecialists(candidateSpecialistSet);
        when(gigRepository.save(Mockito.any())).thenReturn(gig);
        when(feignRestClientProxy.createGig(Mockito.any(), Mockito.any())).thenReturn(ResponseEntity.ok(10L));

        Long response = service.create(gigDTO,user, DATA_OWNER_TOKEN );

        assertNotNull(response);
        assertEquals(100, response);
    }
}
