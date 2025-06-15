package com.github.tennyros.management.util;

import lombok.experimental.UtilityClass;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@UtilityClass
public class ContentDispositionUtil {

    /**
     * Creates a safe Content-Disposition for file download.
     * Supports UTF-8 filenames for modern browsers (RFC 5987).
     *
     * @param filename the name of the file
     * @return Content-Disposition string
     */
    public static String buildContentDisposition(String filename) {
        String encodedFilename = encodeRFC5987(filename);
        return String.format("attachment; filename*=UTF-8''%s", encodedFilename);
    }

    /**
     * Safely encodes filename according to RFC 5987.
     *
     * @param filename the name of the file
     * @return encoded string
     */
    private static String encodeRFC5987(String filename) {
        String encoded;
        encoded = URLEncoder.encode(filename, StandardCharsets.UTF_8)
                             .replace("+", "%20")
                             .replace("%28", "(")
                             .replace("%29", ")")
                             .replace("%27", "'")
                             .replace("%21", "!")
                             .replace("%7E", "~");
        return encoded;
    }
}