package eqt.PfBitrixConverter.dto.pf;

import lombok.Getter;

import java.util.Date;
import java.util.List;

@Getter
public class Preference {
  private int id;
  private Lead lead;
  private String offering_type;
  private List<Type> types;
  private String rent_period;
  private List<Location> locations;
  private int from_price;
  private int to_price;
  private int from_size;
  private int to_size;
  private int from_bedroom;
  private int to_bedroom;
  private int from_bathroom;
  private int to_bathroom;
  private Date created_at;
  private Date updated_at;
  private List<Object> amenities;
}
