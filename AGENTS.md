# AGENTS.md

## General Principles

- Prefer minimal, clear, and maintainable code.
- Avoid unnecessary complexity.
- Do not change behavior unless explicitly requested.
- Keep changes as small and localized as possible.

## Modifying Code

When updating existing code:

- Prefer improving existing implementations instead of introducing new abstractions.
- Avoid duplicating logic.
- Maintain consistency with surrounding code.
- Remove dead or unused code when encountered.

## Types and Static Safety

Prefer strong typing and compile-time safety.

Avoid introducing:
- `any` in TypeScript
- unchecked casts

Prefer type inference when the type is obvious.

## Dependencies

Before introducing a new dependency:

- Prefer existing libraries already used in the project.
- Avoid adding dependencies for small utilities that can be implemented simply.

## Testing

When modifying behavior:

- Update or add tests when appropriate.
- Do not remove tests unless they are clearly obsolete.

Follow the testing conventions defined in the backend or frontend agent rules.

## Scope Control

Agents should not:

- Perform large refactors unless explicitly requested.
- Reorganize project structure without instruction.
- Introduce new frameworks or architectural patterns.

Focus only on the task requested.
