package org.jsalazar.checkoutservice.client

import org.jsalazar.checkoutservice.client.interfaces.PointsServiceClient
import org.jsalazar.checkoutservice.common.dto.PointsBalance


class PointsServiceClientImplStub implements PointsServiceClient {

    PointsBalance pointsBalanceStub = new PointsBalance(userId: 1, balance: 100)

    @Override
    PointsBalance getBalance(Long userId) {
        pointsBalanceStub
    }

    @Override
    PointsBalance addPoints(Long userId, int amount) {
        pointsBalanceStub.balance = +amount

        pointsBalanceStub
    }

    @Override
    PointsBalance redeemPoints(Long userId, int amount) {
        pointsBalanceStub.balance = -amount

        pointsBalanceStub
    }
}
