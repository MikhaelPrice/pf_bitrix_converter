package eqt.PfBitrixConverter.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

import java.util.List;

@Getter
public class CallTrackingLeadsInfo {
  private int count;

  @SerializedName("call_trackings")
  private List<CallTracking> callTrackingLeads;
}
