FROM openjdk:8-jdk-alpine

ENV SBT_VERSION 1.8.2

# Install tools and SBT
RUN apk add --no-cache bash curl openssl ca-certificates && \
    curl -L -o /tmp/sbt.zip \
      https://github.com/sbt/sbt/releases/download/v${SBT_VERSION}/sbt-${SBT_VERSION}.zip && \
    mkdir -p /opt/sbt && \
    unzip /tmp/sbt.zip -d /opt/sbt && \
    rm /tmp/sbt.zip && \
    chmod +x /opt/sbt/sbt/bin/sbt && \
    ln -s /opt/sbt/sbt/bin/sbt /usr/bin/sbt

# Copy your project into the container
COPY . /tmp/build/

# Pre-build with sbt
RUN cd /tmp/build && \
    sbt compile

CMD ["sbt"]

