package eqt.PfBitrixConverter.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

import java.util.List;

@Getter
public class BitrixLeadsInfo {

  @SerializedName("result")
  private List<BitrixLeadById> bitrixLeadsByIds;

  @SerializedName("total")
  private int totalBitrixLeads;

  @SerializedName("next")
  private int nextBitrixLeads;
}
