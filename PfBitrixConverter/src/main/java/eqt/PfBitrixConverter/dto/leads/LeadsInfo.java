package eqt.PfBitrixConverter.dto.leads;

import lombok.Getter;

import java.util.List;

@Getter
public class LeadsInfo {
  private int count;
  private List<Lead> leads;
}
