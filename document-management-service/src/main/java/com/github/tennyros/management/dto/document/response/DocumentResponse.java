package com.github.tennyros.management.dto.document.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DocumentResponse {

    private Long id;
    private String title;
    private String description;
    private LocalDateTime createdAt;
}
