package org.jsalazar.checkoutservice.client

import org.jsalazar.checkoutservice.client.interfaces.FlightReservationService
import org.jsalazar.checkoutservice.common.dto.FlightAvailable
import org.jsalazar.checkoutservice.common.dto.Seat

import java.time.LocalDate
import java.time.LocalDateTime

class FlightReservationServiceImplStub implements FlightReservationService {

    @Override
    List<FlightAvailable> getAvailableFlights(String origin, String destination, LocalDate departureDate, LocalDate returnDate) {

        FlightAvailable flightReservation =
                new FlightAvailable(
                        flightId: 1,
                        origin: "New York",
                        destination: "Chicago",
                        departureDate: LocalDateTime.of(2018,1,1,0,0,0),
                        arrivalDate: LocalDateTime.of(2018,1,1,1,0,0),
                        availableSeats: [new Seat(seatId: 1, name: "1A")])

        [flightReservation]
    }

    @Override
    void reserveFlight(int flightId, List<Seat> seats) {

    }
}
