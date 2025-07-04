package kg.birge.bazar.authservice.components.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.core.StringRedisTemplate;

public class RedisResetPasswordStore extends AbstractRedisConfirmationStore {

    public RedisResetPasswordStore(int expiresIn, StringRedisTemplate redisTemplate, ObjectMapper objectMapper) {
        super(expiresIn, redisTemplate, objectMapper);
    }

    @Override
    public String getKeyPrefix() {
        return "reset_password_store:session_id_to_reset_data:";
    }
}
