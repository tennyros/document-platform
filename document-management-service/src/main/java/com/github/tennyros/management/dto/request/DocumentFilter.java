package com.github.tennyros.management.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DocumentFilter {

    @Size(max = 100, message = "Title length must be less than or equal to 100 characters")
    private String title;


    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime createdAfter;

    @Size(max = 100, message = "Content type length must be less than or equal to 100 characters")
    private String contentType;

    @Min(value = 0, message = "Min size must be positive")
    private Long minSize;

    @Min(value = 0, message = "Max size must be positive")
    private Long maxSize;

    @Min(value = 0, message = "Version number must be positive")
    private Long versionNumber;
}
