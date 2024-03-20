package eqt.PfBitrixConverter.dto.pf;

import lombok.Getter;

@Getter
public class Location {
  private String id;
  private String city;
  private String community;
  private String sub_community;
  private Object tower;
  private double latitude;
  private double longitude;
}
