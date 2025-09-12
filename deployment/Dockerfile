FROM eclipse-temurin:17-jdk-alpine

# Create a non-root user for security
RUN addgroup -g 1001 -S appuser && \
    adduser -u 1001 -S appuser -G appuser

# Set working directory
WORKDIR /app

# Copy the JAR file
COPY applications/app-service/build/libs/*.jar UserService.jar

# Set proper ownership
RUN chown -R appuser:appuser /app

# Create cache directory
RUN mkdir -p /cache && chown appuser:appuser /cache

# Install curl for health checks
RUN apk add --no-cache curl

# Set up environment variables
ENV JAVA_OPTS="-Xshareclasses:name=cacheapp,cacheDir=/cache,nonfatal -XX:+UseContainerSupport -XX:MaxRAMPercentage=70 -Djava.security.egd=file:/dev/./urandom -Dspring.profiles.active=docker"
ENV TZ=America/Bogota

# Expose the port
EXPOSE 8072

# Add health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=30s --retries=3 \
  CMD curl -f http://localhost:8072/actuator/health || exit 1

# Switch to non-root user
USER appuser

# Entry point
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -jar UserService.jar" ]
