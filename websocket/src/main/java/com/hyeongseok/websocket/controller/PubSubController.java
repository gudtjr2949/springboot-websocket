package com.hyeongseok.websocket.controller;

import com.hyeongseok.websocket.dto.MessageDto;
import com.hyeongseok.websocket.service.RedisPubService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/redis/pubsub")
public class PubSubController {

    private final RedisPubService redisPubService;

    @PostMapping("/send/{channel}")
    public void sendMessage(@PathVariable String channel, @RequestBody MessageDto messageDto) {
        log.info("메시지를 전송하고자 하는 채널 = {}", channel);
        redisPubService.pubMsgChannel(channel, messageDto);
    }
}
