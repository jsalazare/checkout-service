package org.jsalazar.checkoutservice.controller

import org.jsalazar.checkoutservice.common.dbmodel.Reservation
import org.jsalazar.checkoutservice.service.CheckoutService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/checkout")
class CheckoutController {

    CheckoutService checkoutService

    CheckoutController(CheckoutService checkoutService) {
        this.checkoutService = checkoutService
    }

    @PostMapping(value="**")
    Reservation checkoutReservation (@RequestBody Reservation reservation){
        checkoutService.createReservation(reservation)
    }

}
