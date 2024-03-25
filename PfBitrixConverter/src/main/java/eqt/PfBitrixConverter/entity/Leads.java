package eqt.PfBitrixConverter.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Leads {

  @Id
  @Column(name = "id", nullable = false)
  private Long id;

  @Column(name = "firstName")
  private String firstName;

  @Column(name = "email")
  private String email;

  @Column(name = "phone")
  private String phone;

  @Column(name = "comment", length = 2000)
  private String comment;

  @Column(name = "createdOnBitrix")
  private boolean createdOnBitrix;

}
