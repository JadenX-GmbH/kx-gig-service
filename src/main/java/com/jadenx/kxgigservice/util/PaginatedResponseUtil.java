package com.jadenx.kxgigservice.util;

import com.jadenx.kxgigservice.model.PaginatedResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public class PaginatedResponseUtil {
    public static PaginatedResponse<?> paginatedResponse(final List<?> dtoList, final Page<?> page) {
        return  PaginatedResponse.builder()
            .data(dtoList)
            .totalPages(page.getTotalPages())
            .totalElements(page.getTotalElements())
            .page(page.getNumber())
            .size(page.getSize())
            .build();
    }
}
