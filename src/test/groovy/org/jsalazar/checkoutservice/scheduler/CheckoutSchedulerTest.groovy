package org.jsalazar.checkoutservice.scheduler

import org.jsalazar.checkoutservice.common.dbmodel.Reservation
import org.jsalazar.checkoutservice.service.CheckoutServiceImpl
import org.jsalazar.checkoutservice.service.CheckoutServiceImplTest
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

import java.time.LocalDateTime

import static org.mockito.Mockito.times
import static org.mockito.Mockito.verify
import static org.mockito.Mockito.when

@RunWith(MockitoJUnitRunner.class)
class CheckoutSchedulerTest {

    @InjectMocks
    CheckoutScheduler checkoutScheduler

    @Mock
    CheckoutServiceImpl checkoutServiceImplMock


    @Test
    void testSchedulerBefore2Hours() {
        Reservation reservation = CheckoutServiceImplTest.createReservation()
        reservation.statusDates.creationDate = LocalDateTime.now()

        List<Reservation> expectedReservations = [reservation]

        when(checkoutServiceImplMock.getAllCreatedReservations()).thenReturn(expectedReservations)

        checkoutScheduler.run()

        verify(checkoutServiceImplMock, times(1)).getAllCreatedReservations()
    }

    @Test
    void testSchedulerAfter2Hours() {
        Reservation reservation = CheckoutServiceImplTest.createReservation()
        reservation.statusDates.creationDate = LocalDateTime.of(1970,1,1,0,0,0)

        List<Reservation> expectedReservations = [reservation]

        when(checkoutServiceImplMock.getAllCreatedReservations()).thenReturn(expectedReservations)
        when(checkoutServiceImplMock.commitReservation(reservation.reservationId)).thenReturn(reservation)

        checkoutScheduler.run()

        verify(checkoutServiceImplMock, times(1)).getAllCreatedReservations()
        verify(checkoutServiceImplMock, times(1)).commitReservation(reservation.reservationId)
    }
}