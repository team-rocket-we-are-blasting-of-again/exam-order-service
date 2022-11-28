package com.teamrocket.orderservice.config;

import com.teamrocket.orderservice.dto.OrderDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;

@Configuration
@RequiredArgsConstructor
public class KafkaConsumerConfig {

    /*private final KafkaUtil kafkaUtil;

    @Bean
    public ConsumerFactory<String, OrderDTO> messageConsumerFactory() {
        return this.kafkaUtil.createClassConsumerFactory(OrderDTO.class);
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, OrderDTO>> messageFactory() {
        ConcurrentKafkaListenerContainerFactory<String, OrderDTO> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(this.messageConsumerFactory());
        return factory;
    }*/
}
