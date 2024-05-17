package eqt.PfBitrixConverter.util;

import eqt.PfBitrixConverter.api.RestApi;
import eqt.PfBitrixConverter.dto.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static eqt.PfBitrixConverter.api.RestApi.getBitrixLeads;

public class LeadUtil {

  public static final int BABENKO_BITRIX_ID = 15;
  public static final int BABENKO_PF_ID = 185401;

  public static final int DIACHKOVA_BITRIX_ID = 13;
  public static final int DIACHKOVA_PF_ID = 165786;

  public static final int VLAD_BITRIX_ID = 27;
  public static final int VLAD_PF_ID = 102034;

  public static final int MIKE_BITRIX_ID = 53;

  public static final int ALEX_BITRIX_ID = 1;

  public static final int EQT_PF_ID = 102033;

  public static List<LeadsInfo> getLeadsFromAllPages(String pfToken) {
    List<LeadsInfo> leadsInfoPages = new ArrayList<>();
    LeadsInfo leadsInfo = RestApi.getPfLeads(pfToken, 1);
    leadsInfoPages.add(leadsInfo);
    double pagesMore = (double) leadsInfo.getCount() / 100;
    for (int pages = 2; pages <= pagesMore; pages++) {
      leadsInfoPages.add(RestApi.getPfLeads(pfToken, pages));
    }
    return leadsInfoPages;
  }

  public static List<CallTrackingLeadsInfo> getCallTrackingLeadsFromAllPages(String pfToken) {
    List<CallTrackingLeadsInfo> callTrackingLeadsInfoPages = new ArrayList<>();
    CallTrackingLeadsInfo callTrackingLeadsInfo = RestApi.getCallTrackingPfLeads(pfToken, 1);
    callTrackingLeadsInfoPages.add(callTrackingLeadsInfo);
    double pagesMore = (double) callTrackingLeadsInfo.getCount() / 100;
    for (int pages = 2; pages <= pagesMore; pages++) {
      callTrackingLeadsInfoPages.add(RestApi.getCallTrackingPfLeads(pfToken, pages));
    }
    return callTrackingLeadsInfoPages;
  }

  private static List<Long> extractLeadIds(List<BitrixLeadById> bitrixLeads) {
    return bitrixLeads.stream().map(BitrixLeadById::getId).collect(Collectors.toList());
  }

  public static List<Long> getBitrixLeadsIdsFromAllPages(){
    List<Long> totalBitrixLeadsIds = new ArrayList<>();
    int totalBitrixLeads = getBitrixLeads(0).getTotalBitrixLeads();
    for (int bitrixLead = 0; bitrixLead <= totalBitrixLeads; bitrixLead += 50) {
      List<BitrixLeadById> bitrixLeadsByIds = getBitrixLeads(bitrixLead).getBitrixLeadsByIds();
      totalBitrixLeadsIds.addAll(extractLeadIds(bitrixLeadsByIds));
    }
    return totalBitrixLeadsIds;
  }

  public static String buildBitrixLeadComment(Lead lead) {
    List<Preference> preferences = lead.getPreferences();
    String offeringType = preferences.get(0).getOfferingType();
    String realEstateObject = preferences.get(0).getTypes().get(0).getName();

    String city = preferences.get(0).getLocations().get(0).getCity();
    String community = preferences.get(0).getLocations().get(0).getCommunity();
    String subCommunity = preferences.get(0).getLocations().get(0).getSubCommunity();

    int fromPrice = preferences.get(0).getFromPrice();
    int toPrice = preferences.get(0).getToPrice();
    String price = buildNumberRange(fromPrice, toPrice);

    int fromBedroom = preferences.get(0).getFromBedroom();
    int toBedroom = preferences.get(0).getToBedroom();
    String bedrooms = buildNumberRange(fromBedroom, toBedroom);

    int fromBathroom = preferences.get(0).getFromBathroom();
    int toBathroom = preferences.get(0).getToBathroom();
    String bathrooms = buildNumberRange(fromBathroom, toBathroom);

    return "Location: "
        + city
        + ", "
        + community
        + "\nProject Name: "
        + subCommunity
        + "\nType: "
        + offeringType
        + ", "
        + realEstateObject
        + "\n Size: Bedrooms "
        + bedrooms
        + ", Bathrooms "
        + bathrooms
        + "\nPrice range: "
        + price
        + " AED";
  }

  private static String buildNumberRange(int num1, int num2) {
    return num1 == num2 ? String.valueOf(num1) : num1 + "-" + num2;
  }

  public static int choosePfLeadAssignee(long pfAgentId, long count) {
    if (pfAgentId == DIACHKOVA_PF_ID) {
      return DIACHKOVA_BITRIX_ID;
    } else if (pfAgentId == BABENKO_PF_ID || pfAgentId == VLAD_PF_ID) {
      return BABENKO_BITRIX_ID;
    }
    return chooseBitrixLeadAssignee(count);
  }

  public static int chooseBitrixLeadAssignee(long count) {
    return count % 2 == 0 ? BABENKO_BITRIX_ID : DIACHKOVA_BITRIX_ID;
  }

  public static int chooseCallTrackingLeadAssignee(long pfAgentId) {
    if (pfAgentId == BABENKO_PF_ID) {
      return BABENKO_BITRIX_ID;
    } else if (pfAgentId == EQT_PF_ID || pfAgentId == VLAD_PF_ID) {
      return VLAD_BITRIX_ID;
    } else if (pfAgentId == DIACHKOVA_PF_ID) {
      return DIACHKOVA_BITRIX_ID;
    } else {
      return VLAD_BITRIX_ID;
    }
  }
}
