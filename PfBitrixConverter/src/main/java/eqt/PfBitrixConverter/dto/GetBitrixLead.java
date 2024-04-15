package eqt.PfBitrixConverter.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

import java.util.List;

@Getter
public class GetBitrixLead {

  @SerializedName("ID")
  private Long id;

  @SerializedName("TITLE")
  private String title;

  @SerializedName("NAME")
  private String name;

  @SerializedName("PHONE")
  private List<BitrixLeadPhone> bitrixLeadPhoneInfo;

  @SerializedName("SOURCE_ID")
  private Object source;

  @SerializedName("COMMENTS")
  private String comment;
}
