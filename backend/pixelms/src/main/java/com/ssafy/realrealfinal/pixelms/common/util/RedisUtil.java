package com.ssafy.realrealfinal.pixelms.common.util;

import com.ssafy.realrealfinal.pixelms.common.model.pixel.RedisNotFoundException;
import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class RedisUtil {

    private final StringRedisTemplate stringRedisTemplate;

    @Value("${canvas.scale}")
    private int SCALE;

    private final List<String> colorKeys = new ArrayList<>();
    private final List<String> pixelKeys = new ArrayList<>();

    @PostConstruct
    public void init() {
        int scale2 = SCALE * SCALE;
        for (int i = 0; i < scale2; ++i) {
            String index = Integer.toString(i);
            colorKeys.add(index + ":R");
            colorKeys.add(index + ":G");
            colorKeys.add(index + ":B");
            pixelKeys.add(index + ":R");
            pixelKeys.add(index + ":G");
            pixelKeys.add(index + ":B");
            pixelKeys.add(index + ":url");
            pixelKeys.add(index + ":name");
        }
    }

    public Integer getIntegerData(String key, String type) throws RedisNotFoundException {
        log.info("getData start: " + key + " " + type);
        HashOperations<String, String, String> hashOperations = stringRedisTemplate.opsForHash();
        log.warn("getData mid: " + hashOperations.get(key, type));
        Integer data = Integer.parseInt(hashOperations.get(key, type));
        log.info("getData end: " + data);
        return data;

    }

    public String getStringData(String key, String type) throws RedisNotFoundException {
        log.info("getData start: " + key + " " + type);
        HashOperations<String, String, String> hashOperations = stringRedisTemplate.opsForHash();
        log.warn("getData mid: " + hashOperations.get(key, type));
        String data = hashOperations.get(key, type);
        log.info("getData end: " + data);
        return data;

    }


    public void setData(String key, String type, Integer value) {
        log.info("setData start: " + key + " " + type + " " + value);
        HashOperations<String, String, String> hashOperations = stringRedisTemplate.opsForHash();
        hashOperations.put(key, type, value.toString());
        log.info("setData end: success");
    }

    public void setData(String key, String type, String value) {
        log.info("setData start: " + key + " " + type + " " + value);
        HashOperations<String, String, String> hashOperations = stringRedisTemplate.opsForHash();
        hashOperations.put(key, type, value.toString());
        log.info("setData end: success");
    }

    public void initPixelRedis() {
        stringRedisTemplate.executePipelined(new SessionCallback<Object>() {
            public List<Object> execute(RedisOperations operations) throws DataAccessException {
                for (int i = 0; i < SCALE; ++i) {
                    for (int j = 0; j < SCALE; ++j) {
                        String index = Integer.toString(i * SCALE + j);
                        operations.opsForValue().set(index + ":R", "255");
                        operations.opsForValue().set(index + ":G", "255");
                        operations.opsForValue().set(index + ":B", "255");
                        operations.opsForValue().set(index + ":url", " ");
                        operations.opsForValue().set(index + ":name", " ");
                    }
                }
                return null;
            }
        });
    }

    public List<Object> getRGBValues() {
        return stringRedisTemplate.executePipelined(new SessionCallback<List<String>>() {
            @Override
            public List<String> execute(RedisOperations operations) throws DataAccessException {
                operations.opsForValue().multiGet(colorKeys);

                return null; // executePipelined가 결과를 자동으로 반환하므로 여기서는 null을 반환
        });
    }
}
