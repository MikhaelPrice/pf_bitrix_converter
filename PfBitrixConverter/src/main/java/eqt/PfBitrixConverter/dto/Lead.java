package eqt.PfBitrixConverter.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

import java.util.List;

@Getter
public class Lead {

  private Long id;

  @SerializedName("user")
  private PfAgent pfAgent;

  @SerializedName("first_name")
  private String firstName;

  private String email;

  private String phone;

  private List<Preference> preferences;
}
