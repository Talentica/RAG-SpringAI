package com.openAi.ai;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class RedisService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    public List<String> getKeysByJsonNodeValue(String nodeName, String nodeValue) {
        List<String> matchingKeys = new ArrayList<>();
        Set<String> keys = redisTemplate.keys("*");

        if (keys != null) {
            for (String key : keys) {
                System.out.println("key: " + key);

                Object value = redisTemplate.opsForValue().get(key);
                if (value != null) {
                    try {
                        JsonNode jsonNode = objectMapper.readTree(value.toString());
                        if (jsonNode.has(nodeName) && jsonNode.get(nodeName).asText().equals(nodeValue)) {
                            matchingKeys.add(key);
                        }
                    } catch (Exception e) {
                        System.err.println("Error parsing JSON for key: " + key + " - " + e.getMessage());
                    }
                }
            }


        }


        return matchingKeys;
    }
  /* public List<String> getKeysByJsonNodeValue(String nodeName, String nodeValue) {
       return new ArrayList<>();
   }*/
}