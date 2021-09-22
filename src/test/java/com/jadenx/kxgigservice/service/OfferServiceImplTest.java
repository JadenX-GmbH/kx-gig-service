package com.jadenx.kxgigservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jadenx.kxgigservice.domain.*;
import com.jadenx.kxgigservice.mapper.GigDTOMapper;
import com.jadenx.kxgigservice.model.ContractDTO;
import com.jadenx.kxgigservice.model.ExecutionType;
import com.jadenx.kxgigservice.model.GigDTO;
import com.jadenx.kxgigservice.model.Status;
import com.jadenx.kxgigservice.proxy.FeignRestClientProxy;
import com.jadenx.kxgigservice.repos.OfferRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OfferServiceImplTest {

    @Mock
    private OfferRepository offerRepository;
    @Mock
    private  FeignRestClientProxy feignRestClientProxy;
    @Mock
    private GigDTOMapper gigDTOMapper;
    @Mock
    private ContractService contractService;
    @InjectMocks
    private OfferServiceImpl service;

    // CHECKSTYLE IGNORE check FOR NEXT 1 LINES
    private static final String DATA_OWNER_TOKEN = "eyJraWQiOiJvUVA3Zlp4NXN2SHRZVVptS0ZPeUw2WXVIUkFWdVZCclZ0XC80QkdaVHVhTT0iLCJhbGciOiJSUzI1NiJ9.eyJhdF9oYXNoIjoiWGMwWnlWUWpWR0doTWI4TDcxRnpEQSIsInN1YiI6ImRjMjY3ZmFiLTYxMmMtNDJiMS1iZmU2LTI2MTQyMGVmYzQ1NyIsImNvZ25pdG86Z3JvdXBzIjpbImtub3dsZWRnZXgtZGF0YW93bmVyIl0sImVtYWlsX3ZlcmlmaWVkIjp0cnVlLCJjb2duaXRvOnByZWZlcnJlZF9yb2xlIjoiYXJuOmF3czppYW06OjA0ODk0Mjk0MTI3Mzpyb2xlXC9rbm93bGVkZ2V4LWRhdGFvd25lciIsImlzcyI6Imh0dHBzOlwvXC9jb2duaXRvLWlkcC5ldS1jZW50cmFsLTEuYW1hem9uYXdzLmNvbVwvZXUtY2VudHJhbC0xX2ZkNUQ0b2RuQSIsImNvZ25pdG86dXNlcm5hbWUiOiJkYzI2N2ZhYi02MTJjLTQyYjEtYmZlNi0yNjE0MjBlZmM0NTciLCJvcmlnaW5fanRpIjoiNmVlNTExYjgtMTNkZS00YjIzLThkZDAtMWJhMjc3N2U5M2NhIiwiY29nbml0bzpyb2xlcyI6WyJhcm46YXdzOmlhbTo6MDQ4OTQyOTQxMjczOnJvbGVcL2tub3dsZWRnZXgtZGF0YW93bmVyIl0sImF1ZCI6IjY0bDFrMzJrajc5OXIyczJ0NGM5c21hdmE1IiwiZXZlbnRfaWQiOiI4ZGQ4MzkzMi0zY2NmLTRiNWYtYTc0MS0xMTdmOTMyOGU5YjYiLCJ0b2tlbl91c2UiOiJpZCIsImF1dGhfdGltZSI6MTYyODU5MDg2MywiZXhwIjoxNjI4Njc3MjYzLCJpYXQiOjE2Mjg1OTA4NjMsImp0aSI6IjRmOTE0ZjdmLTZkYmYtNDE3Mi05OGQzLWQ5YWQ3NDM5MGVmOSIsImVtYWlsIjoiY2xhdWRpby5jb250aUBrbm93bGVkZ2V4LmV1In0.nwS0EG2Z2TnsQzrOG1T_5TT21gLkOISqwUNmkGePVZGjTMVvclNa9e9PjNPpCVoIs_8pBbu2e7LvMs2gj3XY1KCvkqxDPtOHAcwilG2i8wm09BKHmtwPJX2XrogdtSM9TTXwdXxKjqAWioIlfhiCIEpw73uoMlSdHGgn0VXQ6wEWnS8-s7HgKVCld8J1fbVwfDzFDC6vYEY5kPwkmRTNf36cZpZeNFXV6yeZzxQUq70hsv9AdxCYzQ9B9HACi_ZWHDFy6CpQ9lO207dyt0Ds35wDcC6njlUUyI612_C5BT316VauBZhgz4WATRlBSUR6WmHgbVLpKoY81vi2FOpwFw";


    @Test
    public void acceptOffer_success() throws JsonProcessingException {
        DataOwner dataOwner = new DataOwner();
        dataOwner.setId(10023L);
        dataOwner.setUid(UUID.fromString("cfa7de13-c709-450d-9334-49e7adf3b45b"));

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
        gig.setDataOwner(dataOwner);

        Specialist specialist = new Specialist();
        specialist.setId(10000L);
        specialist.setUid(UUID.fromString("585b3bf4-5221-4c18-b5a6-9454e39c2c3f"));

        Offer offer = new Offer();
        offer.setId(1000L);
        offer.setAccepted(Boolean.FALSE);
        offer.setPrice(BigDecimal.valueOf(100.0));
        offer.setPriceToken(80.0);
        offer.setDescription("Sed ut perspiciatis unde omnis iste natus error.");
        offer.setIsActive(Boolean.TRUE);
        offer.setGig(gig);
        offer.setSpecialist(specialist);

        when(offerRepository.findById(Mockito.any())).thenReturn(Optional.of(offer));
        when(gigDTOMapper.mapToDTO(Mockito.any(), Mockito.any())).thenReturn(new GigDTO());
        when(contractService.create(Mockito.any())).thenReturn(100L);

        service.acceptOffer(1000L, Boolean.TRUE, DATA_OWNER_TOKEN);

        verify(contractService, atLeastOnce()).create(Mockito.any(ContractDTO.class));
    }
}
