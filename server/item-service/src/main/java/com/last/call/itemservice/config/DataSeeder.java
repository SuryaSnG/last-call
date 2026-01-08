package com.last.call.itemservice.config;

import com.last.call.itemservice.entity.Item;
import com.last.call.itemservice.enums.ItemCategory;
import com.last.call.itemservice.service.ItemService;
import com.last.call.itemservice.service.ItemTagService;
import com.last.call.itemservice.service.ItemSubscriberService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.Date;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner seedItemData(ItemService itemService,
                                   ItemTagService itemTagService,
                                   ItemSubscriberService itemSubscriberService) {
        return args -> {
            System.out.println("🚀 DataSeeder starting...");
            System.out.println("🗑️ Clearing existing data...");
            itemSubscriberService.deleteAll();
            itemTagService.deleteAll();
            itemService.deleteAll();
            System.out.println("📦 Seeding item data...");

            long now = System.currentTimeMillis();
            long dayMs = 24 * 60 * 60 * 1000L;
            long hourMs = 60 * 60 * 1000L;

            // CAR items - Tony (1L), Uncle (2L), Peter (3L)
            Item car1 = new Item("Ferrari 488", "Red sports car in excellent condition",
                    1L, 50000, ItemCategory.CAR,
                    new Date(now + dayMs), new Date(now + dayMs + 2 * hourMs));
            Item car2 = new Item("BMW M3", "High-performance sedan",
                    2L, 35000, ItemCategory.CAR,
                    new Date(now + 2 * dayMs), new Date(now + 2 * dayMs + 2 * hourMs));
            Item car3 = new Item("Tesla Model S", "Electric luxury sedan",
                    3L, 40000, ItemCategory.CAR,
                    new Date(now + 3 * dayMs), new Date(now + 3 * dayMs + 2 * hourMs));

            // JEWELRY items - Bruce (4L), Clark (5L), Diana (6L)
            Item jew1 = new Item("Diamond Ring", "2-carat diamond engagement ring",
                    4L, 8000, ItemCategory.JEWELRY,
                    new Date(now + 4 * dayMs), new Date(now + 4 * dayMs + 2 * hourMs));
            Item jew2 = new Item("Gold Necklace", "18k gold chain necklace",
                    5L, 1500, ItemCategory.JEWELRY,
                    new Date(now + 5 * dayMs), new Date(now + 5 * dayMs + 2 * hourMs));
            Item jew3 = new Item("Pearl Earrings", "Cultured pearl drop earrings",
                    6L, 800, ItemCategory.JEWELRY,
                    new Date(now + 6 * dayMs), new Date(now + 6 * dayMs + 2 * hourMs));

            // ELECTRONICS items - Tony (1L), Uncle (2L), Peter (3L)
            Item elec1 = new Item("iPhone 15", "Latest Apple smartphone",
                    1L, 500, ItemCategory.ELECTRONICS,
                    new Date(now + 7 * dayMs), new Date(now + 7 * dayMs + 2 * hourMs));
            Item elec2 = new Item("MacBook Pro", "High-performance laptop",
                    2L, 1500, ItemCategory.ELECTRONICS,
                    new Date(now + 8 * dayMs), new Date(now + 8 * dayMs + 2 * hourMs));
            Item elec3 = new Item("Sony Headphones", "Noise-cancelling headphones",
                    3L, 200, ItemCategory.ELECTRONICS,
                    new Date(now + 9 * dayMs), new Date(now + 9 * dayMs + 2 * hourMs));

            // ART items - Bruce (4L), Clark (5L), Diana (6L)
            Item art1 = new Item("Mona Lisa", "Famous painting by Leonardo da Vinci",
                    4L, 1000, ItemCategory.ART,
                    new Date(now + 10 * dayMs), new Date(now + 10 * dayMs + 2 * hourMs));
            Item art2 = new Item("Starry Night", "Van Gogh masterpiece",
                    5L, 800, ItemCategory.ART,
                    new Date(now + 11 * dayMs), new Date(now + 11 * dayMs + 2 * hourMs));
            Item art3 = new Item("The Scream", "Edvard Munch painting",
                    6L, 1200, ItemCategory.ART,
                    new Date(now + 12 * dayMs), new Date(now + 12 * dayMs + 2 * hourMs));

            // MOTORCYCLE items - Tony (1L), Uncle (2L), Peter (3L)
            Item moto1 = new Item("Harley Davidson", "Classic cruiser motorcycle",
                    1L, 15000, ItemCategory.MOTORCYCLE,
                    new Date(now + 13 * dayMs), new Date(now + 13 * dayMs + 2 * hourMs));
            Item moto2 = new Item("Yamaha R1", "Sport bike in mint condition",
                    2L, 12000, ItemCategory.MOTORCYCLE,
                    new Date(now + 14 * dayMs), new Date(now + 14 * dayMs + 2 * hourMs));
            Item moto3 = new Item("BMW GS", "Adventure touring motorcycle",
                    3L, 18000, ItemCategory.MOTORCYCLE,
                    new Date(now + 15 * dayMs), new Date(now + 15 * dayMs + 2 * hourMs));

            // COLLECTIBLES items - Bruce (4L), Clark (5L), Diana (6L)
            Item coll1 = new Item("Baseball Card", "Mickey Mantle rookie card",
                    4L, 3000, ItemCategory.COLLECTIBLES,
                    new Date(now + 16 * dayMs), new Date(now + 16 * dayMs + 2 * hourMs));
            Item coll2 = new Item("Comic Book", "First edition Superman",
                    5L, 5000, ItemCategory.COLLECTIBLES,
                    new Date(now + 17 * dayMs), new Date(now + 17 * dayMs + 2 * hourMs));
            Item coll3 = new Item("Vintage Toy", "Original Star Wars figure",
                    6L, 800, ItemCategory.COLLECTIBLES,
                    new Date(now + 18 * dayMs), new Date(now + 18 * dayMs + 2 * hourMs));

            // HOUSE items - Tony (1L), Uncle (2L), Peter (3L)
            Item house1 = new Item("Victorian House", "Historic 4-bedroom home",
                    1L, 250000, ItemCategory.HOUSE,
                    new Date(now + 19 * dayMs), new Date(now + 19 * dayMs + 2 * hourMs));
            Item house2 = new Item("Modern Villa", "Contemporary 5-bedroom villa",
                    2L, 400000, ItemCategory.HOUSE,
                    new Date(now + 20 * dayMs), new Date(now + 20 * dayMs + 2 * hourMs));
            Item house3 = new Item("Beach House", "Oceanfront property",
                    3L, 600000, ItemCategory.HOUSE,
                    new Date(now + 21 * dayMs), new Date(now + 21 * dayMs + 2 * hourMs));

            // APARTMENT items - Bruce (4L), Clark (5L), Diana (6L)
            Item apt1 = new Item("City Loft", "Downtown 2-bedroom loft",
                    4L, 150000, ItemCategory.APARTMENT,
                    new Date(now + 22 * dayMs), new Date(now + 22 * dayMs + 2 * hourMs));
            Item apt2 = new Item("Penthouse", "Luxury penthouse suite",
                    5L, 300000, ItemCategory.APARTMENT,
                    new Date(now + 23 * dayMs), new Date(now + 23 * dayMs + 2 * hourMs));
            Item apt3 = new Item("Studio Apartment", "Cozy downtown studio",
                    6L, 80000, ItemCategory.APARTMENT,
                    new Date(now + 24 * dayMs), new Date(now + 24 * dayMs + 2 * hourMs));

            // PLOT items - Tony (1L), Uncle (2L), Peter (3L)
            Item plot1 = new Item("Commercial Plot", "Prime commercial land",
                    1L, 100000, ItemCategory.PLOT,
                    new Date(now + 25 * dayMs), new Date(now + 25 * dayMs + 2 * hourMs));
            Item plot2 = new Item("Residential Plot", "Suburban building lot",
                    2L, 50000, ItemCategory.PLOT,
                    new Date(now + 26 * dayMs), new Date(now + 26 * dayMs + 2 * hourMs));
            Item plot3 = new Item("Farm Land", "Agricultural property",
                    3L, 75000, ItemCategory.PLOT,
                    new Date(now + 27 * dayMs), new Date(now + 27 * dayMs + 2 * hourMs));

            // ANTIQUES items - Bruce (4L), Clark (5L), Diana (6L)
            Item ant1 = new Item("Antique Clock", "18th century grandfather clock",
                    4L, 2500, ItemCategory.ANTIQUES,
                    new Date(now + 28 * dayMs), new Date(now + 28 * dayMs + 2 * hourMs));
            Item ant2 = new Item("Victorian Chair", "Ornate wooden armchair",
                    5L, 1200, ItemCategory.ANTIQUES,
                    new Date(now + 29 * dayMs), new Date(now + 29 * dayMs + 2 * hourMs));
            Item ant3 = new Item("Antique Vase", "Ming dynasty porcelain",
                    6L, 4000, ItemCategory.ANTIQUES,
                    new Date(now + 30 * dayMs), new Date(now + 30 * dayMs + 2 * hourMs));

            // BOOKS items - Tony (1L), Uncle (2L), Peter (3L)
            Item book1 = new Item("First Edition", "Rare Hemingway novel",
                    1L, 500, ItemCategory.BOOKS,
                    new Date(now + 31 * dayMs), new Date(now + 31 * dayMs + 2 * hourMs));
            Item book2 = new Item("Signed Copy", "Autographed Stephen King book",
                    2L, 300, ItemCategory.BOOKS,
                    new Date(now + 32 * dayMs), new Date(now + 32 * dayMs + 2 * hourMs));
            Item book3 = new Item("Ancient Manuscript", "Medieval illuminated text",
                    3L, 8000, ItemCategory.BOOKS,
                    new Date(now + 33 * dayMs), new Date(now + 33 * dayMs + 2 * hourMs));

            // SPORTS items - Bruce (4L), Clark (5L), Diana (6L)
            Item sport1 = new Item("Signed Baseball", "Babe Ruth autographed ball",
                    4L, 2000, ItemCategory.SPORTS,
                    new Date(now + 34 * dayMs), new Date(now + 34 * dayMs + 2 * hourMs));
            Item sport2 = new Item("Golf Clubs", "Professional titanium set",
                    5L, 800, ItemCategory.SPORTS,
                    new Date(now + 35 * dayMs), new Date(now + 35 * dayMs + 2 * hourMs));
            Item sport3 = new Item("Racing Bike", "Carbon fiber road bike",
                    6L, 3000, ItemCategory.SPORTS,
                    new Date(now + 36 * dayMs), new Date(now + 36 * dayMs + 2 * hourMs));

            // FASHION items - Tony (1L), Uncle (2L), Peter (3L)
            Item fash1 = new Item("Designer Dress", "Vintage Chanel gown",
                    1L, 1500, ItemCategory.FASHION,
                    new Date(now + 37 * dayMs), new Date(now + 37 * dayMs + 2 * hourMs));
            Item fash2 = new Item("Luxury Handbag", "Limited edition Hermès bag",
                    2L, 5000, ItemCategory.FASHION,
                    new Date(now + 38 * dayMs), new Date(now + 38 * dayMs + 2 * hourMs));
            Item fash3 = new Item("Designer Shoes", "Christian Louboutin heels",
                    3L, 800, ItemCategory.FASHION,
                    new Date(now + 39 * dayMs), new Date(now + 39 * dayMs + 2 * hourMs));

            // OTHER items - Bruce (4L), Clark (5L), Diana (6L)
            Item other1 = new Item("Mystery Box", "Unopened vintage collection",
                    4L, 100, ItemCategory.OTHER,
                    new Date(now + 40 * dayMs), new Date(now + 40 * dayMs + 2 * hourMs));
            Item other2 = new Item("Rare Mineral", "Geological specimen",
                    5L, 600, ItemCategory.OTHER,
                    new Date(now + 41 * dayMs), new Date(now + 41 * dayMs + 2 * hourMs));
            Item other3 = new Item("Vintage Camera", "Classic film camera",
                    6L, 400, ItemCategory.OTHER,
                    new Date(now + 42 * dayMs), new Date(now + 42 * dayMs + 2 * hourMs));

            itemService.saveItem(car1);
            itemService.saveItem(car2);
            itemService.saveItem(car3);
            itemService.saveItem(jew1);
            itemService.saveItem(jew2);
            itemService.saveItem(jew3);
            itemService.saveItem(elec1);
            itemService.saveItem(elec2);
            itemService.saveItem(elec3);
            itemService.saveItem(art1);
            itemService.saveItem(art2);
            itemService.saveItem(art3);
            itemService.saveItem(moto1);
            itemService.saveItem(moto2);
            itemService.saveItem(moto3);
            itemService.saveItem(coll1);
            itemService.saveItem(coll2);
            itemService.saveItem(coll3);
            itemService.saveItem(house1);
            itemService.saveItem(house2);
            itemService.saveItem(house3);
            itemService.saveItem(apt1);
            itemService.saveItem(apt2);
            itemService.saveItem(apt3);
            itemService.saveItem(plot1);
            itemService.saveItem(plot2);
            itemService.saveItem(plot3);
            itemService.saveItem(ant1);
            itemService.saveItem(ant2);
            itemService.saveItem(ant3);
            itemService.saveItem(book1);
            itemService.saveItem(book2);
            itemService.saveItem(book3);
            itemService.saveItem(sport1);
            itemService.saveItem(sport2);
            itemService.saveItem(sport3);
            itemService.saveItem(fash1);
            itemService.saveItem(fash2);
            itemService.saveItem(fash3);
            itemService.saveItem(other1);
            itemService.saveItem(other2);
            itemService.saveItem(other3);

            // Add subscribers to each item
            itemSubscriberService.register(car1, 4L);
            itemSubscriberService.register(car1, 5L);
            itemSubscriberService.register(car2, 5L);
            itemSubscriberService.register(car2, 6L);
            itemSubscriberService.register(car3, 6L);
            itemSubscriberService.register(car3, 4L);

            itemSubscriberService.register(jew1, 1L);
            itemSubscriberService.register(jew1, 2L);
            itemSubscriberService.register(jew2, 2L);
            itemSubscriberService.register(jew2, 3L);
            itemSubscriberService.register(jew3, 3L);
            itemSubscriberService.register(jew3, 1L);

            itemSubscriberService.register(elec1, 4L);
            itemSubscriberService.register(elec1, 5L);
            itemSubscriberService.register(elec2, 5L);
            itemSubscriberService.register(elec2, 6L);
            itemSubscriberService.register(elec3, 6L);
            itemSubscriberService.register(elec3, 4L);

            itemSubscriberService.register(art1, 1L);
            itemSubscriberService.register(art1, 2L);
            itemSubscriberService.register(art2, 2L);
            itemSubscriberService.register(art2, 3L);
            itemSubscriberService.register(art3, 3L);
            itemSubscriberService.register(art3, 2L);

            itemSubscriberService.register(moto1, 4L);
            itemSubscriberService.register(moto1, 5L);
            itemSubscriberService.register(moto2, 5L);
            itemSubscriberService.register(moto2, 6L);
            itemSubscriberService.register(moto3, 6L);
            itemSubscriberService.register(moto3, 4L);

            itemSubscriberService.register(coll1, 1L);
            itemSubscriberService.register(coll1, 2L);
            itemSubscriberService.register(coll2, 2L);
            itemSubscriberService.register(coll2, 3L);
            itemSubscriberService.register(coll3, 3L);
            itemSubscriberService.register(coll3, 1L);

            itemSubscriberService.register(house1, 4L);
            itemSubscriberService.register(house1, 5L);
            itemSubscriberService.register(house2, 5L);
            itemSubscriberService.register(house2, 6L);
            itemSubscriberService.register(house3, 6L);
            itemSubscriberService.register(house3, 4L);

            itemSubscriberService.register(apt1, 1L);
            itemSubscriberService.register(apt1, 2L);
            itemSubscriberService.register(apt2, 2L);
            itemSubscriberService.register(apt2, 3L);
            itemSubscriberService.register(apt3, 3L);
            itemSubscriberService.register(apt3, 1L);

            itemSubscriberService.register(plot1, 4L);
            itemSubscriberService.register(plot1, 5L);
            itemSubscriberService.register(plot2, 5L);
            itemSubscriberService.register(plot2, 6L);
            itemSubscriberService.register(plot3, 6L);
            itemSubscriberService.register(plot3, 4L);

            itemSubscriberService.register(ant1, 1L);
            itemSubscriberService.register(ant1, 2L);
            itemSubscriberService.register(ant2, 2L);
            itemSubscriberService.register(ant2, 3L);
            itemSubscriberService.register(ant3, 3L);
            itemSubscriberService.register(ant3, 1L);

            itemSubscriberService.register(book1, 4L);
            itemSubscriberService.register(book1, 5L);
            itemSubscriberService.register(book2, 5L);
            itemSubscriberService.register(book2, 6L);
            itemSubscriberService.register(book3, 6L);
            itemSubscriberService.register(book3, 4L);

            itemSubscriberService.register(sport1, 1L);
            itemSubscriberService.register(sport1, 2L);
            itemSubscriberService.register(sport2, 2L);
            itemSubscriberService.register(sport2, 3L);
            itemSubscriberService.register(sport3, 3L);
            itemSubscriberService.register(sport3, 1L);

            itemSubscriberService.register(fash1, 4L);
            itemSubscriberService.register(fash1, 5L);
            itemSubscriberService.register(fash2, 5L);
            itemSubscriberService.register(fash2, 6L);
            itemSubscriberService.register(fash3, 6L);
            itemSubscriberService.register(fash3, 4L);

            itemSubscriberService.register(other1, 1L);
            itemSubscriberService.register(other1, 2L);
            itemSubscriberService.register(other2, 2L);
            itemSubscriberService.register(other2, 3L);
            itemSubscriberService.register(other3, 3L);
            itemSubscriberService.register(other3, 1L);

            System.out.println("✅ Data seeding completed successfully!");
        };
    }
}