package eqt.PfBitrixConverter.service;

import eqt.PfBitrixConverter.api.RestApi;
import eqt.PfBitrixConverter.dto.leads.Lead;
import eqt.PfBitrixConverter.dto.leads.LeadsInfo;
import eqt.PfBitrixConverter.dto.leads.Preference;

import java.util.ArrayList;
import java.util.List;

public class LeadService {

  public static List<LeadsInfo> getPfLeadsFromAllPages(String pfToken) {
    List<LeadsInfo> pfLeadsInfoPages = new ArrayList<>();
    LeadsInfo pfLeadsInfo = RestApi.getPfLeads(pfToken, 1);
    pfLeadsInfoPages.add(pfLeadsInfo);
    double pagesMore = (double) pfLeadsInfo.getCount() / 100;
    for (int pages = 2; pages <= pagesMore; pages++) {
      pfLeadsInfoPages.add(RestApi.getPfLeads(pfToken, pages));
    }
    return pfLeadsInfoPages;
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
        + ", "
        + subCommunity
        + ",\n "
        + offeringType
        + ", "
        + realEstateObject
        + ", Price range "
        + price
        + " AED, Bedrooms "
        + bedrooms
        + ", Bathrooms "
        + bathrooms;
  }

  private static String buildNumberRange(int num1, int num2) {
    return num1 == num2 ? String.valueOf(num1) : num1 + "-" + num2;
  }
}
