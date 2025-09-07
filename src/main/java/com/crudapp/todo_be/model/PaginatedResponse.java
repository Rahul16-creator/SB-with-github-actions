package com.crudapp.todo_be.model;

import java.io.Serializable;
import java.util.List;


public record PaginatedResponse<T>(List<T> content,
                                   long totalElements,
                                   int pageNumber,
                                   int totalPages,
                                   int size) implements Serializable {
}
