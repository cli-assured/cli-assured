# Project Standards

This rule file contains build tools, commands, and code style constraints for the project. Commands read this file to determine how to build, test, and format code.

- **Build tool:** Maven
- **Build command:** `mvn verify`
- **Test command:** `mvn verify`
- **Parallelized Maven:** no
- **Code style restrictions:**
  - Java 8 source level
  - Never use Lombok
  - Do NOT change public API signatures without justification
  - Do NOT add new dependencies without justification
  - Maintain backwards compatibility for public APIs
  - Always use immutable data structures
  - Prefer functional style of programming without side effects
  - Every public method, class and enum item must have valid and informative JavaDoc with a `@since` clause
