package org.jsalazar.checkoutservice.service

import org.jsalazar.checkoutservice.common.ReservationStatus
import org.jsalazar.checkoutservice.common.dbmodel.Reservation
import org.jsalazar.checkoutservice.repository.ReservationRepository
import org.springframework.stereotype.Service


@Service
class CheckoutServiceImpl implements CheckoutService{

    ReservationRepository reservationRepository

    CheckoutServiceImpl(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository
    }

    @Override
    Reservation createReservation(Reservation reservation) {
        reservation.reservationStatus = ReservationStatus.CREATED
        reservationRepository.save(reservation)
    }

    @Override
    Reservation commitReservation(Long reservationId) {

        reservationRepository.findById(reservationId).ifPresent({reservation ->
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
