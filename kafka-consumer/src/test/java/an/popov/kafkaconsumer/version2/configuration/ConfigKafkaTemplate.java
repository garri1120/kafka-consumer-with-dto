package an.popov.kafkaconsumer.version2.configuration;

import an.popov.PersonDtoForKafka.dto.PersonDto;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;


@Configuration
public class ConfigKafkaTemplate {

  @Value("${name.topic}")
  private String nameTopic;

  private final String bootstrapAddress = System.getProperty("spring.kafka.consumer.bootstrap-servers");

  @Bean
  public KafkaAdmin kafkaAdmin() {
    Map<String, Object> configs = new HashMap<>();
    configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
    return new KafkaAdmin(configs);
  }

  @Bean
  public NewTopic topic1() {
    return new NewTopic(nameTopic, 1, (short) 1);
  }

  @Bean
  public ProducerFactory<String, PersonDto> producerFactory() {
    Map<String, Object> configProps = new HashMap<>();
    configProps.put(
        ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
        bootstrapAddress);
    configProps.put(
        ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
        StringSerializer.class);
    configProps.put(
        ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
        JsonSerializer.class);
    configProps.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, "my-transactional-id"); // Установка идентификатора транзакции
    configProps.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true); // Включение идемпотентности
    return new DefaultKafkaProducerFactory<>(configProps);
  }

  @Bean
  public KafkaTemplate<String, PersonDto> kafkaTemplate() {
    return new KafkaTemplate<>(producerFactory());
  }
}
