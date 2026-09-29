package persional.jobfinder_api.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import persional.jobfinder_api.dto.respones.ProfileRespone;
import persional.jobfinder_api.dto.respones.ResumeRespone;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableCaching
public class RedisConfig {

    @Value(("${spring.data.redis.host}"))
    private String redisHost;

    @Value(("${spring.data.redis.port}"))
    private int redisPort;

    //
    @Bean
    public RedisStandaloneConfiguration redisStandaloneConfiguration() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(redisHost);
        configuration.setPort(redisPort);
        return  configuration;
    }

    @Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        return new JedisConnectionFactory(redisStandaloneConfiguration());
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(jedisConnectionFactory());
        //Key
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        //Value
        redisTemplate.setValueSerializer(this.jackson2JsonRedisSerializer());
        //value hashmap
        redisTemplate.setHashValueSerializer(this.jackson2JsonRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());

        return redisTemplate;
    }


    /**
     *
     * Configure Jackson2JsonRedisSerializer with custom ObjectMapper
     */
    protected Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        return new Jackson2JsonRedisSerializer<>(objectMapper,Object.class);
    }

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {

        RedisSerializer<String> redisSerializer = new StringRedisSerializer();


        // --- Default configuration ---
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(10)) // Default TTL
                .serializeKeysWith(RedisSerializationContext
                        .SerializationPair
                        .fromSerializer(redisSerializer))
                .serializeValuesWith(RedisSerializationContext
                        .SerializationPair
                        .fromSerializer(this.jackson2JsonRedisSerializer()))
                .disableCachingNullValues();


        // --- Per-cache configurations ---
        Map<String, RedisCacheConfiguration> cacheConfigs = new HashMap<>();


        // userProfiles cache
        cacheConfigs.put("userProfiles",
                config
                        .entryTtl(Duration.ofMinutes(60))
                        .serializeKeysWith(RedisSerializationContext
                                .SerializationPair
                                .fromSerializer(
                                        redisSerializer
                                ))
                        .serializeValuesWith(RedisSerializationContext
                                .SerializationPair
                                .fromSerializer(
                                        new Jackson2JsonRedisSerializer<>(ProfileRespone.class)
                                ))
                        .disableCachingNullValues()
        );


        // jobresponse cache
        cacheConfigs.put("JobResponse",
                config
                        .entryTtl(Duration.ofMinutes(60))
                        .serializeKeysWith(RedisSerializationContext
                                .SerializationPair
                                .fromSerializer(
                                        redisSerializer
                                ))
                        .serializeValuesWith(RedisSerializationContext
                                .SerializationPair
                                .fromSerializer(
                                        this.jackson2JsonRedisSerializer()
                                ))
                        .disableCachingNullValues()
        );


        // jobs cache
        cacheConfigs.put("jobs",
                config
                        .entryTtl(Duration.ofMinutes(60))
                        .serializeKeysWith(RedisSerializationContext
                                .SerializationPair
                                .fromSerializer(redisSerializer))
                        .serializeValuesWith(RedisSerializationContext
                                .SerializationPair
                                .fromSerializer(this.jackson2JsonRedisSerializer()))
                        .disableCachingNullValues()
        );

        // resum cache
        cacheConfigs.put("resumes",
                config
                        .entryTtl(Duration.ofMinutes(10))
                        .serializeKeysWith(RedisSerializationContext
                                .SerializationPair
                                .fromSerializer(
                                        redisSerializer
                                ))
                        .serializeValuesWith(RedisSerializationContext
                                .SerializationPair
                                .fromSerializer(
                                        new Jackson2JsonRedisSerializer<>(ResumeRespone.class)
                                ))
                        .disableCachingNullValues()
        );


        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(config) // default config applied to caches without specific config
                .withInitialCacheConfigurations(cacheConfigs) // specific cache configurations
                .transactionAware() //if action to Db fails, the cache will roll back. cache will constant old data
                .build();
    }

    @Bean
    public KeyGenerator userProfileKeyGenerator(){
        return (target, method, params) -> {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            return auth.getName();
        };
    }
}
