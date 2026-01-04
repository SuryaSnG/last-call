# Kafka & Zookeeper to RabbitMQ Migration Analysis

## Current Architecture Analysis

### 1. Kafka & Zookeeper Usage

#### Infrastructure Setup (docker-compose.yml)
- **Zookeeper**: Manages Kafka cluster metadata and coordination
  - Port: 2181
  - Used by Kafka for broker coordination and configuration management
  
- **Kafka**: Message broker for asynchronous communication
  - Port: 9092 (external), 29092 (internal)
  - Single broker setup (BROKER_ID: 1)
  - Replication factor: 1 (development setup)

#### Services Using Kafka

**1. Item Service** (Producer)
- **Dependency**: `spring-kafka`
- **Purpose**: Publishes item creation events
- **Topic**: `schedule-item-jobs`
- **Message**: `ItemRoomCreationDto` (itemId, startingPrice, registrationClosingDate, auctionStartDate)
- **Trigger**: When new auction item is created

**2. Room Service** (Producer & Consumer)
- **Kafka dependency**: Currently commented out in pom.xml
- **Producer Role**: Schedules room closure
  - Topic: `schedule-room-close`
  - Message: roomId + auctionEndDate
- **Consumer Role**: Listens for room lifecycle events
  - Topics: `room-creation-with-item`, `room-activation`, `room-closure`
  - Actions: Create room, activate room, close room

**3. Scheduler Service** (Consumer & Producer)
- **Dependency**: `spring-kafka`
- **Consumer Role**: Receives scheduling requests
  - Topics: `schedule-item-jobs`, `schedule-room-close`
- **Producer Role**: Publishes scheduled events using Quartz jobs
  - Topics: `room-creation-with-item`, `room-activation`, `room-closure`
- **Integration**: Uses Quartz Scheduler to trigger time-based events

### 2. Message Flow Architecture

```
Item Created → Item Service → [schedule-item-jobs] → Scheduler Service
                                                           ↓
                                                    Quartz Jobs Created:
                                                    1. Room Creation Job
                                                    2. Room Activation Job
                                                           ↓
Room Creation Job Fires → [room-creation-with-item] → Room Service (creates room)
                                                           ↓
Room Activation Job Fires → [room-activation] → Room Service (activates room)
                                                           ↓
Room Service → [schedule-room-close] → Scheduler Service → Room Close Job
                                                           ↓
Room Close Job Fires → [room-closure] → Room Service (closes room)
```

### 3. Kafka Topics Summary

| Topic | Producer | Consumer | Message Type | Purpose |
|-------|----------|----------|--------------|---------|
| `schedule-item-jobs` | Item Service | Scheduler Service | ItemRoomCreationDto | Schedule room creation & activation |
| `schedule-room-close` | Room Service | Scheduler Service | Date | Schedule room closure |
| `room-creation-with-item` | Scheduler Service | Room Service | ItemRoomCreationDto | Create auction room |
| `room-activation` | Scheduler Service | Room Service | Long (itemId) | Activate auction room |
| `room-closure` | Scheduler Service | Room Service | Long (roomId) | Close auction room |

---

## RabbitMQ Migration Strategy

### Why RabbitMQ?

**Advantages over Kafka for this use case:**
1. **Simpler Setup**: No Zookeeper dependency, single container
2. **Lower Resource Usage**: Lighter footprint for microservices
3. **Better for Task Queues**: Designed for work distribution patterns
4. **Message Routing**: Flexible exchange types (direct, topic, fanout)
5. **Management UI**: Built-in web interface for monitoring
6. **Message TTL & DLQ**: Better support for message expiration and dead letter queues

**Trade-offs:**
- Lower throughput than Kafka (not an issue for this application)
- No built-in log retention (messages consumed are deleted)
- Less suitable for event streaming (this app uses task queue pattern)

### Migration Plan

#### Phase 1: Infrastructure Changes

**docker-compose.yml modifications:**

```yaml
# Remove Zookeeper and Kafka, add RabbitMQ
rabbitmq:
  image: rabbitmq:3.12-management-alpine
  container_name: rabbitmq
  ports:
    - "5672:5672"    # AMQP protocol
    - "15672:15672"  # Management UI
  environment:
    RABBITMQ_DEFAULT_USER: guest
    RABBITMQ_DEFAULT_PASS: guest
  healthcheck:
    test: ["CMD", "rabbitmq-diagnostics", "ping"]
    interval: 10s
    timeout: 5s
    retries: 5
  volumes:
    - rabbitmq_data:/var/lib/rabbitmq

volumes:
  rabbitmq_data:
```

**Update service dependencies:**
- Replace `kafka` dependency with `rabbitmq`
- Update environment variables from `KAFKA_BOOTSTRAP_SERVERS` to `RABBITMQ_HOST`, `RABBITMQ_PORT`

