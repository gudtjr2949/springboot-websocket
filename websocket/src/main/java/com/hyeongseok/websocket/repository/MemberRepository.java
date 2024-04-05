package com.hyeongseok.websocket.repository;

import com.hyeongseok.websocket.repository.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findMemberByMemberId(Long memberId);
}
