package com.last.call.itemservice.scheduler;

import com.last.call.itemservice.entity.Item;
import com.last.call.itemservice.repository.ItemRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class ItemRecreationScheduler {

    private final ItemRepository itemRepository;

    public ItemRecreationScheduler(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Scheduled(fixedRate = 3600000) // Every hour
    public void recreateExpiredItems() {
        Date now = new Date();
        List<Item> allItems = itemRepository.findAll();
        
        allItems.stream()
            .filter(item -> item.getAuctionStartDate().before(now))
            .forEach(item -> {
                long duration = item.getAuctionStartDate().getTime() - item.getRegistrationClosingDate().getTime();
                long offset = 7 * 24 * 60 * 60 * 1000L; // 7 days ahead
                
                item.setRegistrationClosingDate(new Date(now.getTime() + offset));
                item.setAuctionStartDate(new Date(now.getTime() + offset + duration));
                itemRepository.save(item);
            });
    }
}
