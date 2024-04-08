package com.hyeongseok.websocket.service.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hyeongseok.websocket.dto.MessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class RedisSubscribeListener implements MessageListener {

    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, Object> template;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            String publishMessage = template
                    .getStringSerializer().deserialize(message.getBody());

            MessageDto messageDto = objectMapper.readValue(publishMessage, MessageDto.class);

            log.info("Redis Subscribe Channel : " + messageDto.getRoomId());
            log.info("Redis SUB Message : {}", publishMessage);

            // Return || Another Method Call(etc.save to DB)
            // TODO
        } catch (JsonProcessingException e) {
            log.error(e.getMessage());
        }
    }
}
