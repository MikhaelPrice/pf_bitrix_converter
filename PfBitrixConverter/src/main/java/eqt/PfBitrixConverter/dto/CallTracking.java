package eqt.PfBitrixConverter.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

@Getter
public class CallTracking {
  private Long id;
  private String phone;

  @SerializedName("call_time")
  private int callTime;

  private CallTrackingLead lead;
}
