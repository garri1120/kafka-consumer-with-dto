package an.popov.kafkaconsumer.container;

import org.springframework.context.annotation.Configuration;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

@Configuration
public class KafkaContainerTest {
  @Container
  static KafkaContainer kafkaContainer = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:6.2.1"));

  static {
    kafkaContainer.start();
    System.setProperty("spring.kafka.consumer.bootstrap-servers", kafkaContainer.getBootstrapServers());
  }
}
