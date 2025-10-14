#!/bin/bash

# Script to load test data into the batch-low-platform database
# Usage: ./load-data.sh [options]

# Build the application if the JAR doesn't exist
if [ ! -f target/batch-low-platform-0.0.1-SNAPSHOT.jar ]; then
    echo "Building application..."
    ./mvnw clean package -DskipTests
fi

# Run the application with the dataloader profile
java -jar target/batch-low-platform-0.0.1-SNAPSHOT.jar --spring.profiles.active=dataloader "$@"

# Exit with the same code as the Java application
exit $?