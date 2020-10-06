package org.jsalazar.checkoutservice.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "checkout-service")
class AppConfiguration {

    int commitReservationTime
}
