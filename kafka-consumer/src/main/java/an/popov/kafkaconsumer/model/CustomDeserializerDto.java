package an.popov.kafkaconsumer.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomDeserializerDto {
    private String fullName;

    private Boolean adult;
}
