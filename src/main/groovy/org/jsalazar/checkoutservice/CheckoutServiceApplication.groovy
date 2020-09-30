package org.jsalazar.checkoutservice

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class CheckoutServiceApplication {

    static void main(String[] args) {
        SpringApplication.run(CheckoutServiceApplication, args)
    }

}
