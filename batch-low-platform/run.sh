#!/bin/bash

# Script to run the batch-low-platform application
# Usage: ./run.sh [options]

# Build the application if the JAR doesn't exist
if [ ! -f target/batch-low-platform-0.0.1-SNAPSHOT.jar ]; then
    echo "Building application..."
    ./mvnw clean package -DskipTests
fi

# Run the application with the provided arguments
java -jar target/batch-low-platform-0.0.1-SNAPSHOT.jar "$@"

# Exit with the same code as the Java application
exit $?