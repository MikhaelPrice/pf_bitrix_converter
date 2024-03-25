package eqt.PfBitrixConverter.api;

import com.google.gson.Gson;
import eqt.PfBitrixConverter.dto.PfToken;
import eqt.PfBitrixConverter.dto.leads.LeadsInfo;
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

  public static LeadsInfo getPfLeads(String pfToken, int page) {
    String leadsJson = "";
    try {
      HttpResponse<JsonNode> response =
              Unirest.get(String.format("https://api-v2.mycrm.com/leads?page=%s", page))
                      .header("Content-Type", "application/json")
                      .header("Authorization", String.format("Bearer %s", pfToken))
                      .asJson();
      leadsJson = response.getBody().toPrettyString();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return gson.fromJson(leadsJson, LeadsInfo.class);
  }

  public static boolean createBitrixLead(
      String firstName, String phone, String email, String title, String comment) {
    boolean leadCreationResult;
    try {
      HttpResponse<JsonNode> response =
          Unirest.post("https://eqt.bitrix24.by/rest/53/t2kn6fktlq973hco/crm.lead.add.json")
              .queryString("fields[PHONE][0][VALUE]", phone)
              .queryString("fields[EMAIL][0][VALUE]", email)
              .queryString("fields[TITLE]", title)
              .queryString("fields[NAME]", firstName)
              .queryString("fields[COMMENTS]", comment)
              .header("Cookie", "qmb=0")
              .asJson();
      leadCreationResult = response.isSuccess();
    } catch (Exception e) {
      leadCreationResult = false;
    }
    return leadCreationResult;
  }
}
