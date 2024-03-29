package eqt.PfBitrixConverter.service;

import eqt.PfBitrixConverter.api.RestApi;
import eqt.PfBitrixConverter.dto.CallTrackingLeadsInfo;
import eqt.PfBitrixConverter.dto.Lead;
import eqt.PfBitrixConverter.dto.LeadsInfo;
import eqt.PfBitrixConverter.dto.Preference;

import java.util.ArrayList;
import java.util.List;

public class LeadService {

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

  public static int chooseAssignee(long count){
    return count % 2 == 0 ? 15 : 13;
  }
}
