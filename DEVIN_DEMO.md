# Devin demo: Java 8 to Java 17 migration with proof

**What it shows:** Devin records tests and a benchmark on Java 8, replaces APIs removed from the JDK, gets the suite green on Java 17, and puts before/after numbers in the PR.

**Needs:** GitHub only (Devin installs JDK 8 and 17 itself)

More demos: [devin-advanced-demos](https://github.com/thequantexplorer/devin-advanced-demos)

## Set it up

1. Click **Use this template → Create a new repository**, choose your GitHub account or org as the owner, and keep the name `devin-demo-java-migration`. The links and prompt below assume that name. Use the template rather than a fork: pull requests on a fork go to the original repo by default.
2. In Devin, open [**Settings → Connections → GitHub**](https://app.devin.ai/settings/connections/github) and make sure Devin can access the new repository.

## Prompt

Start a new Devin session and paste this. Replace `<you>` with your GitHub user or org.

```text
Migrate https://github.com/<you>/devin-demo-java-migration from Java 8 to Java 17.

Before touching anything, capture a baseline on Java 8: full `mvn test` results and the invoice benchmark (see README). Then plan the migration, replace removed/internal APIs (sun.misc.BASE64*, javax.xml.bind, Joda-Time where sensible), bump JUnit and the compiler target, and get the suite green on Java 17.

Deliver a PR with a before/after report in the description: test counts per JDK, benchmark elapsed_ms per JDK (same input size), every API replaced and why, and any behavioral risks. If Maven Central rate-limits you, use the GCS mirror https://maven-central.storage-download.googleapis.com/maven2/.
```
