package org.jsalazar.checkoutservice.client.interfaces

import org.jsalazar.checkoutservice.common.dto.PointsBalance

interface PointsServiceClient {


    PointsBalance getBalance(Long userId)

    PointsBalance addPoints(Long userId, int amount)

    PointsBalance redeemPoints(Long userIdm, int amount)
}
