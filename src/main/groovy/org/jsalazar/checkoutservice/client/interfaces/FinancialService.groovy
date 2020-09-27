package org.jsalazar.checkoutservice.client.interfaces

import org.jsalazar.checkoutservice.common.dto.Card

interface FinancialService {
    Long charge(Card card, double amount)

    void voidTransaction(Long transactionId)

    void refund(Long transactionId)
}