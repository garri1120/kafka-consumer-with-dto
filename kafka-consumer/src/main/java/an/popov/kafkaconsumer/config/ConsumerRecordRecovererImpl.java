package an.popov.kafkaconsumer.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.listener.ConsumerRecordRecoverer;

// погуглить
@Slf4j
public class ConsumerRecordRecovererImpl implements ConsumerRecordRecoverer {
    @Override
    public void accept(ConsumerRecord<?, ?> consumerRecord, Exception e) {
      log.info(e.getMessage() +"------------------ " +  consumerRecord.value().toString());
    }
}
