package com.hyeongseok.websocket.service;

import com.hyeongseok.websocket.dto.ChatRoomList;
import com.hyeongseok.websocket.dto.HelloMessage;
import com.hyeongseok.websocket.dto.Message;
import com.hyeongseok.websocket.repository.ChatRepository;
import com.hyeongseok.websocket.repository.MemberRepository;
import com.hyeongseok.websocket.repository.entity.ChatMember;

import com.hyeongseok.websocket.repository.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRepository chatRepository;
    private final MemberRepository memberRepository;

    public List<ChatRoomList> getChatRoomList(Long memberId) {
        Member member = memberRepository.findMemberByMemberId(memberId).get();

        List<ChatMember> chatMembers = chatRepository.findChatMemberByMember(member);

        List<ChatRoomList> chatRoomList = new ArrayList<>();
        for (ChatMember chatMember : chatMembers) {
            ChatRoomList chatRoom = new ChatRoomList();
            chatRoom.setChatroom_id(chatMember.getChatRoom().getChatRoomId());
            chatRoomList.add(chatRoom);
        }

        return chatRoomList;
    }

}
