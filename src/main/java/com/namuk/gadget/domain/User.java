package com.namuk.gadget.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


/**
 * user 엔티티 클래스
 */
@Getter
@Setter
@Entity
@Builder
@DynamicUpdate
@NoArgsConstructor // (access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_number", nullable = false)
    private Long userNumber;

    @Column(name = "user_id", nullable = false, unique = true)
    private String userId;

    @Column(name = "user_password", nullable = false)
    private String userPassword;

    public void updatePassword(String newPassword) {
        this.userPassword = newPassword;
    }

    @Column(name = "user_birth_date", nullable = false)
    private LocalDate userBirthDate;

    @Column(name = "user_phone_number", unique = true)
    private String userPhoneNumber;

    @Column(name = "user_gender", nullable = false)
    private String userGender;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(name = "user_email", unique = true)
    private String userEmail;

    @Column(name = "user_sns")
    private String userSns;

    @Column(name = "user_role")
    private String userRole;

//    @Column(name = "refresh_token", length = 1000, nullable = false)
//    private String refreshToken;

//    public void updateRefreshToken(String refreshToken) {
//        this.refreshToken = refreshToken;
//    }

//    public void destroyRefreshToken() {
//        this.refreshToken = null;
//    }

//    /**
//     * 일대일(1:1) 양방향
//     * User(1):RefreshTokens(1)
//     * 양방향 매핑을 사용했으니 연관관계의 주인을 mappedBy로 지정
//     * 연관관계의 주인은 주체인 User쪽에 외래키
//     */
//    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
//    private RefreshTokens refreshTokens;
//
//    public void updateRefreshToken(String refreshToken) {
//        this.refreshTokens.setRefreshToken(refreshToken);
//    }
//
//    public void destroyRefreshToken() {
//        this.refreshTokens.setRefreshToken(null);
//    }

    /**
     * 다대일(N:1) 양방향
     * Chat(N):User(1)
     * 1쪽인 User에 @OneToMany 추가
     * 양방향 매핑을 사용했으니 연관관계의 주인을 mappedBy로 지정
     * mappedBy 값은 Chat 객체(대상)의 User user를 지칭
     */
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Chat> chatList = new ArrayList<>();

    /**
     * 다대일(N:1) 양방향
     * Reservation(N):User(1)
     * 1쪽인 User에 @OneToMany 추가
     * 양방향 매핑을 사용했으니 연관관계의 주인을 mappedBy로 지정
     * mappedBy 값은 Reservation 객체(대상)의 User user를 지칭
     */
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Reservation> userReservationList = new ArrayList<>();

    /**
     * 다대일(N:1) 양방향
     * Review(N):User(1)
     * 1쪽인 User에 @OneToMany 추가
     * 양방향 매핑을 사용했으니 연관관계의 주인을 mappedBy로 지정
     * mappedBy 값은 Review 객체(대상)의 User user를 지칭
     */
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Review> reviewList = new ArrayList<>();
}
