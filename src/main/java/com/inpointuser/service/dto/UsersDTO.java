package com.inpointuser.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "사용자 정보 응답 DTO")
public class UsersDTO {

    @Schema(description = "사용자 이름", example = "홍길동", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @Schema(description = "프로필 조회수", example = "123", requiredMode = Schema.RequiredMode.REQUIRED)
    private Long profileViewCount;

    @Schema(description = "생성 일시 (UTC)", example = "2025-04-27T00:36:53Z", requiredMode = Schema.RequiredMode.REQUIRED)
    private Instant createdAt;

}
