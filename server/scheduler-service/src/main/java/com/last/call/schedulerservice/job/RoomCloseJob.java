package com.last.call.schedulerservice.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class RoomCloseJob implements Job {
    private static final Logger logger = LoggerFactory.getLogger(RoomCloseJob.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            ApplicationContext applicationContext = (ApplicationContext) context.getScheduler().getContext().get("applicationContext");
            RabbitTemplate rabbitTemplate = applicationContext.getBean(RabbitTemplate.class);

            Long roomId = context.getJobDetail().getJobDataMap().getLong("roomId");

            System.out.println("🏠 Closing room for room ID: " + roomId + ")");

            rabbitTemplate.convertAndSend("room-closure", roomId);

            System.out.println("✅ Room closing request with data sent for room ID: " + roomId);
        } catch (Exception e) {
            logger.error("Error executing room closing job: {}", e.getMessage());
            throw new JobExecutionException(e);
        }
    }
}
