package com.min.testapi;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@Service
public class TestService {

    private final ObjectMapper objectMapper;

    public TestService(final ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public Map<String, String> getBigData() {
        ClassPathResource resource = new ClassPathResource("/data/big-data.json");
        try (final InputStream inputStream = resource.getInputStream()) {
            return objectMapper.readValue(inputStream, new TypeReference<>() {});
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
