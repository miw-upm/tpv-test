package es.upm.miw.functionaltests.tpv.ticket;

import es.upm.miw.functionaltests.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private String mobile;
    private String firstName;
    private String familyName;
    private String email;
    private String dni;
    private String address;
    private String password;
    private Role role;
    private LocalDateTime registrationDate;
    private Boolean active;
}
