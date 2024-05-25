package eqt.PfBitrixConverter.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

@Getter
public class WhatsappLead {

  private Long id;
  private String phone;

  private String status;

  @SerializedName("user")
  private PfAgent pfAgent;

  @SerializedName("property_reference")
  private String propertyReference;
}
