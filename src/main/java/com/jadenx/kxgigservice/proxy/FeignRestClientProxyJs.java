package com.jadenx.kxgigservice.proxy;

import com.jadenx.kxgigservice.model.ContractBcResponseDTO;
import com.jadenx.kxgigservice.model.ContractBlockchainDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

@FeignClient(name = "kx-blockchain-middleware", url = "${feign.jsUrl}")
public interface FeignRestClientProxyJs {

    @PostMapping("/contracts")
    ResponseEntity<ContractBcResponseDTO> createContract(
        @RequestBody @Valid final ContractBlockchainDto contractBlockchainDto);

}
