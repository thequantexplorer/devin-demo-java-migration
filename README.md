# acme-legacy-billing

Monthly invoicing batch for Acme (2016). Builds invoices, computes regional tax, applies net-30 terms, and exports CSV/XML for the ERP.

Runs on **Java 8** today. The platform team wants it on **Java 17 LTS**.

```sh
mvn -q test                 # JUnit 4 suite
mvn -q package -DskipTests
java -cp target/classes:$(mvn -q dependency:build-classpath -Dmdep.outputFile=/dev/stdout) com.acme.billing.BillingRun 200000
```

Known legacy debt:
- `sun.misc.BASE64Encoder/Decoder` (removed in JDK 9+)
- `javax.xml.bind` (removed from the JDK in 11)
- `joda-time` and `java.util.Date`/`Calendar`/shared `SimpleDateFormat`
- `BigDecimal.ROUND_HALF_EVEN` constants (deprecated since 9)
- JUnit 4.12

---

This repo is a starter for a Devin demo. See [DEVIN_DEMO.md](DEVIN_DEMO.md) for setup and the prompt.
