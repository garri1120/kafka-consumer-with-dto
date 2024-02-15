package an.popov.kafkaconsumer.deserializer;

import an.popov.PersonDtoForKafka.dto.PersonDto;
import an.popov.kafkaconsumer.model.CustomDeserializerDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Deserializer;
import org.springframework.kafka.support.serializer.DeserializationException;

@Slf4j
public class CustomDeserializer implements Deserializer<CustomDeserializerDto> {
  private final ObjectMapper objectMapper = new ObjectMapper();

  @Override
  public CustomDeserializerDto deserialize(String topic, byte[] data) {
    try {
      if (data == null) {
        log.error("data is null");
        return null;
      }
      PersonDto personDto = objectMapper.readValue(data, PersonDto.class);
      return CustomDeserializerDto
          .builder()
          .fullName(personDto.getFirstname() + " " + personDto.getLastname())
          .adult(personDto.getAge()>=18)
          .build();

    } catch (Exception e) {
      throw new DeserializationException(e.getMessage(), data, false, e);
    }
  }
}