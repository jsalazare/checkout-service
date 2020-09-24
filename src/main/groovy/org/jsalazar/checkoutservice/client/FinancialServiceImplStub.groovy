package org.jsalazar.checkoutservice.client

import org.jsalazar.checkoutservice.client.interfaces.FinancialService
import org.jsalazar.checkoutservice.common.dto.Card

class FinancialServiceImplStub implements FinancialService{
    @Override
    Long charge(Card card, int amount) {
        1
    }

    @Override
    void voidTransaction(Long transactionId) {

    }

    @Override
    void refund(Long transactionId) {

    }
}
