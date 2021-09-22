package com.jadenx.kxgigservice.mapper;

import com.jadenx.kxgigservice.domain.Contract;
import com.jadenx.kxgigservice.domain.Offer;
import com.jadenx.kxgigservice.model.ContractDTO;
import com.jadenx.kxgigservice.repos.OfferRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class ContractMapper {

    private final OfferRepository offerRepository;

    public ContractMapper(final  OfferRepository offerRepository) {
        this.offerRepository = offerRepository;
    }

    public ContractDTO mapToDTO(final Contract contract, final ContractDTO contractDTO) {
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

    public Contract mapToEntity(final ContractDTO contractDTO, final Contract contract) {
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
