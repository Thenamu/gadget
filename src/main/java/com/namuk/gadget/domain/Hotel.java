package com.namuk.gadget.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * hotel 엔티티 클래스
 */
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "hotel")
@Data
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hotel_number", nullable = false)
    private Long hotelNumber;

    @Column(name = "hotel_name")
    private String hotelName;

    @Column(name = "hotel_phone")
    private String hotelPhone;

    @Column(name = "hotel_email")
    private String hotelEmail;

    @Column(name = "hotel_site")
    private String hotelSite;

    @Column(name = "hotel_latitude")
    private double hotelLatitude;

    @Column(name = "hotel_longitude")
    private double hotelLongitude;

    @Column(name = "hotel_type")
    private Long hotelType;

    @Column(name = "hotel_address")
    private String hotelAddress;

    @Column(name = "hotel_rating")
    private String hotelRating; // (추가 날짜: 2024-05-01)

    @Column(name = "hotel_review_count")
    private Long hotelReviewCount; // (추가 날짜: 2024-05-01)

    @Column(name = "hotel_location")
    private String hotelLocation; // (추가 날짜: 2024-05-01)

    /**
     * 다대일(N:1) 양방향
     * Hotel(N):City(1)
     * N쪽인 Hotel에 @ManyToOne 추가
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_number", referencedColumnName = "city_number", nullable = false)
    private City city;

    /**
     * 다대일(N:1) 양방향
     * Room(N):Hotel(1)
     * 1쪽인 Hotel에 @OneToMany 추가
     * 양방향 매핑을 사용했으니 연관관계의 주인을 mappedBy로 지정
     * mappedBy 값은 Room 객체(대상)의 Hotel hotel를 지칭
     */
    @OneToMany(mappedBy = "hotel", fetch = FetchType.LAZY)
    private List<Room> hotelRoomList = new ArrayList<>();

    /**
     * 다대일(N:1) 양방향
     * HotelPicture(N):Hotel(1)
     * 1쪽인 Hotel에 @OneToMany 추가
     * 양방향 매핑을 사용했으니 연관관계의 주인을 mappedBy로 지정
     * mappedBy 값은 HotelPicture 객체(대상)의 Hotel hotel를 지칭
     */
    @OneToMany(mappedBy = "hotel", fetch = FetchType.LAZY)
    private List<HotelPicture> hotelPictureList = new ArrayList<>();

    /**
     * 다대일(N:1) 양방향
     * NativePage(N):Hotel(1)
     * 1쪽인 Hotel에 @OneToMany 추가
     * 양방향 매핑을 사용했으니 연관관계의 주인을 mappedBy로 지정
     * mappedBy 값은 NativePage 객체(대상)의 Hotel hotel를 지칭
     */
    @OneToMany(mappedBy = "hotel", fetch = FetchType.LAZY)
    private List<NativePage> hotelNativePageList = new ArrayList<>();
}
