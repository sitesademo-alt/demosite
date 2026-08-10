package com.example.vulnlab;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.Test;

/**
 * Functional test. Stable across jackson-databind 2.x, so a compatible upgrade
 * keeps it GREEN.
 */
public class JsonServiceTest {

    @Test
    public void roundTrip() throws Exception {
        JsonService svc = new JsonService();
        Map<String, Object> parsed = svc.parse("{\"a\":1}");
        assertEquals(1, parsed.get("a"));
        assertEquals("{\"a\":1}", svc.toJson(parsed));
    }
}
