package com.BIT.Apex_Approval_System.model;
import jakarta.persistence.*;
        import lombok.Data;
import java.time.LocalDateTime;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "apex_forms")
public class ApexForm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;
    private String subject;
    private boolean financialRequirement;
    private double amountRequired;
    private boolean onDuty;
    private boolean da;
    private String description;

    private String departmentOrClub;
    private String facultyName;
    private String facultyId;

    private boolean accommodation;
    private boolean vehicle;
    private boolean food;
    private boolean notApplicable;

    private String expectedOutcome;

    // Status: SUBMITTED, FORWARDED, RECOMMENDED, APPROVED, REJECTED
    private String status = "SUBMITTED";

    private String forwardedBy;
    private LocalDateTime forwardedAt;

    private String recommendedBy;
    private LocalDateTime recommendedAt;

    private String approvedBy;
    private LocalDateTime approvedAt;

    private String remarks; // optional comments
}
