package eqt.PfBitrixConverter.dto.leads;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

@Getter
public class Location {
  private String city;
  private String community;

  @SerializedName("sub_community")
  private String subCommunity;
}
