package eqt.PfBitrixConverter.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

@Getter
public class CreatedBitrixLead {

  @SerializedName("result")
  private Long createdLeadId;
}
