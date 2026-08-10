package com.example.vulnlab;

import java.util.Map;

import org.apache.commons.text.StringSubstitutor;

/**
 * Renders simple {@code ${var}} templates using Apache Commons Text.
 *
 * <p><b>Vulnerability:</b> with {@code commons-text 1.9},
 * {@link StringSubstitutor#createInterpolator()} enables interpolation lookups
 * such as {@code ${script:...}}, {@code ${dns:...}} and {@code ${url:...}}
 * (CVE-2022-42889, "Text4Shell"). Passing attacker-controlled text to
 * {@link #interpolate(String)} can lead to code execution / SSRF.
 *
 * <p>Both methods keep working on the fixed {@code commons-text 1.10.0}
 * (the dangerous lookups are simply disabled by default), so upgrading is a
 * compatible remediation. Downgrading below 1.3, however, removes
 * {@code StringSubstitutor} entirely — see the {@code broken} Maven profile.
 */
public class TemplateService {

    /** Benign, explicit-variable substitution — safe on every version. */
    public String render(String template, Map<String, String> vars) {
        return new StringSubstitutor(vars).replace(template);
    }

    /** Uses the interpolator (the API involved in Text4Shell). */
    public String interpolate(String template) {
        return StringSubstitutor.createInterpolator().replace(template);
    }
}
