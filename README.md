# RunescapeCombatBot

This project showcases a modular combat bot for RuneScape private servers. It includes a Gradle-based build, documentation, and a stub of the KS Bot API so you can compile/run the script locally without the proprietary client.

## Requirements

- Java 21 (Temurin/OpenJDK). Example for macOS/Homebrew:
  ```bash
  brew install openjdk@21
  export JAVA_HOME=$(/usr/libexec/java_home -v 21)
  export PATH="/opt/homebrew/opt/openjdk@21/bin:$PATH"
  ```
- Gradle wrapper is already included, so no global Gradle install is needed.

## Build & Test

```bash
./gradlew build
```

This compiles the sources in `src/main/java` and runs tests (add them under `src/test/java`). Artifacts land in `build/libs/`.

## Run the Demo Bot

A lightweight runner (`CombatBotRunner`) exercises the task system using the stubbed API:

```bash
./gradlew runCombatBot
```

The runner seeds the context with sample NPCs, inventory, and ground items, then executes the configured tasks (eating, attacking, looting, special attacks, etc.). Use it as a reference for wiring the bot into your own environment.

## Development Workflow

- **Branches:** GitFlow is in effect (`main` for releases, `develop` for integration). Branch off `develop` for features/fixes and raise PRs back into it. Merge `develop` into `main` when you cut a release.
- **CI:** `.github/workflows/gradle.yml` runs on pushes/PRs targeting `main` and `develop`. It builds with Java 21 and also publishes dependency graphs for Dependabot alerts.
- **Dependency Graph:** GitHub’s Dependency Graph is enabled; failing submissions mean the feature might be disabled on your fork.

## IDE Support

Open the repository in IntelliJ IDEA, Eclipse, or VS Code and import it as a Gradle project to get code completion and task runners automatically.

## License

RunescapeCombatBot is distributed under the [MIT License](LICENSE). See the license file for details before redistributing or embedding the code.
