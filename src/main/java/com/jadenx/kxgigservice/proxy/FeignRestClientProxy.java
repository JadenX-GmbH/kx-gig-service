package com.jadenx.kxgigservice.proxy;



import com.jadenx.kxgigservice.model.ExecutionGigDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@FeignClient(name = "kx-execution-service", url = "${feign.url}")
public interface FeignRestClientProxy {

    @PostMapping()
    ResponseEntity<Long> createGig(@RequestBody @Valid final ExecutionGigDTO executionGigDTO,
                                   @RequestHeader("Authorization") String user);

    @PutMapping("/{id}/gigId")
    ResponseEntity<Void> updateByGigId(@PathVariable("id") final Long id,
                                       @RequestBody @Valid final ExecutionGigDTO executionGigDTO,
                                       @RequestHeader("Authorization") String user);
}
