package com.example.demo.services;

import com.example.demo.Dto.EmiOutstandingResponse;
import com.example.demo.model.EmiRecord;
import com.example.demo.repo.EmiPaymentHistoryRepository;
import com.example.demo.service.EmiServiceImpl;
import com.example.demo.repo.EMIRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class getoustanding{

    @Mock
    private EMIRepository emiRecordRepository;

    @Mock
    private EmiPaymentHistoryRepository emiPaymentHistoryRepository;

    @InjectMocks
    private EmiServiceImpl emiService;

    @Test
    void testGetOutstanding_PartialPayment() {

        Long loanId = 101L;

        EmiRecord record = EmiRecord.builder()
                .loanId(loanId)
                .principalAmount(new BigDecimal("120000"))
                .monthlyEmiAmount(new BigDecimal("10000"))
                .tenure(12)
                .createdDate(LocalDate.of(2026,1,1))
                .build();

        when(emiRecordRepository.findByloanId(loanId))
                .thenReturn(List.of(record));

        when(emiPaymentHistoryRepository.getTotalPaidByLoanId(loanId))
                .thenReturn(new BigDecimal("30000"));

        EmiOutstandingResponse response = emiService.getOutstanding(loanId);

        assertEquals(new BigDecimal("120000"), response.getTotalLoanAmount());
        assertEquals(new BigDecimal("30000"), response.getTotalPaid());
        assertEquals(new BigDecimal("90000"), response.getRemainingAmount());
    }
    
    @Test
    void testGetNextDue_PartialPayment() {

        Long loanId = 101L;

        EmiRecord record = EmiRecord.builder()
                .loanId(loanId)
                .principalAmount(new BigDecimal("120000"))
                .monthlyEmiAmount(new BigDecimal("10000"))
                .tenure(12)
                .createdDate(LocalDate.of(2026,1,1))
                .build();

        when(emiRecordRepository.findByloanId(loanId))
                .thenReturn(List.of(record));

        when(emiPaymentHistoryRepository.countByLoanId(loanId))
                .thenReturn(3L);

        var response = emiService.getNextDue(loanId);

        assertFalse(response.isLoanClosed());
        assertEquals(9, response.getRemainingEmis());
        assertEquals(LocalDate.of(2026,4,1), response.getNextDueDate());
    }
}