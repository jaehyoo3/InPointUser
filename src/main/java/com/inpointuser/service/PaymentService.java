package com.inpointuser.service;


import com.inpointuser.config.TossConfig;
import com.inpointuser.domain.Coupon;
import com.inpointuser.domain.Payment;
import com.inpointuser.domain.Users;
import com.inpointuser.domain.enumerate.PayStatus;
import com.inpointuser.repository.CouponRepository;
import com.inpointuser.repository.PaymentRepository;
import com.inpointuser.repository.UsersRepository;
import com.inpointuser.service.dto.PaymentConfirmDTO;
import com.inpointuser.service.dto.PaymentDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private static final long MAX_DISCOUNT_AMOUNT_LIMIT = 5000L;
    private final UsersRepository userRepository;
    private final PaymentRepository paymentRepository;
    private final CouponRepository couponRepository;
    private final TossConfig tossConfig;
    private final RestTemplate restTemplate;

    @Transactional
    public Payment requestPayment(PaymentDTO dto) {
        Users user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다"));

        Coupon coupon = null;
        long discountAmount = 0L;
        if (dto.getCouponCode() != null && !dto.getCouponCode().isEmpty()) {
            coupon = couponRepository.findByCodeAndAvailable(dto.getCouponCode(), true)
                    .filter(Coupon::isAvailable)
                    .orElseThrow(() -> new IllegalArgumentException("사용할 수 없는 쿠폰 입니다."));

            discountAmount = calculateDiscount(dto.getAmount(), coupon.getDiscountRate());
        }

        long finalAmount = dto.getAmount() - discountAmount;
        if (finalAmount < 0) {
            throw new IllegalArgumentException("할인 적용 후 최종 금액이 0원 미만 입니다.");
        }

        Payment payment = Payment.builder()
                .user(user)
                .orderId(UUID.randomUUID().toString())
                .requestedAmount(dto.getAmount())
                .discountAmount(discountAmount)
                .amount(finalAmount)
                .usedCoupon(coupon)
                .status(PayStatus.REQUESTED)
                .build();

        return paymentRepository.save(payment);
    }

    @Transactional
    public Payment confirmPayment(PaymentConfirmDTO confirmRequestDto) {
        Payment payment = paymentRepository.findByOrderId(confirmRequestDto.getOrderId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 주문 입니다."));

        if (payment.getStatus() != PayStatus.REQUESTED) {
            throw new IllegalArgumentException("이미 처리 된 주문 입니다");
        }

        HttpHeaders headers = tossConfig.getAuthHeaders();
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("orderId", payment.getOrderId());
        requestBody.put("amount", payment.getAmount());
        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<Map> responseEntity = restTemplate.postForEntity(
                    TossConfig.TOSS_API_URL + confirmRequestDto.getPaymentKey(),
                    requestEntity,
                    Map.class
            );

            if (responseEntity.getStatusCode() == HttpStatus.OK) {

                payment.setPaymentKey(confirmRequestDto.getPaymentKey());
                payment.setStatus(PayStatus.SUCCESS);

                Users user = payment.getUser();
                user.chargePoint(payment.getAmount());

                if (payment.getUsedCoupon() != null) {
                    Coupon usedCoupon = payment.getUsedCoupon();
                    if (usedCoupon.isAvailable()) {
                        usedCoupon.setAvailable(false);
                    }
                }
                return payment;
            } else {
                throw new RuntimeException("토스 API 요청 실패");
            }

        } catch (RestClientException e) {
            payment.setStatus(PayStatus.FAILED);
            throw new RuntimeException("결제 승인 중 통신 오류가 발생했습니다.");
        }
    }

    private long calculateDiscount(long originalAmount, BigDecimal discountRate) {
        if (discountRate == null || discountRate.doubleValue() <= 0) {
            return 0;
        }

        double discount = originalAmount * discountRate.doubleValue();

        long finalDiscount = (long) discount;
        return Math.min(finalDiscount, MAX_DISCOUNT_AMOUNT_LIMIT);
    }

}