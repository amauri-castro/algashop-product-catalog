package com.algashop.product.catalog.infrastructure.message;

import com.algashop.product.catalog.application.ApplicationMessagePublisher;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationMessagePublisherConfig {

    @Bean
    public ApplicationMessagePublisher applicationMessagePublisher(
            ApplicationEventPublisher applicationEventPublisher
    ) {
        return applicationEventPublisher::publishEvent;
    }

}
