package an.popov.kafkaconsumer.config;

import an.popov.PersonDtoForKafka.dto.PersonDto;
import com.google.gson.Gson;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableKafka
public class ConsumerConfig {

  @Value(value = "${spring.kafka.consumer.bootstrap-servers}")
  private String bootstrapAddress;

  @Value(value = "${spring.kafka.consumer.group-id}")
  private String groupId;

  @Bean
  public ConsumerFactory<String, PersonDto> consumerFactory() {
    Map<String, Object> config = new HashMap<>();
    config.put(org.apache.kafka.clients.consumer.ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,
        bootstrapAddress);
    config.put(org.apache.kafka.clients.consumer.ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
        StringDeserializer.class);
    config.put(org.apache.kafka.clients.consumer.ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
            JsonDeserializer.class);
    config.put(org.apache.kafka.clients.consumer.ConsumerConfig.GROUP_ID_CONFIG, groupId);
    return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(), new JsonDeserializer<>(PersonDto.class));
  }

  @Bean
  public ConcurrentKafkaListenerContainerFactory<String, PersonDto> kafkaListenerContainerFactory() {
    ConcurrentKafkaListenerContainerFactory<String, PersonDto> concurrentKafkaListenerContainerFactory = new ConcurrentKafkaListenerContainerFactory<>();
    concurrentKafkaListenerContainerFactory.setConsumerFactory(consumerFactory());
    concurrentKafkaListenerContainerFactory.setMissingTopicsFatal(false);
    return concurrentKafkaListenerContainerFactory;
  }

  @Bean
  public Gson gsonJsonConverter() {
    return new Gson();
  }
}