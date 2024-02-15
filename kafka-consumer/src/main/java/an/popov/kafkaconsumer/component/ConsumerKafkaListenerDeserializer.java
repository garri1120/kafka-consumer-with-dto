package an.popov.kafkaconsumer.component;


import an.popov.kafkaconsumer.model.CustomDeserializerDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ConsumerKafkaListenerDeserializer {
  @KafkaListener(topics = "${name.topic.deserializer}", containerFactory = "kafkaListenerContainerFactoryDeserializer")
  public void getPersonDtoFromKafkaCustomDeserializer(ConsumerRecord<String, CustomDeserializerDto> consumerRecord) {

    CustomDeserializerDto customDeserializerDto = consumerRecord.value();
    log.info("Successfully received message from kafka. Topic = [{}], message = {}", consumerRecord.topic(), customDeserializerDto);

  }
}
