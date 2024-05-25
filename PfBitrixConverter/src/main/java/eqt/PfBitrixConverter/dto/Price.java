package eqt.PfBitrixConverter.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

import java.util.List;

@Getter
public class Price {

  @SerializedName("offering_type")
  private String offeringType;

  @SerializedName("prices")
  private List<PriceInfo> priceInfo;

  private int value;

  @Getter
  public static class PriceInfo {
    private String period;
    private int value;
  }
}
