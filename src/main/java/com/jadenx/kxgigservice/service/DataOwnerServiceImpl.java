package com.jadenx.kxgigservice.service;

import com.jadenx.kxgigservice.domain.DataOwner;
import com.jadenx.kxgigservice.model.DataOwnerDTO;
import com.jadenx.kxgigservice.repos.DataOwnerRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class DataOwnerServiceImpl implements DataOwnerService {

    private final DataOwnerRepository dataOwnerRepository;

    public DataOwnerServiceImpl(final DataOwnerRepository dataOwnerRepository) {
        this.dataOwnerRepository = dataOwnerRepository;
    }

    @Override
    public List<DataOwnerDTO> findAll() {
        return dataOwnerRepository.findAll()
            .stream()
            .map(dataOwner -> mapToDTO(dataOwner, new DataOwnerDTO()))
            .collect(Collectors.toList());
    }

    @Override
    public DataOwnerDTO get(final Long id) {
        return dataOwnerRepository.findById(id)
            .map(dataOwner -> mapToDTO(dataOwner, new DataOwnerDTO()))
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @Override
    public Long create(final DataOwnerDTO dataOwnerDTO) {
        final DataOwner dataOwner = new DataOwner();
        mapToEntity(dataOwnerDTO, dataOwner);
        return dataOwnerRepository.save(dataOwner).getId();
    }

    @Override
    public void update(final Long id, final DataOwnerDTO dataOwnerDTO) {
        final DataOwner dataOwner = dataOwnerRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        mapToEntity(dataOwnerDTO, dataOwner);
        dataOwnerRepository.save(dataOwner);
    }

    @Override
    public void delete(final Long id) {
        dataOwnerRepository.deleteById(id);
    }

    private DataOwnerDTO mapToDTO(final DataOwner dataOwner, final DataOwnerDTO dataOwnerDTO) {
        dataOwnerDTO.setId(dataOwner.getId());
        dataOwnerDTO.setUid(dataOwner.getUid());
        return dataOwnerDTO;
    }

    private DataOwner mapToEntity(final DataOwnerDTO dataOwnerDTO, final DataOwner dataOwner) {
        dataOwner.setUid(dataOwnerDTO.getUid());
        return dataOwner;
    }

}
