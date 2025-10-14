@echo off
REM Script to run the batch-low-platform application
REM Usage: run.bat [options]

REM Build the application if the JAR doesn't exist
if not exist target\batch-low-platform-0.0.1-SNAPSHOT.jar (
    echo Building application...
    call mvnw.cmd clean package -DskipTests
)

REM Run the application with the provided arguments
java -jar target\batch-low-platform-0.0.1-SNAPSHOT.jar %*

REM Exit with the same code as the Java application
exit /b %ERRORLEVEL%