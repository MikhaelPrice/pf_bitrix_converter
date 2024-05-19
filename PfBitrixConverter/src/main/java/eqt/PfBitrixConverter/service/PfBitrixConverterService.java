package eqt.PfBitrixConverter.service;

import eqt.PfBitrixConverter.api.RestApi;
import eqt.PfBitrixConverter.dto.*;
import eqt.PfBitrixConverter.entity.BitrixLeads;
import eqt.PfBitrixConverter.entity.CallTrackingLeads;
import eqt.PfBitrixConverter.entity.Leads;
import eqt.PfBitrixConverter.entity.LeadsErrors;
import eqt.PfBitrixConverter.repository.BitrixLeadsRepository;
import eqt.PfBitrixConverter.repository.CallTrackingLeadsRepository;
import eqt.PfBitrixConverter.repository.LeadsErrorsRepository;
import eqt.PfBitrixConverter.repository.LeadsRepository;
import eqt.PfBitrixConverter.util.LeadUtil;
import eqt.PfBitrixConverter.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static eqt.PfBitrixConverter.api.RestApi.createPfToken;
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
      RestApi restApi = new RestApi();
      LeadUtil leadUtil = new LeadUtil(restApi);
      List<Long> totalBitrixLeadsIds = leadUtil.getBitrixLeadsIdsFromAllPages();
      String pfToken = createPfToken().getToken();
      List<LeadsInfo> pfLeadsInfoPages = leadUtil.getLeadsFromAllPages(pfToken);
      List<CallTrackingLeadsInfo> callTrackingLeadsFromAllPages =
          leadUtil.getCallTrackingLeadsFromAllPages(pfToken);
      for (LeadsInfo leadsInfo : pfLeadsInfoPages) {
        for (Lead lead : leadsInfo.getLeads()) {
          Long leadId = lead.getId();
          String leadPhone = lead.getPhone();
          String leadEmail = lead.getEmail();
          String leadFirstName = lead.getFirstName();
          String leadComment = buildBitrixLeadComment(lead);

          if (!leadsRepository.existsById(leadId)) {
            CreatedBitrixLead bitrixLead =
                restApi.createBitrixLead(
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
      for (CallTrackingLeadsInfo callTrackingLeadsInfo : callTrackingLeadsFromAllPages) {
        for (CallTracking callTracking : callTrackingLeadsInfo.getCallTrackingLeads()) {
          Long callTrackingId = callTracking.getId();
          Long pfAgentId = callTracking.getPfAgent().getId();
          String leadFirstName = "";
          String leadEmail = "";
          String callTrackingPhone = callTracking.getPhone();
          int callTrackingCallTime = callTracking.getCallTime();

          if (callTracking.getLead() != null) {
            Lead lead = restApi.getPfLeadById(pfToken, callTracking.getLead().getId());
            leadFirstName = lead.getFirstName();
            leadEmail = lead.getEmail();
          }

          if (!callTrackingLeadsRepository.existsById(callTrackingId)) {
            CreatedBitrixLead createdBitrixLead =
                restApi.createBitrixLead(
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
      for (Long bitrixLeadId : totalBitrixLeadsIds) {
        GetBitrixLead bitrixLead = restApi.getBitrixLead(bitrixLeadId).getBitrixLead();
        if (Objects.nonNull(bitrixLead)) {
          String bitrixLeadName = bitrixLead.getName();
          String bitrixLeadTitle = bitrixLead.getTitle();
          String bitrixLeadComment = bitrixLead.getComment();
          List<BitrixLeadPhone> bitrixLeadPhoneInfo = bitrixLead.getBitrixLeadPhoneInfo();
          String phone = "";
          if (Objects.nonNull(bitrixLeadPhoneInfo)) {
            phone = bitrixLeadPhoneInfo.get(0).getPhone();
          }
          int bitrixLeadAssignee = bitrixLead.getAssignee();
          if (bitrixLeadAssignee == ALEX_BITRIX_ID) {
            boolean updatedBitrixLead = restApi.updateBitrixLead(bitrixLeadId, BABENKO_BITRIX_ID);
            if (!bitrixLeadsRepository.existsById(bitrixLeadId)) {
              BitrixLeads newBitrixLead =
                  new BitrixLeads(
                      bitrixLeadId,
                      bitrixLeadTitle,
                      phone,
                      bitrixLeadName,
                      bitrixLeadComment,
                      updatedBitrixLead,
                      BABENKO_BITRIX_ID);
              bitrixLeadsRepository.save(newBitrixLead);
            }
          }
        }
      }
    } catch (Exception e) {
      leadsErrorsRepository.save(
          new LeadsErrors((long) e.hashCode(), e.toString(), TimeUtil.currentTime()));
    }
  }
}
