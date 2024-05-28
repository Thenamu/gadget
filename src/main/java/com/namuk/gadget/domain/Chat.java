package com.namuk.gadget.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;


/**
 * chat 엔티티 클래스
 */
@Entity
@Table(name = "chat")
@Data
public class Chat {

    /**
     * 다대일(N:1) 양방향
     * Chat(N):User(1)
     * N쪽인 Chat에 @ManyToOne 추가
     */
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_number", referencedColumnName = "user_number", nullable = false)
    private User user;

    /**
     * 다대일(N:1) 양방향
     * Chat(N):Native(1)
     * N쪽인 Chat에 @ManyToOne 추가
     */
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "native_number", referencedColumnName = "native_number", nullable = false)
    private Native nativeUser;

    @Column(name = "chat_content")
    private String chatContent;

    @Column(name = "chat_date")
    private LocalDate chatDate;
}