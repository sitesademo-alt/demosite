package com.example.vulnlab;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Functional test. Must stay GREEN on the vulnerable version AND on any
 * COMPATIBLE security fix. If a proposed log4j upgrade breaks this, the upgrade
 * is not compatible with the app.
 */
public class LoggingServiceTest {

    @Test
    public void logsAndReturnsValue() {
        LoggingService svc = new LoggingService();
        assertEquals("logged:hello", svc.handle("hello"));
    }
}
