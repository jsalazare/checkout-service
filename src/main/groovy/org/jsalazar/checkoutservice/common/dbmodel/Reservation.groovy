package org.jsalazar.checkoutservice.common.dbmodel

import org.jsalazar.checkoutservice.common.ReservationStatus
import org.jsalazar.checkoutservice.common.dto.Card
import org.jsalazar.checkoutservice.common.dto.FlightReservation
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
class Reservation {

    @Id
    Long reservationId
    Long userId
    ReservationStatus reservationStatus
    FlightReservation flightReservation
    int pointsToRedeem
    Card card
    double reservationCost
}
