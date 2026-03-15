# AGENTS.md

## Lombok Usage
- Use Lombok to reduce boilerplate in Java classes.
- Spring components should use @RequiredArgsConstructor to inject dependencies

## Build
mvn clean verify

## Testing instructions
Run `mvn test -Dfmt.skip` to test

## Code Formatting
After modifying `.java` files run:
mvn com.spotify.fmt:fmt-maven-plugin:format
