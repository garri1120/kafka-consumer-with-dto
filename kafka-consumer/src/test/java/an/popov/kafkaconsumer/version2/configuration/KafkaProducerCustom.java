package an.popov.kafkaconsumer.version2.configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaProducerCustom <K, V> {

  private final KafkaTemplate<K, V> kafkaTemplate;
  @Transactional
  public void send(String topic, V object) {
    kafkaTemplate.executeInTransaction(kafkaOperations -> {
      kafkaOperations.send(topic, object);
      log.info("In topic = [{}] send message = {}", topic, object);
      return object;
    });
  }
}
