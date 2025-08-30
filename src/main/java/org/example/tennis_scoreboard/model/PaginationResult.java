package org.example.tennis_scoreboard.model;

import lombok.Getter;

import java.util.List;

@Getter
public class PaginationResult<T> {
    private final List<T> items;
    private final int currentPage;
    private final int totalPages;
    private final long totalItems;
    private final int pageSize;

    public PaginationResult(List<T> items, int currentPage, long totalItems, int pageSize) {
        this.items = items;
        this.currentPage = currentPage;
        this.totalItems = totalItems;
        this.pageSize = pageSize;
        this.totalPages = (int) Math.ceil((double) totalItems / pageSize);
    }

    public boolean hasPrevious() {
        return currentPage > 1;
    }

    public boolean hasNext() {
        return currentPage < totalPages;
    }

    public int getPreviousPage() {
        return currentPage - 1;
    }

    public int getNextPage() {
        return currentPage + 1;
    }

}
