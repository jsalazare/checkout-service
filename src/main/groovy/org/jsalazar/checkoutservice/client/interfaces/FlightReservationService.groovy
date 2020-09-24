package org.jsalazar.checkoutservice.client.interfaces

import org.jsalazar.checkoutservice.common.dto.FlightAvailable
import org.jsalazar.checkoutservice.common.dto.Seat

import java.time.LocalDate

interface FlightReservationService {

    List<FlightAvailable> getAvailableFlights(String origin, String destination, LocalDate departureDate, LocalDate returnDate)

    void reserveFlight (int flightId, List<Seat> seats)
}