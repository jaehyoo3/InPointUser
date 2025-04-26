package com.inpointuser.domain;

import com.inpointuser.domain.enumerate.PayStatus;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Entity
@Table
@Getter
@NoArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users user;

    @Column(unique = true)
    private String orderId;

    private Long requestedAmount;

    @Column
    private Long discountAmount = 0L;

    @Column(nullable = false)
    private Long amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id")
    private Coupon usedCoupon;

    @Setter
    private String paymentKey;

    @Setter
    private PayStatus status;

    @CreationTimestamp
    private Instant createdAt;

    @Builder
    public Payment(Users user, String orderId, Long requestedAmount, Long discountAmount, Long amount, Coupon usedCoupon, PayStatus status) {
        this.user = user;
        this.orderId = orderId;
        this.requestedAmount = requestedAmount;
        this.discountAmount = discountAmount != null ? discountAmount : 0L;
        this.amount = amount;
        this.usedCoupon = usedCoupon;
        this.status = status != null ? status : PayStatus.REQUESTED;
    }
}