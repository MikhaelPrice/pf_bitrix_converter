package eqt.PfBitrixConverter.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class PfBitrixConverterService {

  @Scheduled(fixedRate = 3000)
  public void sendLeadsFromPfToBitrix() {
    System.out.println("Хуй");
  }
}
