package com.last.call.itemservice.client;

import com.last.call.shared.dto.ItemRoomCreationDto;
import com.last.call.itemservice.entity.Item;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitMqClient {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void scheduleItemJobs(Item item) {
        ItemRoomCreationDto itemRoomCreationDto = new ItemRoomCreationDto(
            item.getId(), 
            item.getStartingPrice(), 
            item.getRegistrationClosingDate(), 
            item.getAuctionStartDate()
        );
        rabbitTemplate.convertAndSend("schedule-item-jobs", itemRoomCreationDto);
        System.out.println("✅ Scheduled jobs for item ID: " + item.getId());
    }
}
