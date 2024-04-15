package eqt.PfBitrixConverter.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

@Getter
public class GetBitrixLeadInfo {

  @SerializedName("result")
  private GetBitrixLead bitrixLead;
}
