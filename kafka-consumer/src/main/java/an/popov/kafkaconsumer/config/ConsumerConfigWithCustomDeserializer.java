package an.popov.kafkaconsumer.config;

import an.popov.PersonDtoForKafka.dto.PersonDto;
import an.popov.kafkaconsumer.deserializer.CustomDeserializer;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

@Configuration
@EnableKafka
public class ConsumerConfigWithCustomDeserializer {

    @Value(value = "${spring.kafka.consumer.bootstrap-servers}")
    private String bootstrapAddress;

    @Value(value = "${consumer.group.deserializer}")
    private String groupId;

    @Bean
    public ConsumerFactory<String, PersonDto> consumerFactoryDeserializer() {
      Map<String, Object> config = new HashMap<>();
      config.put(org.apache.kafka.clients.consumer.ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
          bootstrapAddress);

      config.put(org.apache.kafka.clients.consumer.ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
          ErrorHandlingDeserializer.class);
      config.put(org.apache.kafka.clients.consumer.ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
          ErrorHandlingDeserializer.class);

      config.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, StringDeserializer.class);
      config.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, CustomDeserializer.class);

      config.put(JsonDeserializer.VALUE_DEFAULT_TYPE, "an.popov.PersonDtoForKafka.dto.PersonDto");

      config.put(org.apache.kafka.clients.consumer.ConsumerConfig.GROUP_ID_CONFIG, groupId);
      return new DefaultKafkaConsumerFactory<>(config);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, PersonDto> kafkaListenerContainerFactoryDeserializer() {
      ConcurrentKafkaListenerContainerFactory<String, PersonDto> concurrentKafkaListenerContainerFactory = new ConcurrentKafkaListenerContainerFactory<>();
      concurrentKafkaListenerContainerFactory.setConsumerFactory(consumerFactoryDeserializer());
      concurrentKafkaListenerContainerFactory.setMissingTopicsFatal(false);
      return concurrentKafkaListenerContainerFactory;
    }
}
