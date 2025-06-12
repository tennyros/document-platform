package com.github.tennyros.management.util;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.experimental.UtilityClass;

import java.util.Objects;

@UtilityClass
public class DotenvLoader {

    public static void load() {
        Dotenv dotenv = Dotenv.load();

        // POSTGRE
        System.setProperty("DB_USERNAME", Objects.requireNonNull(dotenv.get("DB_USERNAME")));
        System.setProperty("DB_PASSWORD", Objects.requireNonNull(dotenv.get("DB_PASSWORD")));
        System.setProperty("DB_BASE", Objects.requireNonNull(dotenv.get("DB_BASE")));
        System.setProperty("DB_NAME", Objects.requireNonNull(dotenv.get("DB_NAME")));
        System.setProperty("DB_HOST", Objects.requireNonNull(dotenv.get("DB_HOST")));
        System.setProperty("DB_PORT", Objects.requireNonNull(dotenv.get("DB_PORT")));

        // MONGO
        System.setProperty("MONGO_HOST", Objects.requireNonNull(dotenv.get("MONGO_HOST")));
        System.setProperty("MONGO_PORT", Objects.requireNonNull(dotenv.get("MONGO_PORT")));
        System.setProperty("MONGO_DB", Objects.requireNonNull(dotenv.get("MONGO_DB")));

        // MINIO
        System.setProperty("MINIO_URL", Objects.requireNonNull(dotenv.get("MINIO_URL")));
        System.setProperty("MINIO_ACCESS_KEY", Objects.requireNonNull(dotenv.get("MINIO_ACCESS_KEY")));
        System.setProperty("MINIO_SECRET_KEY", Objects.requireNonNull(dotenv.get("MINIO_SECRET_KEY")));
        System.setProperty("MINIO_BUCKET", Objects.requireNonNull(dotenv.get("MINIO_BUCKET")));
    }
}
