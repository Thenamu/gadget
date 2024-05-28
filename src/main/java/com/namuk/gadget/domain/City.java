package com.namuk.gadget.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * city 엔티티 클래스
 */
@Entity
@Table(name = "city")
@Data
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "city_number")
    private Long cityNumber;

    @Column(name = "city_name")
    private String cityName;

    @Column(name = "city_latitude")
    private double cityLatitude;

    @Column(name = "city_longitude")
    private double cityLongitude;

    @Column(name = "city_information")
    private String cityInformation;

    /**
     * 다대일(N:1) 양방향
     * Hotel(N):City(1)
     * 1쪽인 City에 @OneToMany 추가
     * 양방향 매핑을 사용했으니 연관관계의 주인을 mappedBy로 지정
     * mappedBy 값은 Hotel 객체(대상)의 City city를 지칭
     */
    @OneToMany(mappedBy = "city", fetch = FetchType.LAZY)
    private List<Hotel> CityHotelList = new ArrayList<>();

    /**
     * 다대일(N:1) 양방향
     * CityPicture(N):City(1)
     * 1쪽인 City에 @OneToMany 추가
     * 양방향 매핑을 사용했으니 연관관계의 주인을 mappedBy로 지정
     * mappedBy 값은 CityPicture 객체(대상)의 City city를 지칭
     */
    @OneToMany(mappedBy = "city", fetch = FetchType.LAZY)
    private List<CityPicture> cityPictureList = new ArrayList<>();

    /**
     * 다대일(N:1) 양방향
     * City(N):Nation(1)
     * N쪽인 City에 @ManyToOne 추가
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "nation_number",  referencedColumnName = "nation_number", nullable = false)
    private Nation nation;
}
