package com.inpointuser.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "결제 요청 정보 DTO")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDTO {

    @Schema(description = "결제를 요청하는 사용자 ID", example = "1", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long userId;

    @Schema(description = "결제 요청 금액 (할인 전)", example = "10000", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long amount;

    @Schema(description = "사용할 쿠폰 코드", example = "abcdefg")
    private String couponCode;
}