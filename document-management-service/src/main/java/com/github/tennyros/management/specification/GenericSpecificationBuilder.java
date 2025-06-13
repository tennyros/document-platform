package com.github.tennyros.management.specification;

import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public abstract class GenericSpecificationBuilder<T> {

    protected final List<Specification<T>> specifications = new ArrayList<>();

    public Specification<T> build() {
        if (specifications.isEmpty()) {
            return (root, query, cb) -> cb.conjunction();
        }
        Specification<T> result = specifications.get(0);
        for (int i = 1; i < specifications.size(); i++) {
            result = result.and(specifications.get(i));
        }
        return result;
    }

    protected void add(Specification<T> spec) {
        if (spec != null) {
            specifications.add(spec);
        }
    }
}