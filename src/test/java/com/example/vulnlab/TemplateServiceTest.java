package com.example.vulnlab;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

/**
 * Functional tests for the template service. These exercise only benign,
 * stable behaviour, so they pass on commons-text 1.9 (vulnerable) and on
 * 1.10.0 (fixed) — proving the security upgrade is compatible.
 */
public class TemplateServiceTest {

    @Test
    public void substitutesVariables() {
        TemplateService svc = new TemplateService();
        Map<String, String> vars = new HashMap<>();
        vars.put("name", "world");
        assertEquals("Hi world", svc.render("Hi ${name}", vars));
    }

    @Test
    public void interpolatorHandlesPlainText() {
        // No dangerous lookup used: identical result on 1.9 and 1.10.
        TemplateService svc = new TemplateService();
        assertEquals("plain", svc.interpolate("plain"));
    }
}
