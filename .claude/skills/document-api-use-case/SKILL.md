---
name: document-api-use-case
description: Document an API use case by adding content to an .adoc stub file for a given fully qualified method name.
args:
  - name: adocPath
    description: The path of the .adoc stub where the docs should be added
    required: true
  - name: fqMethodName
    description: The fully qualified name of the method that should be documented
    required: true
Usage: `/document-api-use-case <adocPath> <fqMethodName>`
---

# Document API Use Case

Generate documentation for an API method and add it to an AsciiDoc stub file.

## Parameters

- `adocPath` — the path of the .adoc stub file where the documentation should be added
- `fqMethodName` — the fully qualified name of the method that should be documented (e.g. `org.example.MyClass.myMethod`)

## Instructions

### 1. Parse Arguments

Extract the two positional arguments from `$ARGUMENTS`:
- First argument: `adocPath`
- Second argument: `fqMethodName`

If either argument is missing, ask the user to provide both.

### 2. Locate the Method

Split `fqMethodName` into `methodName` (after the last `.`) and `className` (before the last `.`).
Find the Java source file hosting the `className` under `@cli-assured/src/main/java/org/cliassured` directory.
Use grep/find to locate the source file containing the class, then read the implementation of `methodName`.
Note that the method can be in an inner class - so replacing `.` for `/` does not always give the right Java source file.

### 3. Analyze the Method

Read and understand:
- The method signature (parameters, return type)
- Its JavaDoc
- What the method does
- How it is used in existing code and tests (search for call sites in all Maven modules of the project)
- Any relevant context from surrounding class/interface

### 4. Create a testable example

* Create a new JUnit class in `@cli-assured-tests-java21/src/test/java/org/cliassured/test/j21/docs` directory.
* Look at other existing Java files in that directory for style and coding conventions,
  especially `@cli-assured-tests-java21/src/test/java/org/cliassured/test/j21/docs/HelloWorldTest.java` and
  `@cli-assured-tests-java21/src/test/java/org/cliassured/test/j21/docs/GetStdOutLinesTest.java` that were crafted manually.
* The example should focus on the currently documented feature and keep the rest minimal.
* Ideally, there should be one test class per feature.
* There may exist multiple test methods in the class, if needed, for explaining variants of the feature.
* Enclose all imports except the `org.junit.*` ones in Antora source snippet tags `// tag::imports[]` and `// end::imports[]`
* Explain the code using inline comments. The `//` comments should be training and aligned to each other
* Do not use AsciiDoc Callouts
* Make sure that the test is passing after each change in the test file - use
  `mvn -f cli-assured-tests-java21/pom.xml -Dtest=<TestClass>`
* Running the test also copies the test class to `@docs/modules/ROOT/examples/j21` where Antora can see it.

### 5. Read the .adoc stub file and locate the section where the new feature should be added

Read the existing `.adoc` stub at `adocPath` to understand:
- The current structure and formatting conventions
- Any existing content or placeholders
- The AsciiDoc style used in the project
- Find and existing section whose title matches the documented feature
- If there is no such section, create a new one. The insertion point in the file can follow the order of methods in
  `className`.

### 6. Include the code snippet from the runnable example and write a concise introductory text

* Add an AsciiDoc code snippet using the following template:

```adoc

== <sectionTitle>

[source,java]
----
include::example$j21/<TestClass>.java[tag=imports]

include::example$j21/<TestClass>.java[tag=snippet,indent=0]
----
```

The imports must be there unless there are no imports enclosed in `// tag::imports[]` and `// end::imports[]` in
the imported source file.

Add a concise explanation of the use case above the snippet:

* What it does
* Any important notes or caveats

Do not use AsciiDoc callouts. Rely only on the inline comments.
Wrap lines at 120 characters in `.adoc` files.

