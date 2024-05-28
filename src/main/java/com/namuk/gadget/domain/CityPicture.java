package com.namuk.gadget.domain;

import jakarta.persistence.*;
import lombok.Data;

/**
 * city_picture 엔티티 클래스
 */
@Entity
@Table(name = "city_picture")
@Data
public class CityPicture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "city_picture_number", nullable = false)
    private Long cityPictureNumber;

    /**
     * 다대일(N:1) 양방향
     * CityPicture(N):City(1)
     * N쪽인 CityPicture에 @ManyToOne 추가
     */
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_number", referencedColumnName = "city_number", nullable = false)
    private City city;

    @Column(name = "city_picture_url", nullable = false)
    private String cityPictureUrl;
}
