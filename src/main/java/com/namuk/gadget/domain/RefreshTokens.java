package com.namuk.gadget.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "refresh_tokens")
public class RefreshTokens {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "refresh_token_id")
    private Long refreshTokenId;

    @Column(name = "refresh_user_id")
    private String refreshUserId;

    @Column(name = "refresh_token", length = 1000, nullable = false)
    private String refreshToken;

    @Column(name = "refresh_expiration")
    private String expiration;

//    /**
//     * 일대일(1:1) 양방향
//     * User(1):RefreshTokens(1)
//     */
//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_number", referencedColumnName = "user_number")
//    private User user;
}
