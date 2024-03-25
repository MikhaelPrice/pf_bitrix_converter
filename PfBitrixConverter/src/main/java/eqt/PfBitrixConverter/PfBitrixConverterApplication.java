package eqt.PfBitrixConverter;

import eqt.PfBitrixConverter.dto.leads.Lead;
import eqt.PfBitrixConverter.dto.leads.LeadsInfo;
import eqt.PfBitrixConverter.entity.Leads;
import eqt.PfBitrixConverter.repository.LeadsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.List;

import static eqt.PfBitrixConverter.api.RestApi.*;
import static eqt.PfBitrixConverter.service.LeadService.*;

@EnableScheduling
@SpringBootApplication
public class PfBitrixConverterApplication {

  private static final String PROPERTY_FINDER_LEADS_TITLE = "Property Finder Lead";

  public static void main(String[] args) {
    SpringApplication.run(PfBitrixConverterApplication.class, args);
    /*String pfToken = createPfToken().getToken();
    List<LeadsInfo> pfLeadsInfoPages = getPfLeadsFromAllPages(pfToken);
    System.out.println("Pages total: " + pfLeadsInfoPages.size());

    for (LeadsInfo leadsInfo : pfLeadsInfoPages) {
      System.out.println(leadsInfo);
      for (Lead lead : leadsInfo.getLeads()) {
        Long leadId = lead.getId();
        String leadPhone = lead.getPhone();
        String leadEmail = lead.getEmail();
        String leadFirstName = lead.getFirstName();
        String leadComment = buildBitrixLeadComment(lead);

        Leads newLead = new Leads(leadId, leadFirstName, leadEmail, leadPhone, leadComment, false);

        createBitrixLead(
        leadFirstName,
        leadPhone,
        leadEmail,
        PROPERTY_FINDER_LEADS_TITLE.concat(" Все еще тестированние (не обрабатывать)"),
        leadComment);

      }
    }*/
  }
}
