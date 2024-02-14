package an.popov.kafkaconsumer.component;

import an.popov.PersonDtoForKafka.dto.PersonDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ConsumerKafkaListener {

  @KafkaListener(topics = "${name.topic}", containerFactory = "kafkaListenerContainerFactory")
  public void getPersonDtoFromKafka(PersonDto personDto) {
    log.info("Successfully received message from kafka {}", personDto);

  }
}
