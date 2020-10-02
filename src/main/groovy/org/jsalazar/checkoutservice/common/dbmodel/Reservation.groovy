package org.jsalazar.checkoutservice.common.dbmodel

import org.jsalazar.checkoutservice.common.ReservationStatus
import org.jsalazar.checkoutservice.common.dto.Card
import org.jsalazar.checkoutservice.common.dto.FlightReservation
import org.jsalazar.checkoutservice.common.dto.StatusDates
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

import javax.validation.constraints.NotNull
import javax.validation.constraints.Positive


@Document
class Reservation {

    @Id
    Long reservationId
    Long transactionId
    @NotNull
    Long userId
    ReservationStatus reservationStatus
    @NotNull
    FlightReservation flightReservation
    int pointsToRedeem
    Card card
    @NotNull
    @Positive
    double reservationCost
    StatusDates statusDates

}
