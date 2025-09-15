FROM openjdk:17

WORKDIR /app

COPY build/libs/estoque-bebidas-app.jar app.jar

# Fallback: se não vier SPRING_PROFILES_ACTIVE, usa "dev"
ENTRYPOINT ["sh","-c","java -Dspring.profiles.active=${SPRING_PROFILES_ACTIVE:-dev} -jar app.jar"]