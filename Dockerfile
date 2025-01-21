FROM openjdk:17-jdk-slim

# Установка необходимых инструментов
RUN apt-get update && apt-get install -y curl gnupg && rm -rf /var/lib/apt/lists/*

# Устанавливаем sbt через официальное хранилище
RUN echo "deb https://repo.scala-sbt.org/scalasbt/debian all main" > /etc/apt/sources.list.d/sbt.list && \
    echo "deb https://repo.scala-sbt.org/scalasbt/debian /" >> /etc/apt/sources.list.d/sbt.list && \
    curl -sL "https://keyserver.ubuntu.com/pks/lookup?op=get&search=0x99E82A75642AC823" | apt-key add - && \
    apt-get update && apt-get install -y sbt && rm -rf /var/lib/apt/lists/*

# Устанавливаем рабочую директорию
WORKDIR /app

COPY .sbtopts /root/.sbt/.sbtopts

# Копируем код приложения
COPY . .

# Компилируем приложение
RUN sbt compile

# Открываем порт приложения
EXPOSE 9000

# Запускаем приложение
CMD ["sh", "-c", "sbt run -Dhttp.port=${PORT} -Dhttp.address=0.0.0.0"]
