package com.example.demo2.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder

public class RequestIsbnDto {
    private Long id;
    private String name;
}
