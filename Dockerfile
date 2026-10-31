# ==============================================================================
# Dockerfile for Spring Boot (E-COMMERCE) - Multi-stage build
# Optimized for Cloud Deployment (Render, Koyeb, Railway, Fly.io, etc.)
# ==============================================================================

# Stage 1: Build the JAR with pre-installed Maven (reliable, fast, no wrapper/CRLF issues)
FROM maven:3.9.6-eclipse-temurin-17-alpine AS builder
WORKDIR /workspace

# Copy POM first to leverage Docker layer caching for dependencies
COPY pom.xml ./
RUN mvn dependency:go-offline -B || true

# Copy source code
COPY src src

# Package JAR (skipping tests for cloud deployment build)
RUN mvn clean package -DskipTests -B

# Stage 2: Lightweight Runtime Environment
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Ensure directories for uploaded assets exist
RUN mkdir -p /app/uploads/product_img /app/uploads/category_img /app/uploads/profile_img

# Copy packaged jar from builder stage
COPY --from=builder /workspace/target/*.jar app.jar

# Expose default port (Render will override via $PORT)
EXPOSE 8080

# Optimized memory flags for Render 512MB free tier containers
ENV JAVA_OPTS="-XX:MaxRAMPercentage=75.0 -XX:+UseG1GC -XX:+ExitOnOutOfMemoryError -Dfile.encoding=UTF-8"

# Dynamic port binding: Render injects $PORT (e.g. 10000), default fallback to 8080
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Dserver.port=${PORT:-8080} -jar app.jar"]

