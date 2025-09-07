package com.crudapp.todo_be.utils;

import org.springframework.data.domain.Sort;

public class CacheKeyUtil {
    public static String generateKey(int pageNumber, int pageSize, Sort.Direction sortOrder, String sortBy) {
        return pageNumber + "-" + pageSize + "-" + sortOrder + "-" + sortBy;
    }
}