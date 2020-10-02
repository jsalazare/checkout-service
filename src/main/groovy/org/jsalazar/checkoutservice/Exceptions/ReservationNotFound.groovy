package org.jsalazar.checkoutservice.Exceptions

class ReservationNotFound extends Exception{

    ReservationNotFound(Long id) {
        super("Could not find reservation " + id);
    }
}
