FROM openjdk:11-jdk-slim

ENV SBT_VERSION 1.8.2

# Install tools and SBT
RUN apt-get update && apt-get install -y --no-install-recommends \
    bash curl unzip ca-certificates && \
    curl -L -o /tmp/sbt.zip \
      https://github.com/sbt/sbt/releases/download/v${SBT_VERSION}/sbt-${SBT_VERSION}.zip && \
    mkdir -p /opt/sbt && \
    unzip /tmp/sbt.zip -d /opt/sbt && \
    rm /tmp/sbt.zip && \
    chmod +x /opt/sbt/sbt/bin/sbt && \
    ln -s /opt/sbt/sbt/bin/sbt /usr/bin/sbt && \
    apt-get clean && rm -rf /var/lib/apt/lists/*

# Copy your project into the container
COPY . /tmp

# Pre-build with sbt
RUN cd /tmp && \
    sbt compile

CMD ["sbt", "-Dsbt.rootdir=true"]
