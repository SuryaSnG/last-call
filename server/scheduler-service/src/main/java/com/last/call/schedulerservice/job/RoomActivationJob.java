package com.last.call.schedulerservice.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class RoomActivationJob implements Job {
    private static final Logger logger = LoggerFactory.getLogger(RoomActivationJob.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            ApplicationContext applicationContext = (ApplicationContext) context.getScheduler().getContext().get("applicationContext");
            RabbitTemplate rabbitTemplate = applicationContext.getBean(RabbitTemplate.class);

            Long itemId = context.getJobDetail().getJobDataMap().getLong("itemId");

            System.out.println("🏠 Activating room for item ID: " + itemId + ")");

            rabbitTemplate.convertAndSend("room-activation", itemId);

            System.out.println("✅ Room activation request with data sent for item ID: " + itemId);
        } catch (Exception e) {
            logger.error("Error executing room activation job: {}", e.getMessage());
            throw new JobExecutionException(e);
        }
    }
}
