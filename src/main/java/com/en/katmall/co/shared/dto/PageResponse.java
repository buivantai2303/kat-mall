/**
 * KATMALL Application
 * tai.buivan@outlook.com Copyright (c) 2025 All rights reserved
 */
package com.en.katmall.co.shared.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Paginated response wrapper for list data.
 * 
 * @param <T> Type of items in the list
 * @author tai.buivan
 * @version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PageResponse<T> {

    /** List of items for current page */
    private List<T> items;

    /** Current page number (0-based) */
    private int page;

    /** Number of items per page */
    private int size;

    /** Total number of items */
    private long totalItems;

    /** Total number of pages */
    private int totalPages;

    /** Whether this is the first page */
    private boolean first;

    /** Whether this is the last page */
    private boolean last;

    /**
     * Creates a PageResponse from items and pagination info
     */
    public static <T> PageResponse<T> of(List<T> items, int page, int size, long totalItems) {
        int totalPages = (int) Math.ceil((double) totalItems / size);
        return PageResponse.<T>builder()
                .items(items)
                .page(page)
                .size(size)
                .totalItems(totalItems)
                .totalPages(totalPages)
                .first(page == 0)
                .last(page >= totalPages - 1)
                .build();
    }
}
