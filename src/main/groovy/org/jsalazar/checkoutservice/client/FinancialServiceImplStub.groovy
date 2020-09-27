package org.jsalazar.checkoutservice.client

import org.jsalazar.checkoutservice.client.interfaces.FinancialService
import org.jsalazar.checkoutservice.common.dto.Card
import org.springframework.stereotype.Service


@Service
class FinancialServiceImplStub implements FinancialService{
    @Override
    Long charge(Card card, double amount) {
        1
    }

    @Override
    void voidTransaction(Long transactionId) {

    }

    @Override
    void refund(Long transactionId) {

    }
}
