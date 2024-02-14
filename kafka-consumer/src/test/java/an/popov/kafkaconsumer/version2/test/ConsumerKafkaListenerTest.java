package an.popov.kafkaconsumer.version2.test;

import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

import an.popov.PersonDtoForKafka.dto.PersonDto;
import an.popov.kafkaconsumer.component.ConsumerKafkaListener;
import an.popov.kafkaconsumer.config.ConsumerConfig;
import an.popov.kafkaconsumer.container.KafkaContainerTest;
import an.popov.kafkaconsumer.version2.configuration.ConfigKafkaTemplate;
import an.popov.kafkaconsumer.version2.configuration.KafkaProducerCustom;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.junit.jupiter.Testcontainers;


@SpringBootTest(classes = {ConsumerConfig.class, ConsumerKafkaListener.class})
@ContextConfiguration(classes = {KafkaContainerTest.class, ConfigKafkaTemplate.class,
    KafkaProducerCustom.class})
@Testcontainers(disabledWithoutDocker = true)
@TestPropertySource(locations = "classpath:application.properties")
class ConsumerKafkaListenerTest {

  @Value("${name.topic}")
  private String nameTopic;

  @Autowired
  private KafkaProducerCustom<String, PersonDto> producerCustom;

  @SpyBean
  private ConsumerKafkaListener consumerKafkaListener;

  @Captor
  private ArgumentCaptor<PersonDto> captor;


  @Test
  void getPersonDtoFromKafkaTest() {
    PersonDto personDto = PersonDto.builder().firstname("Bob").lastname("White").age(999).build();

    producerCustom.send(nameTopic, personDto);

    verify(consumerKafkaListener, timeout(5000)).getPersonDtoFromKafka(captor.capture());

    Assertions.assertEquals(personDto.getFirstname(), captor.getValue().getFirstname());
  }
}
