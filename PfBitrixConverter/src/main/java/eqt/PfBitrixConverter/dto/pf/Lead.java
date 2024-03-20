package eqt.PfBitrixConverter.dto.pf;

import lombok.Getter;

import java.util.Date;
import java.util.List;

@Getter
public class Lead {
    private int id;
    private User user;
    private Object title;
    private String first_name;
    private Object middle_name;
    private String last_name;
    private String email;
    private String mobile;
    private String phone;
    private Object address;
    private Object pobox;
    private Object city;
    private Object country;
    private String status;
    private Object quality;
    private Object financial;
    private String source;
    private String source_channel;
    private Object bank;
    private String time_frame;
    private Object passport_number;
    private Object passport_expiry_date;
    private Object legal_representative_first_name;
    private Object legal_representative_last_name;
    private Object legal_representative_email;
    private Object legal_representative_phone;
    private String created_by;
    private Date created_at;
    private Date updated_at;
    private List<Preference> preferences;
    private List<Object> mls_properties;
    private String acl_rule;
    private boolean marked_as_seen;
    private Metadata metadata;
}
