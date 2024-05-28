package com.namuk.gadget.domain;

import jakarta.persistence.*;
import lombok.Data;

/**
 * hotel_picture 엔티티 클래스
 */
@Entity
@Table(name = "hotel_picture")
@Data
public class HotelPicture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hotel_picture_number", nullable = false)
    private Long hotelPictureNumber;

    /**
     * 다대일(N:1) 양방향
     * HotelPicture(N):Hotel(1)
     * N쪽인 HotelPicture에 @ManyToOne 추가
     */
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_number", referencedColumnName = "hotel_number", nullable = false)
    private Hotel hotel;

    @Column(name = "hotel_picture_url")
    private String hotelPictureUrl;
}

