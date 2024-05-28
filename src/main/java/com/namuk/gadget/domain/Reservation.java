package com.namuk.gadget.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

/**
 * reservation 엔티티 클래스
 */
@Entity
@Table(name = "reservation")
@Data
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_number", nullable = false)
    private Long reservationNumber;

    @Column(name = "reservation_date")
    private LocalDate reservationDate;

    @Column(name = "reservation_people")
    private Long reservationPeople;

    @Column(name = "reservation_paydate")
    private LocalDate reservationPayDate; // (추가 날짜: 2024-05-08)

    @Column(name = "reservation_cost")
    private Long reservationCost; // (추가 날짜: 2024-05-08)

    /**
     * 다대일(N:1) 양방향
     * Chat(N):User(1)
     * N쪽인 Reservation에 @ManyToOne 추가
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_number", referencedColumnName = "user_number", nullable = false)
    private User user;

    /**
     * 다대일(N:1) 양방향
     * Reservation(N):Room(1)
     * N쪽인 Reservation에 @ManyToOne 추가
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_number", referencedColumnName = "room_number")
    private Room room;
}
