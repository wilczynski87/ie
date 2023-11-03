package dlarodziny.wolontariusze.ie.model;

import java.util.Arrays;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Volunteer {

    @Id
    private Long id;
    private String username;
    private String password;
    private String role;
    private boolean active;

    public VolunteerDetails getVolunteerDetails(VolunteerDetails[] volunteerDetails) {
        return Arrays.stream(volunteerDetails)
            .filter(details -> details.getPatron().equals(this.id))
            .findFirst()
            .orElse(null);
    }

    public String readRole() {
        return this.role.equals("ROLE_ADMIN") ? Role.ADMIN.getRoleDesc() : Role.USER.getRoleDesc();
    }

}
