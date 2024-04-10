# Introduce

- Spring Boot + Web Socket 을 통새 사용해 클라이언트와 서버 간 실시간 통신 구현
- Stomp, Redis pub/sub 사용

</br>

# Install Dependency

build.gradle 에 Spring JPA, MySQL, JSP, Web Socket 관련 의존성을 설정함

```gradle

// JPA
implementation 'org.springframework.boot:spring-boot-starter-data-jpa'

// MySql
implementation 'org.springframework.boot:spring-boot-starter-jdbc'
runtimeOnly 'com.mysql:mysql-connector-j'

implementation "org.apache.tomcat.embed:tomcat-embed-jasper"

// Redis
implementation 'org.springframework.boot:spring-boot-starter-data-redis'

//자바 역직렬화 문제 해결 패키지
implementation 'com.fasterxml.jackson.datatype:jackson-datatype-jsr310'
implementation 'com.fasterxml.jackson.core:jackson-databind'

implementation 'org.webjars:webjars-locator-core'
implementation 'org.webjars:sockjs-client:1.0.2'
implementation 'org.webjars:stomp-websocket:2.3.3'
implementation 'org.webjars:bootstrap:3.3.7'
implementation 'org.webjars:jquery:3.1.1-1'

```

</br>

# resources/application.yml

- JSP 를 위한 경로 설정
- JPA 설정
- Redis 연결을 위한 Host, Port 설정

```yml
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://${DB_URL}/websocket?serverTimezone=Asia/Seoul
    username: ${DB_USER}
  mvc:
    view:
      suffix: .jsp

  jpa:
    hibernate:
      ddl-auto: none
    properties:
      hibernate:
        format_sql: true

  data:
    redis:
      host: localhost
      port: 6379
```

</br>

# Stomp

Stomp 를 사용한 Web Socket 기능 구현

```java
@Configuration
@RequiredArgsConstructor
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {


    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/gs-guide-websocket")
                .setAllowedOrigins("*");
    }
}
```

```java
@Controller
@RequiredArgsConstructor
public class GreetingController {

    private final ChatService chatService;

    @MessageMapping("/hello/{chatroomId}")
    @SendTo("/topic/greetings/{chatroomId}")
    public Greeting greeting(@DestinationVariable String chatroomId, HelloMessage message) throws Exception {
        // 전처리를 거쳐도 됨. DB, Redis 저장 등등

        return new Greeting(" " + HtmlUtils.htmlEscape(message.getName()));
    }

    @GetMapping("/main/{memberId}")
    public ModelAndView showMain(@PathVariable Long memberId) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/main");
        mv.addObject("chatRooms", chatService.getChatRoomList(memberId));
        return mv;
    }

    @GetMapping("/chatroom/{chatroomId}")
    public ModelAndView getChatRoomList(@PathVariable Long chatroomId) {
        ModelAndView mv = new ModelAndView();
        mv.setViewName("/chatroom");
        mv.addObject("chatroomId", chatroomId);
        return mv;
    }
}
```

</br>

# Redis Pub/Sub

Redis 를 사용한 Web Socket 기능 구현

```java
@Configuration
public class RedisConfig {

    @Value("${spring.data.redis.port}")
    private int port;

    @Value("${spring.data.redis.host}")
    private String host;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(host, port);
    }

    @Bean
    public RedisTemplate<?, ?> redisTemplate() {
        RedisTemplate<?, ?> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());   //connection
        redisTemplate.setKeySerializer(new StringRedisSerializer());    // key
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(String.class)); //Java Obj <-> JSON -> String Value
        return redisTemplate;
    }

    /**
     * Redis pub/sub 메시지 처리 Listener
     */
    @Bean
    public RedisMessageListenerContainer redisMessageListener() {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory());
        return container;
    }

}
```

```java
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/redis/pubsub")
public class PubSubController {

    private final RedisPubService redisPubService;

    @PostMapping("/send/{channel}")
    public void sendMessage(@PathVariable String channel, @RequestBody MessageDto messageDto) {
        log.info("메시지를 전송하고자 하는 채널 = {}", channel);
        redisPubService.pubMsgChannel(channel, messageDto);
    }
}
```

</br>

# Blog

https://velog.io/@gudtjr2949/Spring-Boot-Web-Socket-STOMP
