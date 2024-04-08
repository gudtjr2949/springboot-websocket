package com.hyeongseok.websocket.service.redis;

import com.hyeongseok.websocket.dto.MessageDto;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Service
public class RedisPublisher {

    private final RedisTemplate<String, Object> template;

    public RedisPublisher(RedisTemplate<String, Object> template) {
        this.template = template;
    }


    /**
     * Object publish
     */
    public void publish(ChannelTopic topic, MessageDto dto) {
        template.convertAndSend(topic.getTopic(), dto);
    }

    /**
     * String publish
     */
    public void publish(ChannelTopic topic ,String data) {
        template.convertAndSend(topic.getTopic(), data);
    }
}
