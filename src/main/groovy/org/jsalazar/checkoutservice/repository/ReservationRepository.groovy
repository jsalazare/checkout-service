package org.jsalazar.checkoutservice.repository

import org.jsalazar.checkoutservice.common.ReservationStatus
import org.jsalazar.checkoutservice.common.dbmodel.Reservation
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository


@Repository
interface ReservationRepository extends MongoRepository<Reservation, Long> {

    List<Reservation> findByReservationStatus(ReservationStatus reservationStatus)
}
