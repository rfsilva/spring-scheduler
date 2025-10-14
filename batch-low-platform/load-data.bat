@echo off
REM Script to load test data into the batch-low-platform database
REM Usage: load-data.bat [options]

REM Build the application if the JAR doesn't exist
if not exist target\batch-low-platform-0.0.1-SNAPSHOT.jar (
    echo Building application...
    call mvnw.cmd clean package -DskipTests
)

REM Run the application with the dataloader profile
java -jar target\batch-low-platform-0.0.1-SNAPSHOT.jar --spring.profiles.active=dataloader %*

REM Exit with the same code as the Java application
exit /b %ERRORLEVEL%