package an.popov.kafkaconsumer.service;

import an.popov.PersonDtoForKafka.dto.PersonDto;
import an.popov.kafkaconsumer.mapper.PersonMapper;
import an.popov.kafkaconsumer.model.Person;
import an.popov.kafkaconsumer.repository.PersonRepository;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConsumerService {

  private final PersonMapper personMapper;

  private final Gson gson;

  private final PersonRepository personRepository;

  public void personSave(PersonDto personDto) {
    log.info("Получен запрос на сохранение в БД personDtoString {}", personDto);
    Person person = personMapper.toPersonFromPersonDto(personDto);
    personRepository.save(person);
    log.info("Сохранен в БД person {}", person);
  }
}
