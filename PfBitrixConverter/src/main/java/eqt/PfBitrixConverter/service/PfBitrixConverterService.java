package eqt.PfBitrixConverter.service;

import eqt.PfBitrixConverter.dto.leads.Lead;
import eqt.PfBitrixConverter.dto.leads.LeadsInfo;
import eqt.PfBitrixConverter.entity.Leads;
import eqt.PfBitrixConverter.repository.LeadsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

import static eqt.PfBitrixConverter.api.RestApi.createBitrixLead;
import static eqt.PfBitrixConverter.api.RestApi.createPfToken;
import static eqt.PfBitrixConverter.service.LeadService.buildBitrixLeadComment;
import static eqt.PfBitrixConverter.service.LeadService.getPfLeadsFromAllPages;

@Service
public class PfBitrixConverterService {

  private static final String PROPERTY_FINDER_LEADS_TITLE = "Property Finder Lead";

  @Autowired private LeadsRepository leadsRepository;

  @Scheduled(fixedRate = 900000)
  public void sendLeadsFromPfToBitrix() {
    String pfToken = createPfToken().getToken();
    List<LeadsInfo> pfLeadsInfoPages = getPfLeadsFromAllPages(pfToken);

    for (LeadsInfo leadsInfo : pfLeadsInfoPages) {
      Lead lead = leadsInfo.getLeads().get(0);
      Long leadId = 5L;
        String leadPhone = lead.getPhone();
        String leadEmail = lead.getEmail();
        String leadFirstName = lead.getFirstName();
        String leadComment = buildBitrixLeadComment(lead);

        if (!leadsRepository.existsById(leadId)) {
        System.out.println("Lead will be created on bitrix");
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
}
