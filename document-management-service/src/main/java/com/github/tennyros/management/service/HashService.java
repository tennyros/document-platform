package com.github.tennyros.management.service;

import java.io.InputStream;

public interface HashService {

    String calculateSHA256(InputStream inputStream);
}
