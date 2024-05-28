package com.namuk.gadget.domain;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "native_cost")
@Data
public class NativeCost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "native_cost_number", nullable = false)
    private Long nativeCostNumber;

    @Column(name = "native_payment", nullable = false)
    private Long nativePayment;

    /**
     * 다대일(N:1) 양방향
     * NativeCost(N):NativePage(1)
     * N쪽인 NativeCost에 @ManyToOne 추가
     */
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "native_page_number", referencedColumnName = "native_page_number", nullable = false)
    private NativePage nativePage;
}
