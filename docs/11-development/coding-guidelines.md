# Coding Guidelines

## General

- Java 17 language level (`--release 17`).
- No comments added unless requested/necessary; code should be self-explanatory and follow existing patterns.
- Keep layering clean: view → controller → service → (model, repository). No upward imports.
- No secrets in source. No unused code. Narrowly scoped `Util`.

## Style

- Follow surrounding conventions; standard Java naming (camelCase fields, PascalCase types, UPPER_SNAKE constants).
- Prefer interfaces for service/repository contracts where they aid polymorphism (Phase 02 decision).

## Changes

- A later phase must not be implemented early.
- Never change architecture silently — document in `memory.md`.