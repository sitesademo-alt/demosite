package com.example.vulnlab;

import java.util.HashMap;
import java.util.Map;

/**
 * Tiny demo application that exercises every third-party dependency.
 *
 * <p>The point of touching each library from real code (and from the tests) is
 * that an <em>incompatible</em> dependency change is caught at compile time or
 * by a failing test — which is exactly what a remediation tool should verify
 * before claiming a "fix" is safe.
 */
public class App {

    public static void main(String[] args) throws Exception {
        LoggingService logging = new LoggingService();
        TemplateService templates = new TemplateService();
        JsonService json = new JsonService();

        String input = args.length > 0 ? args[0] : "hello";

        logging.handle(input);

        Map<String, String> vars = new HashMap<>();
        vars.put("name", input);
        System.out.println(templates.render("Hi ${name}", vars));

        System.out.println(json.toJson(vars));
    }
}
