package com.last.call.schedulerservice.listener;

import com.last.call.schedulerservice.service.SchedulerService;
import com.last.call.shared.dto.ItemRoomCreationDto;
import org.quartz.SchedulerException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

@Component
public class RabbitMqListener {

    @Autowired
    private SchedulerService schedulerService;

    @RabbitListener(queues = "schedule-room-close")
    public void handleScheduleRoomClose(Map<String, Object> message) throws SchedulerException {
        Long roomId = ((Number) message.get("roomId")).longValue();
        Date auctionEndDate = new Date(((Number) message.get("auctionEndDate")).longValue());
        
        System.out.println("📥 Received message for room ID: " + roomId);
        schedulerService.scheduleRoomCloseJob(roomId, auctionEndDate);
        System.out.println("✅ Scheduled jobs for room ID: " + roomId);
    }

    @RabbitListener(queues = "schedule-item-jobs")
    public void handleScheduleItemJobs(ItemRoomCreationDto itemRoomCreationDto) throws SchedulerException {
        System.out.println("📥 Received schedule-item-jobs message for item ID: " + itemRoomCreationDto.getItemId());
        schedulerService.scheduleRoomCreationJob(itemRoomCreationDto);
        schedulerService.scheduleRoomActivationJob(itemRoomCreationDto.getItemId(), itemRoomCreationDto.getAuctionStartDate());
        System.out.println("✅ Scheduled jobs for item ID: " + itemRoomCreationDto.getItemId());
    }
}
