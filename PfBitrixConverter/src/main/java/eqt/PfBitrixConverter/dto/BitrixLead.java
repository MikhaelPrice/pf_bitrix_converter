package eqt.PfBitrixConverter.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

@Getter
public class BitrixLead {

  @SerializedName("ID")
  private Long id;

  @SerializedName("TITLE")
  private String title;

  @SerializedName("ASSIGNED_BY_ID")
  private int assignee;

}
