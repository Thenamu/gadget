package com.namuk.gadget.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "nation")
@Data
public class Nation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nation_number", nullable = false)
    private Long nationNumber;

    @Column(name = "nation_name")
    private String nationName;

    /**
     * 다대일(N:1) 양방향
     * City(N):nation(1)
     * 1쪽인 nation에 @OneToMany 추가
     * 양방향 매핑을 사용했으니 연관관계의 주인을 mappedBy로 지정
     * mappedBy 값은 City 객체(대상)의 Nation nation를 지칭
     */
    @OneToMany(mappedBy = "nation", fetch = FetchType.LAZY)
    private List<City> NationCityList = new ArrayList<>();
}
