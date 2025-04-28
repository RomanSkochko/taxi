package com.romanskochko.taxi.core.model.dto;

import org.springframework.data.domain.Page;

import java.util.List;

public record PageResponse<T>(
        List<T> content,
        int page,
        int size,
        int totalPages,
        long totalElements,
        boolean last,
        boolean first,
        boolean hasNext,
        boolean hasPrevious) {

    public static <T> PageResponse<T> fromPage(Page<T> page) {
        return new PageResponse<>(page.getContent(),
                                  page.getNumber(),
                                  page.getSize(),
                                  page.getTotalPages(),
                                  page.getTotalElements(),
                                  page.isLast(),
                                  page.isFirst(),
                                  page.hasNext(),
                                  page.hasPrevious());
    }
}
