package an.popov.kafkaconsumer.configuration;

import an.popov.PersonDtoForKafka.dto.PersonDto;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.support.serializer.JsonSerializer;


@Configuration
public class Config {

  @Value("${name.topic}")
  private String nameTopic;

  private final String bootstrapAddress = System.getProperty(
      "spring.kafka.consumer.bootstrap-servers");

  ;

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
  public Producer<String, PersonDto> producer() {
    Properties producerProps = new Properties();
    producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
    producerProps.put(
        ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
        StringSerializer.class);
    producerProps.put(
        ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
        JsonSerializer.class);
    producerProps.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, "my-transactional-id");
    producerProps.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, "true");
    return new KafkaProducer<>(producerProps);
  }
}
