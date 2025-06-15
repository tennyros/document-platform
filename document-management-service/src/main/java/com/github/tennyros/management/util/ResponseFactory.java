package com.github.tennyros.management.util;

import lombok.experimental.UtilityClass;
import org.springframework.http.ResponseEntity;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Collection;

@UtilityClass
public final class ResponseFactory {

    public static <ID, T> ResponseEntity<T> created(ID id, T body) {
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();

        return ResponseEntity.created(location).body(body);
    }

    public static <T> ResponseEntity<T> created(String locationPath, T body) {
        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path(locationPath)
                .build()
                .toUri();

        return ResponseEntity.created(location).body(body);
    }

    public static <T> ResponseEntity<T> ok(T body) {
        return ResponseEntity.ok(body);
    }

    public static <T> ResponseEntity<Collection<T>> createdBatch(Collection<T> body) {
        return ResponseEntity.ok(body);
    }

    public static ResponseEntity<Void> noContent() {
        return ResponseEntity.noContent().build();
    }
}
