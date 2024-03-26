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
public class CallTrackingLeads {

  @Id
  @Column(name = "id", nullable = false)
  private Long id;

  @Column(name = "firstName")
  private String firstName;

  @Column(name = "email")
  private String email;

  @Column(name = "callTrackingLeadPhone")
  private String callTrackingLeadPhone;

  @Column(name = "callDuration")
  private int callDuration;

  @Column(name = "createdOnBitrix")
  private boolean createdOnBitrix;
}
