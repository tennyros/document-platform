package com.github.tennyros.management.dto.version;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DocumentVersionInfo {

    private Long id;
    private String storageKey;
    private Long versionNumber;
}
