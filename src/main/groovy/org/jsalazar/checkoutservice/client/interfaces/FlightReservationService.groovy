package org.jsalazar.checkoutservice.client.interfaces

import org.jsalazar.checkoutservice.common.dto.FlightReservation
import org.jsalazar.checkoutservice.common.dto.Seat

import java.time.LocalDate

interface FlightReservationService {

    List<FlightReservation> getAvailableFlights(String origin, String destination, LocalDate departureDate, LocalDate returnDate)

    void reserveFlight (int flightId, List<Seat> seats)
}