#### Phase 2: Code Changes

**1. Maven Dependencies (pom.xml)**

Replace:
```xml
<dependency>
    <groupId>org.springframework.kafka</groupId>
    <artifactId>spring-kafka</artifactId>
</dependency>
```

With:
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-amqp</artifactId>
</dependency>
```

**2. Configuration Changes**

**Item Service (application.properties):**
```properties
# Replace Kafka config
spring.rabbitmq.host=${RABBITMQ_HOST:localhost}
spring.rabbitmq.port=${RABBITMQ_PORT:5672}
spring.rabbitmq.username=${RABBITMQ_USER:guest}
spring.rabbitmq.password=${RABBITMQ_PASS:guest}
```

**3. Producer Code Migration**

**Item Service - KafkaClient.java → RabbitMqClient.java:**
```java
@Service
public class RabbitMqClient {
    
    @Autowired
    private RabbitTemplate rabbitTemplate;
    
    public void scheduleItemJobs(Item item) {
        ItemRoomCreationDto dto = new ItemRoomCreationDto(
            item.getId(), 
            item.getStartingPrice(), 
            item.getRegistrationClosingDate(), 
            item.getAuctionStartDate()
        );
        rabbitTemplate.convertAndSend("schedule-item-jobs", dto);
        System.out.println("✅ Scheduled jobs for item ID: " + item.getId());
    }
}
```

**Room Service - KafkaClient.java → RabbitMqClient.java:**
```java
@Service
public class RabbitMqClient {
    
    @Autowired
    private RabbitTemplate rabbitTemplate;
    
    public void scheduleRoomClose(Long roomId, Date auctionEndDate) {
        Map<String, Object> message = Map.of(
            "roomId", roomId,
            "auctionEndDate", auctionEndDate
        );
        rabbitTemplate.convertAndSend("schedule-room-close", message);
        System.out.println("✅ Scheduled jobs for room ID: " + roomId);
    }
}
```

**4. Consumer Code Migration**

**Room Service - KafkaListener.java → RabbitMqListener.java:**
```java
@Service
public class RabbitMqListener {
    
    private final RoomService roomService;
    private final RoomRepository roomRepository;
    
    @RabbitListener(queues = "room-creation-with-item")
    public void handleRoomCreationWithItem(ItemRoomCreationDto itemData) {
        try {
            System.out.println("📥 Creating room for item ID: " + itemData.getItemId());
            
            if (roomRepository.findByItemId(itemData.getItemId()).isPresent()) {
                logger.warn("Room already exists for item ID: {}", itemData.getItemId());
                return;
            }
            
            roomService.createRoom(
                itemData.getItemId(), 
                itemData.getStartingPrice(), 
                itemData.getAuctionStartDate()
            );
            
            System.out.println("✅ Room created for item id: " + itemData.getItemId());
        } catch (Exception e) {
            logger.error("Error creating room: {}", e.getMessage());
        }
    }
    
    @RabbitListener(queues = "room-activation")
    public void handleRoomActivation(Long itemId) {
        try {
            System.out.println("🏠 Activating room for item ID: " + itemId);
            roomService.activateRoom(itemId);
            System.out.println("✅ Room activated for item ID: " + itemId);
        } catch (Exception e) {
            logger.error("Error activating room: {}", e.getMessage());
        }
    }
    
    @RabbitListener(queues = "room-closure")
    public void handleRoomClosure(Long roomId) {
        try {
            System.out.println("🏠 Closing room ID: " + roomId);
            roomService.closeRoom(roomId);
            System.out.println("✅ Room closed for room ID: " + roomId);
        } catch (Exception e) {
            logger.error("Error closing room: {}", e.getMessage());
        }
    }
}
```

**Scheduler Service - KafkaListener.java → RabbitMqListener.java:**
```java
@Component
public class RabbitMqListener {
    
    @Autowired
    private SchedulerService schedulerService;
    
    @RabbitListener(queues = "schedule-room-close")
    public void handleScheduleRoomClose(Map<String, Object> message) throws SchedulerException {
        Long roomId = ((Number) message.get("roomId")).longValue();
        Date auctionEndDate = (Date) message.get("auctionEndDate");
        
        System.out.println("📥 Received message for room ID: " + roomId);
        schedulerService.scheduleRoomCloseJob(roomId, auctionEndDate);
        System.out.println("✅ Scheduled jobs for room ID: " + roomId);
    }
    
