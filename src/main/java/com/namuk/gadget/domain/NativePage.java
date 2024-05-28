package com.namuk.gadget.domain;

import jakarta.persistence.*;
import lombok.Data;
import org.apache.catalina.LifecycleState;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * native_page 엔티티 클래스
 */
@Entity
@Table(name = "native_page")
@Data
public class NativePage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "native_page_number", nullable = false)
    private Long nativePageNumber;

    @Column(name = "native_page_title", nullable = false)
    private String nativePageTitle;

    @Column(name = "native_page_writer", nullable = false)
    private String nativePageWriter;

    @Column(name = "native_page_content", nullable = false)
    private String nativePageContent;

    @Column(name = "native_page_like")
    private Long nativePageLike;

    @Column(name = "native_page_date", nullable = false)
    private LocalDate nativePageDate; // (추가 날짜: 2024-05-08)

    @Column(name = "native_page_cost", nullable = false)
    private Long nativePageCost; // (추가 날짜: 2024-05-08)

    @Column(name = "native_page_total_cost")
    private Long nativePageTotalCost; // (추가 날짜: 2024-05-08)

    @Column(name = "native_page_rate")
    private double nativePageRate; // (추가 날짜: 2024-05-08)


    /**
     * 다대일(N:1) 양방향
     * NativePage(N):Native(1)
     * N쪽인 NativePage에 @ManyToOne 추가
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "native_number", referencedColumnName = "native_number", nullable = false)
    private Native nativeNumber;

    /**
     * 다대일(N:1) 양방향
     * NativePage(N):Hotel(1)
     * N쪽인 NativePage에 @ManyToOne 추가
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_number", referencedColumnName = "hotel_number", nullable = false)
    private Hotel hotel;

    /**
     * 다대일(N:1) 양방향
     * NativeCost(N):NativePage(1)
     * 1쪽인 NativePage @OneToMany 추가
     * 양방향 매핑을 사용했으니 연관관계의 주인을 mappedBy로 지정
     * mappedBy 값은 NativeCost 객체(대상)의  NativePage nativePage를 지칭
     */
    @OneToMany(mappedBy = "nativePage", fetch = FetchType.LAZY)
    private List<NativeCost> nativeCostList = new ArrayList<>();

    /**
     * 다대일(N:1) 양방향
     * NativePagePicture(N):NativePage(1)
     * 1쪽인 NativePage @OneToMany 추가
     * 양방향 매핑을 사용했으니 연관관계의 주인을 mappedBy로 지정
     * mappedBy 값은 NativePagePicture 객체(대상)의 NativePage nativePage를 지칭
     */
    @OneToMany(mappedBy = "nativePage", fetch = FetchType.LAZY)
    private List<NativePagePicture> nativePagePictureList = new ArrayList<>();
}
