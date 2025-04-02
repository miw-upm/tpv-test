package es.upm.miw.functionaltests.tpv.article;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Provider {
    private String company;
    private String nif;
    private String phone;
    private String address;
    private String email;
    private String note;
    private Boolean active;
}

