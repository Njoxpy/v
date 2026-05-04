# Errors & Solutions

## 2026-05-04 — `./mvnw spring-boot:run` compilation failure

### Error

```
[ERROR] /src/test/java/com/learning/learning/HomeController.java:[4,36] package org.springframework.web.bind does not exist
[ERROR] /src/test/java/com/learning/learning/HomeController.java:[9,6] cannot find symbol
  symbol:   class RequestMapping
  location: class com.learning.learning.HomeController
```

### Cause

1. `HomeController.java` was placed under `src/test/java/...` instead of `src/main/java/...`. Test sources don't have access to `spring-web` at runtime/compile of the app, and controllers belong in main sources anyway.
2. The import statement was malformed:
   ```java
   import org.springframework.web.bind.annotation;
   ```
   This imports a package, not a class. `@RequestMapping` was therefore unresolved.

### Solution

1. Move the file:
   ```bash
   mv src/test/java/com/learning/learning/HomeController.java \
      src/main/java/com/learning/learning/HomeController.java
   ```
2. Fix the import to reference the class:
   ```java
   import org.springframework.web.bind.annotation.RequestMapping;
   ```
3. Re-run:
   ```bash
   ./mvnw spring-boot:run
   ```

---

## 2026-05-04 — `package org.springframework.web.bind.annotation does not exist`

### Error

```
[ERROR] /src/main/java/com/learning/learning/HomeController.java:[4,47] package org.springframework.web.bind.annotation does not exist
[ERROR] /src/main/java/com/learning/learning/HomeController.java:[9,6] cannot find symbol
  symbol:   class RequestMapping
```

### Cause

`pom.xml` declared only `spring-boot-starter`, which does not pull in Spring MVC / `spring-web`. The `@RequestMapping` annotation lives in `spring-web`, so the import could not be resolved.

### Solution

Replace `spring-boot-starter` with `spring-boot-starter-web` in `pom.xml`:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

This brings in Spring MVC plus an embedded Tomcat so the controller can actually serve HTTP. Re-run `./mvnw spring-boot:run`.

---

## 2026-05-04 — `Web server failed to start. Port 8080 was already in use.`

### Error

```
***************************
APPLICATION FAILED TO START
***************************

Description:

Web server failed to start. Port 8080 was already in use.
```

### Cause

Docker (a running container/daemon mapping) was bound to port 8080. Resolved by disabling Docker.

### Solution

Find the offending process:

```bash
lsof -i :8080
# or
ss -ltnp | grep :8080
```

Kill it:

```bash
kill $(lsof -t -i:8080)
```

Or run the app on a different port:

```bash
./mvnw spring-boot:run -Dspring-boot.run.arguments=--server.port=8081
```

A more permanent option is to set `server.port=8081` in `src/main/resources/application.properties`.
