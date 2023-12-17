package an.popov.kafkaconsumer.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Person {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;
  private String firstname;
  private String lastname;
  private int age;
}