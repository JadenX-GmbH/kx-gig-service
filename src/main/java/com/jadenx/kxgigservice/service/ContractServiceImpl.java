package com.jadenx.kxgigservice.service;

import com.jadenx.kxgigservice.domain.Contract;
import com.jadenx.kxgigservice.domain.Offer;
import com.jadenx.kxgigservice.model.ContractDTO;
import com.jadenx.kxgigservice.repos.ContractRepository;
import com.jadenx.kxgigservice.repos.OfferRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class ContractServiceImpl implements ContractService {

    private final ContractRepository contractRepository;
    private final OfferRepository offerRepository;

    public ContractServiceImpl(final ContractRepository contractRepository,
                               final OfferRepository offerRepository) {
        this.contractRepository = contractRepository;
        this.offerRepository = offerRepository;
    }

    @Override
    public List<ContractDTO> findAll() {
        return contractRepository.findAll()
            .stream()
            .map(contract -> mapToDTO(contract, new ContractDTO()))
            .collect(Collectors.toList());
    }

    @Override
    public ContractDTO get(final Long id) {
        return contractRepository.findById(id)
            .map(contract -> mapToDTO(contract, new ContractDTO()))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Override
    public Long create(final ContractDTO contractDTO) {
        final Contract contract = new Contract();
        mapToEntity(contractDTO, contract);
        return contractRepository.save(contract).getId();
    }

    @Override
    public void update(final Long id, final ContractDTO contractDTO) {
        final Contract contract = contractRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        mapToEntity(contractDTO, contract);
        contractRepository.save(contract);
    }

    @Override
    public void delete(final Long id) {
        contractRepository.deleteById(id);
    }

    private ContractDTO mapToDTO(final Contract contract, final ContractDTO contractDTO) {
        contractDTO.setId(contract.getId());
        contractDTO.setSignatureRdo(contract.getSignatureRdo());
        contractDTO.setSignatureDs(contract.getSignatureDs());
        contractDTO.setAggregatedJson(contract.getAggregatedJson());
        contractDTO.setTransactionId(contract.getTransactionId());
        contractDTO.setBlockchainIdentifier(contract.getBlockchainIdentifier());
        contractDTO.setIsActive(contract.getIsActive());
        contractDTO.setOffer(contract.getOffer() == null ? null : contract.getOffer().getId());
        return contractDTO;
    }

    private Contract mapToEntity(final ContractDTO contractDTO, final Contract contract) {
        contract.setSignatureRdo(contractDTO.getSignatureRdo());
        contract.setSignatureDs(contractDTO.getSignatureDs());
        contract.setAggregatedJson(contractDTO.getAggregatedJson());
        contract.setTransactionId(contractDTO.getTransactionId());
        contract.setBlockchainIdentifier(contractDTO.getBlockchainIdentifier());
        contract.setIsActive(contractDTO.getIsActive());
        if (contractDTO.getOffer() != null
            && (contract.getOffer() == null
            || !contract.getOffer().getId().equals(contractDTO.getOffer()))) {
            final Offer offer = offerRepository.findById(contractDTO.getOffer())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "offer not found"));
            contract.setOffer(offer);
        }
        return contract;
    }

}
