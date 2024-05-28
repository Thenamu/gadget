package com.namuk.gadget.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * native 엔티티 클래스
 */
@Entity
@Table(name = "native")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Native {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "native_number", nullable = false)
    private Long nativeNumber;

    @Column(name = "native_id", nullable = false)
    private String nativeId;

    @Column(name = "native_password", nullable = false)
    private String nativePassword;

    @Column(name = "native_phone_number", nullable = false)
    private String nativePhoneNumber;

    @Column(name = "native_email", nullable = false)
    private String nativeEmail;

    @Column(name = "native_location", nullable = false)
    private String nativeLocation;

    @Column(name = "native_profile")
    private String nativeProfile;

    @Column(name = "native_gender", nullable = false)
    private String nativeGender;

    @Column(name = "native_bank")
    private String nativeBank; // (추가 날짜: 2024-05-08)

    @Column(name = "native_account")
    private Long nativeAccount; // (추가 날짜: 2024-05-08)


    /**
     * 다대일(N:1) 양방향
     * Chat(N):Native(1)
     * 1쪽인 Native에 @OneToMany 추가
     * 양방향 매핑을 사용했으니 연관관계의 주인을 mappedBy로 지정
     * mappedBy 값은 Chat 객체(대상)의 Native nativeUser를 지칭
     */
    @OneToMany(mappedBy = "nativeUser", fetch = FetchType.LAZY)
    private List<Chat> NativeChatList = new ArrayList<>();

    /**
     * 다대일(N:1) 양방향
     * NativePage(N):Native(1)
     * 1쪽인 Native에 @OneToMany 추가
     * 양방향 매핑을 사용했으니 연관관계의 주인을 mappedBy로 지정
     * mappedBy 값은 NativePage 객체(대상)의 Native nativeNumber를 지칭
     */
    @OneToMany(mappedBy = "nativeNumber", fetch = FetchType.LAZY)
    private List<NativePage> nativePageList = new ArrayList<>();
}
