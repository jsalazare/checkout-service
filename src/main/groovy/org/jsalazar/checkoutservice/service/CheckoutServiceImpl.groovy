package org.jsalazar.checkoutservice.service

import org.jsalazar.checkoutservice.client.interfaces.FinancialService
import org.jsalazar.checkoutservice.client.interfaces.FlightReservationService
import org.jsalazar.checkoutservice.client.interfaces.PointsServiceClient
import org.jsalazar.checkoutservice.common.ReservationStatus
import org.jsalazar.checkoutservice.common.dbmodel.Reservation
import org.jsalazar.checkoutservice.repository.ReservationRepository
import org.springframework.stereotype.Service


@Service
class CheckoutServiceImpl implements CheckoutService{

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

        if(reservation.pointsToRedeem) {
            financialServiceImplStub.charge(reservation.card, reservation.reservationCost - (reservation.pointsToRedeem/10)) // 10 points is equals to 1 dollar.
        } else {
            financialServiceImplStub.charge(reservation.card, reservation.reservationCost) // if pointsToRedeem is null the the user is not using points
        }

        reservation.card = null //do not save credit card information in the database.
        reservation.reservationStatus = ReservationStatus.CREATED
        reservationRepository.save(reservation)
    }

    @Override
    Reservation commitReservation(Long reservationId) {
        reservationRepository.findById(reservationId).ifPresent({reservation ->
            if(reservation.pointsToRedeem) {
                pointsServiceClientImplStub.redeemPoints(reservation.userId, reservation.pointsToRedeem)  //points are discounted once the reservation is committed
            } else {
                pointsServiceClientImplStub.addPoints(reservation.userId, reservation.reservationCost/10 as int) //if the user does not use its points, will get more points based on reservationCost/10. 10 dollars = 1 point
            }
            reservation.reservationStatus = ReservationStatus.COMMITTED
            return reservationRepository.save(reservation)
        })

        return null
    }

    @Override
    Reservation cancelReservation(Long reservationId) {
        reservationRepository.findById(reservationId).ifPresent({reservation ->
            reservation.reservationStatus = ReservationStatus.CANCELLED
            return reservationRepository.save(reservation)
        })

        return null
    }

    @Override
    List<Reservation> getAllCreatedReservations() {
        reservationRepository.findByReservationStatus(ReservationStatus.CREATED)
    }
}
