package com.inpointuser.web;

import com.inpointuser.domain.Payment;
import com.inpointuser.service.PaymentService;
import com.inpointuser.service.dto.PaymentConfirmDTO;
import com.inpointuser.service.dto.PaymentDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "TossPayment API")
@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @Operation(summary = "결제 요청 생성", description = "사용자의 주문 정보를 기반으로 결제를 요청하고, 결제 대기 상태의 정보를 생성합니다.")
    @RequestBody(
            description = "결제 요청에 필요한 정보 (사용자 ID, 금액, 쿠폰 코드 등)",
            required = true,
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = PaymentDTO.class))
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "결제 요청 성공 (생성됨)",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Payment.class)))
    })
    @PostMapping("/request")
    public ResponseEntity<Payment> requestPayment(@org.springframework.web.bind.annotation.RequestBody PaymentDTO paymentDto) {
        return ResponseEntity.ok(paymentService.requestPayment(paymentDto));
    }

    @Operation(summary = "결제 승인 처리", description = "외부 결제 시스템(예: Toss Payments)으로부터 받은 정보를 사용하여 최종 결제 승인을 요청하고 결과를 반영합니다.")
    @RequestBody(
            description = "결제 승인에 필요한 정보 (주문 ID, 결제 키 등)",
            required = true,
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = PaymentConfirmDTO.class))
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "결제 승인 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Payment.class)))
    })
    @PostMapping("/confirm")
    public ResponseEntity<Payment> confirmPayment(@org.springframework.web.bind.annotation.RequestBody PaymentConfirmDTO confirmDto) {
        return ResponseEntity.ok(paymentService.confirmPayment(confirmDto));
    }

}
