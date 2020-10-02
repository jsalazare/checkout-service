package org.jsalazar.checkoutservice.controller

import org.jsalazar.checkoutservice.common.dbmodel.Reservation
import org.jsalazar.checkoutservice.service.CheckoutService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

import javax.validation.Valid


@RestController
@RequestMapping("/checkout")
class CheckoutController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CheckoutController.class)

    CheckoutService checkoutService

    CheckoutController(CheckoutService checkoutService) {
        this.checkoutService = checkoutService
    }

    @PostMapping(value="**")
    ResponseEntity<Reservation> checkoutReservation (@Valid @RequestBody Reservation reservation){
        LOGGER.info("checking out reservation")
        Reservation createdReservation = checkoutService.createReservation(reservation)

        new ResponseEntity<Reservation>(createdReservation, HttpStatus.OK)
    }

}
