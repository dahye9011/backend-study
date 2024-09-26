package com.example.demo2.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
//@Builder
public class RequestIsbnDto {
    private Long id;
    private String name;
    private String isbn;

    @Builder
    public RequestIsbnDto(Long id, String name, String isbn) {
        this.id = id;
        this.name = name;
        this.isbn = isbn;
    }
}
