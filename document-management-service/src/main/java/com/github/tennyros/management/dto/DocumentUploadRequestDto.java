package com.github.tennyros.management.dto;

import com.github.tennyros.management.annotation.NotEmptyFile;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class DocumentUploadRequestDto {

    @NotBlank(message = "File title must be defined")
    @Size(max = 100, message = "File title is too long")
    private String title;

    @NotBlank(message = "Description must be defined")
    @Size(max = 500, message = "Description is too long")
    private String description;

    @NotEmptyFile
    private MultipartFile file;

}
