package com.namuk.gadget.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "native_page_picture")
@Data
public class NativePagePicture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "native_page_picture_number", nullable = false)
    private Long nativePagePictureNumber;

    @Column(name = "native_page_picture_url")
    private String nativePagePictureUrl;

    /**
     * 다대일(N:1) 양방향
     * NativePagePicture(N):NativePage(1)
     * N쪽인 NativePagePicture @ManyToOne 추가
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "native_page_number", referencedColumnName = "native_page_number", nullable = false)
    private NativePage nativePage;
}

