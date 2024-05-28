package com.namuk.gadget.domain;

import jakarta.persistence.*;
import lombok.Data;

/**
 * room_picture 엔티티 클래스
 */
@Entity
@Table(name = "room_picture")
@Data
public class RoomPicture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_picture_number", nullable = false)
    private Long roomPictureNumber;

    /**
     * 다대일(N:1) 양방향
     * RoomPicture(N):room(1)
     * N쪽인 RoomPicture에 @ManyToOne 추가
     */
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_number", referencedColumnName = "room_number", nullable = false)
    private Room room;

    @Column(name = "room_picture_url")
    private String roomPictureUrl;
}
