package an.popov.kafkaconsumer.version1.test;


import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import an.popov.PersonDtoForKafka.dto.PersonDto;
import an.popov.kafkaconsumer.component.ConsumerKafkaListener;
import an.popov.kafkaconsumer.config.ConsumerConfig;
import an.popov.kafkaconsumer.container.KafkaContainerTest;
import an.popov.kafkaconsumer.version1.configuration.Config;
import java.util.concurrent.ExecutionException;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
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

@Slf4j
@SpringBootTest(classes = {ConsumerConfig.class, ConsumerKafkaListener.class})
@ContextConfiguration(classes = {KafkaContainerTest.class, Config.class})
@Testcontainers(disabledWithoutDocker = true)
@TestPropertySource(locations = "classpath:application.properties")
class ConsumerKafkaListenerTest {

  @Value("${name.topic}")
  private String nameTopic;

  @Autowired
  private Producer<String, PersonDto> producer;


  @SpyBean
  private ConsumerKafkaListener consumerKafkaListener;

  @Captor
  private ArgumentCaptor<PersonDto> captor;

  @Test
  void getPersonDtoFromKafkaTest() throws ExecutionException, InterruptedException {

    PersonDto personDto = PersonDto.builder().firstname("Bob").lastname("White").age(999).build();

    ProducerRecord<String, PersonDto> record = new ProducerRecord<>(nameTopic, personDto);
    producer.initTransactions();
    producer.beginTransaction();
    try {
      RecordMetadata metadataFuture = producer.send(record).get();
      log.error("Message send topic = {}, message = {} , partition = {}", metadataFuture.topic(), personDto, metadataFuture.partition());
      producer.commitTransaction();
      producer.close();
    } catch (Exception e) {
      producer.abortTransaction();
      throw e;
    }


    verify(consumerKafkaListener, timeout(5000)).getPersonDtoFromKafka(captor.capture());

    Assertions.assertEquals(personDto.getFirstname(), captor.getValue().getFirstname());
  }

}
