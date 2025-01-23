FROM openjdk:8-jdk-alpine

ENV SBT_VERSION 1.8.2

# Install required tools and SBT
RUN apk add --no-cache bash curl openssl ca-certificates && \
    curl -L -o /tmp/sbt.zip \
      https://github.com/sbt/sbt/releases/download/v${SBT_VERSION}/sbt-${SBT_VERSION}.zip && \
    mkdir -p /opt/sbt && \
    unzip /tmp/sbt.zip -d /opt/sbt && \
    rm /tmp/sbt.zip && \
    chmod +x /opt/sbt/sbt/bin/sbt && \
    ln -s /opt/sbt/sbt/bin/sbt /usr/bin/sbt

# Copy your project
COPY . /temp/build/

# Prebuild with sbt
RUN cd /tmp/build && \
    (sbt compile || sbt compile || sbt compile) && \
    (sbt test:compile || sbt test:compile || sbt test:compile) && \
    rm -rf /tmp/build

# Default command
CMD ["sbt"]
