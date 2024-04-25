package eqt.PfBitrixConverter.service;

import eqt.PfBitrixConverter.dto.*;
import eqt.PfBitrixConverter.entity.BitrixLeads;
import eqt.PfBitrixConverter.entity.CallTrackingLeads;
import eqt.PfBitrixConverter.entity.Leads;
import eqt.PfBitrixConverter.repository.BitrixLeadsRepository;
import eqt.PfBitrixConverter.repository.CallTrackingLeadsRepository;
import eqt.PfBitrixConverter.repository.LeadsRepository;
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

  @Scheduled(fixedRate = 300000)
  public void sendNewLeadsFromPfExpertToBitrix() {
    List<Long> bitrixLeadsIds = extractLeadIds(getBitrixLeads().getBitrixLeadsByIds());
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
    for (CallTrackingLeadsInfo callTrackingLeadsInfo : callTrackingLeadsFromAllPages) {
      for (CallTracking callTracking : callTrackingLeadsInfo.getCallTrackingLeads()) {
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
    for (Long bitrixLeadId : bitrixLeadsIds) {
      GetBitrixLead bitrixLead = getBitrixLead(bitrixLeadId).getBitrixLead();
      String bitrixLeadName = bitrixLead.getName();
      String bitrixLeadTitle = bitrixLead.getTitle();
      Object bitrixLeadSource = bitrixLead.getSource();
      String bitrixLeadComment = bitrixLead.getComment();
      String phone = bitrixLead.getBitrixLeadPhoneInfo().get(0).getPhone();
      boolean isPfLead =
          bitrixLeadTitle.equals(PROPERTY_FINDER_CALL_TRACKING_LEADS_TITLE)
              || bitrixLeadTitle.equals(PROPERTY_FINDER_LEADS_TITLE);
      boolean isCustomLead = Objects.nonNull(bitrixLeadSource);
      if (!bitrixLeadsRepository.existsById(bitrixLeadId) && !isPfLead && !isCustomLead) {
        boolean deletedBitrixLead = deleteBitrixLead(bitrixLeadId);
        int assigneeId = chooseLeadAssignee(bitrixLeadsRepository.count());
        Long createdLeadId =
            createBitrixLead(
                    bitrixLeadName, phone, null, bitrixLeadTitle, bitrixLeadComment, assigneeId)
                .getCreatedLeadId();
        boolean createBitrixLead = Objects.nonNull(createdLeadId);
        BitrixLeads newBitrixLead =
            new BitrixLeads(
                createdLeadId,
                bitrixLeadTitle,
                phone,
                bitrixLeadName,
                bitrixLeadComment,
                deletedBitrixLead,
                createBitrixLead,
                assigneeId);
        bitrixLeadsRepository.save(newBitrixLead);
      }
    }
  }
}
