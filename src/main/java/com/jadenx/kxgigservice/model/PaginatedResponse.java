package com.jadenx.kxgigservice.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class PaginatedResponse<T> {

    private List<?> data;

    private int page;

    private int size;

    private int totalPages;

    private long totalElements;
}
