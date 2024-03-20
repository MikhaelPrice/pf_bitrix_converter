package eqt.PfBitrixConverter.dto.pf;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

@Getter
public class PfToken {

  @SerializedName("access_token")
  private String token;

  @SerializedName("token_type")
  private String tokenType;

  @SerializedName("expires_in")
  private int timeForExpiring;
}
