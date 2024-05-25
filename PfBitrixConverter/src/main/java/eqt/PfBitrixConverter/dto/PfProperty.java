package eqt.PfBitrixConverter.dto;

import lombok.Getter;

@Getter
public class PfProperty {

  private String reference;
  private double size;
  private int bedrooms;
  private int bathrooms;
  private Location location;
  private Type type;
  private Price price;

}
