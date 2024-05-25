package eqt.PfBitrixConverter.dto;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

import java.util.List;

@Getter
public class WhatsappLeadsInfo {

  private int count;

  @SerializedName("whatsapp")
  private List<WhatsappLead> whatsappLeads;
}
