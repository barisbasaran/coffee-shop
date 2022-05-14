package io.baris.coffeeshop.system;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Utilities for Coffee Shop application
 */
public class SystemUtils {

    private static ObjectMapper objectMapper;

    static {
        SystemUtils.objectMapper = new ObjectMapper();
        configureObjectMapper(objectMapper);
    }

    public static void configureObjectMapper(final ObjectMapper objectMapper) {
        objectMapper.configure(FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.registerModule(new JavaTimeModule());
    }

    public static String toJsonString(final Object obj) {
        return escapeException(() ->
            objectMapper.writeValueAsString(obj)
        );
    }

    public static String readFileToString(final String path) {
        return escapeException(() ->
            isClasspathResource(path) ?
                readClasspathFileToString(extractPath(path)) :
                Files.readString(Paths.get(path))
        );
    }

    public static String resourceFilePath(final String path) {
        return escapeException(() -> {
            if (isClasspathResource(path)) {
                return new File(
                    getContextClassLoader().getResource(extractPath(path)).toURI()
                ).getAbsolutePath();
            } else {
                return new File(path).getAbsolutePath();
            }
        });
    }

    private static String extractPath(String path) {
        return path.substring(10);
    }

    private static boolean isClasspathResource(String path) {
        return path.startsWith("classpath:");
    }

    public static String readClasspathFileToString(String path) {
        return escapeException(() -> {
            try (var inputStream = getContextClassLoader().getResourceAsStream(path)) {
                return new String(inputStream.readAllBytes(), UTF_8);
            }
        });
    }

    private static ClassLoader getContextClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    public interface ThrowingSupplier<T> {
        T get() throws Exception;
    }

    public static <T> T escapeException(ThrowingSupplier<T> supplier) {
        try {
            return supplier.get();
        } catch (Exception e) {
            if (e instanceof RuntimeException) {
                throw (RuntimeException) e;
            }
            throw new RuntimeException(e);
        }
    }
}
