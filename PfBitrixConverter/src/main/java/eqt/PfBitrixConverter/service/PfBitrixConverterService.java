package eqt.PfBitrixConverter.service;

import eqt.PfBitrixConverter.dto.CallTracking;
import eqt.PfBitrixConverter.dto.CallTrackingLeadsInfo;
import eqt.PfBitrixConverter.dto.Lead;
import eqt.PfBitrixConverter.dto.LeadsInfo;
import eqt.PfBitrixConverter.entity.CallTrackingLeads;
import eqt.PfBitrixConverter.entity.Leads;
import eqt.PfBitrixConverter.repository.CallTrackingLeadsRepository;
import eqt.PfBitrixConverter.repository.LeadsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

import static eqt.PfBitrixConverter.api.RestApi.*;
import static eqt.PfBitrixConverter.service.LeadService.*;

@Service
public class PfBitrixConverterService {

  private static final String PROPERTY_FINDER_LEADS_TITLE = "Property Finder Lead";
  private static final String PROPERTY_FINDER_CALL_TRACKING_LEADS_TITLE =
      "Property Finder Lead from Call";

  @Autowired private LeadsRepository leadsRepository;
  @Autowired private CallTrackingLeadsRepository callTrackingLeadsRepository;

  @Scheduled(fixedRate = 900000)
  public void sendNewLeadsFromPfExpertToBitrix() {
    String pfToken = createPfToken().getToken();
    List<LeadsInfo> pfLeadsInfoPages = getLeadsFromAllPages(pfToken);
    List<CallTrackingLeadsInfo> callTrackingLeadsFromAllPages =
        getCallTrackingLeadsFromAllPages(pfToken);
    for (LeadsInfo leadsInfo : pfLeadsInfoPages) {
      for (Lead lead : leadsInfo.getLeads()) {
        Long leadId = lead.getId();
        String leadPhone = lead.getPhone();
        String leadEmail = lead.getEmail();
        String leadFirstName = lead.getFirstName();
        String leadComment = buildBitrixLeadComment(lead);

        if (!leadsRepository.existsById(leadId)) {
          boolean createdBitrixLead =
              createBitrixLead(
                  leadFirstName,
                  leadPhone,
                  leadEmail,
                  PROPERTY_FINDER_LEADS_TITLE.concat(" Тестирование (не обрабатывать)"),
                  leadComment);
          Leads newLead =
              new Leads(
                  leadId, leadFirstName, leadEmail, leadPhone, leadComment, createdBitrixLead);
          leadsRepository.save(newLead);
        }
      }
    }
    for (CallTrackingLeadsInfo callTrackingLeadsInfo : callTrackingLeadsFromAllPages) {
      for (CallTracking callTracking : callTrackingLeadsInfo.getCallTrackingLeads()) {
        Long callTrackingId = callTracking.getId();
        String leadFirstName = "";
        String leadEmail = "";
        String callTrackingPhone = callTracking.getPhone();
        int callTrackingCallTime = callTracking.getCallTime();

        if (callTracking.getLead() != null) {
          Lead lead = getPfLeadById(pfToken, callTracking.getLead().getId());
          leadFirstName = lead.getFirstName();
          leadEmail = lead.getEmail();
        }

        if (!callTrackingLeadsRepository.existsById(callTrackingId)) {
          boolean createdBitrixLead =
              createBitrixLead(
                  leadFirstName,
                  callTrackingPhone,
                  leadEmail,
                  PROPERTY_FINDER_CALL_TRACKING_LEADS_TITLE.concat(
                      " Тестирование (не обрабатывать)"),
                  "Call duration: " + callTrackingCallTime);
          CallTrackingLeads newCallTrackingLead =
              new CallTrackingLeads(
                  callTrackingId,
                  leadFirstName,
                  leadEmail,
                  callTrackingPhone,
                  callTrackingCallTime,
                  createdBitrixLead);
          callTrackingLeadsRepository.save(newCallTrackingLead);
        }
      }
    }
  }
}
