package com.github.tennyros.management.dto.version.request;

import com.github.tennyros.management.annotation.NotEmptyFile;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DocumentVersionUploadRequest {

    @NotEmptyFile
    private MultipartFile file;
}