    @RabbitListener(queues = "schedule-item-jobs")
    public void handleScheduleItemJobs(ItemRoomCreationDto dto) throws SchedulerException {
        System.out.println("📥 Received schedule-item-jobs for item ID: " + dto.getItemId());
        schedulerService.scheduleRoomCreationJob(dto);
        schedulerService.scheduleRoomActivationJob(dto.getItemId(), dto.getAuctionStartDate());
        System.out.println("✅ Scheduled jobs for item ID: " + dto.getItemId());
    }
}
```

**5. Quartz Job Updates**

**RoomCreationJob.java:**
```java
@Component
public class RoomCreationJob implements Job {
    
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            ApplicationContext appContext = (ApplicationContext) 
                context.getScheduler().getContext().get("applicationContext");
            RabbitTemplate rabbitTemplate = appContext.getBean(RabbitTemplate.class);
            
            Long itemId = context.getJobDetail().getJobDataMap().getLong("itemId");
            Double startingPrice = context.getJobDetail().getJobDataMap().getDouble("startingPrice");
            Long auctionStartDateLong = context.getJobDetail().getJobDataMap().getLong("auctionStartDate");
            
            ItemRoomCreationDto dto = new ItemRoomCreationDto(
                itemId, startingPrice, null, new Date(auctionStartDateLong)
            );
            
            rabbitTemplate.convertAndSend("room-creation-with-item", dto);
            System.out.println("✅ Room creation request sent for item ID: " + itemId);
        } catch (Exception e) {
            logger.error("Error executing room creation job: {}", e.getMessage());
            throw new JobExecutionException(e);
        }
    }
}
```

**RoomActivationJob.java:**
```java
@Component
public class RoomActivationJob implements Job {
    
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            ApplicationContext appContext = (ApplicationContext) 
                context.getScheduler().getContext().get("applicationContext");
            RabbitTemplate rabbitTemplate = appContext.getBean(RabbitTemplate.class);
            
            Long itemId = context.getJobDetail().getJobDataMap().getLong("itemId");
            
            rabbitTemplate.convertAndSend("room-activation", itemId);
            System.out.println("✅ Room activation sent for item ID: " + itemId);
        } catch (Exception e) {
            logger.error("Error executing room activation job: {}", e.getMessage());
            throw new JobExecutionException(e);
        }
    }
}
```

**RoomCloseJob.java:**
```java
@Component
public class RoomCloseJob implements Job {
    
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            ApplicationContext appContext = (ApplicationContext) 
                context.getScheduler().getContext().get("applicationContext");
            RabbitTemplate rabbitTemplate = appContext.getBean(RabbitTemplate.class);
            
            Long roomId = context.getJobDetail().getJobDataMap().getLong("roomId");
            
            rabbitTemplate.convertAndSend("room-closure", roomId);
            System.out.println("✅ Room closure sent for room ID: " + roomId);
        } catch (Exception e) {
            logger.error("Error executing room close job: {}", e.getMessage());
            throw new JobExecutionException(e);
        }
    }
}
```

**6. RabbitMQ Configuration Class**

Create in each service that uses RabbitMQ:

```java
@Configuration
public class RabbitMqConfig {
    
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
    
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }
    
    // Queue declarations
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
```

#### Phase 3: Testing Strategy

1. **Unit Tests**: Update message producer/consumer tests
2. **Integration Tests**: Test with RabbitMQ container
3. **End-to-End Tests**: Verify complete auction lifecycle
4. **Performance Tests**: Ensure message delivery times are acceptable

#### Phase 4: Deployment

1. Update docker-compose.yml
2. Deploy RabbitMQ container
3. Deploy updated services (scheduler → room → item)
4. Monitor RabbitMQ management UI (http://localhost:15672)
5. Remove Kafka and Zookeeper containers

---

## Key Differences: Kafka vs RabbitMQ

| Aspect | Kafka | RabbitMQ |
|--------|-------|----------|
| **Architecture** | Distributed log | Message broker |
| **Dependencies** | Requires Zookeeper | Standalone |
| **Message Model** | Pub/Sub (topics) | Queue + Exchange |
| **Persistence** | Log-based, retained | Queue-based, consumed |
| **Ordering** | Partition-level | Queue-level |
| **Throughput** | Very high | Moderate |
| **Latency** | Low | Very low |
| **Use Case** | Event streaming | Task queues, RPC |
| **Management** | CLI/3rd party | Built-in UI |

---

## Recommendations

1. **For this project**: RabbitMQ is more suitable
   - Task queue pattern (not event streaming)
   - Lower infrastructure complexity
   - Easier monitoring and debugging
   - Sufficient performance for auction system

2. **Migration Effort**: Medium (2-3 days)
   - Replace dependencies
   - Update configuration
   - Refactor producer/consumer code
   - Test thoroughly

3. **Future Considerations**:
   - Add Dead Letter Queues (DLQ) for failed messages
   - Implement message retry logic
   - Add monitoring/alerting for queue depths
   - Consider message TTL for time-sensitive events
