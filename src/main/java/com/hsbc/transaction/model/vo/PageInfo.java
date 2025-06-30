package com.hsbc.transaction.model.vo;

import java.util.List;

public class PageInfo {
    private final List<BondTransactionVO> data;
    private final int pageNum;
    private final int pageSize;
    private final int totalPages;
    private final long totalItems;

    public PageInfo(List<BondTransactionVO> data, int pageNum, int pageSize, int totalPages, long totalItems) {
        this.data = data;
        this.pageNum = pageNum;
        this.pageSize = pageSize;
        this.totalPages = totalPages;
        this.totalItems = totalItems;
    }

    public List<BondTransactionVO> getData() {
        return data;
    }

    public int getPageNum() {
        return pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public long getTotalItems() {
        return totalItems;
    }

}
