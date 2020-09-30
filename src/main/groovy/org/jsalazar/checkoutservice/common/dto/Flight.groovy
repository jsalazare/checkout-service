package org.jsalazar.checkoutservice.common.dto

import com.fasterxml.jackson.annotation.JsonFormat
import org.jsalazar.checkoutservice.common.Constants

import java.time.LocalDateTime

abstract class Flight {

    Long flightId
    String origin
    String destination

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = Constants.datePatter)
    LocalDateTime departureDate

    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = Constants.datePatter)
    LocalDateTime arrivalDate
}
