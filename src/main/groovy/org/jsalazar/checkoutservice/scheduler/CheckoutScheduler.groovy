package org.jsalazar.checkoutservice.scheduler

import org.jsalazar.checkoutservice.Exceptions.ReservationNotFound
import org.jsalazar.checkoutservice.common.ReservationStatus
import org.jsalazar.checkoutservice.common.dbmodel.Reservation
import org.jsalazar.checkoutservice.repository.ReservationRepository
import org.jsalazar.checkoutservice.service.CheckoutService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

import java.time.LocalDateTime


@Component
class CheckoutScheduler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass())

    ReservationRepository repository

    CheckoutService checkoutService

    CheckoutScheduler(ReservationRepository repository, CheckoutService checkoutService) {
        this.repository = repository
        this.checkoutService = checkoutService
    }

    @Scheduled(cron = "*/5 * * * * *")
    void run() {
        logger.info("CheckoutScheduler executed")

        List<Reservation> reservations = repository.findByReservationStatus(ReservationStatus.CREATED)

        if (reservations && reservations.size() > 0){
            reservations.each {
                if(it.statusDates.creationDate
                        && it.statusDates.creationDate.plusSeconds(2).isBefore(LocalDateTime.now())) {
                    Reservation reservation = checkoutService.commitReservation(it.reservationId)
                    if(!reservation){
                        throw new ReservationNotFound(it.reservationId)
                    }
                }
            }
        }
    }
}
