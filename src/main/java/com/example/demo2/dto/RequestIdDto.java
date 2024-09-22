package com.example.demo2.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
// @Builder => 빌더가 생성하는 매개변수 생성자는 private으로 설정돼서 외부 패키지에서 접근 X
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestIdDto {
    private String name;
    private String isbn;
}
