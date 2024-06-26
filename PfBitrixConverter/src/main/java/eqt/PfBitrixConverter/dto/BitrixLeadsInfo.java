package eqt.PfBitrixConverter.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

import java.util.List;

@Getter
public class BitrixLeadsInfo {

  @SerializedName("result")
  private List<BitrixLead> bitrixLeads;

  @SerializedName("total")
  private int total;
}
