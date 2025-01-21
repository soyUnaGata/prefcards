# Use a lightweight JDK base image
FROM openjdk:17-jdk-slim

# Установка необходимых инструментов
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем код приложения
COPY . .

# Устанавливаем sbt
RUN curl -L -o sbt.deb https://github.com/sbt/sbt/releases/download/v1.8.3/sbt-1.8.3.deb && \
    dpkg -i sbt.deb && \
    sbt compile

# Открываем порт приложения
EXPOSE 9000

# Запускаем приложение
CMD ["sbt", "run", "-Dhttp.port=$PORT"]

