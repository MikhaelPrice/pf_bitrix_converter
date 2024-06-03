package eqt.PfBitrixConverter.service;

import eqt.PfBitrixConverter.dto.*;
import eqt.PfBitrixConverter.entity.*;
import eqt.PfBitrixConverter.repository.*;
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
  private static final String PROPERTY_FINDER_WHATSAPP_TITLE = "Property Finder Whatsapp";
  private static final String PROPERTY_FINDER_CALL_TRACKING_LEADS_TITLE = "Property Finder Call";

  @Autowired private LeadsRepository leadsRepository;
  @Autowired private CallTrackingLeadsRepository callTrackingLeadsRepository;
  @Autowired private BitrixLeadsRepository bitrixLeadsRepository;
  @Autowired private LeadsErrorsRepository leadsErrorsRepository;
  @Autowired private WhatsappLeadsRepository whatsappLeadsRepository;

  @Scheduled(fixedRate = 300000)
  public void sendNewLeadsFromPfExpertToBitrix() {
    try {
      String pfToken = createPfToken().getToken();
      List<PfProperty> pfProperties = getAllPfProperties(pfToken);
      List<LeadsInfo> pfLeadsInfoPages = getAllLeadsPages(pfToken);
      List<CallTrackingLeadsInfo> pfCallTrackingLeadsInfoPages =
          getAllCallTrackingLeadsPages(pfToken);
      List<BitrixLead> totalBitrixLeads = getBitrixLeadsFromAllPages();
      List<WhatsappLeadsInfo> pfWhatsappInfoPages = getAllWhatsappLeadsPages(pfToken);
      for (WhatsappLeadsInfo whatsappLeadsPage : pfWhatsappInfoPages) {
        for (WhatsappLead whatsappLead : whatsappLeadsPage.getWhatsappLeads()) {
          Long whatsappLeadId = whatsappLead.getId();
          String whatsappLeadPhone = whatsappLead.getPhone();
          String whatsappLeadStatus = whatsappLead.getStatus();
          Long pfAgentId = whatsappLead.getPfAgent().getId();
          String propertyReference = whatsappLead.getPropertyReference();
          String whatsappLeadComment = "";
          for (PfProperty pfProperty : pfProperties) {
            if (Objects.isNull(propertyReference)) {
              break;
            } else if (propertyReference.equals(pfProperty.getReference())) {
              whatsappLeadComment = buildWhatsappLeadComment(pfProperty);
            }
          }
          if (!whatsappLeadsRepository.existsById(whatsappLeadId)) {
            CreatedBitrixLead bitrixLead =
                createBitrixLead(
                    propertyReference,
                    whatsappLeadPhone,
                    null,
                    PROPERTY_FINDER_WHATSAPP_TITLE + " " + whatsappLeadStatus,
                    whatsappLeadComment,
                    choosePfLeadAssignee(
                        whatsappLead.getPfAgent().getId(), leadsRepository.count()));
            WhatsappLeads newWhatsappLead =
                new WhatsappLeads(
                    whatsappLeadId,
                    whatsappLeadPhone,
                    whatsappLeadStatus,
                    String.valueOf(pfAgentId),
                    whatsappLeadComment,
                    Objects.nonNull(bitrixLead));
            whatsappLeadsRepository.save(newWhatsappLead);
          }
        }
      }
      for (LeadsInfo leadsPage : pfLeadsInfoPages) {
        for (Lead lead : leadsPage.getLeads()) {
          Long leadId = lead.getId();
          String leadPhone = lead.getPhone();
          String leadEmail = lead.getEmail();
          String leadFirstName = lead.getFirstName();
          String leadComment = buildLeadComment(lead);

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
      for (CallTrackingLeadsInfo callTrackingLeadsPage : pfCallTrackingLeadsInfoPages) {
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
            CreatedBitrixLead bitrixLead =
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
                    Objects.nonNull(bitrixLead));
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
          boolean updateBitrixLead =
              updateBitrixLead(
                  bitrixLeadId, chooseBitrixLeadAssignee(bitrixLeadsRepository.count()));
          if (!bitrixLeadsRepository.existsById(bitrixLeadId) && !isPfLead) {
            BitrixLeads newBitrixLead =
                new BitrixLeads(bitrixLeadId, bitrixLeadTitle, updateBitrixLead);
            bitrixLeadsRepository.save(newBitrixLead);
          }
        }
      }
    } catch (Exception e) {
      leadsErrorsRepository.save(
          new LeadsErrors((long) e.hashCode(), Objects.toString(e), TimeUtil.currentTime()));
    }
  }
}
