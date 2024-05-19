package eqt.PfBitrixConverter.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeadsErrors {
  @Id
  @Column(name = "id", nullable = false)
  private Long id;

  @Column(name = "description", length = 2000)
  private String description;

  @Column(name = "timeAppeared", nullable = false)
  private String timeAppeared;
}
