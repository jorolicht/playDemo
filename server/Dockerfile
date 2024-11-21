# Start with a base image for Java
FROM eclipse-temurin:21-jdk AS mainstage

ENV APP_VERSION="1.0.0"
ENV DEMON_USER="playd"
ENV DEMON_USERID="1111"

USER root

# Set /opt/docker as the working directory
WORKDIR /opt/docker


# Copy the distribution zip file to the container
# Assuming that the dist .zip file is named like server-1.0.0.zip
COPY ./target/universal/server-$APP_VERSION.zip .

# Unzip the distribution and remove the zip file afterward
RUN apt-get update && apt-get install -y unzip && \
    unzip server-${APP_VERSION}.zip && \
    mv server-${APP_VERSION}/* . && \
    rm server-${APP_VERSION}.zip && \
    apt-get remove -y unzip && apt-get clean && rm -rf /var/lib/apt/lists/*


# create daemon user 
RUN id -u playd 1>/dev/null 2>&1 || (( getent group 0 1>/dev/null 2>&1 || ( type groupadd 1>/dev/null 2>&1 && groupadd -g 0 root || addgroup -g 0 -S root )) && ( type useradd 1>/dev/null 2>&1 && useradd --system --create-home --uid 1111 --gid 0 playd || adduser -S -u 1111 -G root playd ))

# Create a volume for logs with owner
RUN mkdir /opt/docker/logs  && chown playd /opt/docker/logs
VOLUME ["/opt/docker/logs"]

USER 1111:0

# for testing
# CMD exec /bin/bash -c "trap : TERM INT; sleep infinity & wait"
ENTRYPOINT ["./bin/server", "-Dapplication.home=/opt/docker" ]
