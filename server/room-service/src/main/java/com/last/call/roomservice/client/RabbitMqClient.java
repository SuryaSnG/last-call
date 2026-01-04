package com.last.call.roomservice.client;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class RabbitMqClient {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void scheduleRoomClose(Long roomId, Date auctionEndDate) {
        Map<String, Object> message = new HashMap<>();
        message.put("roomId", roomId);
        message.put("auctionEndDate", auctionEndDate);
        
        rabbitTemplate.convertAndSend("schedule-room-close", message);
        System.out.println("✅ Scheduled jobs for room ID: " + roomId);
    }
}
