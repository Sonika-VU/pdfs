package com.example.demo.services;

import com.example.demo.model.EmiPaymentHistory;
import com.example.demo.repo.EmiPaymentHistoryRepository;
import com.example.demo.service.EmiServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmiPaymentServiceTest {

    private EmiPaymentHistoryRepository paymentRepository;
    private EmiServiceImpl emiService;

    @BeforeEach
    void setUp() {
        paymentRepository = mock(EmiPaymentHistoryRepository.class);
        emiService = new EmiServiceImpl(null, null, paymentRepository);
    }

    @Test
    void testRecordPayment_success() {
        Long loanId = 1L;
        BigDecimal amountPaid = new BigDecimal("5000");
        LocalDate paymentDate = LocalDate.now();

        // Mock repository save
        when(paymentRepository.save(any(EmiPaymentHistory.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        EmiPaymentHistory result = emiService.recordPayment(loanId, amountPaid, paymentDate);

        // Verify repository save called once
        ArgumentCaptor<EmiPaymentHistory> captor = ArgumentCaptor.forClass(EmiPaymentHistory.class);
        verify(paymentRepository, times(1)).save(captor.capture());

        EmiPaymentHistory saved = captor.getValue();
        assertEquals(loanId, saved.getLoanId());
        assertEquals(amountPaid, saved.getAmountPaid());
        assertEquals(paymentDate, saved.getPaymentDate());

        // Also verify returned object is the same
        assertEquals(saved, result);
    }

    @Test
    void testGetPaymentHistoryByLoanId_success() {
        Long loanId = 1L;
        EmiPaymentHistory payment1 = EmiPaymentHistory.builder()
                .loanId(loanId)
                .amountPaid(new BigDecimal("1000"))
                .paymentDate(LocalDate.now())
                .build();
        EmiPaymentHistory payment2 = EmiPaymentHistory.builder()
                .loanId(loanId)
                .amountPaid(new BigDecimal("2000"))
                .paymentDate(LocalDate.now())
                .build();

        when(paymentRepository.findByLoanId(loanId))
                .thenReturn(Arrays.asList(payment1, payment2));

        List<EmiPaymentHistory> history = emiService.getPaymentHistoryByLoanId(loanId);

        assertEquals(2, history.size());
        assertTrue(history.contains(payment1));
        assertTrue(history.contains(payment2));

        verify(paymentRepository, times(1)).findByLoanId(loanId);
    }

    @Test
    void testGetPaymentHistoryByLoanId_emptyHistory_throwsException() {
        Long loanId = 99L;

        when(paymentRepository.findByLoanId(loanId))
                .thenReturn(Collections.emptyList());

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                emiService.getPaymentHistoryByLoanId(loanId)
        );

        assertTrue(exception.getMessage().contains("No payment history found"));
        verify(paymentRepository, times(1)).findByLoanId(loanId);
    }
}