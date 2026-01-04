package com.last.call.schedulerservice.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public Queue scheduleItemJobsQueue() {
        return new Queue("schedule-item-jobs", true);
    }

    @Bean
    public Queue scheduleRoomCloseQueue() {
        return new Queue("schedule-room-close", true);
    }

    @Bean
    public Queue roomCreationQueue() {
        return new Queue("room-creation-with-item", true);
    }

    @Bean
    public Queue roomActivationQueue() {
        return new Queue("room-activation", true);
    }

    @Bean
    public Queue roomClosureQueue() {
        return new Queue("room-closure", true);
    }
}
