package org.jsalazar.checkoutservice.service

import org.jsalazar.checkoutservice.common.dbmodel.Reservation

interface CheckoutService {

    void createReservation(Reservation reservation)

    void commitReservation(Long reservationId)

    void cancelReservation(Long reservationId)

    List<Reservation> getAllCreatedReservations()


}