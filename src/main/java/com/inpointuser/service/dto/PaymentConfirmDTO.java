package com.inpointuser.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "결제 승인 요청 정보 DTO")
public class PaymentConfirmDTO {

    @NotBlank
    @Schema(description = "토스에서 발급된 키", requiredMode = Schema.RequiredMode.REQUIRED)
    private String paymentKey;

    @NotBlank
    @Schema(description = "주문 ID", requiredMode = Schema.RequiredMode.REQUIRED)
    private String orderId;

    @NotNull
    @Schema(description = "최종 결제 승인 금액", example = "10000", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long amount;
}