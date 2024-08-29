package com.example.players.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

@Service
public class RateLimiterService {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    private static final long WINDOW_SIZE_IN_SECONDS = 60; // 1 Min
    private static final int MAX_REQUESTS_PER_WINDOW = 5;

    public boolean isAllowed(String clientId) {
        long currentTime = Instant.now().getEpochSecond();
        String key = "rate_limit:" + clientId;

        //clean up older requests outside the sliding window
        redisTemplate.opsForZSet().removeRangeByScore(key, 0, currentTime-WINDOW_SIZE_IN_SECONDS);

        //Get the current count of request within the window
        Long currentCount = redisTemplate.opsForZSet().size(key);

        if (currentCount != null && currentCount >= MAX_REQUESTS_PER_WINDOW) {
            return false; // Rate limit exceeded
        }

        //Add the current request time stamp to Redis
        redisTemplate.opsForZSet().add(key, String.valueOf(currentTime),currentTime);
        redisTemplate.expire(key, WINDOW_SIZE_IN_SECONDS, TimeUnit.SECONDS);
        return true;
    }

}
