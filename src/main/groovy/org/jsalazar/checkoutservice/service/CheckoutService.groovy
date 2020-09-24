package org.jsalazar.checkoutservice.service

import org.jsalazar.checkoutservice.common.dbmodel.Reservation

interface CheckoutService {

    Reservation createReservation(Reservation reservation)

    Reservation commitReservation(Long reservationId)

    Reservation cancelReservation(Long reservationId)

    List<Reservation> getAllCreatedReservations()


}