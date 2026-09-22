package net.myapplication.myapp.object.admin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import net.myapplication.myapp.common.ApiResponseDTO;
import net.myapplication.myapp.object.admin.dto.dashboard.AdminDashboardResponseDto;
import net.myapplication.myapp.object.admin.service.AdminDashboardService;

@RestController
@RequestMapping("/api/admin/dashboard")
@RequiredArgsConstructor
// hasAuthority thi DB la ADMIN | hasRole thi DB phai ROLE_ADMIN
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminDashboardController {

    private final AdminDashboardService adminDashboardService;

    @GetMapping
    public ResponseEntity<ApiResponseDTO<AdminDashboardResponseDto>> getDashboard() {

        AdminDashboardResponseDto response = adminDashboardService
                .getDashboard();

        return ResponseEntity.ok(

                ApiResponseDTO
                        .<AdminDashboardResponseDto>builder()

                        .status("SUCCESS")

                        .message(
                                "Dashboard data retrieved successfully")

                        .response(response)

                        .build());
    }
}
