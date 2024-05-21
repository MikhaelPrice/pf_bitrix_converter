package eqt.PfBitrixConverter.service;

import eqt.PfBitrixConverter.dto.*;
import eqt.PfBitrixConverter.entity.BitrixLeads;
import eqt.PfBitrixConverter.entity.CallTrackingLeads;
import eqt.PfBitrixConverter.entity.Leads;
import eqt.PfBitrixConverter.entity.LeadsErrors;
import eqt.PfBitrixConverter.repository.BitrixLeadsRepository;
import eqt.PfBitrixConverter.repository.CallTrackingLeadsRepository;
import eqt.PfBitrixConverter.repository.LeadsErrorsRepository;
import eqt.PfBitrixConverter.repository.LeadsRepository;
import eqt.PfBitrixConverter.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static eqt.PfBitrixConverter.api.RestApi.*;
import static eqt.PfBitrixConverter.util.LeadUtil.*;

@Service
public class PfBitrixConverterService {

  private static final String PROPERTY_FINDER_LEADS_TITLE = "Property Finder Lead";
  private static final String PROPERTY_FINDER_CALL_TRACKING_LEADS_TITLE = "Property Finder Call";

  @Autowired private LeadsRepository leadsRepository;
  @Autowired private CallTrackingLeadsRepository callTrackingLeadsRepository;
  @Autowired private BitrixLeadsRepository bitrixLeadsRepository;
  @Autowired private LeadsErrorsRepository leadsErrorsRepository;

  @Scheduled(fixedRate = 300000)
  public void sendNewLeadsFromPfExpertToBitrix() {
    try {
      List<BitrixLead> totalBitrixLeads = getBitrixLeadsFromAllPages();
      String pfToken = createPfToken().getToken();
      List<LeadsInfo> pfLeadsInfoPages = getAllLeadsPages(pfToken);
      List<CallTrackingLeadsInfo> callTrackingLeadsFromAllPages =
          getAllCallTrackingLeadsPages(pfToken);
      for (LeadsInfo leadsPage : pfLeadsInfoPages) {
        for (Lead lead : leadsPage.getLeads()) {
          Long leadId = lead.getId();
          String leadPhone = lead.getPhone();
          String leadEmail = lead.getEmail();
          String leadFirstName = lead.getFirstName();
          String leadComment = buildBitrixLeadComment(lead);

          if (!leadsRepository.existsById(leadId)) {
            CreatedBitrixLead bitrixLead =
                createBitrixLead(
                    leadFirstName,
                    leadPhone,
                    leadEmail,
                    PROPERTY_FINDER_LEADS_TITLE,
                    leadComment,
                    choosePfLeadAssignee(lead.getPfAgent().getId(), leadsRepository.count()));
            Leads newLead =
                new Leads(
                    leadId,
                    leadFirstName,
                    leadEmail,
                    leadPhone,
                    leadComment,
                    Objects.nonNull(bitrixLead));
            leadsRepository.save(newLead);
          }
        }
      }
      for (CallTrackingLeadsInfo callTrackingLeadsPage : callTrackingLeadsFromAllPages) {
        for (CallTracking callTracking : callTrackingLeadsPage.getCallTrackingLeads()) {
          Long callTrackingId = callTracking.getId();
          Long pfAgentId = callTracking.getPfAgent().getId();
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
            CreatedBitrixLead createdBitrixLead =
                createBitrixLead(
                    leadFirstName,
                    callTrackingPhone,
                    leadEmail,
                    PROPERTY_FINDER_CALL_TRACKING_LEADS_TITLE,
                    "Call duration: " + callTrackingCallTime,
                    chooseCallTrackingLeadAssignee(pfAgentId));
            CallTrackingLeads newCallTrackingLead =
                new CallTrackingLeads(
                    callTrackingId,
                    leadFirstName,
                    leadEmail,
                    callTrackingPhone,
                    callTrackingCallTime,
                    Objects.nonNull(createdBitrixLead));
            callTrackingLeadsRepository.save(newCallTrackingLead);
          }
        }
      }
      for (BitrixLead bitrixLead : totalBitrixLeads) {
        Long bitrixLeadId = bitrixLead.getId();
        String bitrixLeadTitle = bitrixLead.getTitle();
        int responsible = bitrixLead.getAssignee();
        boolean isPfLead =
            bitrixLeadTitle.equals(PROPERTY_FINDER_LEADS_TITLE)
                || bitrixLeadTitle.equals(PROPERTY_FINDER_CALL_TRACKING_LEADS_TITLE);
        if (responsible == ALEX_BITRIX_ID) {
          boolean updateBitrixLead = updateBitrixLead(bitrixLeadId, BABENKO_BITRIX_ID);
          if (!bitrixLeadsRepository.existsById(bitrixLeadId) && !isPfLead) {
            BitrixLeads newBitrixLead =
                new BitrixLeads(bitrixLeadId, bitrixLeadTitle, updateBitrixLead);
            bitrixLeadsRepository.save(newBitrixLead);
          }
        }
      }
    } catch (Exception e) {
      leadsErrorsRepository.save(
          new LeadsErrors((long) e.hashCode(), e.toString(), TimeUtil.currentTime()));
    }
  }
}
