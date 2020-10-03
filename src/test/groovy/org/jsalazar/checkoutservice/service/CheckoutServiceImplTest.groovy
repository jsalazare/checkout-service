package org.jsalazar.checkoutservice.service

import org.jsalazar.checkoutservice.client.interfaces.FinancialService
import org.jsalazar.checkoutservice.client.interfaces.FlightReservationService
import org.jsalazar.checkoutservice.client.interfaces.PointsServiceClient
import org.jsalazar.checkoutservice.common.ReservationStatus
import org.jsalazar.checkoutservice.common.dbmodel.Reservation
import org.jsalazar.checkoutservice.common.dto.Card
import org.jsalazar.checkoutservice.common.dto.FlightReservation
import org.jsalazar.checkoutservice.common.dto.PointsBalance
import org.jsalazar.checkoutservice.common.dto.Seat
import org.jsalazar.checkoutservice.common.dto.StatusDates
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock

import static org.junit.Assert.assertNotNull
import static org.mockito.Mockito.when
import static org.mockito.Mockito.verify
import static org.junit.Assert.assertEquals
import static org.mockito.Mockito.times
import org.mockito.junit.MockitoJUnitRunner
import org.jsalazar.checkoutservice.repository.ReservationRepository

import java.time.LocalDateTime


@RunWith(MockitoJUnitRunner.class)
class CheckoutServiceImplTest {

    @InjectMocks
    CheckoutServiceImpl checkoutServiceImpl

    @Mock
    ReservationRepository repositoryMock

    @Mock
    FinancialService financialServiceImplMock

    @Mock
    FlightReservationService flightReservationServiceImplMock

    @Mock
    PointsServiceClient pointsServiceClientImplMock


    @Test
    void testCreateReservationNullPoints() {
        Reservation expectedReservation = createReservation()
        expectedReservation.pointsToRedeem = null
        Card card = expectedReservation.card

        Long expectedTransactionId = 2
        when(repositoryMock.save(expectedReservation)).thenReturn(expectedReservation)
        when(financialServiceImplMock.charge(expectedReservation.card, expectedReservation.reservationCost)).thenReturn(expectedTransactionId)

        Reservation actualReservation = checkoutServiceImpl.createReservation(expectedReservation)

        assertEquals(expectedReservation, actualReservation)
        assertEquals(expectedTransactionId, actualReservation.transactionId)
        assertEquals(expectedReservation.reservationStatus, ReservationStatus.CREATED)
        assertNotNull(actualReservation.statusDates.creationDate)
        verify(repositoryMock, times(1)).save(expectedReservation)
        verify(financialServiceImplMock, times(1)).charge(card, expectedReservation.reservationCost)
    }

    @Test
    void testCreateReservationNotNullPoints() {
        Reservation expectedReservation = createReservation()
        expectedReservation.pointsToRedeem = 100
        Card card = expectedReservation.card

        Long expectedTransactionId = 2
        when(repositoryMock.save(expectedReservation)).thenReturn(expectedReservation)
        when(financialServiceImplMock.charge(expectedReservation.card, expectedReservation.reservationCost - (expectedReservation.pointsToRedeem/10))).thenReturn(expectedTransactionId)

        Reservation actualReservation = checkoutServiceImpl.createReservation(expectedReservation)

        assertEquals(expectedReservation, actualReservation)
        assertEquals(expectedTransactionId, actualReservation.transactionId)
        assertEquals(expectedReservation.reservationStatus, ReservationStatus.CREATED)
        assertNotNull(actualReservation.statusDates.creationDate)
        verify(repositoryMock, times(1)).save(expectedReservation)
        verify(financialServiceImplMock, times(1)).charge(card, expectedReservation.reservationCost - (expectedReservation.pointsToRedeem/10))
    }


