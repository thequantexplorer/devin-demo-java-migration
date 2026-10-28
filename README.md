# acme-legacy-billing

Monthly invoicing batch for Acme (2016). Builds invoices, computes regional tax, applies net-30 terms, and exports CSV/XML for the ERP.

Requires **Java 17 LTS** (migrated from Java 8).

```sh
mvn -q test                 # JUnit 5 suite
mvn -q package -DskipTests
java -cp target/classes:$(mvn -q dependency:build-classpath -Dmdep.outputFile=/dev/stdout) com.acme.billing.BillingRun 200000
```

Remaining legacy debt:
- Public model APIs (`Invoice`, `DueDateCalculator`, `AgingReport`) still take/return `java.util.Date`; internals use `java.time`.

---

This repo is a starter for a Devin demo. See [DEVIN_DEMO.md](DEVIN_DEMO.md) for setup and the prompt.
