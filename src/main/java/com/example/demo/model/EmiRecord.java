package com.example.demo.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "emi_records")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmiRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long EmiId;

    // Loan service primary key
    @Column(name = "loan_id", nullable = false)
    private Long loanId;

    // account number from loan-service
    @Column(name = "account_no", nullable = false)
    private Long accountNo;

    @Column(name = "principal_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal principalAmount;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal roi;

    @Column(nullable = false)
    private Integer tenure;

    @Column(name = "monthly_emi_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal monthlyEmiAmount;

    @Column(name = "created_date", nullable = false)
    private LocalDate createdDate;
}