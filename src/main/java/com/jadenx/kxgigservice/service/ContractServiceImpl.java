package com.jadenx.kxgigservice.service;

import com.google.common.hash.Hashing;
import com.jadenx.kxgigservice.domain.Contract;
import com.jadenx.kxgigservice.mapper.ContractMapper;
import com.jadenx.kxgigservice.model.ContractBcResponseDTO;
import com.jadenx.kxgigservice.model.ContractBlockchainDto;
import com.jadenx.kxgigservice.model.ContractDTO;
import com.jadenx.kxgigservice.proxy.FeignRestClientProxyJs;
import com.jadenx.kxgigservice.repos.ContractRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
@Transactional
public class ContractServiceImpl implements ContractService {

    private final ContractRepository contractRepository;
    private final ContractMapper contractMapper;
    private final FeignRestClientProxyJs feignRestClientProxyJs;

    public ContractServiceImpl(final ContractRepository contractRepository,
                               final ContractMapper contractMapper,
                               final FeignRestClientProxyJs feignRestClientProxyJs) {
        this.contractRepository = contractRepository;
        this.contractMapper = contractMapper;
        this.feignRestClientProxyJs = feignRestClientProxyJs;
    }

    @Override
    public List<ContractDTO> findAll() {
        return contractRepository.findAll()
            .stream()
            .map(contract -> contractMapper.mapToDTO(contract, new ContractDTO()))
            .collect(Collectors.toList());
    }

    @Override
    public ContractDTO get(final Long id) {
        return contractRepository.findById(id)
            .map(contract -> contractMapper.mapToDTO(contract, new ContractDTO()))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Override
    public Long create(final ContractDTO contractDTO) {
        final Contract contract = new Contract();

        String contractHash = "0x" + Hashing.sha256()
            .hashString(contractDTO.toString(), StandardCharsets.UTF_8)
            .toString();

        try {
            ContractBlockchainDto contractBlockchainDto = new ContractBlockchainDto();
            contractBlockchainDto.setContractHash(contractHash);
            ResponseEntity<ContractBcResponseDTO> responseDTO = feignRestClientProxyJs
                .createContract(contractBlockchainDto);
            contractDTO.setTransactionId(responseDTO.getBody().getTransactionId());
            contractDTO.setBlockchainIdentifier(responseDTO.getBody().getBlockchainIdentifier());
            log.info("Contract successfully saved in blockchain");

        } catch (Exception e) {
            log.error("Saving contract in blockchain failed!!");
        }
        contractMapper.mapToEntity(contractDTO, contract);
        return contractRepository.save(contract).getId();
    }

    @Override
    public void update(final Long id, final ContractDTO contractDTO) {
        final Contract contract = contractRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        contractMapper.mapToEntity(contractDTO, contract);
        contractRepository.save(contract);
    }

    @Override
    public void delete(final Long id) {
        contractRepository.deleteById(id);
    }


}
