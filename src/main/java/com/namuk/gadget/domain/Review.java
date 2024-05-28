package com.namuk.gadget.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "review")
@Data
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_number", nullable = false)
    private Long reviewNumber; // (추가 날짜: 2024-05-08)

    @Column(name = "review_rate")
    private Long reviewRate; // (추가 날짜: 2024-05-08)

    @Column(name = "review_content")
    private String reviewContent; // (추가 날짜: 2024-05-08)

    @Column(name = "review_date")
    private LocalDate reviewDate; // (추가 날짜: 2024-05-08)

    /**
     * 다대일(N:1) 양방향
     * ReviewPicture(N):Review(1)
     * 1쪽인 Review @OneToMany 추가
     * 양방향 매핑을 사용했으니 연관관계의 주인을 mappedBy로 지정
     * mappedBy 값은 Review 객체(대상)의 Nation nation를 지칭
     */
    @OneToMany(mappedBy = "review", fetch = FetchType.LAZY)
    private List<ReviewPicture> reviewPictureList = new ArrayList<>(); // (추가 날짜: 2024-05-08)

    /**
     * 다대일(N:1) 양방향
     * Review(N):User(1)
     * N쪽인 Review에 @ManyToOne 추가
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_number", referencedColumnName = "user_number", nullable = false)
    private User user; // (추가 날짜: 2024-05-08)
}
