package com.namuk.gadget.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * room 엔티티 클래스
 */
@Entity
@Table(name = "room")
@Data
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_number", nullable = false)
    private Long roomNumber;

    @Column(name = "room_people")
    private Long roomPeople;

    @Column(name = "room_Cost")
    private Long roomCost;

    @Column(name = "room_reservation_info")
    private String roomReservationInfo;

    @Column(name = "room_name")
    private String roomName; // (추가 날짜: 2024-05-08)

    @Column(name = "room_description")
    private String roomDescription; // (추가 날짜: 2024-05-08)

    @Column(name = "room_facility")
    private String roomFacility; // (추가 날짜: 2024-05-08)

    @Column(name = "room_bed_count")
    private Long roomBedCount; // (추가 날짜: 2024-05-08)

    @Column(name = "room_type")
    private String roomType; // (추가 날짜: 2024-05-08)

    @Column(name = "room_no")
    private Long roomNo; // (추가 날짜: 2024-05-08)

    /**
     * 다대일(N:1) 양방향
     * Room(N):Hotel(1)
     * N쪽인 Room에 @ManyToOne 추가
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_number", referencedColumnName = "hotel_number", nullable = false)
    private Hotel hotel;

    /**
     * 다대일(N:1) 양방향
     * Reservation(N):Room(1)
     * 1쪽인 Room에 @OneToMany 추가
     * 양방향 매핑을 사용했으니 연관관계의 주인을 mappedBy로 지정
     * mappedBy 값은 Reservation 객체(대상)의 Room room를 지칭
     */
    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY)
    private List<Reservation> roomReservationList = new ArrayList<>();

    /**
     * 다대일(N:1) 양방향
     * RoomPicture(N):Room(1)
     * 1쪽인 Room에 @OneToMany 추가
     * 양방향 매핑을 사용했으니 연관관계의 주인을 mappedBy로 지정
     * mappedBy 값은 RoomPicture 객체(대상)의 Room room를 지칭
     */
    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY)
    private List<RoomPicture> roomPictureList = new ArrayList<>();
}
