package com.example.demo.leave;

import com.example.demo.employee.Employee;
import com.example.demo.employee.EmployeeRepository;
import com.example.demo.leave.dto.CreateLeaveRequestDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.demo.leave.dto.LeaveBalanceDto;
import com.example.demo.leave.dto.LeaveApprovalDto;
import com.example.demo.leave.dto.LeaveRequestDto;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class LeaveService {

    private final EmployeeRepository employeeRepository;
    private final LeaveTypeRepository leaveTypeRepository;
    private final LeaveBalanceRepository leaveBalanceRepository;
    private final LeaveRequestRepository leaveRequestRepository;
    private final LeaveApprovalRepository leaveApprovalRepository;

    public LeaveService(
            EmployeeRepository employeeRepository,
            LeaveTypeRepository leaveTypeRepository,
            LeaveBalanceRepository leaveBalanceRepository,
            LeaveRequestRepository leaveRequestRepository,
            LeaveApprovalRepository leaveApprovalRepository
    ) {
        this.employeeRepository = employeeRepository;
        this.leaveTypeRepository = leaveTypeRepository;
        this.leaveBalanceRepository = leaveBalanceRepository;
        this.leaveRequestRepository = leaveRequestRepository;
        this.leaveApprovalRepository = leaveApprovalRepository;
    }

    public List<LeaveBalance> getMyBalances(Long employeeId, int year) {
        return leaveBalanceRepository.findByEmployeeIdAndYear(employeeId, year);
    }

    public List<LeaveRequest> getMyRequests(Long employeeId) {
        return leaveRequestRepository.findByEmployeeIdOrderBySubmittedAtDesc(employeeId);
    }

    public List<LeaveApproval> getMyPendingApprovals(Long approverEmployeeId) {
        return leaveApprovalRepository.findByApproverIdAndDecisionOrderByIdDesc(
                approverEmployeeId,
                LeaveApproval.Decision.PENDING
        );
    }

    @Transactional
    public LeaveRequest createLeaveRequest(Long employeeId, CreateLeaveRequestDto dto) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        LeaveType leaveType = leaveTypeRepository.findByCode(dto.getLeaveTypeCode())
                .orElseThrow(() -> new RuntimeException("Leave type not found"));

        LocalDate start = dto.getStartDate();
        LocalDate end = dto.getEndDate();
        if (end.isBefore(start)) {
            throw new RuntimeException("End date cannot be before start date");
        }

        BigDecimal days = calculateDaysInclusive(start, end);

        int year = start.getYear();
        LeaveBalance balance = leaveBalanceRepository.findByEmployeeIdAndLeaveTypeIdAndYear(employeeId, leaveType.getId(), year)
                .orElseThrow(() -> new RuntimeException("Leave balance not set for this leave type and year"));

        if (balance.getAvailable().compareTo(days) < 0) {
            throw new RuntimeException("Insufficient leave balance");
        }

        LeaveRequest req = new LeaveRequest();
        req.setEmployee(employee);
        req.setLeaveType(leaveType);
        req.setStartDate(start);
        req.setEndDate(end);
        req.setDays(days);
        req.setReason(dto.getReason());
        req.setStatus(LeaveRequest.Status.SUBMITTED);

        LeaveRequest saved = leaveRequestRepository.save(req);

        createApprovalChain(saved);

        return saved;
    }

    @Transactional
    public LeaveRequest approve(Long approverEmployeeId, Long leaveRequestId, String note) {
        LeaveApproval approval = leaveApprovalRepository.findByLeaveRequestIdAndLevel(leaveRequestId, LeaveApproval.Level.TEAM)
                .orElse(null);

        LeaveRequest req = leaveRequestRepository.findById(leaveRequestId)
                .orElseThrow(() -> new RuntimeException("Leave request not found"));

        LeaveApproval pendingForMe = leaveApprovalRepository.findByLeaveRequestIdOrderByIdAsc(leaveRequestId).stream()
                .filter(a -> a.getDecision() == LeaveApproval.Decision.PENDING)
                .filter(a -> a.getApprover().getId().equals(approverEmployeeId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No pending approval for this user"));

        pendingForMe.setDecision(LeaveApproval.Decision.APPROVED);
        pendingForMe.setNote(note);
        pendingForMe.setDecidedAt(Instant.now());
        leaveApprovalRepository.save(pendingForMe);

        boolean anyPending = leaveApprovalRepository.findByLeaveRequestIdOrderByIdAsc(leaveRequestId).stream()
                .anyMatch(a -> a.getDecision() == LeaveApproval.Decision.PENDING);

        if (!anyPending) {
            finalizeApproval(req, approverEmployeeId);
        }

        return req;
    }

    @Transactional
    public LeaveRequest reject(Long approverEmployeeId, Long leaveRequestId, String note) {
        LeaveRequest req = leaveRequestRepository.findById(leaveRequestId)
                .orElseThrow(() -> new RuntimeException("Leave request not found"));

        LeaveApproval pendingForMe = leaveApprovalRepository.findByLeaveRequestIdOrderByIdAsc(leaveRequestId).stream()
                .filter(a -> a.getDecision() == LeaveApproval.Decision.PENDING)
                .filter(a -> a.getApprover().getId().equals(approverEmployeeId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No pending approval for this user"));

        pendingForMe.setDecision(LeaveApproval.Decision.REJECTED);
        pendingForMe.setNote(note);
        pendingForMe.setDecidedAt(Instant.now());
        leaveApprovalRepository.save(pendingForMe);

        req.setStatus(LeaveRequest.Status.REJECTED);
        req.setDecidedAt(Instant.now());
        req.setFinalDecisionBy(employeeRepository.findById(approverEmployeeId).orElse(null));
        leaveRequestRepository.save(req);

        clearOtherPendings(leaveRequestId);

        return req;
    }

    private void finalizeApproval(LeaveRequest req, Long approverEmployeeId) {
        int year = req.getStartDate().getYear();
        LeaveBalance balance = leaveBalanceRepository
                .findByEmployeeIdAndLeaveTypeIdAndYear(req.getEmployee().getId(), req.getLeaveType().getId(), year)
                .orElseThrow(() -> new RuntimeException("Leave balance not set"));

        balance.setAvailable(balance.getAvailable().subtract(req.getDays()));
        balance.setUsed(balance.getUsed().add(req.getDays()));
        leaveBalanceRepository.save(balance);

        req.setStatus(LeaveRequest.Status.APPROVED);
        req.setDecidedAt(Instant.now());
        req.setFinalDecisionBy(employeeRepository.findById(approverEmployeeId).orElse(null));
        leaveRequestRepository.save(req);
    }

    private void clearOtherPendings(Long leaveRequestId) {
        List<LeaveApproval> approvals = leaveApprovalRepository.findByLeaveRequestIdOrderByIdAsc(leaveRequestId);
        for (LeaveApproval a : approvals) {
            if (a.getDecision() == LeaveApproval.Decision.PENDING) {
                a.setDecision(LeaveApproval.Decision.REJECTED);
                a.setNote("Auto closed");
                a.setDecidedAt(Instant.now());
                leaveApprovalRepository.save(a);
            }
        }
    }

    private void createApprovalChain(LeaveRequest req) {
        Employee requester = req.getEmployee();

        if (requester.getTeam() != null &&
        	    requester.getTeam().getTeamHead() != null) {

        	    Employee teamHead = requester.getTeam().getTeamHead();

        	    LeaveApproval approval = new LeaveApproval();
        	    approval.setLeaveRequest(req);
        	    approval.setLevel(LeaveApproval.Level.TEAM);
        	    approval.setApprover(teamHead);
        	    approval.setDecision(LeaveApproval.Decision.PENDING);

        	    leaveApprovalRepository.save(approval);
        	}

        if (requester.getDepartment() != null && requester.getDepartment().getDepartmentHead() != null) {

            Employee deptHead = requester.getDepartment().getDepartmentHead();

            LeaveApproval approval = new LeaveApproval();
            approval.setLeaveRequest(req);
            approval.setLevel(LeaveApproval.Level.DEPARTMENT);
            approval.setApprover(deptHead);
            approval.setDecision(LeaveApproval.Decision.PENDING);

            leaveApprovalRepository.save(approval);
        }


        if (leaveApprovalRepository.findByLeaveRequestIdOrderByIdAsc(req.getId()).isEmpty()) {
            throw new RuntimeException("No approvers configured for this employee");
        }
    }

    private BigDecimal calculateDaysInclusive(LocalDate start, LocalDate end) {
        long days = ChronoUnit.DAYS.between(start, end) + 1;
        return BigDecimal.valueOf(days);
    }
    
    public List<LeaveBalanceDto> getMyBalancesDto(Long employeeId, int year) {
        return leaveBalanceRepository.findByEmployeeIdAndYear(employeeId, year)
                .stream()
                .map(b -> new LeaveBalanceDto(
                        b.getLeaveType().getId(),
                        b.getLeaveType().getCode(),
                        b.getLeaveType().getName(),
                        b.getYear(),
                        b.getAvailable(),
                        b.getUsed()
                ))
                .toList();
    }
    
    private LeaveRequestDto toDto(LeaveRequest req) {
        LeaveRequestDto dto = new LeaveRequestDto();
        dto.setId(req.getId());

        dto.setEmployeeId(req.getEmployee().getId());
        dto.setEmployeeName(req.getEmployee().getFirstName() + " " + req.getEmployee().getLastName());

        dto.setLeaveTypeId(req.getLeaveType().getId());
        dto.setLeaveTypeCode(req.getLeaveType().getCode());
        dto.setLeaveTypeName(req.getLeaveType().getName());

        dto.setStartDate(req.getStartDate());
        dto.setEndDate(req.getEndDate());
        dto.setDays(req.getDays());
        dto.setReason(req.getReason());
        dto.setStatus(req.getStatus().name());

        dto.setSubmittedAt(req.getSubmittedAt());
        dto.setDecidedAt(req.getDecidedAt());

        var approvals = leaveApprovalRepository.findByLeaveRequestIdOrderByIdAsc(req.getId())
                .stream()
                .map(a -> new LeaveApprovalDto(
                        a.getId(),
                        a.getLevel().name(),
                        a.getDecision().name(),
                        a.getApprover().getId(),
                        a.getApprover().getFirstName() + " " + a.getApprover().getLastName(),
                        a.getNote(),
                        a.getDecidedAt()
                ))
                .toList();

        dto.setApprovals(approvals);
        return dto;
    }
    
    public List<LeaveRequestDto> getMyRequestsDto(Long employeeId) {
        return leaveRequestRepository.findByEmployeeIdOrderBySubmittedAtDesc(employeeId)
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Transactional
    public LeaveRequestDto createLeaveRequestDto(Long employeeId, com.example.demo.leave.dto.CreateLeaveRequestDto dto) {
        LeaveRequest saved = createLeaveRequest(employeeId, dto);
        return toDto(saved);
    }

    @Transactional
    public LeaveRequestDto approveDto(Long approverEmployeeId, Long leaveRequestId, String note) {
        LeaveRequest req = approve(approverEmployeeId, leaveRequestId, note);
        return toDto(req);
    }

    @Transactional
    public LeaveRequestDto rejectDto(Long approverEmployeeId, Long leaveRequestId, String note) {
        LeaveRequest req = reject(approverEmployeeId, leaveRequestId, note);
        return toDto(req);
    }



}
