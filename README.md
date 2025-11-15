# RunescapeCombatBot

This repository contains an example combat bot for RuneScape private servers as well as supporting documentation.

## Building with Gradle

1. Ensure a JDK 17+ is installed (`java -version`).
2. Use the supplied Gradle wrapper to compile the project:

```bash
./gradlew build
```

This command compiles all Java sources under `src/main/java` and runs unit tests (if you add any under `src/test/java`). The resulting artifact can be found in `build/libs/`.

## Optional IDE Import

Most modern IDEs detect the Gradle build automatically. Open the project root in IntelliJ IDEA, Eclipse, or VS Code and allow it to import the Gradle model to get code-completion and task runners.