    @Test
    void testCommitReservationNullPoints() {
        Reservation expectedReservation = createReservation()
        expectedReservation.pointsToRedeem = null
        PointsBalance expectedPointsBalance = new PointsBalance(userId: 1, balance: 110)

        when(repositoryMock.save(expectedReservation)).thenReturn(expectedReservation)
        when(repositoryMock.findById(expectedReservation.reservationId)).thenReturn(Optional.of(expectedReservation))
        when(pointsServiceClientImplMock.addPoints(expectedReservation.userId,  expectedReservation.reservationCost/10 as int)).thenReturn(expectedPointsBalance)

        Reservation actualReservation = checkoutServiceImpl.commitReservation(expectedReservation.reservationId)

        assertEquals(expectedReservation, actualReservation)
        assertEquals(actualReservation.reservationStatus, ReservationStatus.COMMITTED)
        assertNotNull(actualReservation.statusDates.committedDate)
        verify(repositoryMock, times(1)).save(expectedReservation)
        verify(repositoryMock, times(1)).findById(expectedReservation.reservationId)
        verify(pointsServiceClientImplMock, times(1)).addPoints(expectedReservation.userId,  expectedReservation.reservationCost/10 as int)
    }

    @Test
    void testCommitReservationNotNullPoints() {
        Reservation expectedReservation = createReservation()
        expectedReservation.pointsToRedeem = 100
        PointsBalance expectedPointsBalance = new PointsBalance(userId: 1, balance: 110)

        when(repositoryMock.save(expectedReservation)).thenReturn(expectedReservation)
        when(repositoryMock.findById(expectedReservation.reservationId)).thenReturn(Optional.of(expectedReservation))
        when(pointsServiceClientImplMock.redeemPoints(expectedReservation.userId,  expectedReservation.reservationCost as int)).thenReturn(expectedPointsBalance)

        Reservation actualReservation = checkoutServiceImpl.commitReservation(expectedReservation.reservationId)

        assertEquals(expectedReservation, actualReservation)
        assertEquals(actualReservation.reservationStatus, ReservationStatus.COMMITTED)
        assertNotNull(actualReservation.statusDates.committedDate)
        verify(repositoryMock, times(1)).save(expectedReservation)
        verify(repositoryMock, times(1)).findById(expectedReservation.reservationId)
        verify(pointsServiceClientImplMock, times(1)).redeemPoints(expectedReservation.userId,  expectedReservation.reservationCost as int)
    }

    @Test
    void testCancelReservation() {
        Reservation expectedReservation = createReservation()
        expectedReservation.pointsToRedeem = 100
        expectedReservation.transactionId = 1


        when(repositoryMock.save(expectedReservation)).thenReturn(expectedReservation)
        when(repositoryMock.findById(expectedReservation.reservationId)).thenReturn(Optional.of(expectedReservation))

        Reservation actualReservation = checkoutServiceImpl.cancelReservation(expectedReservation.reservationId)

        assertEquals(expectedReservation, actualReservation)
        assertEquals(actualReservation.reservationStatus, ReservationStatus.CANCELLED)
        assertNotNull(actualReservation.statusDates.cancellationDate)
        verify(repositoryMock, times(1)).save(expectedReservation)
        verify(repositoryMock, times(1)).findById(expectedReservation.reservationId)
        verify(financialServiceImplMock, times(1)).refund(expectedReservation.transactionId)
    }

    @Test
    void testGetAllCreatedReservations() {
        List<Reservation> expectedReservations = [createReservation()]
        when(repositoryMock.findByReservationStatus(ReservationStatus.CREATED)).thenReturn(expectedReservations)

        List<Reservation> actualReservations = checkoutServiceImpl.getAllCreatedReservations()

        assertEquals(expectedReservations[0], actualReservations[0])
        assertEquals(1, actualReservations.size())
        verify(repositoryMock, times(1)).findByReservationStatus(ReservationStatus.CREATED)
    }




    static Reservation createReservation() {
        Reservation reservation = new Reservation()

        FlightReservation flightReservation = new FlightReservation(
                flightId: 1,
                origin: "city1",
                destination: "city2",
                departureDate: LocalDateTime.of(2020, 01, 01, 0, 0),
                arrivalDate: LocalDateTime.of(2020, 01, 01, 1, 0),
                reservedSeats: [new Seat(seatId: 1, name: "A1")]

        )
        reservation.reservationId = 1
        reservation.transactionId = null
        reservation.userId = 1
        reservation.flightReservation = flightReservation
        reservation.pointsToRedeem = 100
        reservation.card = new Card(number: "1234123412341234", expiration: "01/01", cvv: "123")
        reservation.reservationCost = 100
        reservation.statusDates = new StatusDates()


        reservation
    }

}
