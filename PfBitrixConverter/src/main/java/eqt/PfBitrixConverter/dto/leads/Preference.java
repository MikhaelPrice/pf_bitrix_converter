package eqt.PfBitrixConverter.dto.leads;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

import java.util.List;

@Getter
public class Preference {

  @SerializedName("offering_type")
  private String offeringType;

  private List<Type> types;
  private List<Location> locations;

  @SerializedName("from_price")
  private int fromPrice;

  @SerializedName("to_price")
  private int toPrice;

  @SerializedName("from_size")
  private int fromSize;

  @SerializedName("to_size")
  private int toSize;

  @SerializedName("from_bedroom")
  private int fromBedroom;

  @SerializedName("to_bedroom")
  private int toBedroom;

  @SerializedName("from_bathroom")
  private int fromBathroom;

  @SerializedName("to_bathroom")
  private int toBathroom;

}
