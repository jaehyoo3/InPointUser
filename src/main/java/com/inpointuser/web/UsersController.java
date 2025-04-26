package com.inpointuser.web;

import com.inpointuser.service.UsersService;
import com.inpointuser.service.dto.UsersDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User API")
@RestController
@RequestMapping("/api/users")
public class UsersController {

    private final UsersService usersService; // UsersService 주입 가정

    public UsersController(UsersService usersService) {
        this.usersService = usersService;
    }

    @Operation(
            summary = "사용자 목록 조회 (페이징)",
            parameters = {
                    @Parameter(name = "page", description = "페이지 번호 (0부터)", in = ParameterIn.QUERY,
                            schema = @Schema(type = "integer", defaultValue = "0")),
                    @Parameter(name = "size", description = "페이지 크기", in = ParameterIn.QUERY,
                            schema = @Schema(type = "integer", defaultValue = "20")), // @PageableDefault와 일치
                    @Parameter(
                            name = "sort",
                            // 핵심 설명: 사용법, 사용 가능 필드 목록, 간단 예시
                            description = "정렬 기준: '프로퍼티명,(asc|desc)'. 사용 가능: `name`, `view`, `created`. 예: `sort=name,desc`",
                            in = ParameterIn.QUERY
                    )
            }
    )
    @GetMapping
    public ResponseEntity<Page<UsersDTO>> getUsers(@PageableDefault(size = 20, sort = "name", direction = Sort.Direction.ASC) Pageable pageable) {
        return ResponseEntity.ok(usersService.getUserList(pageable));
    }

    @Operation(summary = "프로필 조회수 증가", description = "사용자 프로필 조회수를 1 증가시킵니다.")
    @Parameter(name = "userId", description = "사용자 ID", required = true, in = ParameterIn.PATH)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "증가 성공")
    })
    @PostMapping("/{userId}/increment-view-count")
    public ResponseEntity<Void> incrementProfileViewCount(@PathVariable Long userId) {
        usersService.addViewCount(userId);
        return ResponseEntity.ok().build();
    }

}
