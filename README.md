# Project 2 - Point of Sale System

Team Beta

[![Open in Codespaces](https://classroom.github.com/assets/launch-codespace-f4981d0f882b2a3f0472912d15f9806d57e124e0fc890972558857b51b24a6f9.svg)](https://classroom.github.com/open-in-codespaces?assignment_repo_id=10286550)


## Configuration

This project uses Gradle to manage dependencies. To build and run, you can either use the scripts:

```bash
# if linux or mac
./gradlew build # or run, etc.
# if windows
.\gradlew.bat build # or run, etc.
```

Or you can use the VSCode extension. Open the Gradle tab, and select `app/Tasks/application/run` for running or `app/Tasks/build/build` for building.

To login to the database, the environment variables `PSQL_USER` and `PSQL_PASS` are used to determine the username and password. If these are not set, an error will be visible on the login page, and it will not be possible to log in to the system.

## Reference

- [JavaFX](https://openjfx.io/index.html)
- [PostgreSQL](https://jdbc.postgresql.org/documentation/)
