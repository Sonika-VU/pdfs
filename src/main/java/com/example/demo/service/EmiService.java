package com.example.demo.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.example.demo.Dto.EmiCalculateResponse;
import com.example.demo.Dto.EmiFullHistoryResponse;
import com.example.demo.Dto.EmiNextDueResponse;
import com.example.demo.Dto.EmiOutstandingResponse;
import com.example.demo.model.EmiPaymentHistory;

public interface EmiService {

	EmiCalculateResponse calculateAndSaveEmi(String token, Long loanId);    EmiPaymentHistory recordPayment(Long loanId, BigDecimal amountPaid, LocalDate paymentDate);
    List<EmiPaymentHistory> getPaymentHistoryByLoanId(Long loanId);
    BigDecimal quickEmiCheck(BigDecimal principal, BigDecimal roi, Integer tenure);
    EmiFullHistoryResponse getFullHistory(Long loanId);
    EmiOutstandingResponse getOutstanding(Long loanId);
    EmiNextDueResponse getNextDue(Long loanId);


}