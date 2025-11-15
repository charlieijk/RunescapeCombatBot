# RunescapeSimulator GitFlow

This workflow keeps the combat bot stable while letting you iterate quickly on new features, server integrations, and fixes.

## Permanent branches

- `master` – production-ready bot code. Tag each live release from this branch.
- `develop` – integration branch. All accepted features land here after review/testing.

## Supporting branches

- `feature/<short-topic>` – experiments or new capabilities (new tasks, UI hooks, different server connectors). Branch off `develop` and merge back via PR.
- `release/<version>` – stabilization work (QA, documentation, config updates). Branch off `develop`, merge into both `master` and `develop` when tagged.
- `hotfix/<ticket>` – urgent fixes for `master`. Branch off `master`, merge into both `master` and `develop`.

## Workflow checklist

1. **Create feature branch** – `git checkout develop && git checkout -b feature/<topic>`.
2. **Build & test** – run KSBot scripts locally, verify with any targeted RuneScape server configuration, and update docs if needed.
3. **Open PR into develop** – ensure reviewers understand how to configure the server/bot change.
4. **Cut releases** – when `develop` is stable, `git checkout -b release/<version> develop`, bump config numbers, add release notes, test, then merge/rebase into `master` and `develop`.
5. **Tag production** – `git tag -a vX.Y.Z master` after merging a release/hotfix.
6. **Urgent hotfix** – branch from `master`, implement fix, merge into both `master` and `develop`, then retag if necessary.

## Server-specific branches

For experiments against alternate RuneScape private servers, prefix with `server/<name>/<topic>` so that multiplayer testing and deployment remain isolated while still following the feature-branch rules.

Document major milestones (new anti-ban routines, new loot logic, new server adapters) inside pull requests to keep the combat bot’s history understandable.
