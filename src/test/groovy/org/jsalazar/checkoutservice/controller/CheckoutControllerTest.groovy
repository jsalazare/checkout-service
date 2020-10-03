package org.jsalazar.checkoutservice.controller

import org.jsalazar.checkoutservice.common.dbmodel.Reservation
import org.jsalazar.checkoutservice.service.CheckoutServiceImpl
import org.jsalazar.checkoutservice.service.CheckoutServiceImplTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

import static org.mockito.Mockito.when
import static org.mockito.Mockito.any
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@RunWith(SpringRunner)
@SpringBootTest
class CheckoutControllerTest {

    MockMvc mockMvc;

    @MockBean
    CheckoutServiceImpl checkoutServiceImpl

    @Autowired
    WebApplicationContext webApplicationContext

    @Before
    void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build()
    }

    @Test
    void testCheckout(){
        Reservation reservation = CheckoutServiceImplTest.createReservation()
        when(checkoutServiceImpl.createReservation(any())).thenReturn(reservation)

        this.mockMvc.perform(
                        post("/checkout/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content('{"reservationId": 1,"userId": 1,"status": "","flightReservation": {},"card": {},"reservationCost": 10}')
        )
                .andExpect(status().isOk())
                .andDo(print())
    }


    @Test
    void testCheckoutInvalidReservationFields(){
        Reservation reservation = CheckoutServiceImplTest.createReservation()
        when(checkoutServiceImpl.createReservation(any())).thenReturn(reservation)

        this.mockMvc.perform(
                post("/checkout/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content('{"reservationId": 1,"userId": 1,"status": "","flightReservation": {},"card": {},"reservationCost": -1}')
        )
                .andExpect(status().is4xxClientError())
                .andDo(print())
    }


}
