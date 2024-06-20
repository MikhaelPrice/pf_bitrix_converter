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
public class WhatsappLeads {
  @Id
  @Column(name = "id", nullable = false)
  private Long id;

  @Column(name = "phone")
  private String phone;

  @Column(name = "whatsappLeadStatus")
  private String whatsappLeadStatus;

  @Column(name = "pfAgentId")
  private String pfAgentId;

  @Column(name = "comment", length = 2000)
  private String comment;

  @Column(name = "propertyReference")
  private String propertyReference;

  @Column(name = "createdOnBitrix")
  private boolean createdOnBitrix;
}
