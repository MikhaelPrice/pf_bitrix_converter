package eqt.PfBitrixConverter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class PfBitrixConverterApplication {

  public static void main(String[] args) {
    SpringApplication.run(PfBitrixConverterApplication.class, args);
  }
}
