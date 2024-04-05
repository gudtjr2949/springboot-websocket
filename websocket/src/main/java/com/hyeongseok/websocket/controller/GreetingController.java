package com.hyeongseok.websocket.controller;

import com.hyeongseok.websocket.dto.Greeting;
import com.hyeongseok.websocket.dto.HelloMessage;
import com.hyeongseok.websocket.service.ChatService;
import lombok.RequiredArgsConstructor;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.HtmlUtils;

@Controller
@RequiredArgsConstructor
public class GreetingController {

    private final ChatService chatService;

    @MessageMapping("/hello/{chatroomId}")
    @SendTo("/topic/greetings/{chatroomId}")
    public Greeting greeting(@DestinationVariable String chatroomId, HelloMessage message) throws Exception {
        Thread.sleep(1000); // simulated delay

        // 전처리를 거쳐도 됨. DB, Redis 저장 등등

        return new Greeting(" " + HtmlUtils.htmlEscape(message.getName()) + "!");
    }

    @GetMapping("/main/{memberId}")
    public ModelAndView showMain(@PathVariable Long memberId) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/main");
        mv.addObject("chatRooms", chatService.getChatRoomList(memberId));
        return mv;
    }

    @GetMapping("/chatroom/{chatroomId}")
    public ModelAndView getChatRoomList(@PathVariable Long chatroomId) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/chatroom");
        mv.addObject("chatroomId", chatroomId);
        return mv;
    }
}