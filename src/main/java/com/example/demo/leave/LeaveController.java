package com.example.demo.leave;

import com.example.demo.leave.dto.CreateLeaveRequestDto;
import com.example.demo.leave.dto.DecisionDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.demo.leave.dto.LeaveBalanceDto;

import java.time.Year;
import java.util.List;

@RestController
@RequestMapping("/api/leaves")
public class LeaveController {

    private final LeaveService leaveService;

    public LeaveController(LeaveService leaveService) {
        this.leaveService = leaveService;
    }

    @GetMapping("/balances/my")
    public ResponseEntity<List<LeaveBalanceDto>> myBalances(
            @RequestHeader("X-Employee-Id") Long employeeId,
            @RequestParam(required = false) Integer year
    ) {
        int y = year != null ? year : Year.now().getValue();
        return ResponseEntity.ok(leaveService.getMyBalancesDto(employeeId, y));

    }

    @GetMapping("/requests/my")
    public ResponseEntity<List<com.example.demo.leave.dto.LeaveRequestDto>> myRequests(
            @RequestHeader("X-Employee-Id") Long employeeId
    ) {
        return ResponseEntity.ok(leaveService.getMyRequestsDto(employeeId));
    }


    @PostMapping("/request")
    public ResponseEntity<com.example.demo.leave.dto.LeaveRequestDto> create(
            @RequestHeader("X-Employee-Id") Long employeeId,
            @Valid @RequestBody com.example.demo.leave.dto.CreateLeaveRequestDto dto
    ) {
        return ResponseEntity.ok(leaveService.createLeaveRequestDto(employeeId, dto));
    }


    @GetMapping("/approvals/pending")
    public ResponseEntity<List<LeaveApproval>> pendingApprovals(
            @RequestHeader("X-Employee-Id") Long employeeId
    ) {
        return ResponseEntity.ok(leaveService.getMyPendingApprovals(employeeId));
    }

    @PostMapping("/approve")
    public ResponseEntity<com.example.demo.leave.dto.LeaveRequestDto> approve(
            @RequestHeader("X-Employee-Id") Long employeeId,
            @Valid @RequestBody com.example.demo.leave.dto.DecisionDto dto
    ) {
        return ResponseEntity.ok(leaveService.approveDto(employeeId, dto.getLeaveRequestId(), dto.getNote()));
    }


    @PostMapping("/reject")
    public ResponseEntity<com.example.demo.leave.dto.LeaveRequestDto> reject(
            @RequestHeader("X-Employee-Id") Long employeeId,
            @Valid @RequestBody com.example.demo.leave.dto.DecisionDto dto
    ) {
        return ResponseEntity.ok(leaveService.rejectDto(employeeId, dto.getLeaveRequestId(), dto.getNote()));
    }

}
