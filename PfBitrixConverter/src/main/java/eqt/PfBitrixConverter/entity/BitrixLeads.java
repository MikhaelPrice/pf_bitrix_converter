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
public class BitrixLeads {
  @Id
  @Column(name = "id", nullable = false)
  private Long id;

  @Column(name = "title")
  private String title;

  @Column(name = "phone")
  private String phone;

  @Column(name = "name")
  private String name;

  @Column(name = "comment")
  private String comment;

  @Column(name = "updatedOnBitrix")
  private boolean createdOnBitrix;

  @Column(name = "responsible")
  private int assignee;
}
