package eqt.PfBitrixConverter.util;

import eqt.PfBitrixConverter.dto.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static eqt.PfBitrixConverter.api.RestApi.*;

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

  public static List<LeadsInfo> getAllLeadsPages(String pfToken) {
    List<LeadsInfo> leadsInfoPages = new ArrayList<>();
    LeadsInfo leadsInfo = getPfLeads(pfToken, 1);
    leadsInfoPages.add(leadsInfo);
    double pagesMore = (double) leadsInfo.getCount() / 100;
    for (int pages = 1; pages <= pagesMore; pages++) {
      leadsInfoPages.add(getPfLeads(pfToken, pages + 1));
    }
    return leadsInfoPages;
  }

  public static List<PfProperty> getAllPfProperties(String pfToken) {
    List<PfPropertiesInfo> pfPropertiesInfoPages = new ArrayList<>();
    PfPropertiesInfo pfPropertiesInfo = getPfProperties(pfToken, 1);
    pfPropertiesInfoPages.add(pfPropertiesInfo);
    double pagesMore = (double) pfPropertiesInfo.getCount() / 100;
    for (int pages = 1; pages <= pagesMore; pages++) {
      pfPropertiesInfoPages.add(getPfProperties(pfToken, pages + 1));
    }
    List<PfProperty> pfProperties = new ArrayList<>();
    for (PfPropertiesInfo propertiesInfoPage : pfPropertiesInfoPages) {
      pfProperties.addAll(propertiesInfoPage.getProperties());
    }
    return pfProperties;
  }

  public static List<WhatsappLeadsInfo> getAllWhatsappLeadsPages(String pfToken) {
    List<WhatsappLeadsInfo> whatsappLeadsInfoPages = new ArrayList<>();
    WhatsappLeadsInfo whatsappLeadsInfo = getPfWhatsappLeads(pfToken, 1);
    whatsappLeadsInfoPages.add(whatsappLeadsInfo);
    double pagesMore = (double) whatsappLeadsInfo.getCount() / 100;
    for (int pages = 1; pages <= pagesMore; pages++) {
      whatsappLeadsInfoPages.add(getPfWhatsappLeads(pfToken, pages + 1));
    }
    return whatsappLeadsInfoPages;
  }

  public static List<CallTrackingLeadsInfo> getAllCallTrackingLeadsPages(String pfToken) {
    List<CallTrackingLeadsInfo> callTrackingLeadsInfoPages = new ArrayList<>();
    CallTrackingLeadsInfo callTrackingLeadsInfo = getCallTrackingPfLeads(pfToken, 1);
    callTrackingLeadsInfoPages.add(callTrackingLeadsInfo);
    double pagesMore = (double) callTrackingLeadsInfo.getCount() / 100;
    for (int pages = 1; pages <= pagesMore; pages++) {
      callTrackingLeadsInfoPages.add(getCallTrackingPfLeads(pfToken, pages + 1));
    }
    return callTrackingLeadsInfoPages;
  }

  public static List<BitrixLead> getBitrixLeadsFromAllPages() {
    List<BitrixLead> totalBitrixLeads = new ArrayList<>();
    int total = getAllBitrixLeads(0).getTotal();
    for (int bitrixLead = 0; bitrixLead <= total; bitrixLead += 50) {
      List<BitrixLead> bitrixLeads = getAllBitrixLeads(bitrixLead).getBitrixLeads();
      totalBitrixLeads.addAll(bitrixLeads);
    }
    return totalBitrixLeads;
  }

  public static String buildWhatsappLeadComment(PfProperty pfProperty) {
    double propertySize = pfProperty.getSize();
    int bedrooms = pfProperty.getBedrooms();
    int bathrooms = pfProperty.getBathrooms();
    String city = pfProperty.getLocation().getCity();
    String community = pfProperty.getLocation().getCommunity();
    String subCommunity = pfProperty.getLocation().getSubCommunity();
    String offeringType = pfProperty.getPrice().getOfferingType();
    List<Price.PriceInfo> priceInfo = pfProperty.getPrice().getPriceInfo();
    String period;
    int priceValue;
    if (Objects.nonNull(priceInfo)) {
      period = priceInfo.get(0).getPeriod();
      priceValue = priceInfo.get(0).getValue();
    } else {
      period = "not specified";
      priceValue = pfProperty.getPrice().getValue();
    }
    String tower = pfProperty.getLocation().getTower();

    return "Location: "
        + city
        + ", "
        + community
        + "\nProject: "
        + subCommunity
        + ", "
        + tower
        + ", "
        + offeringType
        + "\nSize: "
        + propertySize
        + ", Bedrooms "
        + bedrooms
        + ", Bathrooms "
        + bathrooms
        + ", period for rent "
        + period
        + "\nPrice: "
        + priceValue
        + " AED";
  }

  public static String buildLeadComment(Lead lead) {
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
    } else if (pfAgentId == BABENKO_PF_ID) {
      return BABENKO_BITRIX_ID;
    } else if (pfAgentId == VLAD_PF_ID) {
      return VLAD_BITRIX_ID;
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
