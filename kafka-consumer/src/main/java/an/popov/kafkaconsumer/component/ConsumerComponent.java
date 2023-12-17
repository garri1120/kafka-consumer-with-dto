package an.popov.kafkaconsumer.component;

import an.popov.PersonDtoForKafka.dto.PersonDto;
import an.popov.kafkaconsumer.service.ConsumerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ConsumerComponent {

  private final ConsumerService consumerService;

  @KafkaListener(topics = "Andrew_Gson", containerFactory = "kafkaListenerContainerFactory")
  public void savePersonFromKafka(PersonDto personDto) {
    log.info("Получено из кафки сообщение personDtoString {}", personDto);
    consumerService.personSave(personDto);
    log.info("Успешно сохранено сообщение из кафки в БД personDtoString {}", personDto);
  }
}
