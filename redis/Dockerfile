# Dockerfile for custom Redis setup
FROM redis:6.2

# Copy custom Redis configuration file
COPY ./redis.conf /usr/local/etc/redis/redis.conf

# Set the command to run Redis with the custom configuration
CMD ["redis-server", "/usr/local/etc/redis/redis.conf"]
