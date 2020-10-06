package org.jsalazar.checkoutservice.service

import org.jsalazar.checkoutservice.client.interfaces.FinancialService
import org.jsalazar.checkoutservice.client.interfaces.FlightReservationService
import org.jsalazar.checkoutservice.client.interfaces.PointsServiceClient
import org.jsalazar.checkoutservice.common.ReservationStatus
import org.jsalazar.checkoutservice.common.dbmodel.Reservation
import org.jsalazar.checkoutservice.common.dto.StatusDates
import org.jsalazar.checkoutservice.controller.CheckoutController
import org.jsalazar.checkoutservice.repository.ReservationRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

import java.time.LocalDateTime


@Service
class CheckoutServiceImpl implements CheckoutService{

    private static final Logger LOGGER = LoggerFactory.getLogger(CheckoutController.class)

    ReservationRepository reservationRepository

    FinancialService financialServiceImplStub

    FlightReservationService flightReservationServiceImplStub

    PointsServiceClient pointsServiceClientImplStub

    CheckoutServiceImpl(ReservationRepository reservationRepository, FinancialService financialServiceImplStub, FlightReservationService flightReservationServiceImplStub, PointsServiceClient pointsServiceClientImplStub) {
        this.reservationRepository = reservationRepository
        this.financialServiceImplStub = financialServiceImplStub
        this.flightReservationServiceImplStub = flightReservationServiceImplStub
        this.pointsServiceClientImplStub = pointsServiceClientImplStub
    }

    @Override
    Reservation createReservation(Reservation reservation) {
        LOGGER.info("Saving reservation with status CREATED")
        Long transactionId
        if(reservation.pointsToRedeem) {
            transactionId = financialServiceImplStub.charge(reservation.card, reservation.reservationCost - (reservation.pointsToRedeem/10)) // 10 points is equals to 1 dollar.
        } else {
            transactionId = financialServiceImplStub.charge(reservation.card, reservation.reservationCost) // if pointsToRedeem is null the the user is not using points
        }

        reservation.card = null //do not save credit card information in the database.
        reservation.reservationStatus = ReservationStatus.CREATED
        reservation.transactionId = transactionId

        if (!reservation.statusDates) {
            reservation.statusDates = new StatusDates()
        }

        reservation.statusDates.creationDate = LocalDateTime.now()
        reservationRepository.save(reservation)
    }

    @Override
    Reservation commitReservation(Long reservationId) {
        LOGGER.info("committing reservation")
        Reservation reservationResult = null
         reservationRepository.findById(reservationId).ifPresent({reservation ->
             if(reservation.pointsToRedeem) {
                pointsServiceClientImplStub.redeemPoints(reservation.userId, reservation.pointsToRedeem)  //points are discounted once the reservation is committed
            } else {
                pointsServiceClientImplStub.addPoints(reservation.userId, reservation.reservationCost/10 as int) //if the user does not use its points, will get more points based on reservationCost/10. 10 dollars = 1 point
            }
            reservation.reservationStatus = ReservationStatus.COMMITTED
            reservation.statusDates.committedDate = LocalDateTime.now()
            reservationResult = reservationRepository.save(reservation)
        })

        reservationResult
    }

    @Override
    Reservation cancelReservation(Long reservationId) {
        LOGGER.info("cancelling reservation")
        Reservation reservationResult = null
        reservationRepository.findById(reservationId).ifPresent({reservation ->
            financialServiceImplStub.refund(reservation.transactionId) //get back money after cancellation
            reservation.reservationStatus = ReservationStatus.CANCELLED
            reservation.statusDates.cancellationDate = LocalDateTime.now()
            reservationResult = reservationRepository.save(reservation)
        })

        reservationResult
    }

    @Override
    List<Reservation> getAllCreatedReservations() {
        LOGGER.info("getting all created Reservations")
        reservationRepository.findByReservationStatus(ReservationStatus.CREATED)
    }
}
