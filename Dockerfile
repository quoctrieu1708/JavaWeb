# ==============================================================================
# Dockerfile for Spring Boot (E-COMMERCE) - Multi-stage build
# Optimized for Cloud Deployment (Render, Koyeb, Railway, Fly.io, etc.)
# ==============================================================================

# Stage 1: Build the JAR
FROM eclipse-temurin:17-jdk-alpine AS builder
WORKDIR /workspace

# Copy Maven Wrapper and POM
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN chmod +x mvnw

# Copy source code
COPY src src

# Package JAR (skipping tests for quick and reproducible deployment build)
RUN ./mvnw clean package -DskipTests

# Stage 2: Lightweight Runtime Environment
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Ensure directories for uploaded assets exist
RUN mkdir -p /app/uploads/product_img /app/uploads/category_img /app/uploads/profile_img

# Copy packaged jar from builder stage
COPY --from=builder /workspace/target/*.jar app.jar

# Expose port (Render sets $PORT dynamically)
EXPOSE 8080

# Optimized memory flags for 512MB free tier containers
ENV JAVA_OPTS="-Xms256m -Xmx384m -XX:+UseG1GC -XX:+UseStringDeduplication -Dfile.encoding=UTF-8"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
