package eqt.PfBitrixConverter.api;

import com.google.gson.Gson;
import eqt.PfBitrixConverter.dto.pf.LeadsInfo;
import eqt.PfBitrixConverter.dto.pf.PfToken;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;

public class RestApi {

  private static final Gson gson = new Gson();

  public static PfToken createPfToken() {
    String tokenJson = "";
    String authHeader =
        "Basic WmdLVXouTEFGWmpzcDMxZjhTNFdJQUJnQklScGVZZWNhRFo3NmZYbjpjNTQwMzIxMWM0MTkxMjljYWExYjI2MTNkZjdhODQ0OQ==";
    try {
      HttpResponse<JsonNode> response =
          Unirest.post("https://auth.propertyfinder.com/auth/oauth/v1/token")
              .header("Content-Type", "application/json")
              .header("Authorization", authHeader)
              .body("{\"scope\": \"openid\", \"grant_type\": \"client_credentials\"}")
              .asJson();
      tokenJson = response.getBody().toPrettyString();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return gson.fromJson(tokenJson, PfToken.class);
  }

  public static LeadsInfo getPfLeads(String pfToken) {
    String leadsJson = "";
    try {
      HttpResponse<JsonNode> response =
          Unirest.get("https://api-v2.mycrm.com/leads")
              .header("Content-Type", "application/json")
              .header("Authorization", String.format("Bearer %s", pfToken))
              .asJson();
      leadsJson = response.getBody().toPrettyString();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return gson.fromJson(leadsJson, LeadsInfo.class);
  }
}
