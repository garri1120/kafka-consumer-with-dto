package an.popov.kafkaconsumer.version3;



import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

import an.popov.PersonDtoForKafka.dto.PersonDto;
import an.popov.kafkaconsumer.component.ConsumerKafkaListener;
import an.popov.kafkaconsumer.component.ConsumerKafkaListenerDeserializer;
import an.popov.kafkaconsumer.config.ConsumerConfigWithCustomDeserializer;
import an.popov.kafkaconsumer.container.KafkaContainerTest;
import an.popov.kafkaconsumer.kafkaProducerCustom.KafkaProducerCustom;
import an.popov.kafkaconsumer.model.CustomDeserializerDto;
import an.popov.kafkaconsumer.producerConfig.ConfigProducer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
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

@SpringBootTest(classes = {ConsumerConfigWithCustomDeserializer.class, ConsumerKafkaListenerDeserializer.class})
@ContextConfiguration(classes = {KafkaContainerTest.class, ConfigProducer.class,
    KafkaProducerCustom.class})
@Testcontainers(disabledWithoutDocker = true)
@TestPropertySource(locations = "classpath:application.properties")
class ConsumerKafkaListenerDeserializerTest {

  @Value("${name.topic}")
  private String nameTopic;

  @Autowired
  private KafkaProducerCustom<String, PersonDto> producerCustom;

  @SpyBean
  private ConsumerKafkaListenerDeserializer consumerKafkaListener;

  @Captor
  private ArgumentCaptor<ConsumerRecord<String, CustomDeserializerDto>> captor;


  @Test
  void getPersonDtoFromKafkaTest() {
    PersonDto personDto = PersonDto.builder().firstname("Bob").lastname("White").age(18).build();

    producerCustom.send(nameTopic, personDto);

    verify(consumerKafkaListener, timeout(5000)).getPersonDtoFromKafkaCustomDeserializer(captor.capture());

    CustomDeserializerDto result = captor.getValue().value();

    Assertions.assertAll(() -> {
      assertTrue(result.getAdult());
      assertEquals(personDto.getFirstname() + " " + personDto.getLastname(), result.getFullName());
    });
  }
}
