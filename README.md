# vuln-dep-testbed

A deliberately vulnerable, minimal **Maven / Java 8** application used to test
Software Composition Analysis (SCA) and **auto-remediation** tooling.

It answers two questions a real remediation tool must get right:

1. **Can your tool find the vulnerable third-party libraries?**
2. **When it proposes a fix, is that fix _compatible_ with the app** — i.e. does
   the app still compile and pass its tests, or did the "upgrade" break it?

> ⚠️ **This project ships known-vulnerable dependencies on purpose.**
> It is for local security testing and education only. Do **not** deploy it,
> expose it to a network, or reuse its dependency versions in real software.

---

## What's vulnerable

Every dependency is referenced from real code in `src/main/java` and is
exercised by a test in `src/test/java`, so a breaking change shows up as a
compile error or a failing test.

| Dependency | Vulnerable version (default) | CVE | Nickname | Compatible fix |
|---|---|---|---|---|
| `org.apache.logging.log4j:log4j-core` | `2.14.1` | CVE-2021-44228 (+ 45046 / 45105 / 2021-44832) | **Log4Shell** | `2.17.1` |
| `org.apache.commons:commons-text` | `1.9` | CVE-2022-42889 | **Text4Shell** | `1.10.0` |
| `com.fasterxml.jackson.core:jackson-databind` | `2.9.8` | multiple deserialization CVEs | — | `2.15.4` |

Where each one is used:

- `LoggingService.java` → log4j (`${jndi:...}` message-lookup sink)
- `TemplateService.java` → commons-text (`StringSubstitutor.createInterpolator()`)
- `JsonService.java` → jackson-databind (`ObjectMapper`)

---

## How versions are controlled

All versions are Maven **properties** in `pom.xml`, so they can be overridden
per-run without editing files:

```bash
mvn test -Dlog4j.version=2.17.1
mvn test -Dcommons.text.version=1.10.0 -Djackson.version=2.15.4
```

Three profiles bundle the common scenarios:

| Command | Versions used | Expected result | Represents |
|---|---|---|---|
| `mvn test` | vulnerable (default) | **BUILD SUCCESS** | the starting point your tool scans |
| `mvn -Ppatched test` | security-fixed, compatible | **BUILD SUCCESS** | a *good* remediation |
| `mvn -Pbroken test` | commons-text `1.0` | **BUILD FAILURE** (compile error) | an *incompatible* remediation |

The `broken` profile pins `commons-text 1.0`, which predates the
`StringSubstitutor` / `createInterpolator()` API the app uses — so the code no
longer compiles. That is exactly the kind of change your tool must detect and
reject as incompatible.

---

## Suggested workflow for testing your tool

1. **Detect** — run your scanner against the default (vulnerable) tree:
   ```bash
   mvn test            # green: baseline app works
   ```
   Your tool should report Log4Shell, Text4Shell, and the jackson-databind CVEs.

2. **Remediate** — let your tool pick fix versions and write them into `pom.xml`
   (or pass them via `-D...`).

3. **Verify compatibility** — build + test with the proposed versions:
   ```bash
   mvn -Dlog4j.version=<fix> -Dcommons.text.version=<fix> -Djackson.version=<fix> test
   ```
   - Tests pass  → fix is compatible ✅
   - Compile error / test failure → fix is **incompatible**; the tool should
     back off and try another version ❌

4. **Confirm the guardrail works** — prove your tool catches a bad fix:
   ```bash
   mvn -Pbroken test   # must fail; a correct tool would never ship this
   ```

A good remediation tool ends at the `patched` profile's result: vulnerabilities
gone, `mvn test` still green.

---

## Build & run

Requires JDK 8+ and Maven 3.6+.

```bash
mvn -Ppatched package
java -cp target/classes:target/dependency/* com.example.vulnlab.App "hello"
```

(For a runnable fat jar you can add the shade plugin; it is intentionally left
out to keep the POM minimal.)

---

## Notes for maintainers of this testbed

- Keep the *functional* tests (`*Test.java`) exercising only **stable, benign**
  API so a compatible security upgrade never breaks them. They are the
  compatibility signal.
- Do **not** add tests that assert on the *vulnerable* behaviour itself — those
  would (correctly) fail once patched, muddying the compatibility check.
- Add more vulnerable dependencies the same way: reference the API from
  `src/main`, add a stable functional test, and list it in the table above.
# demosite
