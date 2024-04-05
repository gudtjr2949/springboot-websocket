package com.hyeongseok.websocket.repository;

import com.hyeongseok.websocket.repository.entity.ChatMember;

import com.hyeongseok.websocket.repository.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<ChatMember, Long> {

    List<ChatMember> findChatMemberByMember(Member member);
}
