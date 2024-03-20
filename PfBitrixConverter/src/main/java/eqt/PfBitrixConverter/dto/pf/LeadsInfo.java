package eqt.PfBitrixConverter.dto.pf;

import lombok.Getter;

import java.util.List;

@Getter
public class LeadsInfo {
  private int count;
  private int page;
  private int per_page;
  private List<Lead> leads;
}
