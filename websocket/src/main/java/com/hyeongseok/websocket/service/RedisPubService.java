package com.hyeongseok.websocket.service;

import com.hyeongseok.websocket.dto.MessageDto;
import com.hyeongseok.websocket.service.redis.RedisPublisher;
import com.hyeongseok.websocket.service.redis.RedisSubscribeListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisPubService {

    private final RedisPublisher redisPublisher;
    private final RedisSubscribeListener redisSubscribeListener;
    private final RedisMessageListenerContainer redisMessageListenerContainer;


    public void pubMsgChannel(String channel, MessageDto messageDto) {
        // 1. 메시지를 보내고자 할 channel 가져오기 (channel 구독)
        redisMessageListenerContainer.addMessageListener(redisSubscribeListener, new ChannelTopic(channel));

        // 2. 구독한 channel 에 메시지 전송
        redisPublisher.publish(new ChannelTopic(channel), messageDto);
    }
}
