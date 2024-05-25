package eqt.PfBitrixConverter.api;

import com.google.gson.Gson;
import eqt.PfBitrixConverter.dto.*;
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
              .connectTimeout(10000)
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
              .connectTimeout(10000)
              .asJson();
      leadsJson = response.getBody().toPrettyString();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return gson.fromJson(leadsJson, LeadsInfo.class);
  }

  public static WhatsappLeadsInfo getPfWhatsappLeads(String pfToken, int page) {
    String whatsappLeadsJson = "";
    try {
      HttpResponse<JsonNode> response =
          Unirest.get(String.format("https://api-v2.mycrm.com/whatsapp-leads?page=%s", page))
              .header("Content-Type", "application/json")
              .header("Authorization", String.format("Bearer %s", pfToken))
              .connectTimeout(10000)
              .asJson();
      whatsappLeadsJson = response.getBody().toPrettyString();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return gson.fromJson(whatsappLeadsJson, WhatsappLeadsInfo.class);
  }

  public static PfPropertiesInfo getPfProperties(String pfToken, int page) {
    String pfPropertiesJson = "";
    try {
      HttpResponse<JsonNode> response =
          Unirest.get(String.format("https://api-v2.mycrm.com/properties?page=%s", page))
              .header("Content-Type", "application/json")
              .header("Authorization", String.format("Bearer %s", pfToken))
              .connectTimeout(10000)
              .asJson();
      pfPropertiesJson = response.getBody().toPrettyString();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return gson.fromJson(pfPropertiesJson, PfPropertiesInfo.class);
  }

  public static Lead getPfLeadById(String pfToken, Long leadId) {
    String leadJson = "";
    try {
      HttpResponse<JsonNode> response =
          Unirest.get(String.format("https://api-v2.mycrm.com/leads/%s", leadId))
              .header("Content-Type", "application/json")
              .header("Authorization", String.format("Bearer %s", pfToken))
              .connectTimeout(10000)
              .asJson();
      leadJson = response.getBody().toPrettyString();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return gson.fromJson(leadJson, Lead.class);
  }

  public static CallTrackingLeadsInfo getCallTrackingPfLeads(String pfToken, int page) {
    String callTrackingLeadsJson = "";
    try {
      HttpResponse<JsonNode> response =
          Unirest.get(String.format("https://api-v2.mycrm.com/calltrackings?page=%s", page))
              .header("Content-Type", "application/json")
              .header("Authorization", String.format("Bearer %s", pfToken))
              .connectTimeout(10000)
              .asJson();
      callTrackingLeadsJson = response.getBody().toPrettyString();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return gson.fromJson(callTrackingLeadsJson, CallTrackingLeadsInfo.class);
  }

  public static CreatedBitrixLead createBitrixLead(
      String firstName, String phone, String email, String title, String comment, int assigneeId) {
    String createdBitrixLeadJson = "";
    try {
      HttpResponse<JsonNode> response =
          Unirest.post("https://eqt.bitrix24.by/rest/53/t2kn6fktlq973hco/crm.lead.add.json")
              .queryString("fields[PHONE][0][VALUE]", phone)
              .queryString("fields[EMAIL][0][VALUE]", email)
              .queryString("fields[TITLE]", title)
              .queryString("fields[NAME]", firstName)
              .queryString("fields[COMMENTS]", comment)
              .queryString("fields[ASSIGNED_BY_ID]", assigneeId)
              .header("Cookie", "qmb=0")
              .connectTimeout(10000)
              .asJson();
      createdBitrixLeadJson = response.getBody().toPrettyString();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return gson.fromJson(createdBitrixLeadJson, CreatedBitrixLead.class);
  }

  public static boolean deleteBitrixLead(Long bitrixLeadId) {
    boolean leadUpdatingResult;
    try {
      HttpResponse<JsonNode> response =
          Unirest.post("https://eqt.bitrix24.by/rest/53/t2kn6fktlq973hco/crm.lead.delete.json")
              .header("Cookie", "qmb=0")
              .header("Content-Type", "application/x-www-form-urlencoded")
              .field("ID", bitrixLeadId)
              .connectTimeout(10000)
              .asJson();
      leadUpdatingResult = response.isSuccess();
    } catch (Exception e) {
      leadUpdatingResult = false;
    }
    return leadUpdatingResult;
  }

  public static boolean updateBitrixLead(Long bitrixLeadId, int assigneeLeadId) {
    boolean leadUpdatingResult;
    try {
      HttpResponse<JsonNode> response =
          Unirest.post("https://eqt.bitrix24.by/rest/53/t2kn6fktlq973hco/crm.lead.update.json")
              .header("Cookie", "qmb=0")
              .header("Content-Type", "application/x-www-form-urlencoded")
              .field("ID", bitrixLeadId)
              .field("fields[ASSIGNED_BY_ID]", String.valueOf(assigneeLeadId))
              .connectTimeout(10000)
              .asJson();
      leadUpdatingResult = response.isSuccess();
    } catch (Exception e) {
      leadUpdatingResult = false;
    }
    return leadUpdatingResult;
  }

  public static BitrixLeadsInfo getAllBitrixLeads(int startFromLead) {
    String bitrixLeadsJson = "";
    try {
      HttpResponse<JsonNode> response =
          Unirest.get(
                  String.format(
                      "https://eqt.bitrix24.by/rest/53/t2kn6fktlq973hco/crm.lead.list.json?start=%s",
                      startFromLead))
              .header("Cookie", "qmb=0")
              .header("Content-Type", "application/json")
              .connectTimeout(10000)
              .asJson();
      bitrixLeadsJson = response.getBody().toPrettyString();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return gson.fromJson(bitrixLeadsJson, BitrixLeadsInfo.class);
  }

  public static BitrixLead getBitrixLeadById(Long bitrixLeadId) {
    String bitrixLead = "";
    try {
      HttpResponse<JsonNode> response =
          Unirest.post("https://eqt.bitrix24.by/rest/53/t2kn6fktlq973hco/crm.lead.get.json")
              .header("Cookie", "qmb=0")
              .header("Content-Type", "application/x-www-form-urlencoded")
              .field("ID", bitrixLeadId)
              .connectTimeout(10000)
              .asJson();
      bitrixLead = response.getBody().toPrettyString();
    } catch (Exception e) {
      System.out.println(bitrixLeadId);
      e.printStackTrace();
    }
    return gson.fromJson(bitrixLead, BitrixLead.class);
  }
}
