package eqt.PfBitrixConverter.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

@Getter
public class PfToken {

  @SerializedName("access_token")
  private String token;
}
