package com.example.demo.leave;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LeaveApprovalRepository extends JpaRepository<LeaveApproval, Long> {
    List<LeaveApproval> findByApproverIdAndDecisionOrderByIdDesc(Long approverId, LeaveApproval.Decision decision);
    List<LeaveApproval> findByLeaveRequestIdOrderByIdAsc(Long leaveRequestId);
    Optional<LeaveApproval> findByLeaveRequestIdAndLevel(Long leaveRequestId, LeaveApproval.Level level);
}
