package com.example.vulnlab;

import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Minimal JSON parse/serialize wrapper around Jackson Databind.
 *
 * <p><b>Vulnerability:</b> {@code jackson-databind 2.9.8} is affected by a long
 * list of deserialization CVEs. The safe, API-compatible remediation is to move
 * to a maintained 2.x release (e.g. 2.15.4).
 *
 * <p>{@code ObjectMapper#readValue} and {@code writeValueAsString} are stable
 * across the 2.x line, so the upgrade does not change this code.
 */
public class JsonService {

    private final ObjectMapper mapper = new ObjectMapper();

    @SuppressWarnings("unchecked")
    public Map<String, Object> parse(String json) throws Exception {
        return mapper.readValue(json, Map.class);
    }

    public String toJson(Object value) throws Exception {
        return mapper.writeValueAsString(value);
    }
}
