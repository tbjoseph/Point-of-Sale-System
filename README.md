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
