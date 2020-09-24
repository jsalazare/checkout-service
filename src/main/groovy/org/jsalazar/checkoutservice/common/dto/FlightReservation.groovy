package org.jsalazar.checkoutservice.common.dto

import java.time.LocalDateTime

class FlightReservation {


    Long flightId
    String origin
    String destination
    LocalDateTime departureDate
    LocalDateTime arrivalDate
    List<Seat> availableSeats
}
