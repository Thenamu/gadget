package com.namuk.gadget.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "review_picture")
@Data
public class ReviewPicture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_picture_number", nullable = false)
    private Long reviewPictureNumber; // (추가 날짜: 2024-05-08)

    @Column(name = "review_picture_url")
    private String reviewPictureUrl; // (추가 날짜: 2024-05-08)

    /**
     * 다대일(N:1) 양방향
     * ReviewPicture(N):Review(1)
     * N쪽인 ReviewPicture에 @ManyToOne 추가
     */
    @Id
    @ManyToOne
    @JoinColumn(name = "review_number", referencedColumnName = "review_number", nullable = false)
    private Review review; // (추가 날짜: 2024-05-08)
}
