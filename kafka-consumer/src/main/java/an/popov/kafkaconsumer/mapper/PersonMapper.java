package an.popov.kafkaconsumer.mapper;

import an.popov.PersonDtoForKafka.dto.PersonDto;
import an.popov.kafkaconsumer.model.Person;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = ComponentModel.SPRING)
public interface PersonMapper {
  Person toPersonFromPersonDto(PersonDto personDto);
}
