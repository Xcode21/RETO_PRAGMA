#!/bin/bash

# Docker build script for UserService
# Este script automatiza el proceso de construcción y despliegue

echo "=== Building UserService Docker Container ==="

# Build the Spring Boot application
echo "Building Spring Boot application..."
./gradlew clean build -x test

# Check if build was successful
if [ $? -ne 0 ]; then
    echo "Please fix the errors and try again."
    exit 1
fi

# Copy JAR to deployment directory
echo "Copying JAR file to deployment directory..."
cp applications/app-service/build/libs/*.jar deployment/

# Build Docker image
echo "Building Docker image..."
docker build -t user-service:latest deployment/

# Check if Docker build was successful
if [ $? -ne 0 ]; then
    echo "Docker build failed."
    exit 1
fi

echo "✅ Docker image built successfully: user-service:latest"

# Optional: Run tests in Docker environment
echo "Do you want to run integration tests? (y/n)"
read -r response
if [[ "$response" =~ ^([yY][eE][sS]|[yY])$ ]]; then
    echo "Running integration tests..."
    docker-compose -f docker-compose.test.yml up --abort-on-container-exit
fi

echo "✅ Build process completed successfully!"
echo "To start the services, run: docker-compose up -d"