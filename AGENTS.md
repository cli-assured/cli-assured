# CLI Assured `AGENTS.md`

CLI Assured is a Java domain specific language (DSL) for testing or executing command line applications.

## Build the project

Fallback to `mvn` or `./mvnw` if `mvnd` is not installed.

Build without tests

```bash
mvnd clean install -DskipTests -Dquarkus.build.skip
```

Build with all tests

```bash
mvnd clean install
```

Run all test methods in a specific test class

```bash
mvnd -f path/to/pom.xml clean test -Dtest=MyTest
```

Run specific test(s) in a specific test class(es)

```bash
mvnd -f path/to/pom.xml clean test -Dtest=MyTest[#myTestMethod][,MyOtherTest[#myOtherTestMethod],...]
```

## Documentation sources

The sources of Antora documentation site of CLI Assured is available in `docs/modules/ROOT` directory.

## Source code of Maven dependencies

1. Check whether `-sources.jar` for the given Maven dependency identified by `groupId`, `artifactId` and `version` was downloaded already:

```bash
grouIdPath="${<groupId>//./\/}"
ls -la ~/.m2/repository/$grouIdPath/<artifactId>/<version>/<artifactId>-<version>-sources.jar
```

2. If the `-sources.jar` file does not exist, download it using

```bash
mvn dependency:get -Dartifact=<groupId>:<artifactId>:<version>:jar:sources
```

2. Print a single source file directly without extracting it:

```bash
unzip -p ~/.m2/repository/$grouIdPath/<artifactId>/<version>/<artifactId>-<version>-sources.jar "com/foo/bar/ClassName.java"
```

3. To list source files in a `-sources.jar` use:

```bash
unzip -l ~/.m2/repository/$grouIdPath/<artifactId>/<version>/<artifactId>-<version>-sources.jar
```

4. If sources for the given Maven artifact are not available, use this public-API view without sources:

```bash
javap -public -classpath <jar> com.foo.bar.ClassName
```

